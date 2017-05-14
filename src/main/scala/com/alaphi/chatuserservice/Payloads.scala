package com.alaphi.chatuserservice

trait Payload

case class UserCreation(username: String, forename: String, surname: String) extends Payload
case class User(username: String, forename: String, surname: String) extends Payload
