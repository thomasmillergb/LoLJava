<!DOCTYPE html>
<html>
<head>
  <title>Echo Chamber</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width">
  <link rel="import" href="/test.jsp">
</head>
<body>

<div>
  <input type="text" id="messageinput"/>
</div>
<div>
  <button type="button" onclick="openSocket();" >Open</button>
  <button type="button" onclick="send();" >Send</button>
  <button type="button" onclick="closeSocket();" >Close</button>
</div>
<!-- Server responses get written here -->
<div id="messages"></div>

<!-- Script to utilise the WebSocket -->
<script type="text/javascript">

  var webSocket;
  var messages = document.getElementById("messages");


  function openSocket(){
    // Ensures only one connection is open at a time
    if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
      writeResponse("WebSocket is already opened.");
      return;
    }
    // Create a new instance of the websocket
    webSocket = new WebSocket("ws://localhost:8080/LoLJava/echo");

    /**
     * Binds functions to the listeners for the websocket.
     */
    webSocket.onopen = function(event){
      // For reasons I can't determine, onopen gets called twice
      // and the first time event.data is undefined.
      // Leave a comment if you know the answer.
      if(event.data === undefined)
        return;

      writeResponse(event.data);
    };

    webSocket.onmessage = function(event){
      writeResponse(event.data);
    };

    webSocket.onclose = function(event){
      writeResponse("Connection closed");
    };
  }

  /**
   * Sends the value of the text input to the server
   */
  function send(){
    var text = document.getElementById("messageinput").value;
    webSocket.send(text);
  }

  function closeSocket(){
    webSocket.close();
  }

  function writeResponse(text){
    messages.innerHTML += "<br/>" + text;
  }
  nv.addGraph(function() {
    var chart = nv.models.lineWithFocusChart();

    chart.xAxis
            .tickFormat(d3.format(',f'));

    chart.yAxis
            .tickFormat(d3.format(',.2f'));

    chart.y2Axis
            .tickFormat(d3.format(',.2f'));

    d3.select('#chart svg')
            .datum(data())
            .transition().duration(500)
            .call(chart)
    ;

    nv.utils.windowResize(chart.update);

    return chart;
  });

</script>

</body>
</html>