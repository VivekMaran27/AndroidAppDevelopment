package com.cmpe277.lab2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import uk.co.deanwild.flowtextview.FlowTextView;

public class PostListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Post> postList = new ArrayList<>();
    private LayoutInflater inflater;
    private static final String TAG = "MainActivity";
    private PostListDb postListDb;
    private  ProgressBar progressBar;

    /**
     * Constructor for the adapter
     * @param activity - Main activity
     */
    public PostListAdapter(Activity activity) {
        Log.i(TAG, "PostListAdapter()++");
        this.activity = activity;
        this. postListDb = new PostListDb(activity,this, postList);
        postListDb.initPostsDB();
        Log.i(TAG, "PostListAdapter()--");
    }

    /**
     * Update posts from database
     */
    public void updatePosts(ProgressBar progressBar)
    {
        Log.i(TAG, "updatePosts()++");
        Log.d(TAG,"Setting progress bar to visible");
        progressBar.setVisibility( View.VISIBLE);
        postListDb.retrievePostsFromDb(progressBar);
        Log.i(TAG, "updatePosts()--");
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount()++");
        Log.i(TAG, "getCount()--");
        return postList.size();
    }

    @Override
    public Object getItem(int location) {
        Log.i(TAG, "getItem()++ Position: " + location);
        Log.i(TAG, "getItem()--");
        return postList.get(location);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "getItemId()++ Position: "+position);
        Log.i(TAG, "getItemId()--");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView()++ Position: "+position);
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.project_post,null);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.name);
        TextView tv = (TextView) convertView.findViewById(R.id.txtStatusMsg);
        TextView tvTs = (TextView) convertView.findViewById(R.id.timestamp);
        ImageView iv = (ImageView)convertView.findViewById(R.id.profilePic);

        Log.d(TAG, "Post: "+ postList.get(position).getPost());
        tv.setText(postList.get(position).getPost());
        tvTs.setText(postList.get(position).getTimeStamp());

        /* Set profile picture if one is present */
        if(postList.get(position).getImge() != null)
        {
            byte[] decodedString = Base64.decode(postList.get(position).getImge()
                    , Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    decodedString, 0, decodedString.length);
            Drawable d = new BitmapDrawable(activity.getResources(), bitmap);
            iv.setImageDrawable(d);
        }
        if(postList.get(position).getName() != null)
        {
            tvTitle.setText(postList.get(position).getName());
        }
        Log.i(TAG, "getView()--");
        return convertView;
    }
}
