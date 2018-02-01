package com.alaphi.chatuserservice

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.http.scaladsl.server.StandardRoute
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait UserRoutes {
  this: UserServiceComponent[Future] =>

  implicit def ec: ExecutionContext

  val successHandler: PartialFunction[Try[_], StandardRoute] = {
    case Success(user: User) => complete(OK -> user)
    case Success(true)       => complete(OK -> true)
  }

  val userOpFailureHandler: PartialFunction[Try[_], StandardRoute] = {
    case Success(uof: UserOperationFailure) => complete(BadRequest -> uof)
    case Success(false)                     => complete(BadRequest -> false)
  }

  val failureHandler: PartialFunction[Try[_], StandardRoute] = {
    case Failure(f) => complete(BadRequest -> f)
  }

  val responseHandler: PartialFunction[Try[_], StandardRoute] = successHandler orElse userOpFailureHandler orElse failureHandler

  val routes = {
    path("users") {
      post {
        entity(as[UserCreation]) { userCreation =>
          onComplete(userService.create(userCreation))(responseHandler)
        }
      }
    } ~
      path("users" / Segment) { username =>
        get(onComplete(userService.read(username))(responseHandler))
      } ~
      path("users" / Segment) { username =>
        put {
          entity(as[User]) { user =>
            onComplete(userService.update(username, user))(responseHandler)
          }
        }
      } ~
      path("users" / Segment) { username =>
        delete(onComplete(userService.delete(username))(responseHandler))
      }
  }

}

