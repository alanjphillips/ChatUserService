package com.alaphi.chatuserservice

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.http.scaladsl.server.StandardRoute
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait UserRoutesHashMap {
  this: UserServiceComponent[Option] =>

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
    path("optusers") {
      post {
        entity(as[UserCreation]) { userCreation =>
          onComplete {
            userService.create(userCreation) match {
              case Some(res) => Future.successful(res)
              case None => Future.failed(new Exception("create failed"))
            }
          }(responseHandler)
        }
      }
    } ~
      path("optusers" / Segment) { username =>
        get(onComplete {
          userService.read(username) match {
            case Some(res) => Future.successful(res)
            case None => Future.failed(new Exception("read failed"))
          }
        }(responseHandler))
      }
  }
}

