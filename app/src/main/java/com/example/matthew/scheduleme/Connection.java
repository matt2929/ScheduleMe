package com.example.matthew.scheduleme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class Connection extends AppCompatActivity {
    user thisU;
    int friendsCount;
    EditText friendsList, friendEmail;
    String text;
    Button addFriend, save, cancel;
    Dialog friendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        friendsCount = 0;
        Intent intent = getIntent();
        thisU = Login.USERZHU;
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
                        boolean exist=false;
                        for(int i=0;i<tempArr.size();i++) {
                            if (tempArr.get(i).get(0) == friendEmail.getText().toString()) {
                                exist = true;
                                Toast.makeText(getApplicationContext(), "This friend already exists", Toast.LENGTH_SHORT).show();
                                break;
                            } else {

                            }
                        }
                            if(!exist) {
                                Toast.makeText(getApplicationContext(), "Friend added", Toast.LENGTH_SHORT).show();
                                tempArr.add(new ArrayList<String>());
                                tempArr.get(tempArr.size()-1).add(friendEmail.getText().toString());
                                Log.e("New Friend",""+tempArr.get(tempArr.size()-1).get(0));
                            }


                        Login.USERZHU.setFriends(tempArr);
                        String temp = friendsList.getText().toString();
                        temp = temp + "\n" + friendEmail.getText().toString();
                        friendsList.setText(temp);
                        friendEmail.setText("");
                        friendInfo.dismiss();
                        new HttpSendDatumDank().execute();
                    }
                });

                cancel = (Button) friendInfo.findViewById(R.id.cancil);
                cancel.setOnClickListener(new View.OnClickListener() {
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
    public class HttpSendDatumDank extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            ObjectMapper mapper = new ObjectMapper();
            String url = "http://warmachine.cse.buffalo.edu:"+Login.values+"/update_post";
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
            String stringThis= restTemplate.postForObject(url,Login.USERZHU,String.class);
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            Log.e("I ran","I ran");
        }
    }
}