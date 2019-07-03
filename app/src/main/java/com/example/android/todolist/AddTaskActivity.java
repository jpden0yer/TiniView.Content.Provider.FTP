/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
//import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.todolist.data.TaskContract;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.util.Half.NaN;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class AddTaskActivity extends AppCompatActivity {

    private String server = "107.180.55.10";
    private int port = 21;
    private String user = "Sign1@tiniliteworld.com";
    private String pass = "Sign1";
    private String fileName = "dat/Sign1.data";

    // Declare a member variable to keep track of a task's selected mPriority
    private int mSpeed;

    private int mLinelength = 24;

int lineCount = 10;
    //060819 JP actually added as last update with out comment made. This stores the ids of the  text boxes
    int[] tbid = new int[10];

    //060919 JP added these arrays. they are populated as intent extras passed to this activity.
    //                they store current sign data
    String lines[] = new String[10];
    boolean lineInDatabase[] = new boolean[10];


    //this filter  never worked and is not currently used
    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {


            if (start == 0 && end == 0 ) {return null;}


            return   source.toString().substring(dend, dend + 1 ).toUpperCase();
             //return dest.toString().toUpperCase();


          //   String sourcestr = source.toString().toUpperCase();




           //  String ret = sourcestr.substring(dend, dend + 1);
            // if ( sourcestr.length() < mLinelength ) {return  ret.toUpperCase();  }


             //return ret;


             /*

             int linestart ;




            int j = 0;

            for (int i = start; i < end; i++, j++) {
                  if (sourcestr.substring(i, i+1).equals("\n"))
                                {j = 0;}

                  else if (j >=  mLinelength)
                  {
                      sourcestr = sourcestr.substring(0, i) + "\n" + sourcestr.substring(i + 1) ;
                      j = 0;
                      end = end + 1;
                  }
            }
            return sourcestr;
*/
        }

    };





    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

//060819 JP actually added as last update with out comment made. This stores the ids of the  text boxes
        tbid[0] = R.id.editTextSignData1;
        tbid[1] = R.id.editTextSignData2;
        tbid[2] = R.id.editTextSignData3;
        tbid[3] = R.id.editTextSignData4;
        tbid[4] = R.id.editTextSignData5;
        tbid[5] = R.id.editTextSignData6;
        tbid[6] = R.id.editTextSignData7;
        tbid[7] = R.id.editTextSignData8;
        tbid[8] = R.id.editTextSignData9;
        tbid[9] = R.id.editTextSignData10;

        Intent intentThatStartedThisActivity = getIntent();

        lines= intentThatStartedThisActivity.getStringArrayExtra("lines") ;

        
        lineInDatabase = intentThatStartedThisActivity.getBooleanArrayExtra("lineInDatabase");


        //060919 JP this copies the data sent in the intent into the textboxes
        for (int i = 0; i < 10 ; i++) {

            ((EditText) findViewById(tbid[i])).setText(lines[i]);

        }

      //060619 JP filter doesnt work
      //  ((EditText) findViewById(R.id.editTextSignData)).setFilters(new InputFilter[] { filter });

        //060319 JP comment this out whe01n replacing radio button with text box
        // Initialize to highest mPriority by default (mPriority = 1)
        //((RadioButton) findViewById(R.id.radButton1)).setChecked(true);


        mSpeed = 1;
    }



    /**
     * onClickAddTask is called when the "ADD" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */



    public void onClickAddTask(View view) {





        //060819 JP actually modified as last update with out comment made
        //uses a loop add 10 lines instead of 1
        for (int j = 0; j < 10; j++) {

            String input = ((EditText) findViewById(tbid[j])).getText().toString();


            /*
            if (input.length() == 0) {
                return;
            }*/




            // Insert new task data via a ContentResolver
            // Create new empty ContentValues object
            ContentValues contentValues = new ContentValues();
            // Put the task description and selected mPriority into the ContentValues
            contentValues.put(TaskContract.TaskEntry.COLUMN_TEXTDATA, input);
            Uri uri;

            if (lineInDatabase[j] ) {
                String whereclause ;

                whereclause = TaskContract.TaskEntry._ID + "="  +  (j + 1);

                //contentValues.put(TaskContract.TaskEntry._ID, "" + j + 1);
                int success = getContentResolver().update(TaskContract.TaskEntry.CONTENT_URI, contentValues, whereclause, null);

            }

            else {
                contentValues.put(TaskContract.TaskEntry._ID, "" + (j + 1));
                contentValues.put(TaskContract.TaskEntry.COLUMN_SPEED, mSpeed);
                // Insert the content values via a ContentResolver
                uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);

                if (uri != null) {
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                }
            }
            // Display the URI that's returned with a Toast
            // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete


        }
            // Finish activity (this returns back to MainActivity)
            finish();




        /**  060319 JP remove function
         * onPrioritySelected is called whenever a priority button is clicked.
         * It changes the value of mPriority based on the selected button.

         public void onPrioritySelected(View view) {
         if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
         mPriority = 1;
         } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
         mPriority = 2;
         } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
         mPriority = 3;
         }
         }
         */
    }


    protected static String padLeft(String s, String pad, int n) {

        String returnValue;
        returnValue =  String.format("%" + n + "s", s);

        if (pad != " ") {
            returnValue = returnValue.replace(" ", pad);
        }

        if (returnValue.length() > n ) {
            returnValue = returnValue.substring(returnValue.length() - n);
        }
        return returnValue;
    }

    private String generateFileContents(){
        int speed = 10;
        //formatText();
        String retval = "";
        for (int i =0; i < lineCount; i++)
        {
            retval = retval +  ((EditText) findViewById(tbid[i])).getText().toString() + "\r\n";
        }

        retval = retval +
                "[trick coding version 2.2]\r\n" +
                "020105010001FF050100" +
                padLeft(Integer.toHexString(lineCount), "0", 2 ) +
                padLeft(Integer.toHexString(speed * 10), "0", 2 ) ;


        retval = retval.toUpperCase();

        return retval;

    };

    public void Send(View view) {


        String fileContent = generateFileContents();

        String [] params = {            //params
                server,                 //0
                "" + port,              //1
                user,                   //2
                pass,                   //3
                fileName,               //4
                fileContent             //5

        };
        new SendDataTask().execute(params);

    }


    public class SendDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String[] doInBackground(String... params) {

            Log.e("FTP-jp0", "begin doInBackground");


            String server = params[0];
            int port =   Integer.parseInt(params[1]);
            String user = params[2];
            String pass = params[3];
            String fileName = params[4];
            String fileContents = params[5];


            FTPClient ftpClient = new FTPClient();


            try {

                ftpClient.connect(server, port);

                ftpClient.login(user, pass);


                ftpClient.enterLocalPassiveMode();

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                OutputStream outputStream = ftpClient.storeFileStream(fileName);

                byte[] bytesIn = fileContents.getBytes() ;

                outputStream.write(bytesIn, 0, fileContents.length());

                outputStream.close();

                boolean completed = ftpClient.completePendingCommand();


                Log.e("FTP-jp0", fileContents);
                //"end doInBackground " + completed  );
            } catch (IOException ex) {



                Log.e("FTP-jp0","catch block after location " );
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();


            }



            return null;

        }

        @Override
        protected void onPostExecute(String[] passedData) {
        }

    }



}
