package edu.arizona.sista.discp

import edu.arizona.sista.processors.visualizer.DiscourseParserRunner

/**
 * Service for the Discourse Parser Comparison.
 *   Last modified: Add dependency edge sets and sentence word lists.
 */
class ComparisonService extends ServiceBase {

  // Injected Mock API service
  def mockApiService

  /**
   * Return the results from parsing the given text using the given processor.
   */
  def parseText (inText, processor='core') {
    log.debug("(ComparisonService.parseText): inText=${inText}, proc=${processor}")

    // Call back-end to process input text and return discourse trees:
    // def results = mockApiService.parseText(inText, processor)  // MOCK
    def runner = new DiscourseParserRunner(processor)
    def results = runner.parseText(inText)

    def resultsMap = ['text': results.text, 'timings': results.timings,
                      'dTrees': results.dTrees, 'synTrees': results.synTrees,
                      'sentWords': results.sentWords, 'dependEdges': results.dependEdges]

    log.debug("(ComparisonService.parseText): => ${resultsMap}")
    return resultsMap                       // return results object
  }

}
