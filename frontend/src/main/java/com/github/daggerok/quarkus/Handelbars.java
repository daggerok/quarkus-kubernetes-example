package com.github.daggerok.quarkus;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.ext.web.templ.handlebars.HandlebarsTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static io.vertx.core.http.HttpHeaders.TEXT_HTML;

@Singleton
public class Handelbars {

    @Inject
    Vertx vertx;

    private TemplateEngine engine;

    @PostConstruct
    public void init() {
        engine = HandlebarsTemplateEngine.create(vertx);
    }

    public void render(RoutingContext rc, String template, Object... kv) {
        if (Objects.isNull(kv) || kv.length % 2 != 0)
            throw new RuntimeException("Incorrect context key-values");

        JsonObject context = new JsonObject();
        for (int i = 0; i < kv.length; ) {
            String key = (String) kv[i++];
            Object value = kv[i++];
            context = context.put(key, value);
        }

        render(rc, template, context);
    }

    public void render(RoutingContext rc, String template, JsonObject context) {
        engine.render(context, String.format("templates/%s", template), event -> {
            if (event.failed()) {
                rc.response()
                  .putHeader(CONTENT_TYPE, "application/json")
                  .setStatusCode(400)
                  .end(event.cause().getLocalizedMessage());
                return;
            }
            rc.response()
              .putHeader(CONTENT_TYPE, TEXT_HTML)
              .end(event.result());
        });
    }
}
