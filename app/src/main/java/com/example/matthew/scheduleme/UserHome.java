package com.example.matthew.scheduleme;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
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

public class UserHome extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    Button goToCalender;
    Button testRest;
    String result = "";
    EditText textView;
    ArrayList<user> users;
    Button signOut;
    private static final String TAG = "SignOutActivity";
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        textView = (EditText) findViewById(R.id.upcommingdata);
        goToCalender = (Button) findViewById(R.id.userhomeviewschedule);

        goToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    System.out.println("CONTEXTTTTTTT - "+ getPreferences(view.getContext().MODE_PRIVATE).getAll().toString());
                Intent intent = new Intent(view.getContext(), Schedule.class);
                startActivity(intent);


            }
        });
        testRest= (Button) findViewById(R.id.testrest);
        testRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpRequestTask().execute();
            }
        });
        signOut=(Button) findViewById(R.id.signout_button);
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.signout_button:
                        signOut();
                        break;
                }
            }
        });

    }

    private void signOut(){
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

                    }
                }
        );
    }
    @Override
    protected void onStart() {
        super.onStart();
         }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
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
                final String url = "https://rest-service.guides.spring.io/greeting";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                // Greeting greeting = restTemplate.getForObject(url, Greeting.class);

//                return greeting;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Log.e("nope","");

            }
            String url = "http://warmachine.cse.buffalo.edu:8081/listUsers";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String getTheData = restTemplate.getForObject(url,String.class,"Android");
            Collection<user> ob;
            try {
                ob = new ObjectMapper().readValue(getTheData, new TypeReference<Collection<user>>() { });
                Log.e("Value",""+ob.size());
                users=new ArrayList<user>();
                Iterator allPeople = ob.iterator();
                while(allPeople.hasNext()){
                    users.add((user)allPeople.next());
                }
                Log.e("Value",""+users.get(0).getName());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("print","well that didnt work");
            }
            Log.e("something",getTheData);

            // user[] result = restTemplate.getForObject(url, user[].class, "Android");
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            String string = "";
            for(user u:users){
                string+="name: "+u.getName()+" password: "+u.getPassword()+" proff: "+u.getProfession()+" id: "+u.getId();
                string+="\n";
            }
            textView.setText(string);
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG,"onConnectionFailed:"+connectionResult);
    }
}