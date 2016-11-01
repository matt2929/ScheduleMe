package com.example.matthew.scheduleme;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Schedule extends Activity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    private TextView hintText; // for showing hint
    private TextView mOutputText;
    private Button mCallApiButton;
    ProgressDialog mProgress;
    List<String> eventStrings;
    Button sendData;
    user theUser;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    static String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_READONLY,"https://www.googleapis.com/auth/plus.login" };

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   System.out.println("CONTEXTTTTTTT - "+ getPreferences(Context.MODE_PRIVATE).getAll().toString());
        super.onCreate(savedInstanceState);
        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        hintText = new TextView(this);
        hintText.setLayoutParams(tlp);
        hintText.setPadding(16, 16, 16, 16);
        hintText.setVerticalScrollBarEnabled(true);
        hintText.setText(
                "Click the \'" + BUTTON_TEXT +"\' button to test the API.");
        activityLayout.addView(hintText);

        mCallApiButton = new Button(this);
        mCallApiButton.setText(BUTTON_TEXT);
        //button for sending events string to userhome
        sendData=new Button(this);
        sendData.setClickable(false);
        sendData.setVisibility(View.INVISIBLE);

        mCallApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallApiButton.setEnabled(false);
                mOutputText.setText("");
                getResultsFromApi();
                sendData.setClickable(true);
                sendData.setVisibility(View.VISIBLE);
                sendData.setText("Back To User Home");
                mCallApiButton.setEnabled(true);
            }
        });
        activityLayout.addView(mCallApiButton);
        activityLayout.addView(sendData);

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        activityLayout.addView(mOutputText);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");

        //send back the events string to userhome
        Intent i = getIntent();
        theUser = (user) i.getSerializableExtra("testUser");
        sendData.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
             //   new HttpTaskPost().execute();
               Intent intentSendBack = new Intent(Schedule.this, UserHome.class);
               ArrayList<String> temp = new ArrayList<String>();
               eventStrings.remove(0);
               temp.addAll(eventStrings);
               theUser.setEvents(temp);
               intentSendBack.putExtra("testUser", theUser);
               startActivity(intentSendBack);
            }
        });
        setContentView(activityLayout);

        // Initialize credentials and service object.

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
     //   System.out.println("MCREDENTIALS ------ " + mCredential.getSelectedAccountName());
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
          //  System.out.println("Hereeeeeeeeeeeeeeeeeeeeeee"+mCredential.getSelectedAccountName());
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
//            System.out.println("CONTEXTTTTTTT - "+ getPreferences(Context.MODE_PRIVATE).getAll().toString());

            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
               // System.out.println("Account Name"+accountName);
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
        private com.google.api.services.calendar.Calendar mService = null;
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
            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
             eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();
            ArrayList<String> eve = new ArrayList<String>();
            ArrayList<Integer> yearT = new ArrayList<Integer>();
            ArrayList<Integer> monthT = new ArrayList<Integer>();
            ArrayList<Integer> dayT = new ArrayList<Integer>();
            ArrayList<Integer> hourT = new ArrayList<Integer>();
            ArrayList<Integer> minT = new ArrayList<Integer>();
            ArrayList<Integer> totalT = new ArrayList<Integer>();
            ArrayList<Integer> endTime = new ArrayList<Integer>();
            ArrayList<Integer> freeT = new ArrayList<Integer>();
            for(int j=0; j<24; j++){
                totalT.add(j);
            }

            String s="";
            String s1="";
            int i=0;

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                DateTime end = event.getEnd().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                //eventStrings.add(
                //        String.format("%s (%s) (%s)", event.getSummary(), start, end));
                eve.add(event.getSummary());
                s = start.toString();
                String[] parts = s.split("T");
                String x = parts[0];
                String y = parts[1];
                parts = x.split("-");
                String yearr = parts[0];
                //String uu = parts[2];
                int yea = Integer.parseInt(yearr);
                yearT.add(yea);
                String monthS= parts[1];
                int mon = Integer.parseInt(monthS);
                monthT.add(mon);

                String dayS = parts[2];
                int daY = Integer.parseInt(dayS);
                dayT.add(daY);
                String DayT = monthS+"/"+dayS+"/"+yearr;
                parts = y.split(":");
                String hourS = parts[0];
                int hou = Integer.parseInt(hourS);
                hourT.add(hou);
                String miN = parts[1];
                int minu = Integer.parseInt(miN);
                minT.add(minu);
                String startT = hourS+":"+miN;
                s1=end.toString();
                parts = s1.split("T");
                String sh = parts[1];
                parts = sh.split(":");
                String endH = parts[0];
                String endM = parts[1];
                String endT = endH + ":" + endM;
                int endt = Integer.parseInt(endH);
                endTime.add(endt);

                eventStrings.add(
                        String.format("Event: %s Day: %s StartTime: %s EndTime: %s",eve.get(i), DayT, startT, endT));
                i++;
            }
//            int p=0;
//            int q=0;
//            int r=0;
//
//           ArrayList<Integer> timeT = new ArrayList<Integer>();
//           for (int k=0;k<10;k++){
//               timeT.add(hourT.get(k));
//               timeT.add(endTime.get(k));
//           }
//      //      for(int j=0;j<24;j++){
      //          if(r<timeT.size()) {
      //              if (totalT.get(j) == timeT.get(r)) {
      //                  r++;
      //                  while(timeT.get(r)!=totalT.get(j)){
      //                      j++;
      //                  }
      //                  r++;
      //                  j--;

      //              }else{
      //                  freeT.add(totalT.get(j));
      //              }
      //          }

                // freeT.add(totalT.get(j));
      //      }
//            r=0;
//            int j=0;
//            ArrayList<Boolean> freeB = new ArrayList<Boolean>();
//           // while(r<=endTime.size()) {
//                for (j = 0; j < totalT.size(); j++) {
//                    //       for(int k=0;k<endTime.size();k++){
//                    if (totalT.get(j) != hourT.get(r)) {
//                        freeB.add(true);
//                    } else {
//                        freeB.add(false);
//                        r++;
//                    }
//                    //       }
//                }
//            //}
//            eventStrings.add(
//                String.format("String format that will be sent to compare free time per 24 hours where each boolean" +
//                        " represent one hour (calculated from above calendar): %s", freeB));
            return eventStrings;
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
                mOutputText.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                mOutputText.setText(TextUtils.join("\n", output));
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

    /*
    private class HttpTaskPost extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            ObjectMapper mapper = new ObjectMapper();
            user _user = new user();
            String url = "http://warmachine.cse.buffalo.edu:8083/process_post";
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Log.e("nope", "");
            }
            _user.setName(Login.getGoogleAccount().toString());
            _user.setPassword("winner");
            _user.setEvents(eventStrings);

            _user.setAllFriends(new ArrayList<String>());
            ArrayList<String> strings=new ArrayList<String>();
            strings.add("blow baloons");
            strings.add("dog master");
            _user.setEvents(strings);
            String jsonInString = "";
            try {
                jsonInString = mapper.writeValueAsString(_user);
                Log.e("json, ",jsonInString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
            jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.postForObject(url, _user,user.class);
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            Log.e("what","what");
        }
    }
    */
}
