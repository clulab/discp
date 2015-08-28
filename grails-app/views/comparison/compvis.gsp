<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <asset:stylesheet href="dagrevis.css"/>
    <asset:javascript src="compvis.js" />
    <title>Discourse Parser Comparison</title>
  </head>

  <body>
    <h3>Discourse Parser Comparison <g:if test="${doc}">for: '${doc}'</g:if></h3>

    <g:if test="${flash.message}">
      <div class="indented errors" role="status">${flash.message}</div>
    </g:if>


    <!-- Input Text panel -->
    <div class="panel panel-info indented">
      <div class="panel-heading">
        <h3 class="panel-title">Input Text:</h3>
      </div>

      <div id="input-tr-panel" class="panel-body">

        <g:form name="fetch" action="Fetch">
          <div class="indented">
            <label for="doc">Select an input text file:&nbsp;</label>
            <g:select id="doc" name="doc" from="${docList}"
                      value="${bean?.doc}" noSelection="['':'-- Select a File --']" />
            <button id="fetchBtn" class="btn btn-warning btn-xs">Fetch Contents</button>
          </div>
        </g:form>

        <div class="indented">
          <label for="inText">OR enter text in the box below:</label>
          <g:textArea class="textBox greyed" name="inText" rows="4"
                      placeholder="Enter or paste text here"
                      alt="Enter or paste some text here"
                      value="${inText?:''}" />
        </div>

        <p class="spacer">&nbsp;</p>

        <button id="submitBtn" class="btn btn-primary center-block">
          Submit Text for Parsing</button>

      </div>
    </div>   <!-- panel -->


    <div class="row">

      <!-- Constituent Parse Visualization -->
      <div class="col-md-6">
        <div class="panel panel-warning indented">
          <div class="panel-heading">
            <h3 class="panel-title">Constituent Syntax Parse
              <span id="conSyn-spinner" class="indented"></span>
              <span id="conSyn-time" class="pull-right"></span>
            </h3>
          </div>
          <div id="conSyn-vis-panel" class="panel-body">
            <svg id="conSyn-vis-dtree" class="visTreeBox"
                 width="600" height="400"
                 version="1.1" xmlns="http://www.w3.org/2000/svg"></svg>
          </div>
        </div>
      </div>

      <!-- Dependency Parse Visualization -->
      <div class="col-md-6">
        <div class="panel panel-success indented">
          <div class="panel-heading">
            <h3 class="panel-title">Dependency Syntax Parse
              <span id="depSyn-spinner" class="indented"></span>
              <span id="depSyn-time" class="pull-right"></span>
            </h3>
          </div>
          <div id="depSyn-vis-panel" class="panel-body">
            <svg id="depSyn-vis-dtree" class="visTreeBox"
                 width="600" height="400"
                 version="1.1" xmlns="http://www.w3.org/2000/svg"></svg>
          </div>
        </div>
      </div>

    </div>  <!-- row -->

    <div class="row">  <!-- row2 -->

      <!-- Constituent Parse Timings -->
      <div class="col-md-6">
        <div class="panel panel-warning indented">
          <div class="panel-heading">
            <h3 class="panel-title">Constituent Parse Timings
              <span id="conSyn-timings-toggle" class="pull-right">Show</span>
            </h3>
          </div>
          <div id="conSyn-timings-panel" class="panel-body">
            <table id="conSyn-timings" class="table table-condensed bg-warning">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Phase</th>
                  <th>Elapsed</th>
                  <th>Start</th>
                  <th>Stop</th>
                </tr>
              </thead>
              <tbody></tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Dependency Parse Timings -->
      <div class="col-md-6">
        <div class="panel panel-success indented">
          <div class="panel-heading">
            <h3 class="panel-title">Dependency Parse Timings
              <span id="depSyn-timings-toggle" class="pull-right">Show</span>
            </h3>
          </div>
          <div id="depSyn-timings-panel" class="panel-body">
            <table id="depSyn-timings" class="table table-condensed bg-success">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Phase</th>
                  <th>Elapsed</th>
                  <th>Start</th>
                  <th>Stop</th>
                </tr>
              </thead>
              <tbody></tbody>
            </table>
          </div>
        </div>
      </div>

    </div>  <!-- row2 -->

    <!-- Constituent Parse Syntax Trees -->
    <div class="col-md-12">
      <div class="panel panel-warning indented">
        <div class="panel-heading">
          <h3 class="panel-title">Constituent Syntax Trees
            <span id="conSyn-syn-trees-toggle" class="pull-right">Show</span>
          </h3>
        </div>
        <div id="conSyn-syn-trees-panel" class="panel-body">
          <table id="conSyn-syn-trees" class="table table-condensed bg-warning">
            <thead>
              <tr>
                <th>sent # placeholder</th>
                <th>sentence placeholder</th>
              </tr>
            </thead>
            <tbody></tbody>
          </table>

          <svg id="conSyn-vis-syntree" class="synTreeBox"
               width="1024" height="600"
               version="1.1" xmlns="http://www.w3.org/2000/svg"></svg>
        </div>
      </div>
    </div>

    <!-- Constituent Parse Dependency Visualization -->
    <div class="col-md-12">
      <div class="panel panel-warning indented">
        <div class="panel-heading">
          <h3 class="panel-title">Constituent Parse Dependencies
            <span id="conSyn-depend-toggle" class="pull-right">Show</span>
          </h3>
        </div>
        <div id="conSyn-depend-panel" class="panel-body">
          <table id="conSyn-depend-sents" class="table table-condensed bg-warning">
            <thead>
              <tr>
                <th>sent # placeholder</th>
                <th>sentence placeholder</th>
              </tr>
            </thead>
            <tbody></tbody>
          </table>

          <svg id="conSyn-depend-vis" class="synTreeBox"
               width="1024" height="600"
               version="1.1" xmlns="http://www.w3.org/2000/svg"></svg>
        </div>
      </div>
    </div>

    <!-- Dependency Parse Dependency Visualization -->
    <div class="col-md-12">
      <div class="panel panel-success indented">
        <div class="panel-heading">
          <h3 class="panel-title">Dependency Parse Dependencies
            <span id="depSyn-depend-toggle" class="pull-right">Show</span>
          </h3>
        </div>
        <div id="depSyn-depend-panel" class="panel-body">
          <table id="depSyn-depend-sents" class="table table-condensed bg-success">
            <thead>
              <tr>
                <th>sent # placeholder</th>
                <th>sentence placeholder</th>
              </tr>
            </thead>
            <tbody></tbody>
          </table>

          <svg id="depSyn-depend-vis" class="synTreeBox"
               width="1024" height="600"
               version="1.1" xmlns="http://www.w3.org/2000/svg"></svg>
        </div>
      </div>
    </div>

    <p class="spacer">&nbsp;</p>

  </body>
</html>
