# ScheduleMe

This is a cross-platform app that solves the issue of groups or co-workers not being able to schedule meetings due to time conflicts.

Multiple users will register in the app. The app will link to their google calendars or user can insert the schedule of their work,
school, and social life manually. The app will automatically estimate the time user is available and what their location will be at that
time. Now if one wants to schedule an appointment with another person or between multiple people, the app will look for availability time
between each individuals and match it with others, giving the group a common time where everyone can meet. It will also be synchronized
with google maps, so in case because of distance between two users, one is unable reach other even though both users are free, it will
either cancel that time or offer a reasonable spot to meet in the middle. Another thing it can suggest is the local spots where users can
meet, like restaurants, bars, libraries etc. This app will benefit all the students and aid companies in creating work schedules.

* Update (October 9, 2016)
For a client who wishes to run this code in the early stages of our app, an android simulator is required of API 21 or higher and an event is present, as running on older versions will cause our app to not work properly and crash.  Also in order for a client to retrieve information from our server please use the following information; 
  - We are running a node.js server that is running on port 8081 
Through this server we make restful calls with spring on an android device (or simulator).
* Future Updates Incoming
