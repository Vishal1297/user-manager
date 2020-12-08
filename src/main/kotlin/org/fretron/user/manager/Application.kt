package org.fretron.user.manager

import org.fretron.user.manager.component.DaggerAppComponent

fun main() {
    val component = DaggerAppComponent.builder().build()
    val httpServer = component.server()
    httpServer.start()
}