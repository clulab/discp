// Master file for the syntax tree visualization.
//
//= require jquery
//= require spring-websocket
//= require bootstrap-table/bootstrap-table.js
//= require dagrevis.js
//= require treevis.js
//= require depgrapher.js
//= require_self

$(document).ready(function() {
  var socket = new SockJS('/discp/stomp/');        // callStomp defined in GSP
  var client = Stomp.over(socket);

  var initSyntaxTable = function(atTable, rows, synTreesJson) {
    var maplst = rows.map(function(r, rndx) {
      return { index: rndx, tree: r };
    });
    $(atTable).bootstrapTable({
      data: maplst,
      hover: true,
      striped: true,
      columns: [ { field: 'index', title: 'Sent #',      sortable: false },
                 { field: 'tree',
                   title: 'Syntax Tree (double-click on a sentence to graph the tree)',
                   sortable: false } ]
    });
    $(atTable).on('dbl-click-row.bs.table', function (e, row, $element) {
      $("#conSyn-vis-syntree").empty();   // clear any previous graph
      Treevis.graphSyntaxTree("#conSyn-vis-syntree", row.index, synTreesJson);
    });
  };

  var initTimingsTable = function(atTable, tblStr) {
    var rows = JSON.parse(tblStr);
    var maplst = rows.map(function(r, rndx) {
      return { order:rndx, phase:r[0], elapsed:r[1], start:r[2], stop:r[3] };
    });
    $(atTable).bootstrapTable({
      data: maplst,
      hover: true,
      striped: true,
      columns: [ { field: 'order',   title: 'Phase #',      sortable: true },
                 { field: 'phase',   title: 'Phase',        sortable: true },
                 { field: 'elapsed', title: 'Elapsed (ms)', sortable: true, align: 'right' },
                 { field: 'start',   title: 'Start Time',   sortable: true, align: 'right' },
                 { field: 'stop',    title: 'Stop Time',    sortable: true, align: 'right' } ]
    });
    $(atTable).bootstrapTable('load', maplst);
  };

  var initDependencyTable = function(atTable, graphNode, wordLists, edgeLists) {
    var sentences = wordLists.map(function(wl) { return wl.join(' '); });
    var maplst = sentences.map(function(s, sndx) {
      return { index: sndx, sent: s };
    });
    $(atTable).bootstrapTable({
      data: maplst,
      hover: true,
      striped: true,
      columns: [ { field: 'index', title: 'Sent #',      sortable: false },
                 { field: 'sent',
                   title: 'Sentence (double-click on a sentence to graph the dependencies)',
                   sortable: false } ]
    });
    $(atTable).on('dbl-click-row.bs.table', function (e, sent, $element) {
      $(graphNode).empty();                 // clear any previous graph
      DepGrapher.graphDependencies(graphNode, sent.index, wordLists, edgeLists);
    });
  };

  client.connect({}, function() {
    // constituent syntax:
    client.subscribe("/topic/conSyn.results", function(message) {
      var msg = JSON.parse(String(message.body));
      if (msg.hasOwnProperty("dTrees")) {
        $("#conSyn-vis-dtree").empty();     // clear any previous graph
        Dagrevis.graphTree("#conSyn-vis-dtree", msg.dTrees[0]);
      }
      if (msg.hasOwnProperty("timings")) {
        initTimingsTable("#conSyn-timings", msg.timings);
      }
      if (msg.hasOwnProperty("synTrees")) {
        var synTreesJson = msg.hasOwnProperty("synTreesJson") ? msg.synTreesJson : null;
        initSyntaxTable("#conSyn-syn-trees", msg.synTrees, synTreesJson);
        $("#conSyn-vis-syntree").empty();   // clear any previous graph
      }
      if ( (msg.hasOwnProperty("dependEdges")) && (msg.hasOwnProperty("sentWords")) ) {
        initDependencyTable("#conSyn-depend-sents", "#conSyn-depend-vis", msg.sentWords, msg.dependEdges);
      }
    });

    client.subscribe("/topic/conSyn.status", function(message) {
      var msg = JSON.parse(message.body);
      if (msg.hasOwnProperty("start"))
        $("#conSyn-spinner").addClass("fa fa-lg fa-refresh fa-spin");
      if (msg.hasOwnProperty("stop"))
        $("#conSyn-spinner").removeClass("fa fa-lg fa-refresh fa-spin");
    });

    // dependency syntax:
    client.subscribe("/topic/depSyn.results", function(message) {
      var msg = JSON.parse(String(message.body));
      if (msg.hasOwnProperty("dTrees")) {
        $("#depSyn-vis-dtree").empty();      // clear any previous graph
        Dagrevis.graphTree("#depSyn-vis-dtree", msg.dTrees[0]);
      }
      if (msg.hasOwnProperty("timings")) {
        initTimingsTable("#depSyn-timings", msg.timings);
      }
      if ( (msg.hasOwnProperty("dependEdges")) && (msg.hasOwnProperty("sentWords")) ) {
        initDependencyTable("#depSyn-depend-sents", "#depSyn-depend-vis", msg.sentWords, msg.dependEdges);
      }
    });

    client.subscribe("/topic/depSyn.status", function(message) {
      var msg = JSON.parse(message.body);
      if (msg.hasOwnProperty("start"))
        $("#depSyn-spinner").addClass("fa fa-lg fa-refresh fa-spin");
      if (msg.hasOwnProperty("stop"))
        $("#depSyn-spinner").removeClass("fa fa-lg fa-refresh fa-spin");
    });

  });

  // on click of submit button:
  $("#submitBtn").click(function() {
    var inText = $("#inText").val();
    var jsonText = JSON.stringify(inText);
    client.send("/app/parseConSyn", {}, jsonText);
    client.send("/app/parseDepSyn", {}, jsonText);
  });

  function togglePanel(panel, toggleSwitch) {
    $(panel).toggle();
    if ($(panel).is(':hidden'))
      $(toggleSwitch).text('Show');
    else
      $(toggleSwitch).text('Hide');
  };

  // show/hide table of constituent timings
  $('#conSyn-timings-panel').hide();
  $('#conSyn-timings-toggle').click(function(ev) {
    togglePanel('#conSyn-timings-panel', '#conSyn-timings-toggle');
    togglePanel('#depSyn-timings-panel', '#depSyn-timings-toggle');
  });

  // show/hide table of dependency timings
  $('#depSyn-timings-panel').hide();
  $('#depSyn-timings-toggle').click(function(ev) {
    togglePanel('#conSyn-timings-panel', '#conSyn-timings-toggle');
    togglePanel('#depSyn-timings-panel', '#depSyn-timings-toggle');
  });

  // show/hide table of syntax trees
  $('#conSyn-syn-trees-panel').hide();
  $('#conSyn-syn-trees-toggle').click(function(ev) {
    togglePanel('#conSyn-syn-trees-panel', '#conSyn-syn-trees-toggle');
  });

  // show/hide graph of CORE dependencies
  $('#conSyn-depend-panel').hide();
  $('#conSyn-depend-toggle').click(function(ev) {
    togglePanel('#conSyn-depend-panel', '#conSyn-depend-toggle');
  });

  // show/hide graph of FAST dependencies
  $('#depSyn-depend-panel').hide();
  $('#depSyn-depend-toggle').click(function(ev) {
    togglePanel('#depSyn-depend-panel', '#depSyn-depend-toggle');
  });

});
