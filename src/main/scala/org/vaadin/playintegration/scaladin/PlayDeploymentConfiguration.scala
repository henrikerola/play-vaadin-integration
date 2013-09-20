package org.vaadin.playintegration.scaladin

import vaadin.scala.server.DeploymentConfiguration

/**
 * @author Henri Kerola / Vaadin
 */
class PlayDeploymentConfiguration(override val p: org.vaadin.playintegration.PlayDeploymentConfiguration)
    extends DeploymentConfiguration {

  def uri: String = p.getUi

  def ui: String = p.getUi

  def widgetset: String = p.getWidgetset

  def sessionTimeout: Int = p.getSessionTimeout

  def staticFileLocation: String = p.getStaticFileLocation

  override def isProductionMode: Boolean = p.isProductionMode

  override def isXsrfProtectionEnabled: Boolean = p.isXsrfProtectionEnabled

  override lazy val resourceCacheTime: Int = p.getResourceCacheTime

  override lazy val heartbeatInterval: Int = p.getHeartbeatInterval

  override def isCloseIdleSessions: Boolean = p.isCloseIdleSessions

}
