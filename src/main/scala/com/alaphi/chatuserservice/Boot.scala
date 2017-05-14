package com.alaphi.chatuserservice

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Boot extends App {

  implicit val system = ActorSystem("ChatUserService")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val userService = UserService()

  val routes = UserRoutes(userService).routes

  val bindingFuture = Http().bindAndHandle(routes, "0.0.0.0", 8081)

}
