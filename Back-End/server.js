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
var UserSchema= mongoose.Schema({
  name: String,
  schedule:[String],
  friends: [[String]],
  sentInvites: [],
  recievedInvites:[],		
}
, {collection: 'User'});
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
app.post('/user_post', function (req, res) {
   console.log("user_post"+JSON.stringify(req.body));
   var a = JSON.parse(JSON.stringify(req.body));
   var userTemp ={
       name: a.name,
       schedule: a.schedule,
       friends: a.friends,
       sentInvites: a.sentInvites,
       recievedInvites: a.recievedInvites,

}
   db.collection('User').update(a,function (err,doc){
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
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*app.post('/update_post', function (req, res) {
   console.log("update_post"+JSON.stringify(req.body));
   var a = JSON.parse(JSON.stringify(req.body));
   db.collection('User').update(
        {name: a.name},
		{$set:
			{
			friends: a.friends,
			eventIsSent: a.eventIsSent,
			eventDate: a.eventDate,
			eventNameAndFriends: a.eventNameAndFriends,
			eventIsAccepted: a.eventIsAccepted
			
			}
		
		}
)
   db.close();
   res.end("");
})*/

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`


app.post('/update_events', function (req, res) {
   console.log("update_post"+JSON.stringify(req.body));
   var a = JSON.parse(JSON.stringify(req.body));
/*   var userTemp ={
       name: a.name,
       friends: a.friends,
       eventIsSent: a.eventIsSent,
       eventDate: a.eventDate,
       eventNameAndFriends: a.eventNameAndFriends,
       eventIsAccepted: a.eventIsAccepted
}*/
   db.collection('User').update(
	{name: a.name},
		{$set:
			{
			friends: a.friends,
			schedule: a.schedule,
			sentInvites: a.sentInvites,
			recievedInvites:a.recievedInvites ,
			}
		
		}
)
   db.close();
   res.end("");
})



//--------------------Get friends list-----------------------------------------------------------------------------------------------

app.get('/friends_get',function(req,res){
	db.collection('Friends').findOne({User: req.body}, function(err,result){
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
	console.log("1:{"+a.name+"}");
	db.collection('User').findOne({'name': a.name},function(err,q){	
		if(err){
		   console.log("uh oh getUser()");
		   console.log(err);
		   return;
		}
		if(q!=null){
		   console.log('account exists...');
   		   var userTemp ={
       		   name: q.name,
       		   schedule: q.schedule,
      		   friends: q.friends,
		   sentInvites : q.sentInvites,
		   recievedInvites: q.recievedInvites,
		}
		console.log(q);
		console.log( JSON.stringify(q));
		res.end(JSON.stringify(q));
		}else{
   		   var userTemp ={
                   name: a.name,
       		   schedule: [],
                   friends: [],
                   sentInvites: [],
                   recievedInvites: [],

		}
		console.log(userTemp.name)
   		db.collection('User').insert(userTemp,function (err,doc){
		console.log("...added");
		res.end(JSON.stringify(userTemp));
		if(err) throw err;
		});
		}		
   });
});




app.post('/getUser2', function (req, res) {
        var a = JSON.parse(JSON.stringify(req.body));
	console.log("1:{"+a.name+"}");
	db.collection('User').findOne({'name': a.name},function(err,q){	
		if(err){
		   console.log("uh oh getUser2()");
		   console.log(err);
		   return;
		}
		if(q!=null){
		   console.log('account exists...');
   		   var userTemp ={
       		   name: q.name,
       		   schedule: q.schedule,
      		   friends: q.friends,
		   sentInvites : q.sentInvites,
		   recievedInvites: q.recievedInvites,
		}
		console.log(q);
		console.log( JSON.stringify(q.schedule));
		res.end(JSON.stringify(q));
		}else{
   		   var userTemp ={
                   name: a.name,
       		   schedule: [],
                   friends: [],
                   sentInvites: [],
                   recievedInvites: [],

		}
		console.log(userTemp.name)
   		db.collection('User').insert(userTemp,function (err,doc){
		console.log("...added");
		res.end(JSON.stringify(userTemp.schedule));
		if(err) throw err;
		});
		}		
   });
});




//-------------------------------------------------------------
app.post('/update_post', function (req, res) {
   console.log("update_post"+JSON.stringify(req.body));
   var a = JSON.parse(JSON.stringify(req.body));
   var userTemp ={
       name: a.name,
       friends: a.friends,
       eventIsSent: a.eventIsSent,
       eventDate: a.eventDate,
       eventNameAndFriends: a.eventNameAndFriends,
       eventIsAccepted: a.eventIsAccepted
}
   db.collection('User').update(
        {name: a.name},
		{$set:
			{
			friends: a.friends,
			schedule: a.schedule,
			sentInvites: a.sentInvites,
			recievedInvites:a.recievedInvites ,
			}
		
		}
)
   db.close();
   res.end("");
})



//---------------------------------------------------

var server = app.listen(8089, function () {
   var host = server.address().address
   var port = server.address().port
	var animal="adsadfa";
	var printText="";

var array = fs.readFileSync('christ.txt').toString().split("\n");
for(i in array) {
    console.log(array[i]);
}

//  for(var i=0;i<20;i++){
//	printText+=printText+"\n";
//}
console.log("Example app listening at http://%s:%s"+printText, host, port)
}) 
