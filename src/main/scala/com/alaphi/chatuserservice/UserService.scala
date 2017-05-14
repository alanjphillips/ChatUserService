package com.alaphi.chatuserservice

import scala.concurrent.{ExecutionContext, Future}

class UserService(implicit ec: ExecutionContext) {

  def create(userCreation: UserCreation): Future[User] = {
    Future.successful(
      User(
        number = (userCreation.forename + userCreation.surname).hashCode.toString,
        username = userCreation.username,
        forename = userCreation.forename,
        surname = userCreation.surname
      )
    )
  }

}

object UserService {
  def apply()(implicit ec: ExecutionContext): UserService = new UserService
}