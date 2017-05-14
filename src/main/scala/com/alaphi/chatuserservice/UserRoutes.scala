package com.alaphi.chatuserservice

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, path, post}
import akka.http.scaladsl.server.StandardRoute
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._

import scala.util.{Failure, Success}

class UserRoutes(accountService: UserService) {

  val successHandler: PartialFunction[Any, StandardRoute] = {
    case Success(user: User) => complete(OK -> user)
  }

  val failureHandler: PartialFunction[Any, StandardRoute] = {
    case Failure(f) => complete(BadRequest -> f)
  }

  val routes = {
    path("users") {
      post {
        entity(as[UserCreation]) { accountCreation =>
          onComplete(accountService.create(accountCreation))(successHandler orElse failureHandler)
        }
      }
    }
  }

}

object UserRoutes {
  def apply(accountService: UserService): UserRoutes = new UserRoutes(accountService)
}

