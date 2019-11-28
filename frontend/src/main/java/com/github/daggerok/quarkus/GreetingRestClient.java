package com.github.daggerok.quarkus;

import io.reactivex.Single;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class GreetingRestClient {

    @Inject
    WebClient client;

    public Single<String> greeting() {
        return client.get("/")
                     .rxSend()
                     .map(HttpResponse::bodyAsString)
                     .doOnEvent((res, err) -> log.info("result: {} / error: {}",
                                                       Optional.ofNullable(res), Optional.ofNullable(err)))
                     .onErrorReturn(Throwable::getLocalizedMessage);
    }
}
