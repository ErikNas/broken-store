#!/bin/sh
set -e

# Подставляем переменные в шаблон
envsubst '${FRONTEND_HOST},${AUTH_HOST}' < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf

# (Опционально) проверяем конфиг перед запуском
nginx -t

# Запускаем Nginx в foreground
exec nginx -g 'daemon off;'
