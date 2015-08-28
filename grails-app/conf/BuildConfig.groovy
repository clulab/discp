grails.servlet.version = "3.0"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.8
grails.project.source.level = 1.8
// grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.tomcat.jvmArgs = ["-server", "-Xms1024m", "-Xmx6144m"]

grails.project.fork = [
  // configure settings for compilation JVM:
  // compile: [maxMemory: 256, minMemory: 64, debug: false, daemon:true],

  // configure settings for the test-app JVM, uses the daemon by default:
  test: [maxMemory: 768, minMemory: 64, debug: false, daemon:true],

  // configure settings for the run-app JVM:
  run: [maxMemory: 6144, minMemory: 512, debug: false, forkReserve:false],

  // configure settings for the run-war JVM:
  war: [maxMemory: 6144, minMemory: 512, debug: false, forkReserve:false],

  // configure settings for the Console UI JVM:
  console: [maxMemory: 768, minMemory: 64, debug: false]
]

// enable dependency resolution to user's local Maven repository:
final String localMavenRepo = "${System.getProperty('user.home')}/.m2/repository"

grails.project.dependency.resolver = "maven" // or ivy (deprecated)
grails.dependency.cache.dir = localMavenRepo // enable local Maven resolution
grails.project.dependency.resolution = {
  inherits("global") {                      // inherit Grails' default dependencies:
    // specify dependency exclusions here; for example, uncomment next to disable ehcache:
    // excludes 'ehcache'
  }
  log "verbose"            // log level of Ivy resolver: [error, warn, info, debug, verbose]
  checksums true           // whether to verify checksums on resolve
  legacyResolve false      // whether to do a secondary resolve on plugin installation

  repositories {
    inherits true          // whether to inherit repository definitions from plugins

    grailsPlugins()                         // not supported by Maven Aether
    grailsHome()                            // not supported by Maven Aether
    mavenLocal()                            // not supported by Maven Aether
    grailsCentral()
    mavenCentral()

    // enable dependency resolution to user's local Maven repository:
    localRepository = localMavenRepo

    // BioPAX Releases and Snapshots
    mavenRepo "http://biopax.sourceforge.net/m2repo/releases"
    mavenRepo "http://biopax.sourceforge.net/m2repo/snapshots"

    // uncomment these to enable remote dependency resolution from public Maven repositories:
    // mavenRepo "http://download.java.net/maven/2/"
    // mavenRepo "http://repository.jboss.com/maven2/"
  }

  dependencies {
    // specify dependencies here under scopes: [build, compile, runtime, test, provided]
    compile 'log4j:log4j:1.2.17'
    compile 'log4j:apache-log4j-extras:1.2.17'
    compile('edu.arizona.sista:reach_2.11:1.0-SNAPSHOT') {
      transitive = false
    }

    // runtime dependencies for Processors
    runtime 'com.googlecode.efficient-java-matrix-library:ejml:0.23'
    runtime 'jline:jline:2.12.1'
    runtime 'joda-time:joda-time:2.7'
    runtime 'de.jollyday:jollyday:0.4.7'
    runtime 'net.sf.jopt-simple:jopt-simple:4.5'
    runtime 'de.bwaldvogel:liblinear:1.94'
    runtime 'org.json4s:json4s_2.11:3.2.11'
    runtime 'tw.edu.ntu.csie:libsvm:3.17'
    runtime 'com.thoughtworks.paranamer:paranamer:2.6'
    runtime 'org.scala-lang:scala-library:2.11.6'
    runtime 'org.scala-lang:scala-reflect:2.11.6'
    runtime 'org.scala-lang.modules:scala-parser-combinators_2.11:1.0.3'
    runtime 'org.scala-lang.modules:scala-xml_2.11:1.0.3'
    runtime 'ch.qos.logback:logback-classic:1.0.10'
    runtime 'org.slf4j:slf4j-simple:1.7.10'
    runtime 'org.slf4j:slf4j-api:1.7.10'
    runtime 'org.yaml:snakeyaml:1.14'
    runtime 'edu.stanford.nlp:stanford-corenlp:3.5.1'
    runtime 'edu.stanford.nlp:stanford-corenlp:jar:models:3.5.1'
    runtime('nz.ac.waikato.cms.weka:weka-dev:3.7.10') {
      excludes 'pentaho-package-manager'
    }
    runtime 'com.io7m.xom:xom:1.2.10'
    runtime 'edu.arizona.sista:processors_2.11:5.5-SNAPSHOT'
    runtime 'edu.arizona.sista:processors_2.11:jar:models:5.5-SNAPSHOT'

    // runtime dependencies for Reach
    runtime 'com.typesafe:config:1.2.1'
    runtime 'commons-io:commons-io:2.4'
    runtime('org.biopax.paxtools:paxtools-core:4.3.0') {
      excludes 'commons-logging'
    }

    // test 'org.grails:grails-datastore-test-support:1.0-grails-2.4'
    test 'org.gebish:geb-spock:0.12.1'
    // test 'org.seleniumhq.selenium:selenium-support:2.47.0'
    // test 'org.seleniumhq.selenium:selenium-firefox-driver:2.47.0'
  }

  plugins {
    // plugins for the build system only:
    build ":tomcat:7.0.55"

    // plugins for the compile step:
    compile ":scaffolding:2.1.2"
    compile ':cache:1.1.7'
    compile ":asset-pipeline:1.9.6"
    compile ":spring-websocket:1.2.0"

    // plugins for the testing stage:
    test ":geb:0.12.1"

    // plugins needed at runtime but not for compilation:
    // runtime ":hibernate4:4.3.5.5"           // or ":hibernate:3.6.10.17"
    // runtime ":database-migration:1.4.0"
    runtime ":jquery:1.11.1"

    // Uncomment these to enable additional asset-pipeline capabilities
    //compile ":sass-asset-pipeline:1.9.0"
    //compile ":less-asset-pipeline:1.10.0"
    //compile ":coffee-asset-pipeline:1.8.0"
    //compile ":handlebars-asset-pipeline:1.3.0.3"
  }
}

grails.tomcat.nio = true
grails.tomcat.scan.enabled = true
