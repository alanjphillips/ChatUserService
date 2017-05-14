enablePlugins(JavaAppPackaging)

name := "ChatUserService"

version := "1.0"

scalaVersion := "2.12.2"

val circeVersion     = "0.7.0"
val httpCirceVersion = "1.11.0"
val akkaVersion      = "2.4.17"
val akkaHttpVersion  = "10.0.4"

resolvers ++= Seq(
  Resolver.bintrayRepo("hseeberger", "maven")
)

libraryDependencies ++= Seq(
  "de.heikoseeberger"         %% "akka-http-circe"        % httpCirceVersion,
  "io.circe"                  %% "circe-core"             % circeVersion,
  "io.circe"                  %% "circe-generic"          % circeVersion,
  "io.circe"                  %% "circe-parser"           % circeVersion,
  "com.typesafe.akka"         %% "akka-actor"             % akkaVersion,
  "com.typesafe.akka"         %% "akka-stream"            % akkaVersion,
  "com.typesafe.akka"         %% "akka-http"              % akkaHttpVersion
)

packageName in Docker := "chatuserservice"
version in Docker     := "latest"
        