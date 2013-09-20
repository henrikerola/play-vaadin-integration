package org.vaadin.playintegration.scaladin

import com.vaadin.server.VaadinRequest
import org.vaadin.playintegration.VaadinPlayService
import org.vaadin.playintegration.scaladin.mixins.ScaladinPlayServiceMixin
import vaadin.scala.ScaladinService
import vaadin.scala.server.ScaladinSession
import vaadin.scala.mixins.ScaladinMixin
import vaadin.scala.server.mixins.VaadinSessionMixin

package mixins {

  trait ScaladinPlayServiceMixin extends ScaladinMixin {
    self: VaadinPlayService =>
    override def createVaadinSession(request: VaadinRequest) =
      new ScaladinSession(new com.vaadin.server.VaadinSession(this) with VaadinSessionMixin).p
  }
}

/**
 * @author Henri Kerola / Vaadin
 */
class ScaladinPlayService(override val p: VaadinPlayService with ScaladinPlayServiceMixin)
    extends ScaladinService {
  p.wrapper = this
}
