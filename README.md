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
Once these are downloaded you will also have to use the node package manager to install ‘body-parser’. You will be able to open this project in android studio to work on is, as long as the API is 21 or higher.  In order for a user to install the app on their android device please verify that you have API 21 or higher as these are the available APIs that our app is compatible with.  In order to install the APK the preference would be on the android device as the easiest way to complete this task would be to download the APK then run the apk on the phone by tapping on it then the app will automatically install.
In the case that you wish to install the APK to the phone from a computer the following must be done;
- Turn on USB Debugging 
- First, go to Settings > About Phone/Tablet > Go to "Build Number" at the end of the list > Tap build number 7 times
- Go to developer options and check USB debugging then tap OK 
- Second, connect your android usb charger with one end going to the phone and the other end going to the computer
- Third, Create an empty folder on your phone that can be called anything (For simplicity we'll call it ScheduleMe)
- Also, Copy the apk that was downloaded to your device's storage in the folder that you created
- Then, go to Settings -> Applications -> Unknown sources
- Go to the folder that you downloaded the APK to then tap on it you may get a notification about 
- requesting your permission to install this app that can be accepted then the app will be installed
- Finally, our app is installed and for security go back to Settings -> Applications -> Unknown Sources and 
- block unknown sources again
For more references into what exactly a user would need to do for installing an APK on their computer please consult
these links;
http://www.techbout.com/install-apk-files-pc-android-3323/
http://www.device-recovery.com/how-to-connect-android-devices-to-pc-with-usb-mass-storage-mode#note3
http://www.blogsdna.com/26798/how-to-install-apk-files-to-android-from-your-windows-pc.htm

When running Android Studio, follow all prompts for installations and you should be able to work from there.
* Update (September 19, 2016)
The theoretical layout of our app has been drawn out for our objective at the end of this project.  The introductory GUI has
been created and as a result Google API has not been implemented in, our login is a basic login page.
* Update (October 10, 2016)
A shell of our GUI although the essential layout of our objective is shown without any server backend that will be implemented
in later sprints.
* Update (October 30, 2016)
For a client who wishes to run this code in the early stages of our app, an android simulator is required of API 21 or higher and an event is present, as running on older versions will cause our app to not work properly and crash.  Also in order for a client to retrieve information from our server please use the following information; 
  - We are running a node.js server that is running on port 8081 
Through this server we make restful calls with spring on an android device (or simulator).
A client who requires has an Android simulator may run our app to sign and sign out successfully to access their personal calendar.  The API requirement has not changed from previous versions.  
* Future Updates Incoming
