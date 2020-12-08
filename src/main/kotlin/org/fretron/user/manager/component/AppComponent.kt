package org.fretron.user.manager.component

import dagger.Component
import org.fretron.user.manager.module.*
import org.glassfish.grizzly.http.server.HttpServer
import javax.inject.Singleton

@Singleton
@Component(modules = [ConfigModule::class, HttpModule::class, SchemaModule::class, ServiceModule::class, RepositoryModule::class])
interface AppComponent {
    fun server(): HttpServer
}