package com.unciv.logic.event

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.lang.ref.WeakReference

class EventBusTest {
    open class Parent : Event
    class Child : Parent()

    @Test
    fun `should receive parent event once when receiving child event`() {
        val events = EventBus.EventReceiver()
        var callCount = 0
        events.receive(Parent::class) { ++callCount }

        EventBus.send(Child())

        assertThat(callCount, `is`(1))
    }

    @Test
    fun `should not receive parent event when listening to child event`() {
        val events = EventBus.EventReceiver()
        var callCount = 0
        events.receive(Child::class) { callCount++ }

        EventBus.send(Parent())

        assertThat(callCount, `is`(0))
    }

    @Test
    fun `should stop listening to events when requested`() {
        val events = EventBus.EventReceiver()
        var callCount = 0
        events.receive(Child::class) { callCount++ }

        EventBus.send(Child())
        events.stopReceiving()
        EventBus.send(Child())

        assertThat(callCount, `is`(1))
    }
}
