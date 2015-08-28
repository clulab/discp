package edu.arizona.sista.discp

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.messaging.handler.annotation.*

/**
 * Controller for Discourse Parser Comparator and Visualizer.
 *   Last modified: Parse syntax tree strings to JSON using parser service.
 */
class ComparisonController extends ControllerBase {

  // Force sensitive actions to only accept POST requests
  static allowedMethods = []

  // Injected message broker
  def brokerMessagingTemplate

  // Injected parser comparison service
  def comparisonService

  // Injected Document service
  def documentService

  // Injected Mock API service
  def mockApiService

  // Injected syntax tree parser service
  def parseService

  // The default action for this controller
  static defaultAction = 'compvis'


  /**
   * Compare and Visualize action.
   */
  def compvis () {
    def docList = documentService.getDocumentList()
    return ['bean': params, 'docList': docList]
  }


  /**
   * Show Tree action.
   */
  def showTree () {
    log.debug("[showTree] params=${params}")
    params.syntaxTree = parseService.parseSyntaxTree(params.synTree)
    return params
  }


  /**
   * Parse the websocket text submission using the Constituent Syntax parser
   * and post the results to a pub/sub list dedicated to constituent results.
   */
  @MessageMapping("/parseConSyn")
  @SendTo("/topic/conSyn.results")
  protected def parseConSyn (String text) {
    log.debug("[parseConSyn] text=${text}")

    brokerMessagingTemplate.convertAndSend("/topic/conSyn.status", ['start':true])
    def startTime = System.currentTimeMillis() // start time
    def results = comparisonService.parseText(text, 'core')
    def elapsedTime = (System.currentTimeMillis() - startTime) // stop time
    results.synTreesJson = results?.synTrees.collect { parseService.parseSyntaxTree(it) }
    brokerMessagingTemplate.convertAndSend("/topic/conSyn.status", ['stop':true, 'time':elapsedTime])
    return results                          // return result map
  }


  /**
   * Parse the websocket text submission using the Dependency Syntax parser
   * and post the results to a pub/sub list dedicated to dependency results.
   */
  @MessageMapping("/parseDepSyn")
  @SendTo("/topic/depSyn.results")
  protected def parseDepSyn (String text) {
    log.debug("[parseDepSyn] text=${text}")

    brokerMessagingTemplate.convertAndSend("/topic/depSyn.status", ['start':true])
    def startTime = System.currentTimeMillis() // start time
    def results = comparisonService.parseText(text, 'fast')
    def elapsedTime = (System.currentTimeMillis() - startTime) // stop time
    brokerMessagingTemplate.convertAndSend("/topic/depSyn.status", ['stop':true, 'time':elapsedTime])
    return results                          // return result map
  }


  /**
   * Fetch action. Fetch and return the contents of a text file in the resources area.
   */
  def fetch () {
    log.debug("[fetch] params=${params}")

    def doc = params.doc
    if (!doc) {
      flash.message = "Please select a file whose text content you want to fetch."
      render(view:'compvis',
             model:['bean': params, 'docList': documentService.getDocumentList()])
      return
    }

    def inText = documentService.getDocument(doc)
    if (inText) {
      flash.message = null
      render(view:'compvis',
             model:['bean': params, 'inText': inText,
                    'doc': doc, 'docList': documentService.getDocumentList()])
    }
    else {                                  // couldn't get file content
      flash.message = "Sorry, unable to retrieve content from selected file."
      render(view:'compvis',
             model:['bean': params, 'docList': documentService.getDocumentList()])
      return
    }
  }

}
