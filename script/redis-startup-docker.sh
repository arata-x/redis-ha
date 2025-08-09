#!/bin/bash

# Function to display the menu
show_menu() {
    echo "Please choose a Redis configuration to start:"
    echo "1) redis-replica"
    echo "2) redis-replica-chained"
    echo "q) Quit"
}

# Main script logic
while true; do
    show_menu
    read -p "Enter your choice [1-2]: " choice

    case $choice in
        1)
            echo "Starting redis-replica..."
            docker-compose -f "../docker/redis-replica/docker-compose.yml" up -d
            break
            ;;
        2)
            echo "Starting redis-replica-chained..."
            docker-compose -f "../docker/redis-replica-chained/docker-compose.yml" up -d
            break
            ;;
        q)
            echo "Exiting."
            break
            ;;
        *)
            echo "Invalid option. Please try again."
            ;;
    esac
done
