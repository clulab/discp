// Master file for the syntax tree visualization.
//
//= require jquery
//= require tree-viewer/d3Tree.js
//= require_self

var Treevis = (function ($) {

  /** Create a graph of the syntax tree with given index at the named DOM node. */
  graphSyntaxTree = function(atNode, treeNdx, synTreesJson) {
    if (!synTreesJson || (treeNdx >= synTreesJson.length)) // sanity check on index arg
      return;

    // Add nodes and edges from the JSON form of the syntax tree
    var stString = synTreesJson[treeNdx];   // get the syntax tree in JSON string form
    var jsynTree = JSON.parse(stString);    // convert to JSON object
    d3Tree(atNode, jsynTree);               // graph the syntax tree
  };


  //
  // Public API
  //
  return {
    graphSyntaxTree: graphSyntaxTree
  };

}) (jQuery);
