package edu.arizona.sista.discp

import grails.converters.*

/**
 * Class for returning discourse parser results.
 *   Last modified: Initial stub creation.
 */
class ParserResults {

  String text
  String dTree
  List edus = []

  ParserResults (text, tree, edus) {
    if (text) this.text = text
    if (tree) this.dTree = tree
    if (edus) this.edus = edus
  }

}
