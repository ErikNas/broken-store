# broken-store

## Запуск приложения в докере

Для запуска приложения в докере необходимо выполнить команду
`docker compose up -d`

Результаты успешного выполнения команды:
[+] Running 5/5
✔ Network broken-store_default  Created                                                                                                                                                        0.0s
✔ Container client-backend      Created                                                                                                                                                        0.0s
✔ Container prometheus          Created                                                                                                                                                        0.0s
✔ Container cadvisor-exporter   Created                                                                                                                                                        0.1s
✔ Container grafana             Created 

где
*client-backend* - контейнер с бекендом приложения. Доступен по адресу http://127.0.0.1:8080, сваггер http://127.0.0.1:8080/swagger-ui/index.html
*prometheus* - контейнер с prometheus, который отвечает за сбор метрик с самого приложения и с cadvisor-exporter
Доступен по адресу http://127.0.0.1:9090
*cadvisor-exporter* - контейнер с cadvisor, который снимает метрики с докер контейнеров (ЦПУ, память, сеть)
*grafana* - контейнер с графаной, которая позволяет просматривать собранные метрики на красивых графиках
При поднятии контейнера datasource с prometheus добавляется автоматически. Дашборды для графиков необходимо добавить вручную
(дашборды сохранены в grafana/dashboards)

Для остановки стенда необходимо выполнить
`docker compose down -d`
