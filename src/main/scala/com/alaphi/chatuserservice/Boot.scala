package com.alaphi.chatuserservice

import akka.http.scaladsl.server.Directives._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}

object Boot extends App {

  implicit val system = ActorSystem("ChatUserService")
  implicit val materializer = ActorMaterializer()

  val routes = new UserRoutes with UserServiceImplComponent[Future] with UserRepositoryDBComponent {
    override def ec: ExecutionContext = system.dispatcher
    override def userService: UserService = new UserServiceImpl
    override def userRepository: UserRepository = new UserRepositoryDB
  }

  val otherRoutes = new UserRoutesHashMap with UserServiceImplComponent[Option] with UserRepositoryHashMapComponent {
    override def ec: ExecutionContext = system.dispatcher
    override def userService: UserService = new UserServiceImpl
    override def userRepository: UserRepository = new UserRepositoryHashMap
  }

  val allRoutes = routes.routes ~ otherRoutes.routes

  val bindingFuture = Http().bindAndHandle(allRoutes, "0.0.0.0", 8081)

}
