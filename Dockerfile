# Stage 1: Build the Server
FROM gradle:8.7-jdk17 AS server-builder
WORKDIR /app
COPY . .
RUN ./gradlew :server:installDist --no-daemon

# Stage 2: Runtime Environment
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=server-builder /app/server/build/install/server /app/server

# Copy locally built web distribution
COPY web-dist /app/web

# Start Ktor server
CMD ["/app/server/bin/server"]
