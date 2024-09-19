#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 /path/to/microservices"
    exit 1
fi

MICROSERVICES_DIR=$1

PID_FILE="services-pids"

> "$PID_FILE"

start_service() {
    local service=$1
    echo "Starting $service..."
    (
        cd "$service" && ./mvnw spring-boot:run
    ) &
    local pid=$!  # Capture the PID of the last background process
    echo "Service $service started with PID $pid."
    echo $pid >> "$PID_FILE"  # Save the PID to the file
}

for service in "$MICROSERVICES_DIR"/*/; do
    if [ -f "$service/pom.xml" ]; then
        start_service "$service"
    else
        echo "Skipping $service (no pom.xml found)"
    fi
done

echo "All services are starting..."
echo "PIDs of started services are stored in $PID_FILE."
