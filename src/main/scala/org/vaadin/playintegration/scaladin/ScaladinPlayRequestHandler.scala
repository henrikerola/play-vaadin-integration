package org.vaadin.playintegration.scaladin

import org.vaadin.playintegration.{ VaadinPlayService, VaadinPlayRequestHandler }
import org.vaadin.playintegration.scaladin.mixins.ScaladinPlayServiceMixin
import vaadin.scala.internal.ScaladinUIProvider
/**
 * @author Henri Kerola / Vaadin
 */
class ScaladinPlayRequestHandler(conf: org.vaadin.playintegration.PlayDeploymentConfiguration)
    extends VaadinPlayRequestHandler(conf) {

  override protected def createVaadinPlayService(c: org.vaadin.playintegration.PlayDeploymentConfiguration): VaadinPlayService = {
    val service = new ScaladinPlayService(new org.vaadin.playintegration.VaadinPlayService(this, c) with ScaladinPlayServiceMixin)
    service.sessionInitListeners += { e =>
      e.session.p.addUIProvider(new ScaladinUIProvider)
    }
    service.p
  }
}
