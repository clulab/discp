import org.apache.log4j.rolling.RollingFileAppender
import org.apache.log4j.rolling.TimeBasedRollingPolicy

/**
 * Grails Settings
 */

// Locations to search for config files that get merged into the main config.
// Config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format.
//
// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// Or, take an additional config location from the command line:
// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
// grails.controllers.defaultScope = 'singleton'

grails.converters.encoding = "UTF-8"

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// auto-caching of queries (if false you can cache individual queries with 'cache:true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session,
// queries and criterias.
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default.
// requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false


// enables the parsing of file extensions from URLs into the request format
grails.mime.file.extensions = true

// The ACCEPT header will not be used for content negotiation for user agents
// containing the following strings (defaults to the 4 major rendering engines):
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
// grails.mime.use.accept.header = false

// the first mime type in the list is the default format:
grails.mime.types = [
    all:           '*/*',  // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]


// change this to alter the default package name and Maven publishing destination:
grails.project.groupId = appName

// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting: for codec used to encode data with ${}
grails.views.default.codec = "html"

// whether to disable processing of multi part requests:
grails.web.disable.multipart = false

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'html'               // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html'         // escapes values inside ${}
                scriptlet = 'html'          // escapes output from scriptlets in GSPs
                taglib = 'none'             // escapes output from taglibs
                staticparts = 'none'        // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting:
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


/**
 * Plugin Configurations
 */

// Grails assets pipeline options:
grails.assets.minifyJs = false              // turn if off for now, else use options below
grails.assets.minifyCss = false
grails.assets.minifyOptions = [
  strictSemicolons: false,
  mangleOptions: [mangle: false, toplevel: false, defines: null, except: null, no_functions: false],
  genOptions: [indent_start:0, indent_level:2, quote_keys: false, space_colon: true, beautify: true, ascii_only: false, inline_script: false]
]


/**
 * Per-Environment Configurations
 */
environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // set per-environment serverURL stem for creating absolute links:
        // grails.serverURL = "http://www.changeme.com"
    }
}


/**
 * Logging: log4j Configuration
 */
def catalinaBase = System.properties.getProperty('catalina.base')
if (!catalinaBase) catalinaBase = '.'      // just in case
def logDirectory = "${catalinaBase}/logs"

log4j.main = {
  def dailyRolling =
    new RollingFileAppender(name: 'virfa',
                            layout: pattern(conversionPattern: "%d [%t] %-5p %c{2} %x - %m%n"))
  def rollingPolicy = new TimeBasedRollingPolicy(fileNamePattern:
                                                 "${logDirectory}/discp.%d{yyyy-MM-dd}.log")
  rollingPolicy.activateOptions()
  dailyRolling.setRollingPolicy(rollingPolicy)

  appenders {
    // 'null'(name: 'stacktrace')
    appender dailyRolling
  }
  root {
    error('virfa')
  }
  error 'org.codehaus.groovy.grails.web.servlet',        // controllers
        'org.codehaus.groovy.grails.web.pages',          // GSP
        'org.codehaus.groovy.grails.web.sitemesh',       // layouts
        'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
        'org.codehaus.groovy.grails.web.mapping',        // URL mapping
        'org.codehaus.groovy.grails.commons',            // core / classloading
        'org.codehaus.groovy.grails.plugins',            // plugins
        'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
        'org.springframework',
        'org.springframework.security',                  // Spring Security
        'org.hibernate',
        'net.sf.ehcache.hibernate'
  info 'grails.app'                         // this application
  // debug 'grails.app.conf'
  // debug 'grails.app.controllers'
  // debug 'grails.app.domain'
  // debug 'grails.app.services'
  // info  'org.grails.plugin.resource'
  // debug 'org.springframework.security'
}


/**
 * Application-specific Configuration
 */
discp {
  hosts {
    goldenHost = false
    production = ['thrace','thrace.cs.arizona.edu']
  }
}

beans {
  documentService {
    textResource = 'classpath:inputTexts/'  // trailing slash is critical
  }
}
