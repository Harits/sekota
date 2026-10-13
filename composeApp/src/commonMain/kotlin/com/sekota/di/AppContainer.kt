package com.sekota.di

import com.sekota.core.storage.TokenStorage
import com.sekota.features.auth.data.repository.AuthRepositoryImpl
import com.sekota.features.auth.domain.repository.AuthRepository
import com.sekota.features.auth.domain.usecase.ClearTokenUseCase
import com.sekota.features.auth.domain.usecase.GetTokenUseCase
import com.sekota.features.auth.domain.usecase.LoginUseCase
import com.sekota.features.auth.domain.usecase.SignupUseCase
import com.sekota.features.profile.data.repository.ProfileRepositoryImpl
import com.sekota.features.profile.domain.repository.ProfileRepository
import com.sekota.features.profile.domain.usecase.GetProfileUseCase
import com.sekota.features.profile.domain.usecase.UpdateProfileUseCase
import com.sekota.features.sync.data.repository.SyncService
import com.sekota.navigation.NavigationCoordinator

/**
 * Idiomatic Lightweight Dependency Injection (DI) Container for Sekota KMP.
 * Follows Clean & Screaming Architecture by centralizing service life cycles,
 * token persistence, and navigation coordination without external framework bloat.
 */
class AppContainer(
    val tokenStorage: TokenStorage = TokenStorage(),
    val syncService: SyncService = SyncService()
) {
    val authRepository: AuthRepository by lazy { AuthRepositoryImpl(tokenStorage) }
    val profileRepository: ProfileRepository by lazy { ProfileRepositoryImpl(tokenStorage) }

    val loginUseCase: LoginUseCase by lazy { LoginUseCase(authRepository) }
    val signupUseCase: SignupUseCase by lazy { SignupUseCase(authRepository) }
    val getTokenUseCase: GetTokenUseCase by lazy { GetTokenUseCase(authRepository) }
    val clearTokenUseCase: ClearTokenUseCase by lazy { ClearTokenUseCase(authRepository) }
    val getProfileUseCase: GetProfileUseCase by lazy { GetProfileUseCase(profileRepository) }
    val updateProfileUseCase: UpdateProfileUseCase by lazy { UpdateProfileUseCase(profileRepository) }

    val adminRepository: com.sekota.features.admin.domain.repository.AdminRepository by lazy {
        com.sekota.features.admin.data.repository.AdminRepositoryImpl()
    }
    val getValueLoopUseCase: com.sekota.features.admin.domain.usecase.GetAdminValueLoopUseCase by lazy {
        com.sekota.features.admin.domain.usecase.GetAdminValueLoopUseCase(adminRepository)
    }
    val saveValueLoopUseCase: com.sekota.features.admin.domain.usecase.SaveAdminValueLoopUseCase by lazy {
        com.sekota.features.admin.domain.usecase.SaveAdminValueLoopUseCase(adminRepository)
    }
    val getCategoriesUseCase: com.sekota.features.admin.domain.usecase.GetAdminCategoriesUseCase by lazy {
        com.sekota.features.admin.domain.usecase.GetAdminCategoriesUseCase(adminRepository)
    }
    val addCategoryUseCase: com.sekota.features.admin.domain.usecase.AddAdminCategoryUseCase by lazy {
        com.sekota.features.admin.domain.usecase.AddAdminCategoryUseCase(adminRepository)
    }
    val deleteCategoryUseCase: com.sekota.features.admin.domain.usecase.DeleteAdminCategoryUseCase by lazy {
        com.sekota.features.admin.domain.usecase.DeleteAdminCategoryUseCase(adminRepository)
    }

    val getWhySekotaUseCase: com.sekota.features.admin.domain.usecase.GetAdminWhySekotaUseCase by lazy {
        com.sekota.features.admin.domain.usecase.GetAdminWhySekotaUseCase(adminRepository)
    }
    val saveWhySekotaUseCase: com.sekota.features.admin.domain.usecase.SaveAdminWhySekotaUseCase by lazy {
        com.sekota.features.admin.domain.usecase.SaveAdminWhySekotaUseCase(adminRepository)
    }

    val getStrategicConversationUseCase: com.sekota.features.admin.domain.usecase.GetAdminStrategicConversationUseCase by lazy {
        com.sekota.features.admin.domain.usecase.GetAdminStrategicConversationUseCase(adminRepository)
    }
    val saveStrategicConversationUseCase: com.sekota.features.admin.domain.usecase.SaveAdminStrategicConversationUseCase by lazy {
        com.sekota.features.admin.domain.usecase.SaveAdminStrategicConversationUseCase(adminRepository)
    }

    val getFooterUseCase: com.sekota.features.admin.domain.usecase.GetAdminFooterUseCase by lazy {
        com.sekota.features.admin.domain.usecase.GetAdminFooterUseCase(adminRepository)
    }
    val saveFooterUseCase: com.sekota.features.admin.domain.usecase.SaveAdminFooterUseCase by lazy {
        com.sekota.features.admin.domain.usecase.SaveAdminFooterUseCase(adminRepository)
    }

    val navigationCoordinator: NavigationCoordinator by lazy { NavigationCoordinator() }
}
