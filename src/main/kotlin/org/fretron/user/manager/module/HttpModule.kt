package org.fretron.user.manager.module

import dagger.Module
import dagger.Provides
import org.fretron.user.manager.resource.UserResource
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import javax.inject.Named
import javax.ws.rs.core.UriBuilder

@Module
class HttpModule {

    @Provides
    fun provideResource(userResource: UserResource): ResourceConfig {
        return ResourceConfig().register(userResource)
    }

    @Provides
    fun server(@Named("host.url") host: String, @Named("host.port") port: Int, config: ResourceConfig): HttpServer {
        val url = UriBuilder.fromUri(host).port(port).build()
        return GrizzlyHttpServerFactory.createHttpServer(
            url,
            config
        )
    }

}