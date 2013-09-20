package org.vaadin.playintegration

import javax.servlet.http.Cookie
import java.io.{ OutputStream, PrintWriter, ByteArrayOutputStream }
import play.mvc.Http
import collection.mutable
import com.vaadin.server.{ VaadinService, VaadinServletResponse, VaadinResponse }

/**
 * @author Henri Kerola / Vaadin
 */
class VaadinPlayResponse(vaadinService: VaadinPlayService) extends VaadinResponse {

  var statusCode = Http.Status.OK
  val headersMap = mutable.Map.empty[String, String]

  val outputStream: OutputStream = new ByteArrayOutputStream
  private[this] val printWriter: PrintWriter = new PrintWriter(outputStream)

  def cleanUp() {
    outputStream.close()
    printWriter.close()
  }

  override def setStatus(statusCode: Int) {
    this.statusCode = statusCode
  }

  override def setContentType(contentType: String) {
    setHeader(Http.HeaderNames.CONTENT_TYPE, contentType)
  }

  override def setHeader(name: String, value: String) {
    headersMap += (name -> value)
  }

  override def setDateHeader(name: String, timestamp: Long) {
    // TODO
  }

  override def getOutputStream: OutputStream = outputStream

  override def getWriter: PrintWriter = printWriter

  override def setCacheTime(milliseconds: Long) {
    // copy-pasted from package protected VaadinServletResponse.doSetCacheTime(this, milliseconds)
    if (milliseconds <= 0) {
      setHeader("Cache-Control", "no-cache")
      setHeader("Pragma", "no-cache")
      setDateHeader("Expires", 0)
    } else {
      setHeader("Cache-Control", "max-age=" + milliseconds / 1000)
      setDateHeader("Expires", System.currentTimeMillis() + milliseconds)
      // Required to apply caching in some Tomcats
      setHeader("Pragma", "cache")
    }
  }

  override def sendError(errorCode: Int, message: String) {
    setStatus(errorCode)
    getWriter.write(message)
  }

  override def getService: VaadinService = vaadinService

  override def addCookie(cookie: Cookie) {
    // TODO
  }

  def sendRedirect(location: String) {
    setStatus(Http.Status.FOUND)
    setHeader(Http.HeaderNames.LOCATION, location)
  }
}
