<!DOCTYPE html>
<html>
  <head>
    <link href="style1.css" rel="stylesheet">
    <title>Mashups with google.maps.Data</title>
		<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	<script src="https://code.jquery.com/jquery-1.10.2.js"></script>

  <body>

  <div class="wrapper">
    <header class="header">
    <br>
      <h1> This is the Topic name </h1>
    </header> <!-- .header-->
    <br><br>
    <!-- Content -->
    <div class="content-home">
    <br>
    <div id="controls" class="nicebox">
      <div id ="score_value" >
        <button id ="Score" onclick="myMap()" value="http://115.146.93.244:5984/processed_melbourne_tweets/_design/sum_sentiment/_view/sentiment?group=true"> Sentiment Analysis </button>
        <button id ="Income" onclick="showIncome(this.id)"> Income </button>
				<button id = "Pop" onclick="showIncome(this.id)"> Pop</button>
				<button onclick="draw()">Chart comparison</button>
				<button onclick="clear()"> Clear Table</button>
      </div>
      <div id="legend">
        <div id="census-min">min</div>
        <div class="color-key"><span id="data-caret">&#x25c6;</span></div>
        <div id="census-max">max</div>
      </div>
    </div>
    <div id="data-box" class="nicebox">
      <label id="data-label" for="data-value"></label>
      <span id="data-value"></span>
    </div>
    <div id="map" style="width:100%;height:600px;"></div>
		<div id="chart_div" style="width: 900px; height: 500px; display:none"></div>
	</div>

</div>
    <script type="text/javascript">
      var mapStyle = [{
        'stylers': [{'visibility': 'off'}]
      }, {
        'featureType': 'landscape',
        'elementType': 'geometry',
        'stylers': [{'visibility': 'on'}, {'color': '#fcfcfc'}]
      }, {
        'featureType': 'water',
        'elementType': 'geometry',
        'stylers': [{'visibility': 'on'}, {'color': '#bfd4ff'}]
      }];
			var smallest;
			var biggest;
      var map;
			var getVar;
			var key = new Array();
			//Array for later chart
			var incdata = new Array();
			var Score= new Array();
function clear()
{
	Score= [];
	incdata = [];
}
function draw()
{

	google.charts.load('visualization', '1', {'packages':['corechart']});
	google.charts.setOnLoadCallback(drawChart);

}

      function drawChart() {

				var maxM = -9999999;
				var maxV = -9999999;
				if(Score.length == 0 || incdata.length == 0)
				{
					 window.alert("Please load data first");
				}
				else
				{
						document.getElementById('chart_div').style.display = 'block';
						arr = [];
						Score.forEach(function(v,i){

						var obj = {};
						obj.meta = v;
						obj.value = incdata[i];

						if(obj.meta > maxM)
						{
							maxM= obj.meta;
						}
						if(obj.value > maxV)
						{
							maxV = obj.value;
						}
						arr.push(obj);
					});
 					var data = new google.visualization.DataTable();
					data.addColumn('number','meta');
					data.addColumn('number', 'value');
					data.addRows(arr.map(function(value){
						return[value.meta,value.value];
					}));

					var options = {
						title: 'Income vs. Happiness comparison',
						hAxis: {title: 'Happiness', minValue: 0, maxValue: (maxM+10)},
						vAxis: {title: 'Income', minValue: 0, maxValue: maxV},
						legend: 'none'
					};

					var chart = new google.visualization.ScatterChart(document.getElementById('chart_div'));

					chart.draw(data, options);
				}
		}
//-------init the map with the location in melbourne ------
    function firstMap(){
      map= new google.maps.Map(document.getElementById("map"), {
      zoom: 8,
      center: {lat: -36.686043, lng: 143.580322},
      scrollwheel: true
    });
    loadMapShapes(map);

  }
//---- get the income into map------
    function showIncome(id)
    {
			//clearCensusData();
			smallest = Number.MAX_VALUE;
			biggest = -Number.MAX_VALUE;
			getVar="";
				//clearCensusData(map);
				if(id == "Income")
				{
					getVar="Income";
				}
				else if(id == "Pop") {
						getVar="Pop";
				}
				getBoundary();
				loadMapShapes();
				map.data.setStyle(styleFeature);
				map.data.addListener('mouseover', mouseInToRegion);
				map.data.addListener('mouseout', mouseOutOfRegion);
    }
		function getBoundary(feature){
			var a =0;
			map.data.forEach(function(feature) {
					incdata[a] = feature.getProperty(getVar);
				if (incdata[a] < smallest){
						smallest = incdata[a];

				 }
				if(incdata[a] > biggest) {
						biggest = incdata[a];

					}
				a++;
			});
			setlabel();

		}
