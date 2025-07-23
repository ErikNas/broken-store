# broken-store

Приложение позволяет поднять локально песочницу.  
В состав входит полноценный backend и дополнительное окружение.

Идеально подходит для целей ручного, автоматизированного и нагрузочного тестирования в качестве платформы 
для экспериментов и проведения обучения. 

## Отказ от ответственности

Используя наш Swagger API, вы соглашаетесь с тем, что несете полную ответственность за любые действия, связанные с размещением, изменением или удалением данных. 
Мы не контролируем и не можем гарантировать законность, корректность или добросовестность информации, которую размещают пользователи.
Мы настоятельно призываем вас не использовать наш сервис для размещения любых материалов, содержащих незаконный контент, нарушающих права третьих лиц или национальное и международное законодательство. 
Мы не несем ответственности за последствия неправомерного использования нашего Swagger API, включая, но не ограничиваясь, юридическими последствиями, damages, убытками или штрафами.
Мы оставляем за собой право удалять любой контент, который мы сочтем незаконным, недопустимым или вызывающим споры. 
Однако, мы не обязуемся отслеживать или мониторить контент, размещаемый пользователями. 
Используя наш сервис, вы соглашаетесь освободить нас от любых претензий, убытков или требований третьих лиц, 
возникающих в результате вашего использования.

## Управление приложением в Docker

### Установка Docker
<details>

<summary>Docker для Windows</summary>

Для скачивания Docker на Windows необходимо выполнить команду в PowerShell от имени администратора
```Bash
Invoke-WebRequest -Uri https://desktop.docker.com/win/stable/Docker%20Desktop%20Installer.exe -OutFile DockerDesktopInstaller.exe
```

Для установки Docker на Windows необходимо выполнить команду в PowerShell от имени администратора
```Bash
Start-Process -Wait -FilePath .\DockerDesktopInstaller.exe
```

**Важно**: в процессе установки снять флажок с чекбокса «**Use WSL 2 instead of Hyper-V (recommended)**»

Результат успешной установки Docker:
```Bash
Docker Desktop 4.36.0
Installation succeeded
```

Для запуска Docker на Windows необходимо выполнить команду в PowerShell от имени администратора
```Bash
Start-Process 'C:\Program Files\Docker\Docker\Docker Desktop.exe'
```

Принять соглашение и авторизоваться в Docker.

</details>

<details><summary>Docker для Mac</summary>
Скоро...
</details>

<details><summary>Docker для Linux</summary>
Скоро...
</details>

### Запуск приложения

Для запуска приложения в докере необходимо выполнить команду
```Bash
docker compose up -d
```

Результаты успешного выполнения команды:
```Bash
[+] Running 5/5
✔ Network broken-store_default  Created                                                                                                                                                        0.0s
✔ Container client-backend      Created                                                                                                                                                        0.0s
✔ Container prometheus          Created                                                                                                                                                        0.0s
✔ Container cadvisor-exporter   Created                                                                                                                                                        0.1s
✔ Container grafana             Created 
```

где:

- *client-backend* - контейнер с бекендом приложения. Доступен по адресу http://127.0.0.1:8085, сваггер http://127.0.0.1:8085/swagger-ui/index.html

- *prometheus* - контейнер с prometheus, который отвечает за сбор метрик с самого приложения и с cadvisor-exporter
Доступен по адресу http://127.0.0.1:9090

- *cadvisor-exporter* - контейнер с cadvisor, который снимает метрики с докер контейнеров (ЦПУ, память, сеть)

- *grafana* - контейнер с графаной, которая позволяет просматривать собранные метрики на красивых графиках
При поднятии контейнера datasource с prometheus добавляется автоматически. Дашборды для графиков необходимо добавить вручную
(дашборды сохранены в grafana/dashboards). Доступен по адресу http://127.0.0.1:3000

### Остановка приложения

Для остановки стенда необходимо выполнить
```Bash
docker compose stop
```

### Запуск контейнера в докере на Windows

Перейти в Docker Desktop и запустить вручную контейнер с приложением.


### Полное удаление приложения

Внимание! Этот код удалит ВСЕ образы докер

```Bash
sudo docker container prune
sudo docker volume prune
sudo docker network prune
sudo docker rmi $(sudo docker images -a -q)
```

### Скрипт по удалению контейнеров, образов и томов Broken-store

Скрипт `cleanup_broken_store.sh` удаляет:
- Контейнеры с именем, содержащим `broken`
- Их volumes
- Их образы

#### Как использовать

```Bash
./cleanup_broken_store.sh
```

## Схема сервисов

<details>

<summary>Код для построения диаграммы компонентов в PlantUML</summary>

```PlantUML
@startuml
() "user interface" as frontend #red
package "Controllers" {
[t-shirt-controller] as t_shirt
[user-controller] as user
[pay-controller] as pay #red
[news-controller] as news
[broken-store-controller] as br
[server-error-controller] as erorrs
[orders-controller] as orders #red
}
package "service" {
[keycloak] as keycloak
[grafana]
}
package "DB" {
database "minio" {
}
database "prometheus" {
}
database "postgres" {
}
}
user -- keycloak : Регистрация/авторизация
t_shirt -- orders : Инфо о футболке
user -- orders : Инфо о пользователе
pay -- orders : Оплата
frontend -- news
frontend -- t_shirt
frontend -- keycloak
prometheus -- grafana
@enduml
```

</details>

<details>

<summary>Визуальное отображение</summary>

Красным отмечены нереализованные сервисы
![image](https://github.com/user-attachments/assets/ff8907d5-8c48-4e72-a224-ff2ecb8e35a5)

</details>

## Описание возможностей
### Сервис "Футболки"
```
GET/t-shirt/{id} - Найти футболку по ID
GET/t-shirt/all - Получить список всех футболок (+пагинация)
POST/t-shirt/ - Добавить футболку
PUT/t-shirt/{id} - Изменить футболку
DELETE/t-shirt/{id} - Удалить футболку
```

### Сервис "Пользователи"
```
GET/users{id} - Найти пользователя по ID
GET/users/page - 
GET/users/all - Получить список всех сотрудников (+пагинация)
POST/users - Добавить пользователя
DELETE/users/{id} - Удалить пользователя
```

### Сервис "Новости"
```
GET/news - Получить список всех новостей
GET/news/{id} - Найти новость по ID
POST/news - Добавить новость
DELETE/news/{id} - Удалить новость
```

### Minio
```
GET/download - Получить файл из Minio
```

### Управление стилями
```
GET/css/styles.css
```

### Ошибки сервера
```
GET/api/response200WithError - Ручка, которая присылает 200, но в теле сообщения `Error: Я не смогла`
GET/api/flakyEndpoint - Ручка, которая отвечает код 500 в 30% случаев (флакер)
GET/api/error500 - Ручка, которая отвечатет код 500 в 100% случаев
```

### Приветствие
```
GET/api/hello - Поздороваться со Стасом
```

## Замечания к релизу
Замечания к релизу


## Лицензия
Наш проект распространяется под лицензией MIT.
Вы можете свободно использовать, изменять и распространять код, в том числе в коммерческих целях, при условии указания оригинального авторства и копирайта.
