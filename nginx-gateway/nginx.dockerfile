FROM openresty/openresty:1.21.4.2-jammy

# Устанавливаем зависимости для сборки C-модулей (для lua-resty-openssl и т.п.)
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        ca-certificates \
        wget \
        build-essential \
        libssl-dev \
        libpcre3-dev \
        zlib1g-dev \
        git \
    && rm -rf /var/lib/apt/lists/*

# Устанавливаем lua-resty-openidc напрямую из rockspec
RUN luarocks install --server=https://luarocks.org \
    https://raw.githubusercontent.com/zmartzone/lua-resty-openidc/v1.7.2/lua-resty-openidc-1.7.2-1.rockspec

# Копируем наши файлы
COPY jwks.lua /etc/nginx/lua/jwks.lua
COPY ./templates/nginx.conf.template /etc/nginx/nginx.conf.template
COPY nginx-docker-entrypoint.sh /nginx-docker-entrypoint.sh

RUN chmod +x /nginx-docker-entrypoint.sh

EXPOSE 80

ENTRYPOINT ["/nginx-docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
