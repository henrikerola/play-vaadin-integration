organization := "org.vaadin.playintegration"

name := "play-vaadin-integration"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.4"

resolvers ++= Seq(
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Scaladin Snapshots" at "http://henrikerola.github.io/repository/snapshots/",
  "Vaadin addons" at "http://maven.vaadin.com/vaadin-addons"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.3.7",
  "com.typesafe.play" %% "play-cache" % "2.3.7",
  "com.vaadin" % "vaadin-server" % "7.3.7",
  "org.vaadin.addons" % "scaladin" % "3.1.0",
  "javax.servlet" % "javax.servlet-api" % "3.0.1"
)

publishTo := Some(Resolver.file("GitHub", file(Option(System.getProperty("play-vaadin-integration.repository.path")).getOrElse("../henrikerola.github.io/repository/snapshots"))))

scalariformSettings

vaadinAddOnSettings
