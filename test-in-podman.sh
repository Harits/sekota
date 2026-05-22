#!/bin/bash
# test-in-podman.sh

# Ensure podman machine is running
if [[ "$OSTYPE" == "darwin"* ]]; then
    MACHINE_STATUS=$(podman machine inspect --format '{{.State}}' 2>/dev/null)
    if [[ "$MACHINE_STATUS" != "running" ]]; then
        echo "🔄 Starting Podman machine..."
        podman machine start || exit 1
    fi
fi

echo "🧪 Running Server Tests in Podman Container..."
podman run --rm \
    -v "$(pwd):/home/gradle/project" \
    -w /home/gradle/project \
    gradle:8.7-jdk17 \
    ./gradlew :server:test --no-daemon
