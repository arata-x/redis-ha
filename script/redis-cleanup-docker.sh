#!/bin/bash

echo "=== Redis Docker Cleanup ==="
echo "1) Stop containers only"
echo "2) Delete containers and images"
read -p "Choose (1-2): " choice

case ${choice:-1} in
    1)
        echo "🛑 Stopping containers..."
        for dir in ../docker/redis-*; do
            [[ -f "$dir/docker-compose.yml" ]] && docker-compose -f "$dir/docker-compose.yml" down || true
        done
        echo "✅ Containers stopped!"
        ;;
    2)
        echo "🛑 Stopping containers..."
        for dir in ../docker/redis-*; do
            [[ -f "$dir/docker-compose.yml" ]] && docker-compose -f "$dir/docker-compose.yml" down || true
        done
        
        echo "🗑️ Removing containers..."
        docker ps -aq --filter "name=redis" | xargs -r docker rm -f || true
        
        echo "🖼️ Removing images..."
        docker images --filter "reference=redis*" -q | xargs -r docker rmi -f || true
        docker images --filter "reference=bitnami/redis*" -q | xargs -r docker rmi -f || true
        
        echo "💾 Removing volumes and networks..."
        docker volume ls --filter "name=redis" -q | xargs -r docker volume rm || true
        docker network ls --filter "name=redis" -q | xargs -r docker network rm || true
        
        echo "🔧 System cleanup..."
        docker system prune -f || true
        
        echo "✅ Full cleanup completed!"
        ;;
esac
