package ru.eriknas.brokenstore.services.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.dto.users.Role;

import java.util.ArrayList;
import java.util.List;

//@RestController
//@RequestMapping("/keycloak")
//@SecurityRequirement(name = "Keycloak")
@Service
public class KeycloakRoleService {

	@Autowired
	KeycloakSecurityUtil keycloakUtil;
	
	@Value("${keycloak.realm}")
	private String realm;
	
//	@GetMapping(value = "/roles")
	public List<Role> getRoles() {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		List<RoleRepresentation> roleRepresentations = 
				keycloak.realm(realm).roles().list();
		return mapRoles(roleRepresentations);
    }
	
//	@GetMapping(value = "/roles/{roleName}")
//	public Role getRole(@PathVariable("roleName") String roleName) {
//		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
//		return mapRole(keycloak.realm(realm).roles().get(roleName).toRepresentation());
//    }
//
//	@PostMapping(value = "/role")
//	public Response createRole(Role role) {
//		RoleRepresentation roleRep = mapRoleRep(role);
//		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
//		keycloak.realm(realm).roles().create(roleRep);
//		return Response.ok(role).build();
//	}
//
//	@PutMapping(value = "/role")
//	public Response updateRole(Role role) {
//		RoleRepresentation roleRep = mapRoleRep(role);
//		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
//		keycloak.realm(realm).roles().get(role.getName()).update(roleRep);
//		return Response.ok(role).build();
//	}
//
//	@DeleteMapping(value = "/roles/{roleName}")
//	public Response deleteUser(@PathVariable("roleName") String roleName) {
//		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
//		keycloak.realm(realm).roles().deleteRole(roleName);
//		return Response.ok().build();
//	}

	public static List<Role> mapRoles(List<RoleRepresentation> representations) {
		List<Role> roles = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(representations)) {
			representations.forEach(roleRep -> roles.add(mapRole(roleRep)));
		}
		return roles;
	}

	public static Role mapRole(RoleRepresentation roleRep) {
		Role role = new Role();
		role.setId(roleRep.getId());
		role.setName(roleRep.getName());
		return role;
	}

//	public RoleRepresentation mapRoleRep(Role role) {
//		RoleRepresentation roleRep = new RoleRepresentation();
//		roleRep.setName(role.getName());
//		return roleRep;
//	}
}
