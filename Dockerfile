# Stage 1: Build the Web App
FROM gradle:8.7-jdk17 AS web-builder
WORKDIR /app
COPY . .
RUN ./gradlew :composeApp:wasmJsBrowserDistribution --no-daemon

# Stage 2: Build the Server
FROM gradle:8.7-jdk17 AS server-builder
WORKDIR /app
COPY . .
RUN ./gradlew :server:installDist --no-daemon

# Stage 3: Runtime Environment
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=server-builder /app/server/build/install/server /app/server

# Copy the web build distribution to the server's static resources path
COPY --from=web-builder /app/composeApp/build/dist/wasmJs/productionExecutable /app/server/web

# Start Ktor server
CMD ["/app/server/bin/server"]
