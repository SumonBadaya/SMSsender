package com.igl.smssender;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    TextView fileNameView,currentCharacterView;
    EditText message;

    ImageButton chooseButton;
    Button sendButton,clearButton;

    LinearLayout linearLayout;

    private static final int PICK_FILE_RESULT_CODE=911;

    public static Stack<String> contacts;




    private String separator = ";";

    Intent intentChoos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xmlComponentInitialization();

        //checkManufacture();

        contacts=new Stack<>(); // initialize contacts stack object

        clearAllInput();  // clear  message box and file path view


        clearButton.setVisibility(View.GONE);


        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intentChoos = new Intent(Intent.ACTION_GET_CONTENT);
                intentChoos.setType("text/plain");

                startActivityForResult(intentChoos, PICK_FILE_RESULT_CODE);
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentChoos = new Intent(Intent.ACTION_GET_CONTENT);
                intentChoos.setType("text/plain");

                startActivityForResult(intentChoos, PICK_FILE_RESULT_CODE);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentNext=new Intent(MainActivity.this,MessageSender.class);
                intentNext.putExtra("MESSAGE", getMessage());
                //intentNext.putExtra("CONTACT_LIST", contactsBundle);

                clearAllInput();
                startActivity(intentNext);

            }
        });


        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentCharacterView.setText(String.valueOf(getMessage().length()));
                if(message.getText().length()==0){
                    clearButton.setVisibility(View.GONE);
                }else{
                    clearButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not Granted", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_RESULT_CODE) {

            // Get the Uri of the selected file

            try{
                Uri filePath= data.getData();

                File f=new File(filePath.getPath());
                fileNameView.setText(f.getName());

                readSelectedFileDataAndMakeContactsBundle(filePath);
            }catch (NullPointerException e){

                Log.e("filePath","not found");

                Toast.makeText(this, "Select Contacts List!", Toast.LENGTH_SHORT).show();

                startActivityForResult(intentChoos, PICK_FILE_RESULT_CODE);
            }
        }
    }

    private void readSelectedFileDataAndMakeContactsBundle(Uri filePath){
        BufferedReader br;
        String line="";
        try {
            br = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(filePath)));

            while ((line = br.readLine()) != null) {
                contacts.push(line.trim());
            }
            br.close();
            //contactsBundle = contactsBundle.substring(0, contactsBundle.length()-1);

            //Log.e("LAST ", contactsBundle);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void xmlComponentInitialization(){
        fileNameView= (TextView) findViewById(R.id.fileNameView);
        currentCharacterView= (TextView) findViewById(R.id.currentNumberOfCharacter);
        message= (EditText) findViewById(R.id.messageBox);
        chooseButton=(ImageButton)findViewById(R.id.contactButton);
        sendButton= (Button) findViewById(R.id.sendButton);
        clearButton= (Button) findViewById(R.id.clearButton);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayOut);
    }

    public void clear(View v){
        message.setText("");
    }

    private String getMessage(){
        return message.getText().toString();
    }
    private void clearAllInput(){
        message.setText("");
        fileNameView.setText("Browse Your Contacts List . . .");
        clearButton.setVisibility(View.GONE);
    }

    public static Stack<String> getContacts(){
        return MainActivity.contacts;
    }
    public static void clearContacts(){
        MainActivity.contacts=new Stack<>();
    }


    //not used

    private void checkManufacture() {
        if(android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")){
            separator = ",";
        }
    }


    private String getFileName(URI u) {

        try {
            URL urL = u.toURL();
            File tempFile = new File(urL.getFile());
            return tempFile.getName();
        } catch (Exception e) {
            return null;
        }

    }

}
