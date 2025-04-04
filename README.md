# broken-store

Приложение позволяет поднять локально песочницу.  
В состав входит полноценный backend и дополнительное окружение.

Идеально подходит для целей ручного, автоматизированного и нагрузочного тестирования в качестве платформы 
для экспериментов и проведения обучения. 

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

TODO: Написать команды, которые будут удалять только broken-store
