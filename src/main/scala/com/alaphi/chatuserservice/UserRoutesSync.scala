package com.alaphi.chatuserservice

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.http.scaladsl.server.StandardRoute
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait UserRoutesSync {
  this: UserServiceComponent[Try] =>

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
    path("syncusers") {
      post {
        entity(as[UserCreation]) { userCreation =>
          onComplete {
            userService.create(userCreation) match {
              case Success(res) => Future.successful(res)
              case Failure(e) => Future.failed(e)
            }
          }(successHandler orElse userOpFailureHandler orElse failureHandler)
        }
      }
    } ~
      path("syncusers" / Segment) { username =>
        get(onComplete {
          userService.read(username) match {
            case Success(res) => Future.successful(res)
            case Failure(e) => Future.failed(e)
          }
        }(successHandler orElse userOpFailureHandler orElse failureHandler))
      }
  }
}

