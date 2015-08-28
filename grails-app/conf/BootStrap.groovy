import edu.arizona.sista.discp.*

class BootStrap {

  /** Injected instance of the Grails Application object. */
  def grailsApplication

  def init = {
    servletContext ->

    // find the local hostname and set application variable accordingly
    def hostname = java.net.InetAddress.localHost.hostName
    log.info ("(Bootstrap): hostname=${hostname}")
    if (hostname in grailsApplication.config.discp.hosts.production) {
      grailsApplication.config.discp.hosts.goldenHost = true
      log.info ("(Bootstrap): setting ${hostname} as goldenHost")
    }
  }


  def destroy = {
  }
}
