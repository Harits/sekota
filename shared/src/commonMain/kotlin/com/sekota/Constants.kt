package com.sekota

const val SERVER_PORT = 8080

// Remote API Gateway (Used for User Authentication & Master Profile services)
const val AUTH_BASE_URL = "https://sekota.id/api/v1/"

// Local Sekota Web Platform Server (Used for internal tools, local state & telemetry)
const val LOCAL_API_BASE_URL = "http://localhost:8080/"

// Default Base URL for primary operations
const val BASE_URL = AUTH_BASE_URL
