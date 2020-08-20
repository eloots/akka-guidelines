import sbt._

object Version {
  val logbackVer        = "1.2.3"
  val mUnitVer          = "0.7.9"
  val scalaVersion      = "2.13.3"
  val akkaVersion       = "2.6.8"
  val akkaHttpVersion   = "10.2.0"
}

object Dependencies {

  private val logbackDeps = Seq (
    "ch.qos.logback"                 %  "logback-classic",
  ).map (_ % Version.logbackVer)

  private val munitDeps = Seq(
    "org.scalameta" %% "munit" % Version.mUnitVer % Test
  )

  private val akkaDeps = Seq (
    "com.typesafe.akka"             %% "akka-actor-typed",
    "com.typesafe.akka"             %% "akka-cluster-typed",
    "com.typesafe.akka"             %% "akka-cluster-sharding-typed",
    "com.typesafe.akka"             %% "akka-persistence-typed",
    "com.typesafe.akka"             %% "akka-persistence-query",
    "com.typesafe.akka"             %% "akka-serialization-jackson",
  ).map (_ % Version.akkaVersion)

  private val akkaHttpDeps = Seq (
    "com.typesafe.akka"             %% "akka-http",
    "com.typesafe.akka"             %% "akka-http-spray-json"
  ).map (_ % Version.akkaHttpVersion)

  val dependencies: Seq[ModuleID] =
    logbackDeps ++
    akkaDeps ++
    munitDeps
}
