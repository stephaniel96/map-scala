window.map = L.map( 'map', {
  center: [41, -100],
  minZoom: 2,
  zoom: 3,
  layers:MQ.mapLayer()
});

var myURL = jQuery( 'script[src$="map-leaf.js"]' ).attr( 'src' ).replace( 'map-leaf.js', '' );

var myIcon = L.icon({
  iconUrl: myURL + 'images/pin24.png',
  iconRetinaUrl: myURL + 'images/pin48.png',
  iconSize: [29, 24],
  iconAnchor: [9, 21],
  popupAnchor: [0, -14]
});

var markerClusters = L.markerClusterGroup();
function addDynamicMarker(location) {
  var popup = location.city +
  '<br/><b>Latitude:</b> ' + location.lat +
  '<br/><b>Longitude:</b> ' + location.lng;
  var m = L.marker([location.lat, location.lng], {icon: myIcon} )
  .bindPopup( popup );
  markerClusters.addLayer( m );
  setTimeout(function() {
    markerClusters.removeLayer(m);
  }, 60000);
}
map.addLayer( markerClusters );



var connection = new WebSocket('ws://localhost:8080/getInfo');
connection.onopen = function () {
  console.log("connection established");
};


connection.onmessage = function(e) {
  console.log(e);
  if (e.data) {
    addDynamicMarker(JSON.parse(e.data));
  }
};

window.onbeforeunload = function() {
    websocket.onclose = function () {}; // disable onclose handler first
    websocket.close()
};
