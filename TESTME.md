# Руководство по тестированию Broken Store

Это руководство описывает как начать работу с песочницей: запустить окружение, авторизоваться и отправить первые запросы к API.

## Запуск окружения

```bash
# Запуск всех сервисов
docker compose up -d

# Запуск с пересборкой (после изменений в коде)
docker compose up --build -d

# Остановка
docker compose stop
```

Дождитесь полного запуска всех контейнеров (1–2 минуты). Проверить готовность backend-service:

```bash
curl http://127.0.0.1:8085/backend-service/actuator/health
# Ожидаемый ответ: {"status":"UP"}
```

## Доступные сервисы

| Сервис | URL | Описание |
|--------|-----|----------|
| Главная страница | http://localhost/ | Архитектурная схема (frontend) |
| backend-service Swagger | http://localhost/backend-service/swagger-ui/index.html | Swagger UI (требует авторизацию через Keycloak) |
| user-service Swagger | http://localhost/user-service/swagger-ui/index.html | Swagger UI (требует авторизацию через Keycloak) |
| backend-service (прямой) | http://127.0.0.1:8085 | Прямой доступ без nginx |
| Keycloak Admin Console | http://localhost:8082 | Логин: admin / admin |
| Grafana | http://127.0.0.1:3000 | Дашборды мониторинга |
| Prometheus | http://127.0.0.1:9090 | Метрики |
| Minio Console | http://127.0.0.1:9001 | Объектное хранилище |
| PostgreSQL | localhost:15432 | Логин: admin / admin, БД: broken_store |

## Тестовые пользователи

| Логин | Пароль | Роль | Права |
|-------|--------|------|-------|
| admin123 | admin123 | ROLE_ADMIN | Полный доступ ко всем эндпоинтам |

