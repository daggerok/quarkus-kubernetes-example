package com.github.daggerok.quarkus;

import io.quarkus.vertx.web.Route;
import io.reactivex.functions.Consumer;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class VertxRoutes {

    @Inject
    Handelbars handelbars;

    @Inject
    GreetingRestClient greetingClient;

    @Route(path = "/*", methods = HttpMethod.GET)
    public void handle(RoutingContext rc) {
        // greetingClient.greeting().subscribe((bufferHttpResponse, throwable) -> {
        //     System.out.println("ololo");
        //     String message = Optional.ofNullable(throwable)
        //                              .map(Throwable::getLocalizedMessage)
        //                              .orElse(bufferHttpResponse.bodyAsString());
        //     handelbars.render(rc, "index", "message", message);
        // });
        Consumer<String> view = message -> handelbars.render(rc, "index", "message", message);
        greetingClient.greeting().subscribe(view);
    }
}
