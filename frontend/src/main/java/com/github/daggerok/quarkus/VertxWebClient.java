package com.github.daggerok.quarkus;

import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class VertxWebClient {

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "backend.host")
    Optional<String> backendHost;

    @ConfigProperty(name = "backend.port")
    Optional<Integer> backendPort;

    WebClient client;

    @PostConstruct
    void initialize() {
        String host = backendHost.orElse("127.0.0.1");
        log.info("host: {} => {}", backendPort, host);
        Integer port = backendPort.orElse(8081);
        log.info("host: {} => {}", backendPort, port);
        WebClientOptions options = new WebClientOptions().setDefaultHost(host)
                                                         .setDefaultPort(port)
                                                         .setConnectTimeout(3333)
                                                         .setSsl(false)
                                                         .setProtocolVersion(HttpVersion.HTTP_1_1);
        client = WebClient.create(vertx, options);
    }

    @Produces
    @Singleton
    public WebClient client() {
        return client;
    }
}
