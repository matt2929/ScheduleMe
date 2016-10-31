package com.example.matthew.scheduleme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import java.util.ArrayList;
import static com.google.android.gms.analytics.internal.zzy.co;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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
        friends = new ArrayList<String>();
        if (friendsCount > 0) {
            for (int i=0;i<friendsCount;i++) {
                if (i == friendsCount-1) {
                    text = text + thisU.getAllFriends().get(i);
                }else{
                    text = text + thisU.getAllFriends().get(i)+"\n";
                }
            }
            friendsList.setText(text);
        }else{
            friendsList.setText("No friends found.");
        }
    }

    public void setUp() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        // Go to the Google API Console, open your application's
        // credentials page, and copy the client ID and client secret.
        // Then paste them into the following code.
        String clientId = "904330615405-hma87k1cohsbqt4b0i3427rbtp9aejl3.apps.googleusercontent.com";
        String clientSecret = "38:E8:B6:C3:05:94:41:29:40:AF:0B:37:3C:F3:41:EC:5D:57:FC:CE";

        // Or your redirect URL for web based applications.
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
        String scope = "https://www.googleapis.com/auth/contacts.readonly";

        // Step 1: Authorize -->
        String authorizationUrl = new GoogleBrowserClientRequestUrl(clientId,
                redirectUrl,
                Arrays.asList(scope))
                .build();

        // Point or redirect your user to the authorizationUrl.
        System.out.println("Go to the following link in your browser:");
        System.out.println(authorizationUrl);

        // Read the authorization code from the standard input stream.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What is the authorization code?");
        String code = in.readLine();
        // End of Step 1 <--

        // Step 2: Exchange -->
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl).execute();
        // End of Step 2 <--

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        People peopleService = new People.Builder(httpTransport, jsonFactory, credential)
                .build();
    }
}
