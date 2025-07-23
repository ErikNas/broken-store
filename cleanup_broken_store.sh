#!/bin/bash

# Скрипт для очистки контейнеров с именем, содержащим "broken"
# Работает везде: WSL, Git Bash, Linux
# Без внешних зависимостей (только docker + bash)
# Порядок: остановка → удаление контейнеров → volumes → образы

set -euo pipefail

echo "=== Начало очистки контейнеров с именем, содержащим 'broken' ==="

# Шаг 1: Поиск контейнеров с именем, содержащим "broken"
echo "Шаг 1: Поиск контейнеров с именем, содержащим 'broken'..."
CONTAINER_IDS=$(docker ps -a --filter "name=broken" --format "{{.ID}}")

if [ -z "$CONTAINER_IDS" ]; then
    echo "Контейнеры с именем 'broken' не найдены. Завершение."
    exit 0
fi

echo "Найденные контейнеры: $CONTAINER_IDS"

# Шаг 2: Остановить контейнеры
echo "Шаг 2: Остановка контейнеров..."
for container in $CONTAINER_IDS; do
    running=$(docker inspect -f '{{.State.Running}}' "$container" 2>/dev/null || echo "false")
    if [ "$running" = "true" ]; then
        echo "Остановка контейнера: $container"
        docker stop "$container" > /dev/null
    else
        echo "Контейнер $container уже остановлен."
    fi
done

# Шаг 3: Получить имена образов
echo "Шаг 3: Получение имён образов..."
IMAGE_NAMES=()
for container in $CONTAINER_IDS; do
    image=$(docker inspect -f '{{.Config.Image}}' "$container" 2>/dev/null || true)
    if [ -n "$image" ] && [[ ! " ${IMAGE_NAMES[@]} " =~ " ${image} " ]]; then
        IMAGE_NAMES+=("$image")
    fi
done

if [ ${#IMAGE_NAMES[@]} -eq 0 ]; then
    echo "Образы не найдены."
else
    echo "Найденные образы: ${IMAGE_NAMES[*]}"
fi

# Шаг 4: Получить volumes (надёжная версия без зависаний)
echo "Шаг 4: Получение volumes..."
VOLUMES=()

for container in $CONTAINER_IDS; do
    echo "  Анализ контейнера: $container"

    # Однострочный формат — НЕ зависает, обрабатывает только именованные volumes
    # Убираем лишние пробелы через xargs
    volume_line=$(docker inspect --format '{{range .Mounts}}{{if eq .Type "volume"}}{{.Name}} {{end}}{{end}}' "$container" 2>/dev/null | xargs)

    # Обрабатываем каждый volume
    for vol in $volume_line; do
        if [ -n "$vol" ] && [[ ! " ${VOLUMES[@]} " =~ " ${vol} " ]]; then
            VOLUMES+=("$vol")
        fi
    done
done

if [ ${#VOLUMES[@]} -eq 0 ]; then
    echo "Volumes не найдены."
else
    echo "Найденные volumes: ${VOLUMES[*]}"
fi

# Шаг 5: Удалить контейнеры
echo "Шаг 5: Удаление контейнеров..."
for container in $CONTAINER_IDS; do
    echo "Удаление контейнера: $container"
    docker rm "$container" > /dev/null
done
echo "Контейнеры удалены."

# Шаг 6: Удалить volumes
echo "Шаг 6: Удаление volumes..."
for vol in "${VOLUMES[@]}"; do
    echo "Удаление volume: $vol"
    docker volume rm "$vol" > /dev/null 2>&1 || \
        echo "⚠️ Не удалось удалить volume '$vol' (возможно, используется или не существует)"
done
echo "Volumes удалены."

# Шаг 7: Удалить образы
echo "Шаг 7: Удаление образов..."
for image in "${IMAGE_NAMES[@]}"; do
    echo "Удаление образа: $image"
    docker rmi "$image" > /dev/null 2>&1 || \
        echo "⚠️ Не удалось удалить образ '$image' (возможно, используется другим контейнером или отсутствует)"
done
echo "Образы удалены."

echo "=== Очистка завершена успешно ==="