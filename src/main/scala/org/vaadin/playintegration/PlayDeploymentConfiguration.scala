package org.vaadin.playintegration

import java.util.Properties
import com.vaadin.shared.communication.PushMode
import com.vaadin.server.DeploymentConfiguration.LegacyProperyToStringMode
import com.vaadin.server.Constants
import play.api.{ Mode, Play }

/**
 * @author Henri Kerola / Vaadin
 */
class PlayDeploymentConfiguration(
  val name: String,
  val isStandalone: Boolean,
  val isScaladin: Boolean,
  conf: play.api.Configuration)
    extends com.vaadin.server.DeploymentConfiguration {

  lazy val getPath: String = conf.getString("path").get

  lazy val getUi: String = conf.getString("ui").get

  lazy val getWidgetset: String = conf.getString("widgetset").getOrElse(Constants.DEFAULT_WIDGETSET)

  lazy val getSessionTimeout: Int = conf.getInt("session_timeout").getOrElse(1800)

  lazy val getStaticFileLocation: String = "" // TODO

  override lazy val isProductionMode: Boolean = Play.current.mode == Mode.Prod

  override lazy val isXsrfProtectionEnabled: Boolean = conf.getBoolean("xsrf_protection").getOrElse(true)

  override lazy val getResourceCacheTime: Int = -1 // Not in use in

  override lazy val getHeartbeatInterval: Int = conf.getInt("heartbeat_interval").getOrElse(300)

  override lazy val isCloseIdleSessions: Boolean = conf.getBoolean("close_idle_sessions").getOrElse(false)

  override def getInitParameters: Properties = new Properties {
    if (isScaladin) {
      put("ScaladinUI", getUi)
    }
  }

  override def getApplicationOrSystemProperty(propertyName: String, defaultValue: String): String =
    if (!isScaladin && propertyName == "UI")
      return getUi
    else
      conf.getString(propertyName).getOrElse(defaultValue)

  override def getPushMode: PushMode = PushMode.DISABLED

  override def getLegacyPropertyToStringMode: LegacyProperyToStringMode = LegacyProperyToStringMode.DISABLED
}
