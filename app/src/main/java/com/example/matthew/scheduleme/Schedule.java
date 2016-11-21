package com.example.matthew.scheduleme;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.people.v1.model.Date;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import android.net.Uri;
public class Schedule extends Activity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    ProgressDialog mProgress;
    QuickEventNext qen;
    List<String> eventStrings;
    Button sendData;
    user theUser;
    //TextView mMeeting;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    static String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_READONLY,"https://www.googleapis.com/auth/plus.login" };

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);
        activityLayout.setBackgroundResource(R.drawable.userhome_image);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setTextColor(Color.rgb(27, 56, 104));
        mOutputText.setTextSize(17);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText("");
        getResultsFromApi();

        sendData=new Button(this);
        sendData.setClickable(true);
        sendData.setVisibility(View.VISIBLE);
        sendData.setText("Back To User Home");
        sendData.setBackgroundColor(Color.rgb(27, 56, 104));
        sendData.setTextColor(Color.WHITE);
        activityLayout.addView(sendData);
        Intent i = getIntent();
        theUser = (user) i.getSerializableExtra("testUser");
        sendData.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               Intent intentSendBack = new Intent(Schedule.this, UserHome.class);
               ArrayList<String> temp = new ArrayList<String>();
               temp.addAll(eventStrings);
               intentSendBack.putExtra("testUser", theUser);
               startActivity(intentSendBack);
            }
        });
        activityLayout.addView(mOutputText);
        setContentView(activityLayout);
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {

            chooseAccount();
        } else if (! isDeviceOnline()) {
            mOutputText.setText("No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {

        if (EasyPermissions.hasPermissions(
                this, android.Manifest.permission.GET_ACCOUNTS)) {

            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);

                mCredential.setSelectedAccountName(Login.getGoogleAccount());
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Schedule.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        public com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {

                if(QuickEventNext.writer==1) {
                    QuickEventNext.writer=0;
                    Event eve = new Event()
                            .setSummary(QuickEventNext.eventN)
                            .setLocation(QuickEventNext.locationN);

                    DateTime datetimeE = new DateTime(QuickEventNext.startdateN + "T" + QuickEventNext.starttimeN + ":00-05:00");
                    EventDateTime startE = new EventDateTime()
                            .setDateTime(datetimeE)
                            .setTimeZone("America/New_York");
                    eve.setStart(startE);

                    DateTime dateTimeEnd = new DateTime(QuickEventNext.enddateN + "T" + QuickEventNext.endtimeN + ":00-05:00");
                    EventDateTime startEnd = new EventDateTime()
                            .setDateTime(dateTimeEnd)
                            .setTimeZone("America/New_York");
                    eve.setEnd(startEnd);

                    String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
                    eve.setRecurrence(Arrays.asList(recurrence));

                    for (int i = 0; i < QuickEventNext.friendsList.size(); i++) {
                      EventAttendee[] attendees = new EventAttendee[]{
                                new EventAttendee().setEmail(QuickEventNext.friendsList.get(i)),
                        };
                        eve.setAttendees(Arrays.asList(attendees));
                    }

                    String calendarId = "primary";

                    eve = mService.events().insert(calendarId, eve).execute();
                    System.out.printf("Event created: %s\n", eve.getHtmlLink());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eve.getHtmlLink()));
                    startActivity(browserIntent);
                }
//            Event evenT = new Event()
//                    .setSummary("Testing")
//                    .setLocation("In lab")
//                    .setDescription("Create event in calendar");
//
//            DateTime startDateTime = new DateTime("2016-11-09T22:00:00-04:00");
//            EventDateTime starT = new EventDateTime()
//                    .setDateTime(startDateTime)
//                    .setTimeZone("America/New_York");
//            evenT.setStart(starT);
//
//            DateTime endDateTime = new DateTime("2016-11-09T23:00:00-04:00");
//            EventDateTime enD = new EventDateTime()
//                    .setDateTime(endDateTime)
//                    .setTimeZone("America/New_York");
//            evenT.setEnd(enD);
//
//            String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
//            evenT.setRecurrence(Arrays.asList(recurrence));

//            EventAttendee[] attendees = new EventAttendee[] {
//                    new EventAttendee().setEmail("lpage@example.com"),
//                    new EventAttendee().setEmail("sbrin@example.com"),
//            }
//            event.setAttendees(Arrays.asList(attendees));
//
//            EventReminder[] reminderOverrides = new EventReminder[] {
//                    new EventReminder().setMethod("email").setMinutes(24 * 60),
//                    new EventReminder().setMethod("popup").setMinutes(10),
//            };
//            Event.Reminders reminders = new Event.Reminders()
//                    .setUseDefault(false)
//                    .setOverrides(Arrays.asList(reminderOverrides));
//            event.setReminders(reminders);

//            String calendarId = "primary";
//            evenT = mService.events().insert(calendarId, evenT).execute();
//            System.out.printf("Event created: %s\n", evenT.getHtmlLink());
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(evenT.getHtmlLink()));
//            startActivity(browserIntent);
            // Link with database to grab user calendar
            // Instance of json class (Step 1)
            DateTime now = new DateTime(System.currentTimeMillis());
            // Put calendar info into an arraylist of each event with
            // each event composing of a start, end, and current time
            // Step 2
            // List the next 10 events from the primary calendar.
            eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                DateTime end = event.getEnd().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                String startFinalValue=parseStartTime(start);
                String endFinalValue=parseStartTime(end);
                compareEvent();
                ComparStarEndTimes(start, end);
                eventStrings.add(
                        String.format("%s \nEvent starting on %s \nEvent ending on %s", event.getSummary(), startFinalValue, endFinalValue));
            }
            // Compare users time frames given
            // Step 3a
            // Shows the first event for the user and displays in console
            // When run in debug mode
            /*
            event.getSummary() - retrieves the name of the event
            start - start time
            end - end time
            eventStrings - ArrayList of events in the following format
            1. Event Name 2. Start time with year/day/month in military time
            3. End time with year/day/month in military time
             */
            return eventStrings;
        }
        /*
        * Written by: Dakota Lester
        * Hardcoded events to check comparisons for two random events
        * If the comparison works for two events it will work for
        * any n events
        * The following method is the testcase with two hardcoded events
        * for the sake of time I did not call the comparison between one
        * event's end time and the next events start time as the method are
        * written and the work is there
        * The start/end would need to be parsed which is already done in the
        * the other methods as the times would be parsed, comparsions would be
        * done and then pushed to the server
         */
        private void compareEvent()
        {
            // Event 1 start
            Event event = new Event().setSummary("Testing").setLocation("A Place").setDescription("Random Event");
            DateTime start = new DateTime("2016-11-14T22:00:00-04:00");
            EventDateTime ev_star = new EventDateTime().setDateTime(start).setTimeZone("America/New York");
            event.setStart(ev_star);
            DateTime end = new DateTime("2016-11-14T23:00:00-04:00");
            EventDateTime ev_end = new EventDateTime().setDateTime(end).setTimeZone("America/New York");
            event.setEnd(ev_end);
            // Event 1 end
            // Event 2 start
            Event event2 = new Event().setSummary("Other testing").setLocation("A special Place").setDescription("Comparisons");
            DateTime start_ev2 = new DateTime("2016-11-14T18:00:00-04:00");
            EventDateTime ev2_star = new EventDateTime().setDateTime(start).setTimeZone("America/New York");
            event2.setStart(ev2_star);
            DateTime end_ev2 = new DateTime("2016-11-14T20:00:00-04:00");
            EventDateTime ev2_end = new EventDateTime().setDateTime(end).setTimeZone("America/New York");
            event2.setEnd(ev2_end);
            // Event 2 end
            // Comparisons based on hardcode data
            // Map to hold the event along with the corresponding start and end time
            Map<Event, List<DateTime>> comparisons = new HashMap<>();
            List<DateTime> times = new ArrayList<>();
            List<DateTime> secondtimes = new ArrayList<>();
            times.add(start);
            times.add(end);
            secondtimes.add(start_ev2);
            secondtimes.add(end_ev2);
            comparisons.put(event, times);
            comparisons.put(event2, secondtimes);
            // Used for output
            String events = comparisons.keySet().toString();
            String firstevtimes = times.toString();
            String secondevtimes = secondtimes.toString();
            Log.e("Events", events);
            Log.e("First Event Times", firstevtimes);
            Log.e("Second Event Times", secondevtimes);
        }
        /*
        * Parse the start time and date with splitting to be used
        * for comparisons
         */
        private String parseStartTime(DateTime start)
        {
            String startstr = start.toString();
            String[] starttime = startstr.split("T");
            // Start Date of the event
            String startdate = starttime[0];
//            // Start Time Of Event
            String startime = starttime[1];
            String[] startY = startime.split(":");
            String startX = startY[0];
            String startZ = startY[1];
            String[] startA = startZ.split(":");
            String startB=startA[0];
            int timeHour = Integer.parseInt(startX);
            String AmPm = "";
            if(timeHour<12){
                AmPm="AM";
            }else{
                timeHour=timeHour-12;
                if(timeHour==0){
                    timeHour = 12;
                }
                AmPm="PM";
            }
            //int startZ= Integer.parseInt(startX);
            String startFinal=startdate + " at "+ timeHour+":"+startB+" "+AmPm;
            return startFinal;
        }
        /*
        * Written by Dakota Lester
        * Parse the end time and date with splitting
        * to be used for comparisons
         */
        private String parseEndTime(DateTime end)
        {
            String endstr = end.toString();
            String[] endTimeForm = endstr.split("T");
            // End Date of the Event
            String enddate = endTimeForm[0];
            // End Time of Event
            String endtime = endTimeForm[1];
            return endtime;
        }
        /*
        * Written by: Dakota Lester
        * Create the time to calculate to find
        * the difference to be used to compare for free time
        * Temporary for one calendar event
        * Easily can be changed to multiple events for testing
        * purposes this is staying to one event as of now
        * Return: ArrayList of type String with start and end times
        * as strings
         */
        private void ComparStarEndTimes(DateTime start, DateTime end) {
            String startComp = parseStartTime(start);
            String endComp = parseEndTime(end);
            String[] startTime = startComp.split(":");
            String[] endTime = endComp.split(":");
            ArrayList<Integer> hourlength = new ArrayList<>();
            for (int i = 0; i < startTime.length; i++)
            {
                try
                {
                    // As the starttime is combined with Hr + Min an if statement
                    // is put in place as the only necessary info is the
                    // Hr and min rather than Time Zone and Seconds
                    // Output: Start and End Time of Each Event[i]
                    if (startTime[i].length() < 3 && startTime[i+1].length() < 3)
                    {
                        String star_Out = startTime[i] + startTime[i + 1];
                        String end_Out = endTime[i] + endTime[i + 1];
                        Log.e("Start", star_Out);
                        Log.e("End", end_Out);
                        // Hour Difference Math Begins
                        hourlength.add(Integer.parseInt(end_Out) - Integer.parseInt(star_Out));
                        if (hourlength.get(i) >= -2300 && hourlength.get(i) <= -1200)
                        {
                            // Marking military time with no negative time
                            // in the case of the beginning time is 2AM and
                            // the starting time was 11PM creating a negative time
                            hourlength.set(i, hourlength.get(i) + 2400);
                        }
                    }
                } catch (IndexOutOfBoundsException e)
                {
                    break;
                }
            }
            Log.e("Difference", hourlength.toString());
        }

        public void writeToCalendar() throws IOException{
            //This evenT just for testing
            //Ask for a meeting name from user
            //mMeeting.setVisibility(View.VISIBLE);

                    Event eve = new Event()
                .setSummary(qen.eventN)
                .setLocation(qen.locationN);

        DateTime datetimeE = new DateTime(qen.startdateN+"T"+qen.starttimeN+":00-04:00");
        EventDateTime startE = new EventDateTime()
                .setDateTime(datetimeE)
                .setTimeZone("America/New_York");
        eve.setStart(startE);

        DateTime dateTimeEnd = new DateTime(qen.enddateN+"T"+qen.endtimeN+":00-04:00");
        EventDateTime startEnd = new EventDateTime()
                .setDateTime(dateTimeEnd)
                .setTimeZone("America/New_York");
        eve.setEnd(startEnd);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
            eve.setRecurrence(Arrays.asList(recurrence));

        for (int i =0; i<qen.friendsList.size();i++){
            EventAttendee[] attendees = new EventAttendee[]{
                    new EventAttendee().setEmail(qen.friendsList.get(i)),
            };
            eve.setAttendees(Arrays.asList(attendees));
        }

        String calendarId = "primary";

        eve = mService.events().insert(calendarId, eve).execute();
        System.out.printf("Event created: %s\n", eve.getHtmlLink());
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eve.getHtmlLink()));
        startActivity(browserIntent);

        }

        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            //mProgress.hide();
            if(mProgress.isIndeterminate())
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                mOutputText.setText("You have no events.");
            } else {
                output.add(0, "Your Current Events: ");
                mOutputText.setText(TextUtils.join("\n\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Schedule.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }
    }

}
