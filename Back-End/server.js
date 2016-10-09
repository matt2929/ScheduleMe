var express = require('express');
var app = express();
var fs = require("fs");
var mongoose = require('mongoose');

var db=mongoose.connection;

db.on('error', console.error);
db.once('open', function() {
var friendsListSchema= mongoose.Schema({
  User: String,
  rating: [String]
});

var EventListSchema= mongoose.Schema({
  User: String,
  Events: [Date]
});
 
 
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
