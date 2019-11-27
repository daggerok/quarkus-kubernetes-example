package com.github.daggerok.quarkus;

import io.quarkus.vertx.http.runtime.filters.Filters;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomHeadersInterceptor {

    private static final int PRIORITY = 100;
    private static final String HOSTNAME =
            Optional.ofNullable(System.getenv("HOSTNAME"))
                    .orElse(Optional.of(UUID.randomUUID())
                                    .map(UUID::toString)
                                    .map("env.HOSTNAME is not defined: "::concat)
                                    .get());

    public void addHeaders(@Observes Filters filters) {
        filters.register(this::addHostnameAndPath, PRIORITY);
    }

    private void addHostnameAndPath(RoutingContext rc) {
        HttpServerRequest request = rc.request();
        rc.response()
          .putHeader("X-HOSTNAME", HOSTNAME)
          .putHeader("X-PATH", request.path())
          .putHeader("X-URL", request.absoluteURI());
        rc.next();
    }
}
