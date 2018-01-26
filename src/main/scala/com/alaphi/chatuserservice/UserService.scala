package com.alaphi.chatuserservice

trait UserServiceComponent[M[_]] {

  def userService: UserService

  trait UserService {
    def create(userCreation: UserCreation): M[User]

    def read(username: String): M[User]

    def update(username: String, user: User): M[User]

    def delete(username: String): M[Boolean]
  }

}

trait UserServiceImplComponent[M[_]] extends UserServiceComponent[M] {
  this: UserRepositoryComponent[M] =>

  class UserServiceImpl extends UserService {
    def create(userCreation: UserCreation): M[User] = {
      userRepository.create(userCreation)
    }

    def read(username: String): M[User] = {
      userRepository.read(username)
    }

    def update(username: String, user: User): M[User] = {
      userRepository.update(username, user)
    }

    def delete(username: String): M[Boolean] = {
      userRepository.delete(username)
    }
  }

}
