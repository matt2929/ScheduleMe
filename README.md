# ScheduleMe

This is a Android app that solves the issue of groups or co-workers not being able to schedule meetings due to time conflicts.

Multiple users will register in the app. The app will link to their google calendars or user can insert the schedule of their work,
school, and social life manually. The app will automatically calculate the time user and selected friends is available. Now if one wants to schedule an appointment with another person or between multiple people, the app will look for availability time
between each individuals and match it with others, giving the group a common time where everyone can meet. It will also be synchronized
with google maps, so in case because of distance between two users, one is unable reach other even though both users are free, it will
either cancel that time or offer a reasonable spot to meet in the middle.

* Update (October 9, 2016)ScheduleMe
Update (October 9, 2016) For a client who wishes to run this code in the early stages of our app, an android simulator is required of API 21 or higher and an event is present, as running on older versions will cause our app to not work properly and crash. Also in order for a client to retrieve information from our server please use the following information;
We are running a node.js server. Through this server we make restful calls with spring on an android device (or simulator).
Update (October 30, 2016) A client who requires has an Android simulator may run our app to sign and sign out successfully to access their personal calendar. The API requirement has not changed from previous versions. We are also using MongoDB to store our user information.
Future Updates Incoming




Steps for setting up this project on your computer


Downloads
Node.js
Express
mongoDB
Mongoose
Once these are downloaded you will also have to use the node package manager to install ‘body-parser’. You will be able to open this project in android studio to work on is, as long as the API is 21 or higher.


When running Android Studio, follow all prompts for installations and you should be able to work from there.

For a client who wishes to run this code in the early stages of our app, an android simulator is required of API 21 or higher and an event is present, as running on older versions will cause our app to not work properly and crash.  Also in order for a client to retrieve information from our server please use the following information; 
  - We are running a node.js server that is running on port 8081 
Through this server we make restful calls with spring on an android device (or simulator).
* Update (September 19, 2016)
The theoretical layout of our app has been drawn out for our objective at the end of this project.  The introductory GUI has
been created and as a result Google API has not been implemented in, our login is a basic login page.
* Update (October 10, 2016)
A shell of our GUI although the essential layout of our objective is shown without any server backend that will be implemented
in later sprints.
* Update (October 30, 2016)
A client who requires has an Android simulator may run our app to sign and sign out successfully to access their personal calendar.  The API requirement has not changed from previous versions.
* Update (November, 20, 2016)
A client has the ability to verify the fre time with bug fixes that would be required.  Our app is fully functional to prove most of our original goal.  Please follow the "Steps for setting up this project on your computer" in order to run our application without an APK.
* Update (December 6, 2016)
A user's calendar data has been successfully retrieved from our server. Users can create an event with the ability to invite their friends and be presented with a list of possible free times that everyone can meet. The app in its current setting will be deployed as an APK meaning using any android phone that has an API greater than 21 (Android 5.0 or later) can run it. Anyone can download the APK and immediately install the app to their phone.  Our code has been released as an open source entity meaning anyone has the ability to work with our code in order to continue and improve this product.  With the provided APK the app would be downloaded/installed on your android device.  
