# ScheduleMe

This is a cross-platform app that solves the issue of groups or co-workers not being able to schedule meetings due to time conflicts.

Multiple users will register in the app. The app will link to their google calendars or user can insert the schedule of their work,
school, and social life manually. The app will automatically estimate the time user is available and what their location will be at that
time. Now if one wants to schedule an appointment with another person or between multiple people, the app will look for availability time
between each individuals and match it with others, giving the group a common time where everyone can meet. It will also be synchronized
with google maps, so in case because of distance between two users, one is unable reach other even though both users are free, it will
either cancel that time or offer a reasonable spot to meet in the middle. Another thing it can suggest is the local spots where users can
meet, like restaurants, bars, libraries etc. This app will benefit all the students and aid companies in creating work schedules.

* Update (October 9, 2016)ScheduleMe
This is a cross-platform app that solves the issue of groups or co-workers not being able to schedule meetings due to time conflicts.
Multiple users will register in the app. The app will link to their google calendars or user can insert the schedule of their work, school, and social life manually. The app will automatically estimate the time user is available and what their location will be at that time. Now if one wants to schedule an appointment with another person or between multiple people, the app will look for availability time between each individuals and match it with others, giving the group a common time where everyone can meet. It will also be synchronized with google maps, so in case because of distance between two users, one is unable reach other even though both users are free, it will either cancel that time or offer a reasonable spot to meet in the middle. Another thing it can suggest is the local spots where users can meet, like restaurants, bars, libraries etc. This app will benefit all the students and aid companies in creating work schedules.
Update (October 9, 2016) For a client who wishes to run this code in the early stages of our app, an android simulator is required of API 21 or higher and an event is present, as running on older versions will cause our app to not work properly and crash. Also in order for a client to retrieve information from our server please use the following information;
We are running a node.js server. Through this server we make restful calls with spring on an android device (or simulator).
Update (October 30, 2016) A client who requires has an Android simulator may run our app to sign and sign out successfully to access their personal calendar. The API requirement has not changed from previous versions. We are also using MongoDB to store our user information.
Future Updates Incoming




Steps for setting up this project on your computer


Downloads
Node.js
mongoDB
Mongoose
Android Studio
Once these are downloaded you will also have to use the node package manager to install ‘body-parser’. You will be able to open this project in android studio to work on is, as long as the API is 21 or higher.


When running Android Studio, follow all prompts for installations and you should be able to work from there.

For a client who wishes to run this code in the early stages of our app, an android simulator is required of API 21 or higher and an event is present, as running on older versions will cause our app to not work properly and crash.  Also in order for a client to retrieve information from our server please use the following information; 
  - We are running a node.js server that is running on port 8081 
Through this server we make restful calls with spring on an android device (or simulator).
* Update (October 30, 2016)
A client who requires has an Android simulator may run our app to sign and sign out successfully to access their personal calendar.  The API requirement has not changed from previous versions.  
* Update (December 6, 2016)
A user's calendar data has been successfully retrieved from our server and the user can create or view an event with the ability to invite anyone with the output of possible free times for everyone to meet up.  The app is currently complete and will be deployed as an APK meaning using any android phone that has an API greater than 21 (Android 5.0 or later) a client can download the APK and immediately install the app to their phone.  Our code has been released as an open source entity meaning anyone has the ability to work with our code in order to addon/improve their product.
