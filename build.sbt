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

lazy val `scala-master` = (project in file("."))
  .aggregate(
    `step_000_initial_state`
  )
  .settings(CommonSettings.commonSettings: _*)

lazy val `step_000_initial_state` = project
  .configure(CommonSettings.configure)
       