package com.example.matthew.scheduleme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.ArrayList;

public class Connection extends AppCompatActivity {

    user thisU;
    int friendsCount;
    EditText friendsList;
    ArrayList<String> friends;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        friendsCount = 0;
        Intent intent = getIntent();
        thisU = (user) intent.getSerializableExtra("testUser");
        friendsCount = thisU.getAllFriends().size();
        friendsList = (EditText) findViewById(R.id.friendsText);
        text = "";
        if (friendsCount > 0) {
            for (int i=0;i<friendsCount;i++) {
                if (i == friendsCount-1) {
                    text = text + thisU.getAllFriends().get(i);
                }else{
                    text = text + thisU.getAllFriends().get(i) + "\n";
                }
            }
            friendsList.setText(text);
        }else{
            friendsList.setText("No friends found.");
        }
    }
}
