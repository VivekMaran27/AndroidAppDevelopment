package com.cmpe277.lab2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.amulyakhare.textdrawable.TextDrawable;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private PostListAdapter listAdapter;
    private static final String TAG = "MainActivity";
    private String user = new String();

    @Override
    /**
     * Entry function for this activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "MainActivity onCreate++");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Get the user name from the login activity */
        Bundle extras = getIntent().getExtras();
        user = extras.getString("User");

        /** Setup the list view with the associated adapter */
        listAdapter = new PostListAdapter(this);
        listView = findViewById(R.id.list);
        listView.setAdapter(listAdapter);

        SetupActionBar();
        Log.i(TAG, "MainActivity onCreate--");
    }

    /**
     * Setup the action bar for this activity
     */
    private void SetupActionBar()
    {
        Log.i(TAG, "SetupActionBar++");
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater =
                (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_toolbar, null);
        ImageView login_icon = (ImageView) v.findViewById(R.id.image_view);
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(""+user.charAt(0), Color.RED);
        login_icon.setImageDrawable(drawable);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        actionBar.setDisplayShowCustomEnabled(true);
        Log.i(TAG, "SetupActionBar--");
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu++");
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        Log.i(TAG, "onCreateOptionsMenu--");
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onCreateOptionsMenu++");
        int id = item.getItemId();
        if (id == R.id.stngs) {
            Intent intent = new Intent("android.intent.action" +
                    ".SETTINGS");
            startActivity(intent);
        }
        Log.i(TAG, "onCreateOptionsMenu--");
        return super.onOptionsItemSelected(item);
    }
}

