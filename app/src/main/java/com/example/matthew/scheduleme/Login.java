package com.example.matthew.scheduleme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button button;
    EditText emailAddress;
    EditText passwordBox;
    static AllInfos allInfos;

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
    }
}
