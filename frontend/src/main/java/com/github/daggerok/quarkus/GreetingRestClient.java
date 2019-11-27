package com.github.daggerok.quarkus;

import io.reactivex.Single;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class GreetingRestClient {

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "backend.host")
    Optional<String> backendHost;

    @ConfigProperty(name = "backend.port")
    Optional<String> backendPort;

    WebClient client;

    @PostConstruct
    void initialize() {
        String host = backendHost.orElse("127.0.0.1");
        int port = backendPort.map(Integer::parseInt).orElse(8081);
        WebClientOptions options = new WebClientOptions().setDefaultHost(host)
                                                         .setDefaultPort(port)
                                                         .setSsl(false);
        client = WebClient.create(vertx, options);
    }

    public Single<String> greeting() {
        return client.get("/")
                     .rxSend()
                     .map(HttpResponse::bodyAsString)
                     .onErrorReturn(Throwable::getLocalizedMessage);
    }
}
