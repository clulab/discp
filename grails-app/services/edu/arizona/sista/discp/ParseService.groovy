package edu.arizona.sista.discp

/**
 * Service for the auxiliary parsing methods.
 *   Last modified: Initial creation.
 */
class ParseService extends ServiceBase {

  /** Return the given syntax string as JSON tree, suitable for display. */
  def parseSyntaxTree (treeStr) {
    def jsonStr = new StringBuilder()
    def st = makeTokenizer(treeStr)
    def token = getNextToken(st)
    if (token)
      handleToken(st, jsonStr, token)
    def json = jsonStr.toString()
    return json
  }

  private addLeaf (st, jsonStr, leaf) {
    if (leaf) {
      jsonStr.append('{"name":"')
      jsonStr.append(leaf)
      jsonStr.append('"}')
    }
  }

  private ascend (st, jsonStr) {
    jsonStr.append(']}')
  }

  private descend (st, jsonStr) {
    def word = getNextToken(st)
    if (word) {
      jsonStr.append('{"name":"')
      jsonStr.append(word)
      jsonStr.append('","children":[')
      handleChildren(st, jsonStr)
    }
    ascend(st, jsonStr)
  }

  private handleChildren (st, jsonStr) {
    def moreThan1 = false
    def next = getNextToken(st)
    while (next && (next != ')')) {
      if (moreThan1) jsonStr.append(',')
      handleToken(st, jsonStr, next)
      moreThan1 = true
      next = getNextToken(st)
    }
  }

  private handleToken (st, jsonStr, token) {
    if (token) {
      switch (token) {
        case '(': descend(st, jsonStr); break
        case ')': ascend(st, jsonStr); break
        default: addLeaf(st, jsonStr, token)
      }
    }
  }


  private String getNextToken (StreamTokenizer st) {
    def token = null

    def tt = st.nextToken()                 // get the next token
    switch (tt) {
      case StreamTokenizer.TT_EOF: break
      case StreamTokenizer.TT_WORD: token = st.sval; break
      case 40: token = '('; break
      case 41: token = ')'; break
      default: token = "${tt}"; break
    }

    return token
  }


  private StreamTokenizer makeTokenizer (treeStr) {
    def st = new StreamTokenizer(new StringReader(treeStr))
    st.resetSyntax()
    st.eolIsSignificant(false)
    st.slashStarComments(false)
    st.slashSlashComments(false)
    st.whitespaceChars(0, 32)               // non-printables as whitespace
    st.wordChars(33, 126)                   // printables as word chars
    st.ordinaryChar(40)                     // left paren special
    st.ordinaryChar(41)                     // right paren special
    return st                               // return new stream tokenizer
  }

}
