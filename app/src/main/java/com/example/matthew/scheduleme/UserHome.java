package com.example.matthew.scheduleme;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.client.json.Json;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UserHome extends Activity implements GoogleApiClient.OnConnectionFailedListener{
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
        manageEvents = (Button) findViewById(R.id.userhomemonoageevent);
        goToCalender = (Button) findViewById(R.id.userhomeviewschedule);
        goToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Schedule.class);
                startActivity(intent);

            }
        });
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
        testPost = (Button) findViewById(R.id.takethenamebelowandpost);
        testPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueString = put.getText().toString();

                // for connection class
                thisUser = new user();
                thisUser.setAllFriends(new ArrayList<String>());
                ArrayList<user> testFriends = new ArrayList<user>();
                user one = new user();
                one.setName("Jimmy Johns");
                user two = new user();
                two.setName("Bill Gate");
                user three = new user();
                three.setName("Qiu Kong");
                user four = new user();
                four.setName("Billy Bob");
                user five = new user();
                five.setName("Fried Chicken");
                testFriends.add(one);
                testFriends.add(two);
                testFriends.add(three);
                testFriends.add(four);
                testFriends.add(five);
              //  thisUser.setAllFriends(testFriends);
                viewFriends = (Button) findViewById(R.id.userhomeconnections);
                viewFriends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentJump = new Intent(getApplicationContext(), Connection.class);
                        intentJump.putExtra("testUser", thisUser);
                        startActivity(intentJump);
                    }
                });
            }
        });
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


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
                        string += "name: " + u.getName() + " password: " + u.getPassword() ;
                        string += "\n";
                    }
                    textView.setText(string);
                }
            }

            private class HttpTaskPost extends AsyncTask<Void, Void, Greeting> {
                @Override
                protected Greeting doInBackground(Void... params) {
                    ObjectMapper mapper = new ObjectMapper();
                    user _user = new user();
                    String url = "http://warmachine.cse.buffalo.edu:8081/process_post";
                    try {
                        RestTemplate restTemplate = new RestTemplate();
                        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    } catch (Exception e) {
                        Log.e("MainActivity", e.getMessage(), e);
                        Log.e("nope", "");
                    }
                   _user.setName(valueString);
                    _user.setPassword("thepasswordthatipicked");
                    String jsonInString = "";
                    try {
                        jsonInString = mapper.writeValueAsString(_user);
                        Log.e("json, ", jsonInString);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    RestTemplate restTemplate = new RestTemplate();
                    MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
                    jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    restTemplate.postForObject(url, _user, user.class);
                    return null;
                }

                @Override
                protected void onPostExecute(Greeting greeting) {
                    Log.e("what", "what");
                }
            }
        }