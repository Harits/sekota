package id.sekota

import kotlin.test.Test
import kotlin.test.assertTrue

class SharedLogicDesktopTest {

    @Test
    fun desktopPlatformIsDetected() {
        assertTrue(getPlatform().name.startsWith("Java"), getPlatform().name)
    }
}
