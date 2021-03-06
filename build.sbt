/***************************************************************
  *      THIS IS A GENERATED FILE - EDIT AT YOUR OWN RISK      *
  **************************************************************
  *
  * Use the mainadm command to generate a new version of
  * this build file.
  *
  * See https://github.com/lightbend/course-management-tools
  * for more details
  *
  */

import sbt._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `akka-guidelines-master` = (project in file("."))
  .aggregate(
    `step_000_initial_state`,
    `step_001_add_mocking`
  )
  .settings(ThisBuild / scalaVersion := Version.scalaVersion)
  .settings(CommonSettings.commonSettings: _*)

lazy val `step_000_initial_state` = project
  .configure(CommonSettings.configure)

lazy val `step_001_add_mocking` = project
  .configure(CommonSettings.configure)
       