

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

var UserSchema= mongoose.Schema({
  name: String,
  friends: [String],
  eventIsSent: [Boolean],
  eventDate:[Date],		
  eventNameAndFriends:[String],
  eventAccepted: [Boolean],
});

var User=mongoose.model("User", UserSchema); 

var eventListSchema= mongoose.Schema({
  User: String,
  Events: [Date]
});

var Events=mongoose.model("Events", eventListSchema) 
 
});

mongoose.connect('mongodb://localhost/user');

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
//------------Test post----------------------------------------------------------------------
app.post('/process_post', function (req, res) {
   console.log(JSON.stringify(req.body));
   var a = JSON.parse(JSON.stringify(req.body));
   var name = a.name;
   var password = a.password;
   var prof = a.profession;
   var id = a.id;
   var person ={
       User: a.name,
       Password: a.password,
       Profession: a.profession,
       Id: a.id
};
//   console.log("you just added a new user ~~~~~~~\n"+"Name: {"+name+"}\n"+"Password: {"+password+"}\n"+"Profession:i{"+prof+"}\nID: {"+id+"}");
   db.collection('Test').insert(person,function (err,doc){
//   console.log(date);
if(err) throw err;
});
   db.close();
   res.end("");
})
//--------------post into friends table------------------------------------------------------------------------
app.post('/user_post', function (req, res) {
   console.log(JSON.stringify(req.body));
   var a = JSON.parse(JSON.stringify(req.body));
   var user ={
       User: a.name,
       Friends: a.friends,
       Events: a.eventIsSent,
       InviteDate: a.eventDate,
       EventDateAndFriends: a.eventNameAndFriends,
       Accepted: a.eventIsAccepted
};
   db.collection('User').insert(user,function (err,doc){
if(err) throw err;
});
   db.close();
   res.end("");
})
//-----------------post into events table--------------------------------------------------------------------------------------------
app.post('/events_post', function (req, res) {
   console.log(JSON.stringify(req.body));
   var a = JSON.parse(JSON.stringify(req.body));
   var user = a.user;
   var events = a.events;
   var user ={
       User: a.user,
       Events: a.Events
};
console.log(db.stats());
   db.collection('Events').insert(user,function (err,doc){
if(err) throw err;
});
   db.close();
   res.end("");
})
//--------------------Get friends list-----------------------------------------------------------------------------------------------
app.get('/friends_get',function(req,res){
db.collection('Friends').find({User: req.body}, function(err,result){
if (err){
res.send("error retrieving friends");
}else{
res.send(result);
}

});
});

//----------------------------------------------------------------------------------------------------------------------
app.post('/getUser', function (req, res) {
        var a = JSON.parse(JSON.stringify(req.body));
	console.log("1:"+a.name);
	db.collection('User').findOne({'name': a.name},function(err,q){
        console.log(a.name);
		console.log("we got here.");
		if(err){
		   console.log(err);
		   return;
		}
		if(q){
		   console.log('account exists');
		   console.log(q);

		}else{
		console.log('good to go');
		}
   });
});
//-------------------------------------------------------------

var server = app.listen(8083, function () {
   var host = server.address().address
   var port = server.address().port

   console.log("Example app listening at http://%s:%s", host, port)
})
