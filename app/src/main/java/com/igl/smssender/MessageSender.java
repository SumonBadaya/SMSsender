package com.igl.smssender;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MessageSender extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sender);

        // TODO: 9/17/2017  catch the Contacts file -> collect -> all contacts -> perepare for message String -> make messageSender object -> send the messsage of all contackts list- > show progress bar -> instyead of the preogress of sending message



        String message = getIntent().getExtras().getString("MESSAGE");
        String contacts = getIntent().getExtras().getString("CONTACT_LIST");

        Log.e("1::", message);
        Log.e("2::", contacts);

    }

}
