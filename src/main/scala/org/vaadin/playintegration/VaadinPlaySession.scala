package org.vaadin.playintegration

/**
 * @author Henri Kerola / Vaadin
 */
trait VaadinPlaySession {

  def sessionId: String
  def creationTime: Long
  def lastAccessedTime: Long
  var maxInactiveInterval: Int
  def isNew: Boolean

  def getAttribute(name: String): Option[Any]
  def setAttribute(name: String, value: Any)
  def removeAttribute(name: String)
  def attributeNames: Set[String]

  def onAfterRetrieve() {}
  def onBeforeStore() {}
}
