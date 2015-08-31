package com.yahoo.apps.thirty;

import android.content.Context;
import android.os.AsyncTask;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tumblr.jumblr.JumblrClient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TumblrApi;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TumblrClient extends OAuthBaseClient
{

    public static final Class<? extends Api> REST_API_CLASS = TumblrApi.class;

    public static final String REST_URL = "https://api.tumblr.com/v2";

    public static final String REST_CONSUMER_KEY = "P3H5ykAipCbRs6Dhxyo38ECo0ZlpC8mgUreeWIUU7vkKY1q1i0";

    public static final String REST_CONSUMER_SECRET = "wtrgiIR2Y2N4hwM0WO2hn9jlxoa0Cyhf2OhRQO6eRVi8I49IEi";

    public static final String REST_CALLBACK_URL = "oauth://tumblrclient";

    public TumblrClient (Context context)
    {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getInfo (AsyncHttpResponseHandler handler)
    {
        String api_url = getApiUrl("user/info");

        getClient().get(api_url, null, handler);
    }

    public void postVideo (String blog, String file_path, AsyncHttpResponseHandler handler)
    {
        String api_url = getApiUrl(String.format("blog/%s.tumblr.com/post", blog));

        RequestParams params = new RequestParams();

        params.put("type", "video");
        try {
            params.put("data", new File(file_path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //getClient().post(api_url, params, handler);

        VideoUploadTask t = new VideoUploadTask();

        t.execute(blog, file_path);
    }

    public void postText (String blog, String text, AsyncHttpResponseHandler handler)
    {
        String api_url = getApiUrl(String.format("blog/%s.tumblr.com/post", blog));

        RequestParams params = new RequestParams();

        params.put("type", "text");
        params.put("body", text);

        getClient().post(api_url, params, handler);
    }

    class VideoUploadTask extends AsyncTask<String, Void, Boolean>
    {

        protected Boolean doInBackground (final String... args)
        {
            File file = new File(args[1]);

            JumblrClient client = new JumblrClient(REST_CONSUMER_KEY, REST_CONSUMER_SECRET, getClient().getAccessToken().getToken(), getClient().getAccessToken().getSecret());

            Map<String, Object> map = new HashMap<>();

            map.put("type", "video");
            map.put("data", file);

            try {
                client.postCreate(args[0] + ".tumblr.com", map);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

    }

}
