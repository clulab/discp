<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <asset:stylesheet href="treevis.css"/>
    <script type="text/javascript">
      var syntaxTree = ${raw(syntaxTree)};
    </script>
    <asset:javascript src="treevis.js"/>
    <title>Syntax Tree #${id}</title>
  </head>

  <body>
    <h2>Sentence #${id}</h2>

    <g:if test="${flash.message}">
      <div class="indented errors" role="status">${flash.message}</div>
    </g:if>

    <div class="panel panel-info indented">
      <div class="panel-heading">
        <h3 class="panel-title">${synTree}</h3>
      </div>

      <h4>${tree}</h4>

      <div id="show-tree-panel" class="panel-body">
        <div id="tree-container" class="resultsBox"></div>
      </div>
    </div>   <!-- panel -->

  </body>
</html>
