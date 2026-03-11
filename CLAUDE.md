# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Обзор проекта

Broken-store — песочница для ручного, автоматизированного и нагрузочного тестирования. Состоит из Java/Spring Boot бэкенд-сервисов, Python-микросервиса и инфраструктуры (PostgreSQL, Keycloak, Minio, Prometheus, Grafana), развёрнутых через Docker Compose.

## Команды сборки и запуска

### Java-бэкенд (Gradle мультимодуль, корень: `backend/`)
```bash
# Сборка всех модулей
cd backend && ./gradlew build

# Сборка конкретного модуля
cd backend && ./gradlew :backend-service:build
cd backend && ./gradlew :user-service:build

# Запуск всех тестов
cd backend && ./gradlew test

# Тесты одного модуля
cd backend && ./gradlew :backend-service:test

# Запуск одного тест-класса
cd backend && ./gradlew :backend-service:test --tests "ru.eriknas.brokenstore.SomeTestClass"
```

### Docker Compose (полный стек)
```bash
docker compose up -d              # Запуск всех сервисов
docker compose up --build -d      # Пересборка и запуск (для разработки)
docker compose stop               # Остановка всех сервисов
```

### URL сервисов (при запуске в Docker)
- backend-service: http://127.0.0.1:8085, Swagger: http://127.0.0.1:8085/swagger-ui/index.html
- user-service: http://127.0.0.1:8086
- profanity-validator-service: http://127.0.0.1:8087
- Keycloak: http://localhost:8082
- Grafana: http://127.0.0.1:3000
- Prometheus: http://127.0.0.1:9090
- Minio Console: http://127.0.0.1:9001
- PostgreSQL: localhost:15432 (user/pass: admin/admin, БД: broken_store)

## Архитектура

### Gradle-модули (`backend/`)
- **core** — общие модели, сущности, исключения, хелперы и базовые классы безопасности (`ru.eriknas.brokenstore`)
- **core-security** — конфигурация OAuth2/Spring Security resource server
- **backend-service** — основной REST API (футболки, новости, заказы, файлы Minio, эндпоинты симуляции ошибок). Зависит от `core` и `core-security`
- **user-service** — управление пользователями с интеграцией Keycloak. Зависит от `core` и `core-security`

Все модули используют Java 17, Spring Boot 3.3, JPA/Hibernate, PostgreSQL, Lombok и SpringDoc OpenAPI.

### Python-сервис (`backend-python/profanity-validator-service/`)
Отдельный сервис для валидации нецензурной лексики. Вызывается из backend-service и user-service по HTTP.

### Инфраструктура
- **PostgreSQL** — общая БД `broken_store`, инит-скрипт в `db/create_db.sql`
- **Keycloak** — провайдер аутентификации, конфиг realm импортируется из `keycloak/`
- **Minio** — объектное хранилище для файлов
- **nginx-gateway** — реверс-прокси перед backend-service и oauth2-proxy
- **oauth2-proxy** — прокси аутентификации через Keycloak
- **Prometheus + Grafana + cAdvisor** — стек мониторинга; дашборды в `grafana/dashboards/`

### Структура пакетов (в каждом сервисе)
`controllers/api/` → REST-контроллеры, `dto/` → объекты запросов/ответов, `models/entities/` → JPA-сущности, `repository/` → Spring Data репозитории, `services/` → бизнес-логика, `config/` → конфигурация Spring, `mappers/` → маппинг сущность↔DTO

## Соглашение о коммитах

Формат Conventional Commits (подробнее в CONTRIBUTING.md): `<тип>(<контекст>): <описание>` на английском, императив, с маленькой буквы, без точки в конце. Типы: feat, fix, docs, style, refactor, test, chore, perf, ci, build, revert, deps.
