// See post: http://asmaloney.com/2014/01/code/creating-an-interactive-map-with-leaflet-and-openstreetmap/

// creates a new map, assigns to map div, and sets options
var map = L.map( 'map', {
  center: [41, -100],
  minZoom: 2,
  zoom: 3
});

L.tileLayer( 'http://{s}.mqcdn.com/tiles/1.0.0/map/{z}/{x}/{y}.png', {
  attribution: '&copy; <a href="http://osm.org/copyright" title="OpenStreetMap" target="_blank">OpenStreetMap</a> contributors | Tiles Courtesy of <a href="http://www.mapquest.com/" title="MapQuest" target="_blank">MapQuest</a> <img src="http://developer.mapquest.com/content/osm/mq_logo.png" width="16" height="16">',
  subdomains: ['otile1','otile2','otile3','otile4']
}).addTo( map );

var myURL = jQuery( 'script[src$="map-leaf.js"]' ).attr( 'src' ).replace( 'map-leaf.js', '' );

var myIcon = L.icon({
  iconUrl: myURL + 'images/pin24.png',
  iconRetinaUrl: myURL + 'images/pin48.png',
  iconSize: [29, 24],
  iconAnchor: [9, 21],
  popupAnchor: [0, -14]
});

function addDynamicMarker(index) {
  var popup = markers[index].city +
  '<br/><b>Latitude:</b> ' + markers[index].lat +
  '<br/><b>Longitude:</b> ' + markers[index].lng;
  var m = L.marker([markers[index].lat, markers[index].lng], {icon: myIcon} )
  .bindPopup( popup );2
  markerClusters.addLayer( m );
  // setTimeout(function() {
  //   markerClusters.removeLayer(m);
  // }, 1000);
}

var connection = new WebSocket('ws://localhost:8080/ws-echo');

connection.onopen = function () {
  console.log("hallelujah"); // Works!
};
// connection.onmessage = function(e) {
//   var parse = JSON.parse(e.data);
//   console.log(parse);
// };


var markerClusters = L.markerClusterGroup();
for ( var i = 0; i < markers.length; ++i ) {
  addDynamicMarker(i);
}

map.addLayer( markerClusters );
