# Lupakartta - Fishing permits on map

> As (eventually) seen on _[Lupakartta.fi](http://lupakartta.fi)/[.eu](http://lupakartta.eu)/[.com](http://lupakartta.com)/[.net](http://lupakartta.net)/[.org](http://lupakartta.org)/[.info](http://lupakartta.info)_

Welcome to Lupakartta monorepository root!

# How to navigate this repository

 - This README describes how to build and deploy everything as it represents the _whole project_
 - Each _defined subdirectory_ has its own README which describes the contents beneath

The subdirectories in no particular order are
 - [./apps](./apps) - UI and microservices - that is, applications of all sorts
 - [./libs](./libs) - libraries and shared components; all code used in two or more applications
 - [./configs](.configs) -  Configuration files for applications and whatnot
 
The root of this repository also contains

 - [docker-compose.yml](docker-compose.yml) which has definitions for all Docker services, networks etc. needed to run the whole thing
 - [.editorconfig](.editorconfig) because it is [Awesome](https://editorconfig.org/)

This project is 100% Clojure|Sript and uses the [Leiningen](https://leiningen.org/) ecosystem for builds.

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

# Mad Props

In order of discovery:

 - [dariooddenino/re-frame-sente](https://github.com/dariooddenino/re-frame-sente/) without this repo the frontend would not use WebSockets
 - [https://github.com/pardahlman/docker-rabbitmq-cluster](pardahlman/docker-rabbitmq-cluster) used as basis for RabbitMQ which enables the microservices to work as fully reactive apps
