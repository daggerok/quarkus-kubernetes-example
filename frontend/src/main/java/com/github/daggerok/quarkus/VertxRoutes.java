package com.github.daggerok.quarkus;

import io.quarkus.vertx.web.Route;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class VertxRoutes {

    @Inject
    Handelbars handelbars;

    @Route(path = "/*", methods = HttpMethod.GET)
    public void handle(RoutingContext rc) {
        handelbars.render(rc, "index.hbs", "message", "ololo");
    }
}
