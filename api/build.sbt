lazy val commonSettings = Seq(
  scalaVersion := "2.12.4",
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-Xfuture",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused",
    "-Ywarn-unused-import"
  ),
  // Run scalastyle upon compile
  (compile in Compile) := { (compile in Compile) dependsOn (scalastyle in Compile).toTask("") }.value,
  (compile in Test) := { (compile in Test) dependsOn (scalastyle in Test).toTask("") }.value
)



val root = project.in(file("."))
  .settings(
    name := "file-stats",
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5"
    ).map( _ % Test)
  )
  .settings(commonSettings)
