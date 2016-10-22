package com.example.matthew.scheduleme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Status;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    Button button;
    EditText emailAddress;
    EditText passwordBox;
    static AllInfos allInfos;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private boolean mReturningWithResult=false;
    static GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailAddress = (EditText) (findViewById(R.id.Email_Box));
        passwordBox = (EditText) (findViewById(R.id.Email_Password));
        button = (Button) (findViewById(R.id.Log_Button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailAddress.getText().toString().matches("") && !passwordBox.getText().toString().matches("")) {
                    Intent intentJump = new Intent(getApplicationContext(), UserHome.class);
                    Intent intentSentT = new Intent(Login.this, UserHome.class);
                    intentSentT.putExtra("text", emailAddress.getText().toString());
                    startActivity(intentJump);
                    startActivity(intentSentT);
                    allInfos = AllInfos.getInstance(emailAddress.getContext());
                    allInfos.saveData("Username", emailAddress.getText().toString());
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Please Enter Your Google Email Address / Password!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

       findViewById(R.id.signin_button).setOnClickListener(this);

        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        SignInButton signInButton=(SignInButton) findViewById(R.id.signin_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.signin_button:
                signIn();
                //Intent myintent = new Intent (v.getContext(), UserHome.class);
                //startActivityForResult(myintent,0);
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
            startActivityForResult(myintent,0);
        }else{
            findViewById(R.id.signin_button).setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG,"onConnectionFailed:"+connectionResult);
    }

//    public void moveToDifferentLayout(View v){
//        Intent myintent = new Intent (v.getContext(), UserHome.class);
//        startActivityForResult(myintent,0);
//    }

    public static String getGoogleAccount(){
        return acct.getEmail();
    }
}

