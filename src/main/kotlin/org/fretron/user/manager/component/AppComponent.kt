package org.fretron.user.manager.component

import dagger.Component
import org.fretron.user.manager.module.ConfigModule
import org.fretron.user.manager.module.HttpModule
import org.fretron.user.manager.module.SchemaModule
import org.fretron.user.manager.module.RepositoryModule
import org.fretron.user.manager.module.ServiceModule
import org.glassfish.grizzly.http.server.HttpServer
import javax.inject.Singleton

@Singleton
@Component(modules = [ConfigModule::class, HttpModule::class, SchemaModule::class, ServiceModule::class, RepositoryModule::class])
interface AppComponent {
    fun server(): HttpServer
}