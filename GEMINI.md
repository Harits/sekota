# Containerization Guidelines

This project uses Docker and Docker Compose for development and deployment. We support both Docker and Podman environments.

## 🐳 Quick Start

### Build and Run
```bash
docker compose up --build -d
```
or with Podman:
```bash
podman-compose up --build -d
```

## 🏗️ Dockerfile Structure
- **Multi-stage Build**:
  1. `web-builder`: Builds the Wasm distribution.
  2. `server-builder`: Builds the Ktor server distribution.
  3. Final Stage: A lean JRE image containing only the necessary binaries and static assets.

## 📂 Configuration
- `compose.yaml`: Service definitions for the Sekota Web platform.
- `Dockerfile`: Multi-stage recipe for the production image.

## 🛡️ Security
- **Credentials**: Never bake credentials into images. Use the `.env` file (which is ignored by git) for environment variables.
- **Port Mapping**: Default port is 8080. Ensure it's not conflicted on the host machine.