// ---- used to load the sentiment -----------
    function myMap() {
			clearCensusData();
			smallest = Number.MAX_VALUE;
			biggest = -Number.MAX_VALUE;
				getVar="Score";
				loadMapShapes();
      	loadTweetData(key,map);
      		//var data = new Array();
  			 setvalue();

      		map.data.setStyle(styleFeature);
      		map.data.addListener('mouseover', mouseInToRegion);
      		map.data.addListener('mouseout', mouseOutOfRegion);

      } //-- end of loading mymap function -----------------------------

			function setvalue()
			{
				setTimeout(function() {
					 map.data.forEach(function(feature) {
						 for (var i = 0; i < key.length; i++)	{
							 if(key[i].key == feature.getProperty("SA2_MAINCODE_2011")){
									 feature.setProperty("Score",key[i].value);

								 }}});
					 }, 500);

			}
// ---- clear the date --------------------
      function clearCensusData(feature) {
				map.data.forEach(function (feature) {
				map.data.remove(feature);
				});
            //loadMapShapes();
            }
//-------------------load the melbourne SA2 suburb sape based on geojson---------------
      function loadMapShapes() {


				map.data.loadGeoJson('./Mel_with_income.geojson', { idPropertyName: 'STATE' });
      // load melbourne geojson file localy as the suburb dont change


      // waitting for extra feature add on if there have any
    //google.maps.event.addListenerOnce(map.data, 'addfeature', function() {
    //google.maps.event.trigger(document.getElementById('score_value'),
      //     'change');
    //  });
    }
// ------ mouse move to the Polygon-----------
      function mouseInToRegion(e) {
        // set the hover state so the setStyle function can change the border
        e.feature.setProperty('state', 'hover');
        var percent = (e.feature.getProperty(getVar)) /
          biggest * 100;
        // update the label
        document.getElementById('data-label').textContent =
            e.feature.getProperty('SA2_NAME_2011');
        document.getElementById('data-value').textContent =
            e.feature.getProperty(getVar).toLocaleString();
        document.getElementById('data-box').style.display = 'block';
        document.getElementById('data-caret').style.display = 'block';
        document.getElementById('data-caret').style.paddingLeft = percent + '%';
      }
// -----------move out those Polygon------------------------
      function mouseOutOfRegion(e) {
        // reset the hover state, returning the border to normal
        e.feature.setProperty('state', 'normal');
      }
//---------------- lode data from coachdb ------------------------
      function loadTweetData(key) {
      	// load the requested getVar from the census API (using local copies)
      	var tweetData =  <?php
      					$ch = curl_init();
      					curl_setopt($ch, CURLOPT_URL,
      					'http://115.146.93.244:5984/processed_melbourne_tweets/_design/sum_/_view/sentiment?group=true');
      					curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');
      					curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
      					curl_setopt($ch, CURLOPT_HTTPHEADER, array(
      						'Content-type: application/json',
      						'Accept: */*'
      					));
      					curl_setopt($ch, CURLOPT_USERPWD, 'admin:admin');
      					$response = curl_exec($ch);
      					echo $response;
      					curl_close($ch);
      					?>;
      	var data = tweetData.rows;
        if(data != null)
        {
      	   for(var i = 0; i < data.length; i++) {
      					var obj = data[i];
      					 key[i] = obj;
								 Score[i] = key[i].value;
    					 if (key[i].value <smallest){
      						 smallest =key[i].value;
      					}
    					 if(key[i].value >biggest) {
      						 biggest = key[i].value;
      					 }

      				}

              setlabel();
            }
            else {
              window.alert("No file loaded");
              map.data.loadGeoJson('./Mel_with_income.geojson', { idPropertyName: 'STATE' });
            }

      		}

function setlabel()
{

	document.getElementById('census-min').textContent =
	smallest.toLocaleString();
	document.getElementById('census-max').textContent =
	biggest.toLocaleString();
}

// ------------ set color for -----------------
      function styleFeature(feature) {



        var low = [5, 69, 54];  // color of smallest datum
        var high = [151, 83, 34];   // color of largest datum
        // delta represents where the value sits between the min and max
        var delta = (feature.getProperty(getVar) - smallest) /
            (biggest - smallest);

        var color = [];
        for (var i = 0; i < 3; i++) {
          // calculate an integer color based on the delta
          color[i] = (high[i] - low[i]) * delta + low[i];
        }
        // determine whether to show this shape or not
        var showRow = true;
        if (feature.getProperty(getVar) == null) {
          showRow = false;
        }
        var outlineWeight = 0.5, zIndex = 1;
        if (feature.getProperty('state') === 'hover') {
          outlineWeight = zIndex = 2;
        }

        return {
          strokeWeight: outlineWeight,
          strokeColor: '#fff',
          zIndex: zIndex,
          fillColor: 'hsl(' + color[0] + ',' + color[1] + '%,' + color[2] + '%)',
          fillOpacity: 0.75,
          visible: showRow
        };
      }

      	</script>



				<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDOyHhybVKpBWr6c27HmL3qa1cONaAhwFs&callback=firstMap"></script>

  </body>
</html>
