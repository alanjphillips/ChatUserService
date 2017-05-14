Chat User Service
============================================
Chat User Rest Service. Provides user management functionality for users of Chat application. 
Uses Akka-Http toolkit and Circe to provide Rest Service functionality.

Install docker, docker-machine and docker-compose. See docker docs on how to create machine in virtualbox
https://docs.docker.com/machine/drivers/virtualbox/

Create a VM called 'default'

> docker-machine create --driver virtualbox default

Start up 'default' machine

> docker-machine start default

1) Connect to 'default' machine

> eval "$(docker-machine env default)"

2) CD into project and use SBT to build and publish to local Docker repo:

> sbt clean docker:publishLocal

3) Run docker compose to launch Chat User Service

> docker-compose up -d --no-recreate