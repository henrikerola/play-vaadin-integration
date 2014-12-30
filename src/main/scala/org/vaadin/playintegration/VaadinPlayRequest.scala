package org.vaadin.playintegration

import play.api.mvc.{ AnyContent, Request }
import collection.JavaConverters._
import java.io._
import play.api.http.HeaderNames
import java.util.{ UUID, Locale }
import java.security.Principal
import scala.Some
import com.vaadin.server._
import scala.Some
import scala.Some

/**
 * @author Henri Kerola / Vaadin
 */
class VaadinPlayRequest(
  request: Request[AnyContent],
  vaadinService: VaadinPlayService,
  uri: String,
  session: VaadinPlaySession)
    extends VaadinRequest {

  private[this] lazy val inputStream: InputStream = request.body match {
    case AnyContentAsText(txt) => new ByteArrayInputStream(txt.getBytes)
    case AnyContentAsJson(json) => new ByteArrayInputStream(json.toString().getBytes)
    case AnyContentAsMultipartFormData(mdf) => mdf.files.headOption.map { a =>
      // FIXME: this is far from an optimal solution
      val file = File.createTempFile(UUID.randomUUID.toString, ".tmp")
      a.ref.moveTo(file, replace = true)
      new FileInputStream(file)
    }.orNull
    case _ => null
  }

  override def getParameter(parameter: String): String = request.queryString.get(parameter).fold {
    request.body.asFormUrlEncoded.flatMap(_.get(parameter).flatMap(_.headOption))
  }(_.headOption).orNull

  override def getParameterMap: java.util.Map[String, Array[String]] =
    request.queryString.map(a => (a._1, a._2.toArray)).asJava

  override def getContentLength: Int = Integer.parseInt(request.headers.get(HeaderNames.CONTENT_LENGTH).get)

  override def getInputStream: InputStream = inputStream

  override def getAttribute(name: String): AnyRef = getWrappedSession.getAttribute(name)

  override def setAttribute(name: String, value: Any) {
    getWrappedSession.setAttribute(name, value)
  }

  override def getPathInfo: String = request.path.substring(getContextPath.length)

  override def getContextPath: String = uri

  override def getWrappedSession: WrappedSession = getWrappedSession(true)

  override def getWrappedSession(allowSessionCreation: Boolean): WrappedSession =
    if (!session.isNew) new WrappedPlaySession(session)
    else if (session.isNew && allowSessionCreation) new WrappedPlaySession(session)
    else null

  override def getContentType: String = request.contentType.orNull

  override def getLocale: Locale = null //request.headers.get(HeaderNames.ACCEPT_LANGUAGE).orNull

  override def getRemoteAddr: String = request.remoteAddress

  override def isSecure: Boolean = false

  override def getHeader(headerName: String): String = request.headers.get(headerName).orNull

  override def getService: VaadinService = vaadinService

  override def getCookies: Array[javax.servlet.http.Cookie] = null

  override def getAuthType: String = ""

  override def getRemoteUser: String = ""

  override def getUserPrincipal: Principal = null

  override def isUserInRole(role: String): Boolean = false

  override def removeAttribute(name: String) {
    getWrappedSession.removeAttribute(name)
  }

  override def getAttributeNames: java.util.Enumeration[String] = null

  override def getLocales: java.util.Enumeration[Locale] = null // request.acceptLanguages

  override def getRemoteHost: String = request.domain

  override def getRemotePort: Int = -1

  override def getCharacterEncoding: String = ""

  override def getReader: BufferedReader = new BufferedReader(new InputStreamReader(inputStream))

  override def getMethod: String = request.method

  override def getDateHeader(name: String): Long = 0L

  override def getHeaderNames: java.util.Enumeration[String] =
    request.headers.keys.iterator.asJavaEnumeration

  override def getHeaders(name: String): java.util.Enumeration[String] =
    request.headers.getAll(name).iterator.asJavaEnumeration
}
