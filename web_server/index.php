<?php
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Credentials: true")
?>
<!DOCTYPE html>
<html>
  <head>
    <link href="style1.css" rel="stylesheet">
    <title>Melbourne tweetw sentiment analysis</title>
		<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
  <body>
  <div class="wrapper">
    <header class="header">
    <br>
      <h1> Tweet Analysis </h1>
    </header> <!-- .header-->
    <br><br>
      <div id ="tag1" style="font-weight:150;font-size: 40px;color: #281328;text-align: center; display:none"> Sentiment Analysis </div>
      <div id ="tag2" style="font-weight:150;font-size: 40px;color: #281328;text-align: center; display:none"> Income </div>
      <div id ="tag3" style="font-weight:150;font-size: 40px;color: #281328;text-align: center; display:none"> Population </div>
    <!-- Content -->
    <div class="content-home">
    <br>
    <div id="controls" class="nicebox">
      <div id ="score_value" >
        <button id ="Score" onclick="myMap()" value="http://115.146.93.244:5984/processed_melbourne_tweets/_design/sum_sentiment/_view/sentiment?group=true"> Sentiment Analysis </button>
        <button id ="Income" onclick="showIncome(this.id)"> Income </button>
				<button id = "Pop" onclick="showIncome(this.id)"> Pop</button>
				<button onclick="draw()">Chart comparison</button>
      </div>

      <div id="legend">
        <div id="min">min</div>
        <div class="color-key"><span id="data-caret">&#x25c6;</span></div>
        <div id="max">max</div>
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

      function draw(){
        	google.charts.load('visualization', '1', {'packages':['corechart']});
        	google.charts.setOnLoadCallback(drawChart);
      }

      function drawChart() {
				var maxM = -9999999;
				var maxV = -9999999;
				if(Score.length == 0 || incdata.length == 0){
					 window.alert("Please load data first");
				}
				else{
						document.getElementById('chart_div').style.display = 'block';
						arr = [];
						Score.forEach(function(v,i){

						var obj = {};
						obj.meta = v;
						obj.value = incdata[i];

						if(obj.meta > maxM){
							maxM= obj.meta;
						}
						if(obj.value > maxV){
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
						title: getVar+' vs. Happiness comparison',
						hAxis: {title: 'Happiness', minValue: 0, maxValue: (maxM+10)},
						vAxis: {title:  getVar, minValue: 0, maxValue: maxV},
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
      center: {lat: -37.8136, lng: 144.9631},
      scrollwheel: true
    });
    loadMapShapes(map);

  }
//---- get the income into map------
    function showIncome(id){
			//clearCensusData();
			smallest = Number.MAX_VALUE;
			biggest = -Number.MAX_VALUE;
			getVar="";
				//clearCensusData(map);
				if(id == "Income"){
					getVar="Income";
          document.getElementById('tag1').style.display = 'none';
          document.getElementById('tag2').style.display = 'block';
          document.getElementById('tag3').style.display = 'none';
				}
				else if(id == "Pop") {
					getVar="Pop";
          document.getElementById('tag1').style.display = 'none';
          document.getElementById('tag2').style.display = 'none';
          document.getElementById('tag3').style.display = 'block';
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
        document.getElementById('tag1').style.display = 'block';
        document.getElementById('tag2').style.display = 'none';
        document.getElementById('tag3').style.display = 'none';
        clearCensusData();
        smallest = Number.MAX_VALUE;
        biggest = -Number.MAX_VALUE;
        getVar="Score";
        loadMapShapes();
        loadTweetData(key,map);

        map.data.setStyle(styleFeature);
        map.data.addListener('mouseover', mouseInToRegion);
        map.data.addListener('mouseout', mouseOutOfRegion);

      } //-- end of loading mymap function -----------------------------

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
        var tweetData;
        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'http://115.146.93.244:5984/processed_melbourne_tweets/_design/sum_/_view/sentiment?group=true');
        xhr.onload = function() {
        tweetData = JSON.parse(xhr.responseText);
        var data = tweetData.rows;
         if(data != null){
            for(var i = 0; i < data.length; i++) {
                  //key store for later use
                  key[i] = data[i];
                  Score[i] = key[i].value;
                if (key[i].value <smallest){
                    smallest =key[i].value;
                 }
                if(key[i].value >biggest) {
                    biggest = key[i].value;
                  }
                  setTimeout(function() {
          					 map.data.forEach(function(feature) {
          						 for (var i = 0; i < key.length; i++)	{
          							 if(key[i].key == feature.getProperty("SA2_MAINCODE_2011")){
          									 feature.setProperty("Score",key[i].value); }}});
          					 }, 500);
               }
               setlabel();
               }
               else {
                 window.alert("No data loaded");
                 map.data.loadGeoJson('./Mel_with_income.geojson', { idPropertyName: 'STATE' });
               }};
      xhr.send();
      		}

function setlabel()
{

	document.getElementById('min').textContent =
	smallest.toLocaleString();
	document.getElementById('max').textContent =
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
=======
<?php
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Credentials: true")
?>
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
      <h1> Tweet Analysis </h1>
    </header> <!-- .header-->
    <br><br>
      <div id ="tag1" style="font-weight:150;font-size: 40px;color: #281328;text-align: center; display:none"> Sentiment Analysis </div>
      <div id ="tag2" style="font-weight:150;font-size: 40px;color: #281328;text-align: center; display:none"> Income </div>
      <div id ="tag3" style="font-weight:150;font-size: 40px;color: #281328;text-align: center; display:none"> Population </div>
    <!-- Content -->
    <div class="content-home">
    <br>
    <div id="controls" class="nicebox">
      <div id ="score_value" >
        <button id ="Score" onclick="myMap()" value="http://115.146.93.244:5984/processed_melbourne_tweets/_design/sum_sentiment/_view/sentiment?group=true"> Sentiment Analysis </button>
        <button id ="Income" onclick="showIncome(this.id)"> Income </button>
				<button id = "Pop" onclick="showIncome(this.id)"> Pop</button>
				<button onclick="draw()">Chart comparison</button>
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

      function draw(){
        	google.charts.load('visualization', '1', {'packages':['corechart']});
        	google.charts.setOnLoadCallback(drawChart);
      }

      function drawChart() {
				var maxM = -9999999;
				var maxV = -9999999;
				if(Score.length == 0 || incdata.length == 0){
					 window.alert("Please load data first");
				}
				else{
						document.getElementById('chart_div').style.display = 'block';
						arr = [];
						Score.forEach(function(v,i){

						var obj = {};
						obj.meta = v;
						obj.value = incdata[i];

						if(obj.meta > maxM){
							maxM= obj.meta;
						}
						if(obj.value > maxV){
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
						title: getVar+' vs. Happiness comparison',
						hAxis: {title: 'Happiness', minValue: 0, maxValue: (maxM+10)},
						vAxis: {title: getVar, minValue: 0, maxValue: maxV},
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
    function showIncome(id){
			//clearCensusData();
			smallest = Number.MAX_VALUE;
			biggest = -Number.MAX_VALUE;
			getVar="";
				//clearCensusData(map);
				if(id == "Income"){
					getVar="Income";
          document.getElementById('tag1').style.display = 'none';
          document.getElementById('tag2').style.display = 'block';
          document.getElementById('tag3').style.display = 'none';
				}
				else if(id == "Pop") {
						getVar="Pop";
            document.getElementById('tag1').style.display = 'none';
            document.getElementById('tag2').style.display = 'none';
            document.getElementById('tag3').style.display = 'block';
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
      	document.getElementById('tag1').style.display = 'block';
        document.getElementById('tag2').style.display = 'none';
        document.getElementById('tag3').style.display = 'none';
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

			function setvalue(){
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
        var tweetData = {"rows":[
{"key":null,"value":16},
{"key":"206011105","value":13},
{"key":"206011106","value":16},
{"key":"206011107","value":15},
{"key":"206011108","value":18},
{"key":"206011109","value":17},
{"key":"206021110","value":14},
{"key":"206021111","value":13},
{"key":"206021112","value":14},
{"key":"206031113","value":14},
{"key":"206031114","value":16},
{"key":"206031115","value":17},
{"key":"206031116","value":16},
{"key":"206041117","value":14},
{"key":"206041118","value":14},
{"key":"206041119","value":16},
{"key":"206041120","value":20},
{"key":"206041121","value":15},
{"key":"206041122","value":17},
{"key":"206041123","value":14},
{"key":"206041124","value":14},
{"key":"206041125","value":15},
{"key":"206041126","value":15},
{"key":"206041127","value":13},
{"key":"206051128","value":13},
{"key":"206051129","value":14},
{"key":"206051130","value":17},
{"key":"206051131","value":20},
{"key":"206051132","value":14},
{"key":"206051133","value":16},
{"key":"206051134","value":15},
{"key":"206061135","value":14},
{"key":"206061136","value":14},
{"key":"206061137","value":14},
{"key":"206061138","value":17},
{"key":"206071139","value":13},
{"key":"206071140","value":14},
{"key":"206071141","value":15},
{"key":"206071142","value":13},
{"key":"206071143","value":14},
{"key":"206071144","value":15},
{"key":"206071145","value":14},
{"key":"207011146","value":9},
{"key":"207011147","value":18},
{"key":"207011148","value":18},
{"key":"207011149","value":15},
{"key":"207011150","value":15},
{"key":"207011151","value":14},
{"key":"207011152","value":14},
{"key":"207011153","value":13},
{"key":"207011154","value":11},
{"key":"207011155","value":20},
{"key":"207021156","value":15},
{"key":"207021157","value":21},
{"key":"207021158","value":17},
{"key":"207021159","value":17},
{"key":"207021160","value":15},
{"key":"207031161","value":18},
{"key":"207031162","value":14},
{"key":"207031163","value":21},
{"key":"207031164","value":16},
{"key":"207031165","value":16},
{"key":"207031166","value":18},
{"key":"207031167","value":7},
{"key":"208011168","value":15},
{"key":"208011169","value":14},
{"key":"208011170","value":17},
{"key":"208011171","value":17},
{"key":"208011172","value":19},
{"key":"208011173","value":16},
{"key":"208021174","value":16},
{"key":"208021175","value":12},
{"key":"208021176","value":19},
{"key":"208021177","value":14},
{"key":"208021178","value":21},
{"key":"208021179","value":14},
{"key":"208021180","value":12},
{"key":"208021181","value":13},
{"key":"208021182","value":24},
{"key":"208031183","value":15},
{"key":"208031184","value":5},
{"key":"208031185","value":20},
{"key":"208031186","value":14},
{"key":"208031187","value":17},
{"key":"208031188","value":13},
{"key":"208031189","value":16},
{"key":"208031190","value":20},
{"key":"208031191","value":22},
{"key":"208031192","value":2},
{"key":"208031193","value":14},
{"key":"208041194","value":16},
{"key":"208041195","value":15},
{"key":"209011196","value":9},
{"key":"209011197","value":22},
{"key":"209011198","value":16},
{"key":"209011199","value":23},
{"key":"209011200","value":16},
{"key":"209011201","value":17},
{"key":"209011202","value":17},
{"key":"209011203","value":15},
{"key":"209011204","value":13},
{"key":"209021205","value":18},
{"key":"209021206","value":14},
{"key":"209021207","value":15},
{"key":"209021208","value":19},
{"key":"209031209","value":20},
{"key":"209031210","value":6},
{"key":"209031211","value":5},
{"key":"209031212","value":14},
{"key":"209031213","value":14},
{"key":"209031214","value":11},
{"key":"209031215","value":14},
{"key":"209041216","value":21},
{"key":"209041217","value":18},
{"key":"209041218","value":15},
{"key":"209041219","value":16},
{"key":"209041220","value":21},
{"key":"209041221","value":19},
{"key":"209041222","value":14},
{"key":"209041223","value":14},
{"key":"209041224","value":15},
{"key":"209041225","value":8},
{"key":"210011226","value":16},
{"key":"210011227","value":9},
{"key":"210011228","value":14},
{"key":"210011229","value":18},
{"key":"210011230","value":17},
{"key":"210011231","value":16},
{"key":"210021232","value":20},
{"key":"210021233","value":1},
{"key":"210021234","value":7},
{"key":"210021235","value":2},
{"key":"210031236","value":17},
{"key":"210031237","value":25},
{"key":"210031238","value":15},
{"key":"210031239","value":13},
{"key":"210041240","value":16},
{"key":"210041241","value":14},
{"key":"210051242","value":19},
{"key":"210051243","value":14},
{"key":"210051244","value":14},
{"key":"210051245","value":19},
{"key":"210051246","value":17},
{"key":"210051247","value":9},
{"key":"210051248","value":15},
{"key":"210051249","value":14},
{"key":"210051250","value":20},
{"key":"211011251","value":17},
{"key":"211011252","value":20},
{"key":"211011253","value":14},
{"key":"211011254","value":15},
{"key":"211011255","value":16},
{"key":"211011256","value":20},
{"key":"211011257","value":7},
{"key":"211011258","value":15},
{"key":"211011259","value":17},
{"key":"211011260","value":15},
{"key":"211021261","value":18},
{"key":"211021262","value":15},
{"key":"211031263","value":22},
{"key":"211031264","value":18},
{"key":"211031265","value":17},
{"key":"211031266","value":16},
{"key":"211031267","value":16},
{"key":"211031268","value":17},
{"key":"211041269","value":22},
{"key":"211041270","value":19},
{"key":"211041271","value":13},
{"key":"211041272","value":15},
{"key":"211041273","value":8},
{"key":"211051274","value":19},
{"key":"211051275","value":17},
{"key":"211051276","value":18},
{"key":"211051277","value":8},
{"key":"211051278","value":17},
{"key":"211051279","value":3},
{"key":"211051280","value":4},
{"key":"211051281","value":17},
{"key":"211051282","value":15},
{"key":"211051283","value":10},
{"key":"211051284","value":18},
{"key":"211051285","value":3},
{"key":"211051286","value":18},
{"key":"212011287","value":16},
{"key":"212011288","value":1},
{"key":"212011289","value":9},
{"key":"212011291","value":11},
{"key":"212011292","value":16},
{"key":"212021293","value":20},
{"key":"212021294","value":15},
{"key":"212021295","value":17},
{"key":"212021296","value":22},
{"key":"212021297","value":16},
{"key":"212021298","value":16},
{"key":"212021299","value":6},
{"key":"212031300","value":16},
{"key":"212031301","value":9},
{"key":"212031302","value":20},
{"key":"212031303","value":20},
{"key":"212031304","value":15},
{"key":"212031305","value":16},
{"key":"212031306","value":12},
{"key":"212031307","value":20},
{"key":"212031308","value":11},
{"key":"212041309","value":10},
{"key":"212041310","value":15},
{"key":"212041311","value":14},
{"key":"212041312","value":18},
{"key":"212041313","value":18},
{"key":"212041314","value":16},
{"key":"212041315","value":14},
{"key":"212041316","value":16},
{"key":"212041317","value":17},
{"key":"212041318","value":15},
{"key":"212051319","value":19},
{"key":"212051320","value":14},
{"key":"212051321","value":17},
{"key":"212051322","value":19},
{"key":"212051323","value":17},
{"key":"212051324","value":15},
{"key":"212051325","value":22},
{"key":"212051326","value":15},
{"key":"212051327","value":16},
{"key":"213011328","value":18},
{"key":"213011329","value":9},
{"key":"213011330","value":17},
{"key":"213011331","value":17},
{"key":"213011332","value":17},
{"key":"213011333","value":17},
{"key":"213011334","value":16},
{"key":"213011335","value":10},
{"key":"213011336","value":14},
{"key":"213011337","value":16},
{"key":"213011338","value":13},
{"key":"213011339","value":14},
{"key":"213011340","value":20},
{"key":"213021341","value":21},
{"key":"213021342","value":15},
{"key":"213021343","value":15},
{"key":"213021344","value":15},
{"key":"213021345","value":6},
{"key":"213021346","value":15},
{"key":"213031347","value":17},
{"key":"213031348","value":13},
{"key":"213031349","value":14},
{"key":"213031350","value":20},
{"key":"213031351","value":16},
{"key":"213031352","value":14},
{"key":"213041353","value":19},
{"key":"213041354","value":17},
{"key":"213041355","value":15},
{"key":"213041356","value":15},
{"key":"213041357","value":14},
{"key":"213041358","value":20},
{"key":"213041359","value":10},
{"key":"213041360","value":17},
{"key":"213051361","value":17},
{"key":"213051362","value":16},
{"key":"213051363","value":19},
{"key":"213051364","value":14},
{"key":"213051365","value":14},
{"key":"213051366","value":21},
{"key":"213051367","value":13},
{"key":"213051368","value":16},
{"key":"213051369","value":17},
{"key":"214011370","value":16},
{"key":"214011371","value":17},
{"key":"214011372","value":19},
{"key":"214011373","value":15},
{"key":"214011374","value":21},
{"key":"214011375","value":16},
{"key":"214011376","value":14},
{"key":"214021380","value":19},
{"key":"214021381","value":19},
{"key":"214021382","value":16},
{"key":"214021385","value":12}
]}
//---------------the       header('Access-Control-Allow-Origin: *'); is not working -------
    /**** url = "http://115.146.93.244:5984/processed_melbourne_tweets/_design/sum_/_view/sentiment?group=true";
              var tweetData= $.ajax({
              url: url,
              user: "admin",
              pass: "admin",
              dataType: 'json',
              Origin: "http://localhost:3000",
              contentType: "application/json; charset=utf-8",
              success: ( function(Response){ console.log(26, 'response is: ',
              Response) } ),
              error: function(XMLHttpRequest, textStatus,
              errorThrown){alert("Error"); }
              });
    ****/
      	var data = tweetData.rows;
        if(data != null)  {
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
              window.alert("No data loaded");
              map.data.loadGeoJson('./Mel_with_income.geojson', { idPropertyName: 'STATE' });
            }

      		}

function setlabel(){

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
