# Sekota web + contact API image.
# Ktor serves the Wasm build at "/" and POST /api/contact on the same origin.

# ---- build -----------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace

COPY . .

# The repository pins an Azul Zulu 21 daemon JVM in
# gradle/gradle-daemon-jvm.properties. This image already ships JDK 21, so the
# criteria file is removed to avoid downloading a second JDK in the builder.
# The Android SDK is NOT required: only JVM and Wasm tasks are requested, and
# no :app:androidApp task enters the graph (verified with --dry-run and no SDK).
# libatomic1 is required by the Node binary Kotlin downloads for the Wasm build
# (jammy does not ship it in this image); without it yarn exits 127.
RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
        libatomic1 ca-certificates \
    && rm -rf /var/lib/apt/lists/*

RUN chmod +x gradlew \
    && rm -f gradle/gradle-daemon-jvm.properties \
    && ./gradlew :server:installDist :app:webApp:wasmJsBrowserDistribution \
         --no-daemon --no-configuration-cache --console=plain

# ---- runtime ---------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --gid 10001 sekota \
    && useradd --no-create-home --uid 10001 --gid sekota --home-dir /tmp --shell /usr/sbin/nologin sekota

WORKDIR /opt/sekota

# installDist: /workspace/server/build/install/server -> /opt/sekota/server
# Wasm dist:   app/webApp/build/dist/wasmJs/productionExecutable
COPY --from=build --chown=10001:10001 /workspace/server/build/install/server ./server
COPY --from=build --chown=10001:10001 /workspace/app/webApp/build/dist/wasmJs/productionExecutable ./web

# PORT is fixed to 8080 inside the container; the host port is set in Compose.
# Java ignores cgroup limits on old JVMs, but JDK 21 is container-aware.
ENV PORT=8080 \
    SEKOTA_WEB_DIR=/opt/sekota/web \
    HOME=/tmp \
    LANG=C.UTF-8 \
    JAVA_OPTS="-XX:InitialRAMPercentage=20 -XX:MaxRAMPercentage=60 -XX:+ExitOnOutOfMemoryError -XX:+UseContainerSupport -XX:MaxMetaspaceSize=160m -Dfile.encoding=UTF-8 -Djava.io.tmpdir=/tmp"

EXPOSE 8080
USER 10001:10001

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
    CMD curl -fsS http://127.0.0.1:8080/health || exit 1

# Exec form keeps PID 1 on the JVM so SIGTERM triggers a clean shutdown.
CMD ["/opt/sekota/server/bin/server"]
