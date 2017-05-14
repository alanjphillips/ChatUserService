package com.alaphi.chatuserservice

import scala.concurrent.{ExecutionContext, Future}

class UserService(implicit ec: ExecutionContext) {

  def create(userCreation: UserCreation): Future[User] = {
    Future.successful(
      User(
        username = userCreation.username,
        forename = userCreation.forename,
        surname = userCreation.surname
      )
    )
  }

  def read(username: String): Future[User] = {
    Future.successful(
      User(
        username = "JJJJr",
        forename = "Joey",
        surname = "JoeJoe"
      )
    )
  }

  def update(username: String, user: User): Future[User] = {
    Future.successful(
      user
    )
  }

  def delete(username: String): Future[Boolean] = {
    Future.successful(
      true
    )
  }

}

object UserService {
  def apply()(implicit ec: ExecutionContext): UserService = new UserService
}