package id.sekota

import kotlin.test.Test
import kotlin.test.assertTrue

class SharedLogicWebTest {

    @Test
    fun webPlatformIsDetected() {
        assertTrue(getPlatform().name.isNotBlank(), "web platform name must not be blank")
    }
}
