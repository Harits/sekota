package id.sekota

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedCommonTest {

    @Test
    fun greetingUsesPlatformName() {
        val platform = getPlatform().name
        assertTrue(platform.isNotBlank(), "platform name must not be blank")
        assertEquals("Hello, $platform!", Greeting().greet())
    }

    @Test
    fun coreGreetingIsShared() {
        assertEquals("Hello, Sekota!", sayHello("Sekota"))
    }
}
