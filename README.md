# Lupakartta - Fishing permits on map

> As (eventually) seen on _[Lupakartta.fi](http://lupakartta.fi)/[.eu](http://lupakartta.eu)/[.com](http://lupakartta.com)/[.net](http://lupakartta.net)/[.org](http://lupakartta.org)/[.info](http://lupakartta.info)_

Welcome to Lupakartta monorepository root!

For UI and microservices see [./apps](./apps), libraries see [.libs](./libs) and for runtime configs go to [./configs](.configs).

# Packaging

Lupakartta in its entirety is meant to be run as [Docker Stack](https://docs.docker.com/get-started/part5/) on [Docker Swarm](https://docs.docker.com/get-started/part4/).

To build everything execute
```sh
lein monolith each do clean, install &&
lein monolith each :select :deployable uberjar &&
docker-compose build
```
and to run Lupakartta execute
```sh
docker stack deploy -c docker-compose.yml lupakartta
```

# Messaging Architecture

Lupakartta is built to be reactive and functional which is why there's quite a bit of plumbing in this repository.

 - UI communicates with backend using persistent WebSocket connection
 - On startup microservices announce themselves to all other services; this causes them to send their current capability descriptors to all listeners

# Mad Props

In order of discovery:

 - [dariooddenino/re-frame-sente](https://github.com/dariooddenino/re-frame-sente/) without this repo the frontend would not use WebSockets
 - [https://github.com/pardahlman/docker-rabbitmq-cluster](pardahlman/docker-rabbitmq-cluster) used as basis for RabbitMQ which enables the microservices to work as fully reactive apps
