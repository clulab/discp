// Include file for the dependency visualization using D3 JS.
//
//= require d3/d3.min.js
//= require dagre/dagre-d3.js
//= require_self

var DepGrapher = (function ($) {

  /** Create a graph of the syntax tree with given index at the named DOM node. */
  var graphDependencies = function(atNode, sentNdx, wordLists, edgeLists) {
    // sanity checks on index arguments:
    if (!edgeLists || (sentNdx >= edgeLists.length) ||
        !wordLists || (sentNdx >= wordLists.length))
      return;

    var words = wordLists[sentNdx];         // get word list for current sentence
    var eList = edgeLists[sentNdx];         // get edge list for current sentence
    var edges = JSON.parse(eList);          // convert to JSON object

    graphTree(atNode, edges, words);        // graph it!
  };


  /** Create a graph of the given edges (json) at the named DOM SVG node. */
  var graphTree = function(atNode, edges, words) {
    var g = new dagreD3.graphlib.Graph()
      .setGraph({})
      .setDefaultEdgeLabel(function() { return {arrowhead:"normal"}; });

    // Add nodes and edges from the dependencies
    fillGraph(g, edges, words);

    // Set up an SVG group so that we can translate the final graph.
    var svgBase = d3.select(atNode);
    var svgGroup = svgBase.append("g");

    // Set up zoom support
    var zoom = d3.behavior.zoom().scaleExtent([0.2, 3]).on("zoom", function() {
      svgGroup.attr("transform",
                    "translate(" + d3.event.translate + ")" +
                    "scale(" + d3.event.scale + ")");
    });
    svgBase.call(zoom);

    // Create the renderer
    var render = new dagreD3.render();

    // Run the renderer. This is what draws the final graph.
    render(svgGroup, g);

    // Center the graph
    zoom
      .translate([40 + ((svgBase.attr('width') - g.graph().width) / 2), 20])
      .event(svgBase);
    svgBase.attr('height', g.graph().height);
  };


  /** Fill the given graph from the given edges (json) and words (string array). */
  var fillGraph = function(g, edges, words) {
    // create and label each of the nodes with a word:
    words.forEach(function(word) { g.setNode(word, { label: word}); });

    edges.forEach(function(edge) {
      g.setEdge(words[edge[0]], words[edge[1]], { label: edge[2] });
    });
  };


  //
  // Public API
  //
  return {
    graphDependencies: graphDependencies
  };

}) (jQuery);
