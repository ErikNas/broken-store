# broken-store

Приложение позволяет поднять локально песочницу.  
В состав входит полноценный backend и дополнительное окружение.

Идеально подходит для целей ручного, автоматизированного и нагрузочного тестирования в качестве платформы 
для экспериментов и проведения обучения. 

## Управление приложением в докере

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

### Полное удаление приложения

Внимание! Этот код удалит ВСЕ образы докер

```Bash
sudo docker container prune
sudo docker volume prune
sudo docker network prune
sudo docker rmi $(sudo docker images -a -q)
```

TODO: Написать команды, которые будут удалять только broken-store
