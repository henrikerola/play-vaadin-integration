package org.vaadin.playintegration

import scala.collection.mutable

/**
 * @author Henri Kerola / Vaadin
 */
class DefaultVaadinPlaySession(val sessionId: String) extends VaadinPlaySession {

  private[this] var newSession = true

  private[this] val attributeMap = mutable.Map.empty[String, Any]

  override val creationTime: Long = System.currentTimeMillis

  override def lastAccessedTime: Long = 0 // TODO

  override var maxInactiveInterval: Int = 1800

  override def isNew: Boolean = newSession

  override def getAttribute(name: String): Option[Any] = attributeMap.get(name)

  override def setAttribute(name: String, value: Any) {
    attributeMap += (name -> value)
  }

  override def removeAttribute(name: String) {
    attributeMap -= name
  }

  override def attributeNames: Set[String] = attributeMap.keySet.toSet

  override def onBeforeStore() {
    newSession = false
  }

}
