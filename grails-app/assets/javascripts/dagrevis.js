// Include file for the syntax tree visualization using Dagre-D3 JS.
//
//= require d3/d3.min.js
//= require dagre/dagre-d3.js
//= require dagre/jquery.tipsy.js
//= require_self

var Dagrevis = (function ($) {

  /** Reduce long text to a manageable portion for display. */
  var elideText = function(text) {
    if (text.length > 15)
      return "[" + text.slice(0,7) + " ... " + text.slice(-7) + "]";
    else
      return text
  };


  /** Simple function to style the tooltip for the given node. */
  var styleToolTip = function(nodeNum, name, description) {
    return "<p class='title'><span class='key pull-left'>[" + nodeNum + "]&nbsp;&nbsp;</span>" +
      name + "</p><p class='description'>" + description + "</p>";
  };


  /** Fill the given graph from the given JSON discourse tree. */
  var fillGraph = function(g, dTree) {
    var nodeNum = 0;                        // close over highest current node value

    // figure out which edge to use based on parent and child properties
    var makeEdge = function(g, parentNodeNum, kidNodeNum, kidNdx) {
      var par = g.node(parentNodeNum);
      if (par.hasOwnProperty("dir") && (par.dir == 'LeftToRight')) {
        if (kidNdx == 0)
          g.setEdge(parentNodeNum, kidNodeNum, {arrowhead:"undirected"});
        else
          g.setEdge(parentNodeNum, kidNodeNum, {arrowhead:"normal"});
      }
      else if (par.hasOwnProperty("dir") && (par.dir == 'RightToLeft')) {
        if (kidNdx == 0)
          g.setEdge(parentNodeNum, kidNodeNum, {arrowhead:"normal"});
        else
          g.setEdge(parentNodeNum, kidNodeNum, {arrowhead:"undirected"});
      }
      else {                                // no property or some other property value
          g.setEdge(parentNodeNum, kidNodeNum, {arrowhead:"vee"});
      }
    };

    // inner recursive function to walk tree and generate nodes and edges.
    var fillGraphRec = function(g, dTree, parentNodeNum, kidNdx) {
      nodeNum += 1;
      if (parentNodeNum) {
        makeEdge(g, parentNodeNum, nodeNum, kidNdx);
        dTree.parentNodeNum = parentNodeNum; // store the node num in the tree
      }
      if (dTree.hasOwnProperty("text")) {
        g.setNode(nodeNum, { nodeNum: nodeNum, label: elideText(dTree.text),
                             class: "leaf", text: dTree.text });
        var node = g.node(nodeNum);         // grab that new node
        node.rx = node.ry = 5;              // and round its corners
        dTree.nodeNum = nodeNum;            // store the node num in the tree
      }
      if (dTree.hasOwnProperty("relLabel")) {
        if (dTree.hasOwnProperty("relDir"))
          g.setNode(nodeNum, { nodeNum: nodeNum, label: dTree.relLabel,
                               class: dTree.relLabel, dir: dTree.relDir });
        else
          g.setNode(nodeNum, { nodeNum: nodeNum, label: dTree.relLabel, class: dTree.relLabel });
        dTree.nodeNum = nodeNum;            // store the node num in the tree
      }
      if (dTree.hasOwnProperty("kids")) {
        parentNodeNum = nodeNum;
        dTree.kids.forEach(function(kid, kndx) {
          fillGraphRec(g, kid, parentNodeNum, kndx);
        });
      }
    };

    fillGraphRec(g, dTree, null, null);     // start graph creation
  };


  /** Create a graph of the given discourse tree (json string) at the named DOM node. */
  graphTree = function(atNode, dtString) {
    // Create the input graph
    var g = new dagreD3.graphlib.Graph()
      .setGraph({})
      .setDefaultEdgeLabel(function() { return {arrowhead:"undirected"}; });

    // Add nodes and edges from the discourse tree
    var dTree = JSON.parse(dtString);       // convert the JSON string form to JSON object
    fillGraph(g, dTree);                    // and graph the discourse tree

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

    // Add tooltips to show additional information
    svgGroup.selectAll("g.node")
      .attr("title", function(v) {
        var node = g.node(v);
        if (node.class == "leaf")
          return styleToolTip(node.nodeNum, "TEXT:", node.text);
        else
          return styleToolTip(node.nodeNum, node.label,
                   (node.dir ? ("<span class='key'>direction:&nbsp;</span>"+node.dir) : ""));
      })
      .each(function(v) { $(this).tipsy({gravity:'sw', offset: 2, opacity:1, html:true}); });

    // Center the graph
    zoom
      .translate([40 + ((svgBase.attr('width') - g.graph().width) / 2), 20])
      .event(svgBase);
    svgBase.attr('height', g.graph().height);
  };


  //
  // Public API
  //
  return {
    graphTree: graphTree
  };

}) (jQuery);
