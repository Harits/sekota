package id.sekota

import kotlin.test.Test
import kotlin.test.assertTrue

class SharedLogicIOSTest {

    @Test
    fun iosPlatformIsDetected() {
        assertTrue(getPlatform().name.isNotBlank(), "ios platform name must not be blank")
    }
}
