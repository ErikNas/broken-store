package ru.eriknas.brokenstore.services.keycloak;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.eriknas.brokenstore.dto.users.Role;
import ru.eriknas.brokenstore.dto.users.UserDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class KeycloakUserService {
    private final String rolePrefix = "GROUP_";

    @Autowired
    KeycloakSecurityUtil keycloakUtil;

    @Value("${keycloak.realm}")
    private String realm;

    public List<UserDTO> getUsers() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(realm)
                .users()
                .list();
        return mapUsers(userRepresentations);
    }

    public UserDTO getUser(@PathVariable("id") String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        return mapUser(keycloak.realm(realm).users().get(id).toRepresentation());
    }

    public Response addUser(UserDTO user) throws Exception {
        Map<String, String> roleGroups = getRoleGroups();

        UserRepresentation userRep = mapUserRep(user, roleGroups);
        Response response = keycloakUtil.getKeycloakInstance()
                .realm(realm)
                .users()
                .create(userRep);
        if (response.getStatus() != 201) {
            throw new Exception("Не удалось добавить пользователя в keycloak: " + response.getStatusInfo());
        }
        return Response.ok(user).build();
    }

    public Map<String, String> getRoleGroups() {
        return keycloakUtil.getKeycloakInstance()
                .realm(realm)
                .groups()
                .groups()
                .stream()
                .filter(group -> group.getName().startsWith(rolePrefix))
                .collect(toMap(
                                group -> group.getName().replaceAll(rolePrefix, ""),
                                GroupRepresentation::getName
                        )
                );
    }

    public Response updateUser(UserDTO user) throws Exception {
        Map<String, String> roleGroups = getRoleGroups();
        UserRepresentation userRep = mapUserRep(user, roleGroups);
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        keycloak.realm(realm).users().get(String.valueOf(user.getId())).update(userRep);
        return Response.ok(user).build();
    }

    public Response deleteUser(@PathVariable("id") String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        keycloak.realm(realm).users().delete(id);
        return Response.ok().build();
    }

    public List<Role> getRoles(@PathVariable("id") String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        return KeycloakRoleService.mapRoles(keycloak.realm(realm).users()
                .get(id).roles().realmLevel().listAll());
    }

    public Response createRole(@PathVariable("id") String id,
                               @PathVariable("roleName") String roleName) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(role));
        return Response.ok().build();
    }

    private List<UserDTO> mapUsers(List<UserRepresentation> userRepresentations) {
        List<UserDTO> users = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)) {
            userRepresentations.forEach(userRep -> {
                users.add(mapUser(userRep));
            });
        }
        return users;
    }

    private UserDTO mapUser(UserRepresentation userRep) {
        return UserDTO.builder()
                .id(Integer.valueOf(userRep.getId()))
                .firstName(userRep.getFirstName())
                .lastName(userRep.getLastName())
                .email(userRep.getEmail())
                .build();
    }

    private UserRepresentation mapUserRep(UserDTO user, Map<String, String> roleGroups) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(user.getEmail());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(false);

        if (!roleGroups.containsKey(user.getRole())) {
            throw new IllegalArgumentException("Некорректно задана роль пользователя");
        }
        userRep.setGroups(List.of(roleGroups.get(user.getRole())));

        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        userRep.setCredentials(creds);
        return userRep;
    }
}
