package com.example.matthew.scheduleme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    static user USERZHU =new user();
    static int values =8089;
    private TextView mStatusTextView;
    private boolean mReturningWithResult=false;
    static GoogleSignInAccount acct;
    String stringThis;
    Button button;
    ArrayList<user> users = new ArrayList<user>();
    user TheUser = new user();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.signin_button).setOnClickListener(this);
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        SignInButton signInButton=(SignInButton) findViewById(R.id.signin_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        button = (Button) findViewById(R.id.sendFakeData);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpSendDatum().execute();
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.signin_button:
                signIn();
                break;
        }
    }
    private void signIn(){
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode!=RESULT_CANCELED) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
        // super.onBackPressed();
    }
    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG,"handleSignInResult:"+result.isSuccess());
        if(result.isSuccess()){
            acct = result.getSignInAccount();

//            System.out.println("ACCTCTCTCTCTCTCTCTC EMAILLLLL"+acct.getEmail());
            // mStatusTextView.setText(getString(R.string.sign_in_fmt,acct.getDisplayName()));
            updateUI(true);
        }else{
            updateUI(false);
        }
    }
    private void updateUI(boolean signedIn){
        if(signedIn){
            findViewById(R.id.signin_button).setVisibility(View.GONE);
            Intent myintent = new Intent (this, UserHome.class);
            user thisU = new user();
            thisU.setName(getGoogleAccount());
            new HttpRequestTask().execute();
            myintent.putExtra("testUser", thisU);
            startActivityForResult(myintent,0);
        }else{
            findViewById(R.id.signin_button).setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG,"onConnectionFailed:"+connectionResult);
    }

    public static String getGoogleAccount(){
        return acct.getEmail();
    }
    public class HttpRequestTask extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            ObjectMapper mapper = new ObjectMapper();
            String url = "http://warmachine.cse.buffalo.edu:"+values+"/getUser";
            username name = new username();
            name.setName(acct.getEmail());
            Log.e("value","{"+name.getName()+"}");
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            String jsonInString = "";
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
            jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            try {
                stringThis = restTemplate.postForObject(url, name, String.class);
            }catch (org.springframework.web.client.HttpClientErrorException e){
                Log.e("no connection","couldnt post :(");
            }catch (Exception e){
                Log.e("exception",e.toString());
            }
            username usertemp=new username();
            try {
                Log.e("json returned",stringThis);
                TheUser=  mapper.readValue(stringThis, user.class);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
            Log.e("converted",TheUser.getName());
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            ObjectMapper objectMapper = new ObjectMapper();

            Log.e("greet","value"+stringThis);
            try {
                USERZHU = objectMapper.readValue(stringThis, user.class);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Cant Convert!!!","no");
            }catch (Exception e){

            }
            //    Log.e("sys", USERZHU.getSentInvites().get(0).getFriendsAccepted().get(0).get(0));
        }
    }

    public class HttpSendDatum extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            ObjectMapper mapper = new ObjectMapper();
            String url = "http://warmachine.cse.buffalo.edu:"+values+"/user_post";
            user tempUser = new user();
            tempUser.setName("matthewstafford29@gmail.com");
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            String jsonInString = "";
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
            jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            user.recievedInvite recievedInvite=new user.recievedInvite();
            user.sentInvite sentInvite=new user.sentInvite();
            recievedInvite.setName("grandma's bday");
            recievedInvite.setDate("8/19/2883");
            recievedInvite.setDuration("45");
            recievedInvite.setWhoInvitedMe("grandma");
            sentInvite.setDuration("69");
            sentInvite.setDate("4/3/2323");
            sentInvite.setName("grandma ;)");
            ArrayList<ArrayList<String>> names= new ArrayList<ArrayList<String>>();
            names.add(new ArrayList<String>());
            names.get(0).add("R.L.Taint");
            names.get(0).add("Y");
            sentInvite.setFriendsAndAccepted(names);
            ArrayList<user.sentInvite> sentInvites=new ArrayList<>();
            sentInvites.add(sentInvite);
            ArrayList<user.recievedInvite> recievedInvites=new ArrayList<>();
            recievedInvites.add(recievedInvite);
            sentInvite.setFriendsAndAccepted(names);
            tempUser.setSentInvites(sentInvites);
            tempUser.setRecievedInvites(recievedInvites);
            stringThis= restTemplate.postForObject(url, tempUser, String.class);
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            Log.e("I ran","I ran");
        }
    }

}