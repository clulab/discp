package edu.arizona.sista.discp

/**
 * Base Controller. Defines methods shared by all child Controllers.
 *   Last modified: Initial copy.
 */
class ControllerBase {

  /** Injected instance of the Grails Application object. */
  def grailsApplication


  /**
   * Find action (currently a NOP to be overridden).
   */
  def find () {
    flash.message = 'Search is not yet implemented for this entity type'
    redirect(action: defaultAction)
  }

}
