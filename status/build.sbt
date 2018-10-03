// import sbtcrossproject.{crossProject, CrossType}
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.1.2",
    "javax.xml.bind" % "jaxb-api" % "2.3.0",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.15.1-play26",
    guice,
    specs2 % Test
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
//  EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(PlayScala)
  .dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings)
    .settings(extraResolvers)
    .settings(
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.5",
    "com.cibo" %% "leaflet-facade_sjs0.6" % "1.1.0" % "compile" withSources()),
  ).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(commonSettings)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val extraResolvers = Seq(
  resolvers += Resolver.bintrayRepo("cibotech", "public"),
)

lazy val commonSettings: SettingsDefinition = Seq(
  scalaVersion := "2.12.5",
  organization := "com.regularoddity",
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}