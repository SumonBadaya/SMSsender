package com.igl.smssender;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Stack;

import static android.R.attr.phoneNumber;

public class MessageSender extends AppCompatActivity {

    String message;
    Stack<String> contacts;


    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";

    PendingIntent pendingIntent;
    PendingIntent deliveredPI;

    SentReceiver sentReceiver;
    DeliverReceiver deliverReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sender);

        // TODO: 9/17/2017  catch the Contacts file -> collect -> all contacts -> perepare for message String -> make messageSender object -> send the messsage of all contackts list- > show progress bar -> instyead of the preogress of sending message

        message = getIntent().getExtras().getString("MESSAGE");

        contacts=MainActivity.contacts;
        MainActivity.clearContacts();

        pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, MessageSender.class), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        sentReceiver =new SentReceiver();
        deliverReceiver=new DeliverReceiver();


        //---when the SMS has been sent---
        registerReceiver( sentReceiver, new IntentFilter(SENT));
        //---when the SMS has been delivered---
        //registerReceiver(deliverReceiver, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();

        while (!contacts.isEmpty()){
            Log.e("sent:: ",contacts.peek());
            contacts.pop();
        }
    }

    @Override
    protected void onResume() {
        //---when the SMS has been sent---
        registerReceiver( sentReceiver, new IntentFilter(SENT));

        //---when the SMS has been delivered---
      //  registerReceiver(deliverReceiver, new IntentFilter(DELIVERED));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(sentReceiver);
        //unregisterReceiver(deliverReceiver);
        super.onDestroy();
    }

//    @Override
//    protected void onPause() {
//        unregisterReceiver(sentReceiver);
//        //unregisterReceiver(deliverReceiver);
//        super.onPause();
//    }
}

