package com.cmpe277.lab2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.asksira.bsimagepicker.BSImagePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener{

    /* Tag for logging */
    private static final String TAG = "MainActivity";

    /* Progress bar to display when activity loads post from db */
    private ProgressBar mProgressBar;

    /* Current clicked post and picker */
    private Post mcurrClickedPost;
    private BSImagePicker mpickerDialog;

    /* View and adapter for the activity */
    private ListView mListView;
    private PostListAdapter mListAdapter;

    private String user = new String();

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View v, int position,
                                long arg3) {
            mpickerDialog = new BSImagePicker.Builder(
                    "com.cmpe277.lab2.FileProvider")
                    .setMaximumDisplayingImages(Integer.MAX_VALUE)
                    .isMultiSelect()
                    .setMinimumMultiSelectCount(3)
                    .setMaximumMultiSelectCount(6)
                    .build();
            mcurrClickedPost = (Post) mListAdapter.getItem(position);
            mpickerDialog.show(getSupportFragmentManager(), "picker");
        }
    };

    @Override
    /**
     * Entry function for this activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "MainActivity onCreate++");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressbar2);

        /** Get the user name from the login activity */
        Bundle extras = getIntent().getExtras();
        user = extras.getString("User");

        /** Setup the list view with the associated adapter */
        mListAdapter = new PostListAdapter(this);
        mListView = findViewById(R.id.list);
        mListView.setAdapter(mListAdapter);
        //mProgressBar.setVisibility( View.GONE);
        mListView.setOnItemClickListener(onItemClickListener);

        SetupActionBar();
        Log.i(TAG, "MainActivity onCreate--");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "MainActivity onResume++");
        mListAdapter.updatePosts(mProgressBar);
        Log.i(TAG, "MainActivity onResume++");

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
        ImageView iv_add_post = (ImageView) v.findViewById(R.id.new_post);

        iv_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action" +
                        ".ADDPOST");
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

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
        else if (id == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity( new Intent(this,LoginActivity.class));
        }
        else
        {
            /* Nothing to do */
        }
        Log.i(TAG, "onCreateOptionsMenu--");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {

    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        /* Convert list to arraylist */
        ArrayList<Uri> uriArrayList = new ArrayList<>(uriList.size());
        uriArrayList.addAll(uriList);

        /* Create bundle  */
        Bundle b = new Bundle();
        b.putSerializable("ImageList",uriArrayList);
        b.putString("ProjectId", mcurrClickedPost.getId());

        Intent intent = new Intent("android.intent.action" +
                ".IMAGEUPLOAD");
        intent.putExtra("ImageBundle",b);
        startActivity(intent);
    }
}

