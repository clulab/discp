<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Discourse Parser Comparison</title>
  </head>

  <body>
    <div class="col-md-3">
      <div class="panel panel-default">

        <div class="panel-heading">
          <h3 class="panel-title">Application Status</h3>
        </div>

        <div class="panel-body">
          <ul>
            <li>App version: <g:meta name="app.version"/></li>
            <li>Grails version: <g:meta name="app.grails.version"/></li>
            <li>Groovy version: ${GroovySystem.getVersion()}</li>
            <li>JVM version: ${System.getProperty('java.version')}</li>
            <li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
            <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
            <li>Domains: ${grailsApplication.domainClasses.size()}</li>
            <li>Services: ${grailsApplication.serviceClasses.size()}</li>
            <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
          </ul>
        </div>
      </div>

      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Installed Plugins</h3>
        </div>
        <div class="panel-body">
          <ul>
            <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
              <li>${plugin.name} - ${plugin.version}</li>
            </g:each>
          </ul>
        </div>
      </div>
    </div> <!-- col-md-3 -->

    <div class="col-md-9">
      <h2>Discourse Parser Web</h2>

      <p class="alert alert-info"><span class="fa fa-info-circle"></span>
        <strong>Nota Bene:</strong> This is the "napkin sketch" version: it is just an initial stub!
      </p>

      <p>Welcome to the Discourse Parser Comparison site.</p>

      <h3>Links</h3>
      <ul>
        <li><a href="https://github.com/sistanlp">SISTA NLP Project on Github</a></li>
      </ul>
    </div>

  </body>
</html>
