#!/bin/bash

PID_FILE="services-pids"

if [ ! -f "$PID_FILE" ]; then
    echo "No PID file found. Are the services running?"
    exit 1
fi

while IFS= read -r pid; do
    echo "Stopping service with PID $pid..."
    kill "$pid"
done < "$PID_FILE"

rm -f "$PID_FILE"

echo "All services have been stopped."
