package org.vaadin.playintegration

import com.vaadin.util.CurrentInstance
import java.util.UUID
import play.api.mvc._
import play.api.libs.iteratee.Enumerator
import play.api.cache.Cache
import play.api.Play.current
import java.io.ByteArrayOutputStream

object VaadinPlayRequestHandler {

  def current: VaadinPlayRequestHandler = CurrentInstance.get(classOf[VaadinPlayRequestHandler])

  def current_=(current: VaadinPlayRequestHandler) {
    CurrentInstance.setInheritable(classOf[VaadinPlayRequestHandler], current)
  }
}

/**
 * @author Henri Kerola / Vaadin
 */
class VaadinPlayRequestHandler(deploymentConfiguration: PlayDeploymentConfiguration) {

  CurrentInstance.clearAll()
  VaadinPlayRequestHandler.current = this

  val service: VaadinPlayService = createVaadinPlayService(deploymentConfiguration)
  service.init()
  service.setCurrentInstances(null, null)
  playPluginInitialized()

  CurrentInstance.clearAll()

  protected def playPluginInitialized() {
    // Empty by default
  }

  def handleRequest(request: Request[AnyContent]): Result = {
    val confPathWithSlash = deploymentConfiguration.getPath + "/"
    request.path.startsWith(confPathWithSlash)
    if (!request.path.startsWith(confPathWithSlash)) {
      // requested path must end with slash
      Results.Redirect(confPathWithSlash, request.queryString)
    } else {

      val (playSession, response) = doHandleRequest(request)

      SimpleResult(
        header = ResponseHeader(response.statusCode, response.headersMap.toMap),
        body = Enumerator(response.outputStream.asInstanceOf[ByteArrayOutputStream].toByteArray)).withSession(playSession)
    }
  }

  def doHandleRequest(request: Request[AnyContent]): (Session, VaadinPlayResponse) = {
    val sessionId = request.session.get("vaadinPlaySession") getOrElse UUID.randomUUID.toString
    val playSession = request.session + ("vaadinPlaySession" -> sessionId)

    val vaadinPlaySession = retrieveVaadinPlaySession(sessionId)
    vaadinPlaySession.onAfterRetrieve()

    CurrentInstance.clearAll()

    val vaadinRequest = createVaadinPlayRequest(request, vaadinPlaySession)
    val vaadinResponse = createVaadinPlayResponse()

    // TODO: ensureCookiesEnabled
    try {
      service.handleRequest(vaadinRequest, vaadinResponse)
    } finally {
      vaadinResponse.cleanUp()
      vaadinPlaySession.onBeforeStore()
      storeVaadinPlaySession(sessionId, vaadinPlaySession, deploymentConfiguration.getSessionTimeout)
    }

    (playSession, vaadinResponse)
  }

  protected def createVaadinPlayService(deploymentConfiguration: PlayDeploymentConfiguration): VaadinPlayService =
    new VaadinPlayService(this, deploymentConfiguration)

  protected def createVaadinPlayRequest(request: Request[AnyContent], session: VaadinPlaySession): VaadinPlayRequest =
    new VaadinPlayRequest(request, service, deploymentConfiguration.getPath, session)

  protected def createVaadinPlayResponse(): VaadinPlayResponse =
    new VaadinPlayResponse(service)

  protected def retrieveVaadinPlaySession(sessionId: String): VaadinPlaySession =
    Cache.get(sessionId).getOrElse(new DefaultVaadinPlaySession(sessionId)).asInstanceOf[VaadinPlaySession]

  protected def storeVaadinPlaySession(sessionId: String, vaadinPlaySession: VaadinPlaySession, sessionTimeout: Int) {
    Cache.set(sessionId, vaadinPlaySession, sessionTimeout)
  }

}