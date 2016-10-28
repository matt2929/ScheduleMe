
var express = require('express');
var app = express();
parser = require('body-parser');
app.use(parser.json());
var fs = require("fs");
var mongoose = require('mongoose');
var db=mongoose.connection;
db.on('error', console.error);

db.once('open', function() {
var Schema=mongoose.Schema;

var testSchema= mongoose.Schema({
  User: String,
  Password: String,
  Profession: String,
  Id: Number
});

var Test=mongoose.model("Test", testSchema);

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

//fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
//       var allUsers = JSON.parse(data);
//	for (var i =0; i < allUsers.length;i++){
 // 		console.log("\n"+allUsers[i].name+"...Loaded");
// 		map[""+(allUsers[i].name)]=allUsers[i];
// 	}
//})

app.get('/listUsers', function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
      console.log( data );
      res.end( data );
   });
})


app.get('/addUser/:newuser', function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
      data = JSON.parse(req.params.newuser);
      console.log( data );
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
   var a = JSON.parse(JSON.stringify(req.body));
   var name = a.name;
   var password = a.password;
   var prof = a.profession;
   var id = a.id;
   console.log("you just added a new user ~~~~~~~\n"+"Name: {"+name+"}\n"+"Password: {"+password+"}\n"+"Profession:i{"+prof+"}\nID: {"+id+"}");
   db.collection('Test').insert(JSON.parse(req.body),function (err,doc){
   console.log(date);
if(err) throw err;
});
   db.close();
   res.end("");
})

var server = app.listen(8082, function () {
   var host = server.address().address
   var port = server.address().port

   console.log("Example app listening at http://%s:%s", host, port)
})
