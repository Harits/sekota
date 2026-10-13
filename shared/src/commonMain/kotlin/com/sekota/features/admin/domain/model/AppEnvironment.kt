package com.sekota.features.admin.domain.model

enum class AppEnvironment(
    val displayName: String,
    val description: String
) {
    DEVELOPMENT(
        displayName = "DEVELOPMENT",
        description = "Lingkungan lokal/pengembangan internal. Aman untuk pengujian fitur baru dan eksperimen tanpa dampak publik."
    ),
    STAGING(
        displayName = "STAGING",
        description = "Lingkungan simulasi pra-produksi. Data diuji menyerupai produksi untuk QA dan peninjauan stakeholder."
    ),
    PRODUCTION(
        displayName = "PRODUCTION",
        description = "Lingkungan live publik. Seluruh perubahan langsung berdampak pada pengunjung portal dan klien aktif."
    );

    val isProduction: Boolean get() = this == PRODUCTION
    val isStaging: Boolean get() = this == STAGING
    val isDevelopment: Boolean get() = this == DEVELOPMENT
}

object EnvironmentConfigResolver {
    private var overrideEnvironment: AppEnvironment? = null

    fun setEnvironmentOverride(env: AppEnvironment?) {
        overrideEnvironment = env
    }

    /**
     * Resolves active environment deterministically based on overrides or defaults.
     * Defaults to DEVELOPMENT on client platforms unless explicitly overridden.
     */
    fun resolveEnvironment(): AppEnvironment {
        return overrideEnvironment ?: AppEnvironment.DEVELOPMENT
    }
}
