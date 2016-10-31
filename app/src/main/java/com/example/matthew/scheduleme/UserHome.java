package com.example.matthew.scheduleme;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UserHome extends Activity {
    Button goToCalender;
    Button testRest;
    String valueString="username";
    String result = "";
    EditText textView;
    ArrayList<user> users;
    Button signOut;
    Button viewFriends;
    Button manageEvents;
    private static final String TAG = "SignOutActivity";
    private GoogleApiClient mGoogleApiClient;
    user thisUser;
    Button testPost;
    EditText put;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        textView = (EditText) findViewById(R.id.upcommingdata);

        // for connection class
        Intent intent = getIntent();
        thisUser = (user) intent.getSerializableExtra("testUser");
        thisUser.setAllFriends(new ArrayList<String>());
        ArrayList<String> testFriends = new ArrayList<String>();
        String one = "Jimmy Johns";
        String two = "OMG MEH";
        String three = "Monkey D Luffy";
        String four = "Steve Jobs";
        String five = "Bill Gate";
        testFriends.add(one);
        testFriends.add(two);
        testFriends.add(three);
        testFriends.add(four);
        testFriends.add(five);
        thisUser.setAllFriends(testFriends);

        goToCalender = (Button) findViewById(R.id.userhomeviewschedule);
        goToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Schedule.class);
                intent.putExtra("testUser", thisUser);
                startActivity(intent);
            }
        });

        viewFriends = (Button) findViewById(R.id.userhomeconnections);
        viewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentJump = new Intent(getApplicationContext(), Connection.class);
                intentJump.putExtra("testUser", thisUser);
                startActivity(intentJump);
            }
        });

        manageEvents = (Button) findViewById(R.id.userhomemonoageevent);
        manageEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeAMeeting.class);
                startActivity(intent);
            }
        });

        testRest = (Button) findViewById(R.id.testrest);
        testRest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("butt1", "press");
                        new HttpRequestTask().execute();
                    }
                });

        put = (EditText) findViewById(R.id.postname);
        put.setText(thisUser.getName());
        testPost = (Button) findViewById(R.id.takethenamebelowandpost);
        testPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpTaskPost().execute();
             //   valueString = put.getText().toString();
            }

            private void signOut() {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                //revokeAccess();
//                        SharedPreferences prefs = getSharedPreferences(getApplicationContext().toString(),Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = prefs.edit();
                                //         System.out.println("HERERERERERERERERERERERERERERER"+getPreferences(getApplicationContext().MODE_PRIVATE).getAll().toString());
//                        editor.remove(AccountManager.KEY_ACCOUNT_NAME);
//                        editor.commit();
                                String accPref = getPreferences(getApplicationContext().MODE_PRIVATE).getString(Schedule.PREF_ACCOUNT_NAME, null);
                                //         System.out.println("ACCPREF:::" + accPref);
                                accPref = "";
                                Intent intenT = new Intent(getApplicationContext(), Login.class);
                                startActivity(intenT);
                                Log.e("butt2", "press");
                                new HttpTaskPost().execute();
                            }
                        });
                put = (EditText) findViewById(R.id.postname);
                put.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        Log.e("butt1", "press");

                        return true;
                    }
                });
            }

            protected void onStart() {
                UserHome.super.onStart();
            }
        });
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    private class HttpRequestTask extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                // Greeting greeting = restTemplate.getForObject(url, Greeting.class);
//                return greeting;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Log.e("nope", "");

            }
            String url = "http://warmachine.cse.buffalo.edu:8081/listUsers";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String getTheData = restTemplate.getForObject(url, String.class, "Android");
            Collection<user> ob;
            try {
                ob = new ObjectMapper().readValue(getTheData, new TypeReference<Collection<user>>() {
                });
                Log.e("Value", "" + ob.size());
                users = new ArrayList<user>();
                Iterator allPeople = ob.iterator();
                while (allPeople.hasNext()) {
                    users.add((user) allPeople.next());
                }
                Log.e("Value", "" + users.get(0).getName());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("print", "well that didnt work");
            }
            Log.e("something", getTheData);

            // user[] result = restTemplate.getForObject(url, user[].class, "Android");
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            String string = "";
            for (user u : users) {
                string += "name: " + u.getName() + " password: " + u.getPassword();
                string += "\n";
            }
            textView.setText(string);
        }
    }

    private class HttpTaskPost extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            ObjectMapper mapper = new ObjectMapper();
        //    user _user = new user();
            String url = "http://warmachine.cse.buffalo.edu:8081/process_post";
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Log.e("nope", "");
            }
            thisUser.setPassword("*************");
            String jsonInString = "";
            try {
                jsonInString = mapper.writeValueAsString(thisUser);
                Log.e("json, ", jsonInString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
            jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.postForObject(url, thisUser, user.class);
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            Log.e("what", "what");
        }
    }
}