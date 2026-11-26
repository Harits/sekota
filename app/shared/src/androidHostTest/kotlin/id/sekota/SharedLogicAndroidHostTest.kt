package id.sekota

import kotlin.test.Test
import kotlin.test.assertTrue

class SharedLogicAndroidHostTest {

    @Test
    fun androidPlatformIsDetected() {
        assertTrue(getPlatform().name.isNotBlank(), "android platform name must not be blank")
    }
}