Дополнительных пользователей можно создать через Keycloak Admin Console (http://localhost:8082, admin/admin) в realm `broken-store-realm`.

## Авторизация

### Авторизация через Swagger UI (браузер)

1. Откройте http://localhost/backend-service/swagger-ui/index.html
2. Вы будете автоматически перенаправлены на страницу логина Keycloak
3. Введите логин и пароль тестового пользователя (например, `admin123` / `admin123`)
4. После авторизации Swagger UI откроется, и вы сможете отправлять запросы **без дополнительной авторизации** внутри Swagger — токен подставляется автоматически

### Способ 1: Через эндпоинт /auth/login (рекомендуется)

Эндпоинт `POST /auth/login` не требует знания client_secret — достаточно логина и пароля:

```bash
curl -s -X POST http://localhost/backend-service/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin123", "password": "admin123"}'
```

Ответ:
```json
{
  "access_token": "eyJhbGciOiJS...",
  "refresh_token": "eyJhbGciOiJS...",
  "expires_in": 36000,
  "token_type": "Bearer"
}
```

### Способ 2: Напрямую через Keycloak

Если нужен полный контроль над параметрами OAuth2 (например, scopes или другой grant_type):

```bash
curl -s -X POST http://localhost:8082/realms/broken-store-realm/protocol/openid-connect/token \
  -d "grant_type=password" \
  -d "client_id=broken-store-client" \
  -d "client_secret=6S4nbOfc26q10mlqzZB70SbdLm5r3H3H" \
  -d "username=admin123" \
  -d "password=admin123"
```

> **Примечание:** при работе напрямую с Keycloak необходимо указывать `client_secret`.

Используйте `access_token` из ответа в заголовке `Authorization: Bearer <token>`.

### Получение токена уже авторизованного пользователя

Если вы уже авторизованы (например, через Swagger UI), можно получить свой текущий токен:

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost/backend-service/auth/token
```

## Примеры запросов

### curl

#### Получение токена и вызов /api/hello

```bash
# Получаем access token через /auth/login
TOKEN=$(curl -s -X POST http://localhost/backend-service/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin123", "password": "admin123"}' | jq -r '.access_token')

# Вызываем API с токеном
curl -H "Authorization: Bearer $TOKEN" http://localhost/backend-service/api/hello
# Ожидаемый ответ: Hello, Stasyan
```

#### Получение списка футболок

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost/backend-service/t-shirt/all
```

#### Создание новости

```bash
curl -X POST http://localhost/backend-service/news \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"header": "Тестовая новость", "description": "Содержание новости"}'
```

### Java (JUnit 5 + REST Assured)

#### Зависимости (Gradle)

```groovy
testImplementation 'io.rest-assured:rest-assured:5.4.0'
testImplementation 'org.junit.jupiter:junit-jupiter:5.10.2'
```

#### Зависимости (Maven)

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.2</version>
    <scope>test</scope>
</dependency>
```

#### Базовый тест с авторизацией

```java
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class HelloApiTest {

    private static String accessToken;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/backend-service";

        accessToken = getAccessToken("admin123", "admin123");
    }

    /**
     * Получение access token через эндпоинт /auth/login.
     * Не требует знания client_secret — достаточно логина и пароля.
     */
    static String getAccessToken(String username, String password) {
        return given()
                .contentType("application/json")
                .body("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
            .when()
                .post("/auth/login")
            .then()
                .statusCode(200)
                .extract()
                .path("access_token");
    }

    @Test
    void helloEndpointReturnsGreeting() {
        given()
                .header("Authorization", "Bearer " + accessToken)
            .when()
                .get("/api/hello")
            .then()
                .statusCode(200)
                .body(equalTo("Hello, Stasyan"));
    }

    @Test
    void helloEndpointWithoutTokenReturns401() {
        given()
            .when()
                .get("/api/hello")
            .then()
                .statusCode(401);
    }
}
```

#### Пример теста для CRUD операций (футболки)

```java
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class TShirtApiTest {

    // accessToken и setUp() аналогичны HelloApiTest

    @Test
    void getAllTShirts() {
        given()
                .header("Authorization", "Bearer " + accessToken)
            .when()
                .get("/t-shirt/all")
            .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    void getTShirtById() {
        given()
                .header("Authorization", "Bearer " + accessToken)
            .when()
                .get("/t-shirt/1")
            .then()
                .statusCode(anyOf(is(200), is(404)));
    }
}
```

## Доступные эндпоинты backend-service

| Метод | Путь | Описание | Роли |
|-------|------|----------|------|
| POST | /auth/login | Авторизация по логину и паролю | без авторизации |
| GET | /auth/token | Получить токен текущего пользователя | любая |
| GET | /api/hello | Поздороваться со Стасом | любая |
| GET | /api/error500 | Всегда возвращает 500 | любая |
| GET | /api/flakyEndpoint | Возвращает 500 в 30% случаев | любая |
| GET | /api/response200WithError | Возвращает 200 с ошибкой в теле | любая |
| GET | /t-shirt/all | Список футболок (пагинация: page, size) | все роли |
| GET | /t-shirt/{id} | Футболка по ID | ADMIN, MANAGER, USER |
| POST | /t-shirt | Создать футболку | ADMIN, MANAGER |
| PUT | /t-shirt/{id} | Обновить футболку | ADMIN, MANAGER |
| DELETE | /t-shirt/{id} | Удалить футболку | ADMIN |
| GET | /news | Список новостей (пагинация: page, size) | все роли |
| GET | /news/{id} | Новость по ID | ADMIN, MANAGER, USER |
| POST | /news | Создать новость | любая |
| DELETE | /news/{id} | Удалить новость | ADMIN |
| GET | /order/all | Список заказов (пагинация: page, size) | любая |
| GET | /order/{id} | Заказ по ID | любая |
| POST | /order | Создать заказ | любая |
| PUT | /order/{id} | Обновить заказ | любая |
| DELETE | /order/{id} | Удалить заказ | любая |
| GET | /download?file_name=... | Скачать файл из Minio | любая |

## Доступные эндпоинты user-service

| Метод | Путь | Описание | Роли |
|-------|------|----------|------|
| GET | /users/all | Список пользователей | ADMIN, MANAGER |
| GET | /users/{id} | Пользователь по ID | любая |
| POST | /users | Создать пользователя | ADMIN, MANAGER, USER |
| DELETE | /users/{id} | Удалить пользователя | ADMIN |

> **Примечание:** все пути указаны относительно context-path сервиса. Полный URL через nginx: `http://localhost/backend-service/<путь>` или `http://localhost/user-service/<путь>`.

## Эндпоинты для тестирования ошибок

Песочница содержит специальные эндпоинты для практики работы с ошибками:

```bash
# Всегда 500
curl -H "Authorization: Bearer $TOKEN" http://localhost/backend-service/api/error500

# 500 в ~30% случаев (flaky endpoint)
curl -H "Authorization: Bearer $TOKEN" http://localhost/backend-service/api/flakyEndpoint

# 200, но в теле "Error: Я не смогла"
curl -H "Authorization: Bearer $TOKEN" http://localhost/backend-service/api/response200WithError
```
