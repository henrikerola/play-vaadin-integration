# Play Integration for Vaadin

Run your [Vaadin](http://vaadin.com) applications inside the [Play!](http://www.playframework.com/) Framework.

## Requirements

* [Play](http://www.playframework.com/) 2.1
* [Vaadin](http://vaadin.com) 7.1
* ([Scaladin](https://github.com/henrikerola/scaladin) 3.0-SNAPSHOT)

## Running Vaadin applications inside Play!

1. Use an existing Play! application or create a new by saying `play new PlayVaadinExample`.

2. Add dependencies and specify `VAADIN` as an asset directory in `project/Build.scala`:

       import sbt._
       import Keys._
       import play.Project._

       object ApplicationBuild extends Build {

         val appName         = "PlayVaadinExample"
         val appVersion      = "1.0-SNAPSHOT"

         val appDependencies = Seq(
           "com.vaadin" % "vaadin-server" % "7.1.5",
           "com.vaadin" % "vaadin-client-compiled" % "7.1.5",
           "com.vaadin" % "vaadin-themes" % "7.1.5",
           // TODO: Play integration for Vaadin depencency
           // Add your project dependencies here,
           javaCore,
           javaJdbc,
           javaEbean
         )

         val main = play.Project(appName, appVersion, appDependencies).settings(
           resolvers += "Vaadin Snapshots" at "https://oss.sonatype.org/content/repositories/vaadin-snapshots/",
           playAssetsDirectories <+= baseDirectory / "VAADIN"
           // Add your own project settings here      
         )

       }


3. To hook up Vaadin to Play's request handling mechanism, create a `Global` object in the default package and let it extend `VaadinSupport` (`app/Global.scala`):

       import org.vaadin.playintegration.VaadinSupport
       import play.api.GlobalSettings

       object Global extends GlobalSettings with VaadinSupport
    
    Note, this must be a Scala object even when you are using the Java API of Play.

4. Create a class extending `com.vaadin.ui.UI` in `app/example`:

       package example;

       import com.vaadin.server.VaadinRequest;
       import com.vaadin.ui.*;

       public class MyVaadinUI extends UI {

           @Override
           protected void init(VaadinRequest vaadinRequest) {
               setContent(new Button("Click me!", new Button.ClickListener() {
                   @Override
                   public void buttonClick(Button.ClickEvent event) {
                       Notification.show("Hello World!");
                   }
               }));
           }
       }


5. Define a Vaadin application in `conf/application.conf`:

       vaadin.myapp.ui = example.MyVaadinUI
       vaadin.myapp.path = /my-vaadin-app

After the above steps, there should be a Vaadin application responding in `http://localhost:9000/my-vaadin-app/` when you run Play with `play run`.

## Running Scaladin applications inside Play!

Scaladin is a wrapper library that provides a pure Scala API for Vaadin.

1. Use an existing Play! application or create a new by saying `play new PlayScaladinExample`.

2. Add dependencies and specify `VAADIN` as an asset directory in `project/Build.scala`:

       import sbt._
       import Keys._
       import play.Project._

       object ApplicationBuild extends Build {

         val appName         = "PlayScaladinExample"
         val appVersion      = "1.0-SNAPSHOT"

         val appDependencies = Seq(
           "com.vaadin" % "vaadin-server" % "7.1.5",
           "com.vaadin" % "vaadin-client-compiled" % "7.1.5",
           "com.vaadin" % "vaadin-themes" % "7.1.5",
           "vaadin.scala" %% "scaladin" % "3.0-SNAPSHOT",
           // TODO: Play Integration for Vaadin depencency
           // Add your project dependencies here,
           jdbc,
           anorm
         )


         val main = play.Project(appName, appVersion, appDependencies).settings(
           resolvers += "Vaadin Snapshots" at "https://oss.sonatype.org/content/repositories/vaadin-snapshots/",
           resolvers += "Scaladin Snapshots" at "http://henrikerola.github.io/repository/snapshots/",
           playAssetsDirectories <+= baseDirectory / "VAADIN"
           // Add your own project settings here
         )

       }


3. To hook up Vaadin to Play's request handling mechanism, create a `Global` object in the default package and let it extend `VaadinSupport` (`app/Global.scala`):

       import org.vaadin.playintegration.VaadinSupport
       import play.api.GlobalSettings

       object Global extends GlobalSettings with VaadinSupport
    
    Note, this must be a Scala object even when you are using the Java API of Play.

4. Create a class extending `vaadin.scala.UI` in `app/example`:

       package example

       import vaadin.scala._

       class MyScaladinUI extends UI {
         content = Button("Click me!", Notification.show("Hello World!"))
       }

5. Define a Scaladin application in `conf/application.conf`:

       scaladin.myapp.ui = example.MyScaladinUI
       scaladin.myapp.path = /my-scaladin-app

After the above steps, there should be a Vaadin application responding in `http://localhost:9000/my-scaladin-app/` when you run Play with `play run`.


## License

The project is licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
