organization := "org.vaadin.playintegration"

name := "play-vaadin-integration"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.2"

resolvers ++= Seq(
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Scaladin Snapshots" at "http://henrikerola.github.io/repository/snapshots/",
  "Vaadin addons" at "http://maven.vaadin.com/vaadin-addons"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.2.0",
  "com.typesafe.play" %% "play-cache" % "2.2.0",
  "com.vaadin" % "vaadin-server" % "7.1.5",
  "org.vaadin.addons" % "scaladin" % "3.0.0",
  "javax.servlet" % "servlet-api" % "2.4"
)

publishTo := Some(Resolver.file("GitHub", file(Option(System.getProperty("play-vaadin-integration.repository.path")).getOrElse("../henrikerola.github.io/repository/snapshots"))))

scalariformSettings

vaadinAddOnSettings
