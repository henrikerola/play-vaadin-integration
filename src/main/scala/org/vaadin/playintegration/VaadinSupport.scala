package org.vaadin.playintegration

import play.api.{ Play, Application, GlobalSettings }
import collection.mutable
import play.api.mvc.{ Action, Handler, RequestHeader }
import scala.util.Try
import org.vaadin.playintegration.scaladin.ScaladinPlayRequestHandler

object VaadinSupport {
  trait SuperCalls {
    def onStart(app: Application)

    def onStop(app: Application)

    def onRouteRequest(request: RequestHeader): Option[Handler]
  }
}

/**
 * @author Henri Kerola / Vaadin
 */
trait VaadinSupport extends VaadinSupport.SuperCalls {
  self: GlobalSettings =>

  private[this] val mappings = mutable.Map.empty[String, VaadinPlayRequestHandler]

  abstract override def onStart(app: Application) {
    super.onStart(app)
    val conf = Play.configuration(app)

    def createMappings(isScaladin: Boolean, create: PlayDeploymentConfiguration => VaadinPlayRequestHandler) = {
      val configKey = if (isScaladin) "scaladin" else "vaadin"
      for {
        scaladinConf <- conf.getConfig(configKey)
        name <- scaladinConf.subKeys
        // Try(..) here because keys like "vaadin.foobar" throw exception in getString("vaadin.myapp.ui")
        ui <- Try(conf.getString(s"$configKey.$name.ui")) getOrElse None
        path <- Try(conf.getString(s"$configKey.$name.path")) getOrElse None
      } {
        val subConf = scaladinConf.getConfig(name).get
        val deploymentConf = createPlayDeploymentConfiguration(name, isScaladin, subConf)
        mappings += (path -> create(deploymentConf))
      }
    }

    createMappings(isScaladin = false, createVaadinPlayIntegration)
    createMappings(isScaladin = true, createScaladinPlayIntegration)
  }

  abstract override def onStop(app: Application) {
    mappings.clear()
  }

  abstract override def onRouteRequest(request: RequestHeader): Option[Handler] = {
    val serveStatic = Play.current.configuration.getBoolean("vaadin.serve_static_resources") getOrElse true
    if (serveStatic && request.path.startsWith("/VAADIN/")) {
      Option(controllers.Assets.at("/VAADIN", request.path.split("/VAADIN/")(1)))
    } else super.onRouteRequest(request) orElse {
      val mappingOption = mappings find { request.path startsWith _._1 }
      mappingOption map { m => Action { m._2.handleRequest(_) } }
    }
  }

  protected def createPlayDeploymentConfiguration(name: String,
    isScaladin: Boolean,
    conf: play.api.Configuration): PlayDeploymentConfiguration =
    new PlayDeploymentConfiguration(name, isStandalone = true, isScaladin, conf)

  protected def createVaadinPlayIntegration(deploymentConfiguration: PlayDeploymentConfiguration) =
    new VaadinPlayRequestHandler(deploymentConfiguration)

  protected def createScaladinPlayIntegration(deploymentConfiguration: PlayDeploymentConfiguration) =
    new ScaladinPlayRequestHandler(deploymentConfiguration)
}
