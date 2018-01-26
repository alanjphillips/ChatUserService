package com.alaphi.chatuserservice

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.http.scaladsl.server.StandardRoute
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait UserRoutes {
  this: UserServiceComponent[Future] =>

  implicit def ec: ExecutionContext

  val successHandler: PartialFunction[Any, StandardRoute] = {
    case Success(user: User) => complete(OK -> user)
    case Success(true)       => complete(OK -> true)
  }

  val userOpFailureHandler: PartialFunction[Any, StandardRoute] = {
    case Success(uof: UserOperationFailure) => complete(BadRequest -> uof)
    case Success(false)                     => complete(BadRequest -> false)
  }

  val failureHandler: PartialFunction[Any, StandardRoute] = {
    case Failure(f) => complete(BadRequest -> f)
  }

  val routes = {
    path("users") {
      post {
        entity(as[UserCreation]) { userCreation =>
          onComplete(userService.create(userCreation))(successHandler orElse userOpFailureHandler orElse failureHandler)
        }
      }
    } ~
      path("users" / Segment) { username =>
        get(onComplete(userService.read(username))(successHandler orElse userOpFailureHandler orElse failureHandler))
      } ~
      path("users" / Segment) { username =>
        put {
          entity(as[User]) { user =>
            onComplete(userService.update(username, user))(successHandler orElse userOpFailureHandler orElse failureHandler)
          }
        }
      } ~
      path("users" / Segment) { username =>
        delete(onComplete(userService.delete(username))(successHandler orElse userOpFailureHandler orElse failureHandler))
      }
  }

}

