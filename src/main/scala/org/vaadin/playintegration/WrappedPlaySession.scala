package org.vaadin.playintegration

import collection.JavaConverters._
import com.vaadin.server.WrappedSession

/**
 * @author Henri Kerola / Vaadin
 */
class WrappedPlaySession(session: VaadinPlaySession) extends WrappedSession {

  override def getMaxInactiveInterval: Int = session.maxInactiveInterval

  override def getAttribute(name: String): AnyRef = session.getAttribute(name).orNull.asInstanceOf[AnyRef]

  override def setAttribute(name: String, value: Any) {
    session.setAttribute(name, value)
  }

  override def getAttributeNames: java.util.Set[String] = session.attributeNames.asJava

  override def invalidate() {} // TODO

  override def getId: String = session.sessionId

  override def getCreationTime: Long = session.creationTime

  override def getLastAccessedTime: Long = session.lastAccessedTime

  override def isNew: Boolean = session.isNew // TODO: is this correct?

  override def removeAttribute(name: String) {
    session.removeAttribute(name)
  }

  override def setMaxInactiveInterval(interval: Int) {
    session.maxInactiveInterval = interval
  }
}
