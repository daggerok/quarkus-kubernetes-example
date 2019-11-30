# quarkus k8s reactive vertx handlebars [![Build Status](https://travis-ci.org/daggerok/quarkus-kubernetes-example.svg?branch=master)](https://travis-ci.org/daggerok/quarkus-kubernetes-example)
Quarkus k8s

## docker (fabric8)

requires: `docker network create quarkus-kubernetes-example || echo oops`

### jvm

```bash
./mvnw -Pfabric8-jvm clean compile quarkus:build docker:build docker:start
#for id in $(docker ps -q) ; do docker inspect $id | jq '.[].NetworkSettings.Ports."8080/tcp"[].HostPort' -r ; done
#for id in $(docker ps -q) ; do http :$(docker inspect $id | jq '.[].NetworkSettings.Ports."8080/tcp"[].HostPort' -r) ; done
http :8080
http :8081
./mvnw -Pfabric8-jvm docker:stop docker:remove
```

### native

```bash
./mvnw -Pnative -Dquarkus.native.container-build=true clean compile quarkus:build
./mvnw -Pfabric8-native docker:build docker:start
#for id in $(docker ps -q) ; do docker inspect $id | jq '.[].NetworkSettings.Ports."8080/tcp"[].HostPort' -r ; done
#for id in $(docker ps -q) ; do http :$(docker inspect $id | jq '.[].NetworkSettings.Ports."8080/tcp"[].HostPort' -r) ; done
http :8080
http :8081
./mvnw -Pfabric8-native docker:stop docker:remove
```

## k8s

### jvm k8s in docker for mac / windows

```bash
./mvnw -Pfabric8-jvm clean compile quarkus:build docker:build docker:push
kubectl get pods -o wide -w &
kubectl apply -f k8s/ -f k8s/ingress/docker.yaml -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/mandatory.yaml -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/cloud-generic.yaml
# wait for bootstrap...
http :/
http :/backend
http :30080/
http :30081/
kubectl delete -f k8s/ -f k8s/ingress/docker.yaml -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/mandatory.yaml -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/cloud-generic.yaml
```

### jvm k8s in k3s in k3d

```bash
./mvnw -Pfabric8-jvm clean compile quarkus:build docker:build docker:push
k3d create --name k3s --api-port 6551 --publish 80:80 --publish 30080:30080 --publish 30081:30081 --workers 2
sleep 10s ; export KUBECONFIG="$(k3d get-kubeconfig --name='k3s')" ; kubectl get pods -o wide -w &
kubectl apply -f k8s/ -f k8s/ingress/traefik.yaml
# wait for bootstrap...
http :/
http :/backend
http :30080/
http :30081/
kubectl delete -f k8s/ -f k8s/ingress/traefik.yaml
k3d stop --name=k3s -a ; rm -rf ~/.config/k3d/k3s
```

### native k8s in docker for mac / windows

```bash
./mvnw -Pnative -Dquarkus.native.container-build=true clean compile quarkus:build
./mvnw -Pfabric8-native docker:build docker:push
kubectl get pods -o wide -w &
kubectl apply -f k8s/ -f k8s/ingress/docker.yaml -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/mandatory.yaml -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/cloud-generic.yaml
# wait for bootstrap...
http :/
http :/backend
http :30080/
http :30081/
kubectl delete -f k8s/ -f k8s/ingress/docker.yaml -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/mandatory.yaml -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/cloud-generic.yaml
```

### native k8s in k3s in k3d

```bash
./mvnw -Pnative -Dquarkus.native.container-build=true clean compile quarkus:build
./mvnw -Pfabric8-native docker:build docker:push
k3d create --name k3s --api-port 6551 --publish 80:80 --publish 30080:30080 --publish 30081:30081 --workers 2
sleep 10s ; export KUBECONFIG="$(k3d get-kubeconfig --name='k3s')" ; kubectl get pods -o wide -w &
kubectl apply -f k8s/ -f k8s/ingress/traefik.yaml
# wait for bootstrap...
http :/
http :/backend
http :30080/
http :30081/
kubectl delete -f k8s/ -f k8s/ingress/traefik.yaml
k3d stop --name=k3s -a ; rm -rf ~/.config/k3d/k3s ; docker rm -fv `docker ps -aq`
```

## features

* native app
* global headers
* mustache reactive vertx handlebars templating
* build and push docker images by using fabric8 docker-maven-plugin
* correct docker container CMD and ENTRYPOINT usage
* application is running in docker with qaurkus user
* k3d / k3s k8s CI / local testing
* k3d / k3s import-images from local docker [k3d import-images feature introduced by PR #83, works with rancher/k3s tag of v0.7.0-rc2 or later due to a hard requirement on ctr inside the k3s](https://github.com/rancher/k3d/releases/tag/v1.3.0-dev.0)

## resources

* [Quarkus UI Web development](https://github.com/kabir/blog-quarkus-ui-development)
* [vertx web examples](https://github.com/vert-x3/vertx-examples/tree/master/web-examples)
* [vertx templates](https://vertx.io/docs/vertx-web/java/#_templates)
* [vertx web](https://vertx.io/docs/vertx-web/java/)
* https://vertx.io/docs/vertx-web/java/#_capturing_path_parameters
* https://dmp.fabric8.io/#start-wait
* https://dmp.fabric8.io/#start-volumes
* https://habr.com/ru/company/southbridge/blog/329138/
* [Spring Boot -> Quarkus](https://dzone.com/articles/spring2quarkus-spring-boot-to-quarkus-migration)
