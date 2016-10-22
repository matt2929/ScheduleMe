dar express = require('express');
var app = express();
var fs = require("fs");
var mongoose = require('mongoose');

var db=mongoose.connection;

db.on('error', console.error);
db.once('open', function() {
var Schema=mongoose.Schema;

var friendsListSchema= mongoose.Schema({
  User: String,
  Friends: [String]
});

var Friends=mongoose.model("Friends", friendsListSchema); 

var eventListSchema= mongoose.Schema({
  User: String,
  Events: [Date]
});

var Events=mongoose.model("Events", eventListSchema) 
 
});

mongoose.connect('mongodb://localhost/test');

app.get('/listUsers', function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
      console.log( data );
      res.end( data );
   });
})

var server = app.listen(8888, function () {
   var host = server.address().address
   var port = server.address().port

   console.log("Example app listening at http://%s:%s", host, port)
})
