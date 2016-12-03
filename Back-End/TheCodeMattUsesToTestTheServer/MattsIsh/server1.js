var express = require('express');
var app = express();
parser = require('body-parser');
app.use(parser.json());
var fs = require("fs");
var map = new Object
// these statements config express to use these modules, and only need to be run once
fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
       var allUsers = JSON.parse( data );
   // First read existing users.
	for (var i =0; i < allUsers.length;i++){
  		console.log("\n"+allUsers[i].name+"...Loaded");
 		map[""+(allUsers[i].name)]=allUsers[i];
 	}
})

app.get('/listUsers', function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
      console.log( data );
      res.end( data );
   });
})

app.get("/listUser/:name", function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
   console.log("The param"+req.params.name);
   res.end(map[req.params.name]);
   });
})

app.get('/addUser/:newuser', function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
      data = JSON.parse(req.params.newuser);
      console.log( data );
      res.end( JSON.stringify(data));
   });
})

app.get('/:id', function (req, res) {
fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
	data = JSON.parse(JSON.stringify(data));
   // First read existing users.
	res.end( JSON.stringify(map[req.params.id]));
   });
})

app.post('/process_post', function (req, res) {
   console.log(JSON.stringify(req.body));
   res.end(""); 
})

var server = app.listen(8081, function () {
   var host = server.address().address
   var port = server.address().port
})
