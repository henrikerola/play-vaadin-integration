package org.vaadin.playintegration

import play.api.GlobalSettings

/**
 * Provides a convenient way to enabled support for Vaadin applications when the project
 * doesn't have a `Global` object.
 *
 * Support for Vaadin applications can be enabled by adding the following to `application.conf`:
 *
 * {{{
 * application.global=org.vaadin.playintegration.GlobalWithVaadinSupport
 * }}}
 *
 * @author Henri Kerola / Vaadin
 */
object GlobalWithVaadinSupport extends GlobalSettings with VaadinSupport
