package com.yahoo.apps.thirty;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class HomeActivity extends ActionBarActivity {

    String default_blog_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TumblrApplication.getRestClient().getInfo(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess (int statusCode, Header[] headers, JSONObject response)
            {
                try {
                    default_blog_name = response.getJSONObject("response").getJSONObject("user").getJSONArray("blogs").getJSONObject(0).getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (default_blog_name != null) {
                    TextView text_blog_name = (TextView) HomeActivity.this.findViewById(R.id.tvBlogName);
                    text_blog_name.setText(default_blog_name);
                }
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void onPost (MenuItem item)
    {
        startRecordingVideo();
    }

    public void onLogout (MenuItem item)
    {
        TumblrApplication.getRestClient().clearAccessToken();

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private static final int VIDEO_CAPTURE = 30;

    Uri videoUri;

    public void startRecordingVideo() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File mediaFile = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
            videoUri = Uri.fromFile(mediaFile);
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(intent, VIDEO_CAPTURE);
        } else {
            Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();

                String video_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4";

                TumblrApplication.getRestClient().postVideo(default_blog_name, video_path, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("XXX", String.valueOf(statusCode));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i("XXX", errorResponse.toString());
                    }
                });

                //playbackRecordedVideo();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",  Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",  Toast.LENGTH_LONG).show();
            }
        }
    }

    public void playbackRecordedVideo() {
//        VideoView mVideoView = (VideoView) findViewById(R.id.video_view);
//        mVideoView.setVideoURI(videoUri);
//        mVideoView.setMediaController(new MediaController(this));
//        mVideoView.requestFocus();
//        mVideoView.start();
    }

}
