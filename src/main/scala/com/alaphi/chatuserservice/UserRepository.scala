package com.alaphi.chatuserservice

import scala.concurrent.Future
import scala.util.Try


trait UserRepositoryComponent[M[_]] {

  def userRepository: UserRepository

  trait UserRepository {
    def create(userCreation: UserCreation): M[User]

    def read(username: String): M[User]

    def update(username: String, user: User): M[User]

    def delete(username: String): M[Boolean]
  }

}

trait UserRepositoryDBComponent extends UserRepositoryComponent[Future] {

  class UserRepositoryDB extends UserRepository {
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
          username = "AAAAA",
          forename = "BBBBB",
          surname = "CCCCC"
        )
      )
    }

    def update(username: String, user: User): Future[User] = {
      Future.successful(user)
    }

    def delete(username: String): Future[Boolean] = {
      Future.successful(true)
    }
  }

}


trait UserRepositoryHashMapComponent extends UserRepositoryComponent[Option] {

  class UserRepositoryHashMap extends UserRepository {
    def create(userCreation: UserCreation): Option[User] = {
      Some(
        User(
          username = userCreation.username,
          forename = userCreation.forename,
          surname = userCreation.surname
        )
      )
    }

    def read(username: String): Option[User] = {
      Some(
        User(
          username = "ZZZZ",
          forename = "XXXXX",
          surname = "GGGGG"
        )
      )
    }

    def update(username: String, user: User): Option[User] = {
      Some(user)
    }

    def delete(username: String): Some[Boolean] = {
      Some(true)
    }
  }

}

trait UserRepositorySyncComponent extends UserRepositoryComponent[Try] {

  class UserRepositorySync extends UserRepository {
    def create(userCreation: UserCreation): Try[User] = {
      Try(
        User(
          username = userCreation.username,
          forename = userCreation.forename,
          surname = userCreation.surname
        )
      )
    }

    def read(username: String): Try[User] = {
      Try(
        User(
          username = "wwwww",
          forename = "eeeee",
          surname = "rrrrr"
        )
      )
    }

    def update(username: String, user: User): Try[User] = {
      Try(user)
    }

    def delete(username: String): Try[Boolean] = {
      Try(true)
    }
  }

}
