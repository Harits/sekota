#!/bin/bash

# Check if podman is installed
if ! command -v podman &> /dev/null; then
    echo "❌ Podman is not installed. Please install it first."
    exit 1
fi

# Check if podman machine is running (for macOS/Windows)
if [[ "$OSTYPE" == "darwin"* ]]; then
    MACHINE_STATUS=$(podman machine inspect --format '{{.State}}' 2>/dev/null)
    if [[ "$MACHINE_STATUS" != "running" ]]; then
        echo "🔄 Podman machine is not running. Attempting to start..."
        podman machine start
        if [ $? -ne 0 ]; then
            echo "❌ Failed to start podman machine. Please run 'podman machine init' if you haven't yet."
            exit 1
        fi
    fi
fi

echo "🚀 Building and starting Sekota Web via Podman..."

# Use 'podman compose' if available, otherwise fallback to podman build/run
if podman compose version &> /dev/null; then
    podman compose up --build -d
else
    echo "⚠️  podman-compose not found. Building image manually..."
    podman build -t sekota-web:latest .
    echo "🏃 Running container..."
    podman run -d --name sekota-web-app -p 8080:8080 -e PORT=8080 sekota-web:latest
fi

echo "✅ Done! You can access the app at http://localhost:8080"
echo "📜 Logs: podman logs -f sekota-web-app (or use podman compose logs -f)"
