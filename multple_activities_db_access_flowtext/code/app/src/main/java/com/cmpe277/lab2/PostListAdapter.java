package com.cmpe277.lab2;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import android.content.Context;

import uk.co.deanwild.flowtextview.FlowTextView;

public class PostListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Post> postList;
    private LayoutInflater inflater;
    private static final String TAG = "MainActivity";
    private PostListDb postListDb;

    /**
     * Constructor for the adapter
     * @param activity - Main activity
     */
    public PostListAdapter(Activity activity) {
        Log.i(TAG, "PostListAdapter()++");
        this.activity = activity;
        this. postListDb = new PostListDb(activity);
        postListDb.initPostsDB();
        this.postList =postListDb.retrievePostsFromDb();
        Log.i(TAG, "PostListAdapter()--");
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
        FlowTextView flowTextView = (FlowTextView) convertView.findViewById(R.id.ftv);
        flowTextView.setTextColor(Color.parseColor("#FFFFFF"));
        Log.d(TAG, "Post: "+ postList.get(position).getPost());
        flowTextView.setText(postList.get(position).getPost());
        Log.i(TAG, "getView()--");
        return convertView;
    }
}
