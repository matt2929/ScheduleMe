package com.example.matthew.scheduleme;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Connection extends AppCompatActivity {
    user thisU;
    int friendsCount;
    EditText friendsList, friendEmail;
    String text;
    Button addFriend, save, cancil;
    Dialog friendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        friendsCount = 0;
        Intent intent = getIntent();
        thisU = (user) intent.getSerializableExtra("testUser");
        friendsCount = thisU.getFriends().size();
        friendsList = (EditText) findViewById(R.id.friendsText);
        text = "";
        if (friendsCount > 0) {
            for (int i = 0; i < friendsCount; i++) {
                if (i == friendsCount - 1) {
                    text = text + thisU.getFriends().get(i).get(0);
                } else {
                    text = text + thisU.getFriends().get(i).get(0) + "\n";
                }
            }
            friendsList.setText(text);
        } else {
            friendsList.setText("No friends found.");
        }

        friendInfo = new Dialog(this);
        friendInfo.setTitle("Please Enter the Info for Your New Friend");
        friendInfo.setContentView(R.layout.addfriends_popup);
        addFriend = (Button) findViewById(R.id.addFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendEmail = (EditText) friendInfo.findViewById(R.id.friendEmail);
                save = (Button) friendInfo.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       ArrayList<ArrayList<String>> tempArr = thisU.getFriends();
                        for(int i=0;i<tempArr.size();i++){
                            if(tempArr.get(i).get(0)==friendEmail.getText().toString()){
                                Toast.makeText(getApplicationContext(), "This friend already exists", Toast.LENGTH_SHORT).show();
                            }else{
                                tempArr.add(new ArrayList<String>());
                                tempArr.get(tempArr.size()-1).add(friendEmail.getText().toString());
                            }
                        }
                        String temp = friendsList.getText().toString();
                        temp = temp + "\n" + friendEmail.getText().toString();
                        friendsList.setText(temp);
                        friendEmail.setText("");
                        friendInfo.dismiss();
                    }
                });
                cancil = (Button) friendInfo.findViewById(R.id.cancil);
                cancil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendEmail.setText("");
                        friendInfo.dismiss();
                    }
                });
                friendInfo.show();
            }
        });
    }
}