# quarkus k8s reactive vertx handlebars [![Build Status](https://travis-ci.org/daggerok/quarkus-kubernetes-example.svg?branch=master)](https://travis-ci.org/daggerok/quarkus-kubernetes-example)
Quarkus k8s

## jvm

```bash
./mvnw -Pfabric8-jvm clean compile quarkus:build docker:build docker:start
#for id in $(docker ps -q) ; do docker inspect $id | jq '.[].NetworkSettings.Ports."8080/tcp"[].HostPort' -r ; done
for id in $(docker ps -q) ; do http :$(docker inspect $id | jq '.[].NetworkSettings.Ports."8080/tcp"[].HostPort' -r) ; done
./mvnw -Pfabric8-jvm docker:stop docker:remove
```

## native

```bash
./mvnw -Pnative -Dquarkus.native.container-build=true clean compile quarkus:build
./mvnw -Pfabric8-native docker:build docker:start
#for id in $(docker ps -q) ; do docker inspect $id | jq '.[].NetworkSettings.Ports."8080/tcp"[].HostPort' -r ; done
for id in $(docker ps -q) ; do http :$(docker inspect $id | jq '.[].NetworkSettings.Ports."8080/tcp"[].HostPort' -r) ; done
./mvnw -Pfabric8-native docker:stop docker:remove
```

## features

* native app
* global headers
* mustache reactive vertx handlebars templating
* build and push docker images by using fabric8 docker-maven-plugin

## resources

* [vertx web examples](https://github.com/vert-x3/vertx-examples/tree/master/web-examples)
* [vertx templates](https://vertx.io/docs/vertx-web/java/#_templates)
* [vertx web](https://vertx.io/docs/vertx-web/java/)
* https://vertx.io/docs/vertx-web/java/#_capturing_path_parameters
* https://dmp.fabric8.io/#start-wait
* https://dmp.fabric8.io/#start-volumes
