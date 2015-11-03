package com.spiceworx.android.feedreaderdemo;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class FeedReaderActivity extends Activity {

    // Following the samples from the book:
    // investigating other Activity main functions
    String tag = "Lifecycle";
    Random rand = new Random();

    // "this" works as a parameter; getBaseContext() DOES NOT.
    FeedReaderContract feed = new FeedReaderContract(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.d(tag, "Before anything else...");

        try {
            Log.d(tag,"Opening the SQLiteDatabase");
            feed.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.d(tag,"Database opened without exception.");

        // do something with the open db.
        // test: enter some values into the database;
        Integer n = rand.nextInt();
        String randstr = n.toString();

        TextView txt = (TextView)findViewById(R.id.introtxt);
        txt.setText(txt.getText() + randstr);

        feed.insertRecord(randstr, "Test entry: " + randstr, "Simple demonstration of how DB works on Android.");

        // display what's inside the Databases
        Cursor c = feed.getAllRecords();
        if (c.moveToFirst())
        {
            do {
                DisplayRecord(c);
            } while (c.moveToNext());
        }

        feed.close();

        Log.d(tag, "Database closed.");
    }


    public void DisplayRecord(Cursor c) {
        Toast.makeText(this,
                "_ID: " + c.getString(0) + "\n" +
                        "ENTRY ID (random number): " + c.getString(1) + "\n" +
                        "TITLE: " + c.getString(2) + "\n" +
                        "SUB TITLE: " + c.getString(3),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(tag, "In the onStart() event");

    }

    @Override
    public void onRestart() {
        super.onRestart();

        Log.d(tag, "In the onRestart() event");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(tag, "In the onResume() event");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(tag, "In the onPause() event");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(tag, "In the onStop() event");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(tag, "In the onDestroy() event");
    }
}
