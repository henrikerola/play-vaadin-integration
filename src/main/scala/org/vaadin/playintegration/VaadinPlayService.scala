package org.vaadin.playintegration

import com.vaadin.ui.UI
import play.api.Play
import play.api.Play.current
import java.io.File
import com.vaadin.server.communication.{ ServletUIInitHandler, ServletBootstrapHandler }
import com.vaadin.server._

/**
 * @author Henri Kerola / Vaadin
 */
class VaadinPlayService(plugin: VaadinPlayRequestHandler, deploymentConfiguration: PlayDeploymentConfiguration)
    extends VaadinService(deploymentConfiguration) {

  if (getClassLoader == null) {
    setClassLoader(Play.classloader)
  }

  override def getStaticFileLocation(request: VaadinRequest): String = deploymentConfiguration.getStaticFileLocation

  override def getConfiguredWidgetset(request: VaadinRequest): String = deploymentConfiguration.getWidgetset

  override def getConfiguredTheme(request: VaadinRequest): String = Constants.DEFAULT_THEME_NAME

  override def isStandalone(request: VaadinRequest): Boolean = deploymentConfiguration.isStandalone

  override def getMimeType(resourceName: String): String = null // TODO

  override def getBaseDirectory: File = null // TODO

  override def requestCanCreateSession(request: VaadinRequest): Boolean =
    ServletUIInitHandler.isUIInitRequest(request) || isOtherRequest(request)

  private[this] def isOtherRequest(request: VaadinRequest) =
    !ServletPortletHelper.isAppRequest(request) &&
      !ServletUIInitHandler.isUIInitRequest(request) &&
      !ServletPortletHelper.isFileUploadRequest(request) &&
      !ServletPortletHelper.isHeartbeatRequest(request) &&
      !ServletPortletHelper.isPublishedFileRequest(request) &&
      !ServletPortletHelper.isUIDLRequest(request) &&
      !ServletPortletHelper.isPushRequest(request)

  override def getServiceName: String = deploymentConfiguration.name

  override def getMainDivId(session: VaadinSession, request: VaadinRequest, uiClass: Class[_ <: UI]): String = {
    // expecting that getPath starts always with /, substring(1) removes it
    val appId = deploymentConfiguration.getPath.substring(1) match {
      case "" => "ROOT"
      case p => p
    }
    val hashCode = appId.replaceAll("[^a-zA-Z0-9]", "").hashCode

    appId + "-" + hashCode.abs
  }

  override def getThemeResourceAsStream(uI: UI, themeName: String, resource: String) = ???

  override def createRequestHandlers: java.util.List[RequestHandler] = {
    val handlers = super.createRequestHandlers
    handlers.add(0, new ServletBootstrapHandler)
    handlers.add(new ServletUIInitHandler)
    handlers
  }
}
