package com.alaphi.chatuserservice

import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.mockito.MockitoSugar
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.parser.parse
import io.circe.generic.auto._
import io.circe.syntax._
import org.mockito.Mockito._
import akka.util.ByteString
import io.circe.Json
import scala.concurrent.Future

import UserRoutesSpec._

class UserRoutesSpec extends WordSpec with Matchers with MockitoSugar with ScalatestRouteTest {
  val userService = mock[UserService]

  val userRoutes = UserRoutes(userService).routes

  when(userService.create(createUser)).thenReturn(Future.successful(Right(user)))

  "UserRoutes" should {

    "Create a User" in {
      Post("/users", HttpEntity(MediaTypes.`application/json`, createUserJson)) ~> userRoutes ~> check {
        status shouldBe OK
        responseAs[User].asJson.noSpaces shouldBe userJsonNoSpaces
      }
    }

  }

}

object UserRoutesSpec {
  val createUser = UserCreation("123abc", "Joey", "JoeJoe")
  val user = User("123abc", "Joey", "JoeJoe")

  val createUserJson = ByteString(
    s"""
       |{
       |    "username":"123abc",
       |    "forename":"Joey",
       |    "surname":"JoeJoe"
       |}
        """.stripMargin)

  val userJson =
    s"""
       |{
       |    "username":"123abc",
       |    "forename":"Joey",
       |    "surname":"JoeJoe"
       |}
        """.stripMargin

  val userJsonNoSpaces = parse(userJson).getOrElse(Json.Null).noSpaces
}

