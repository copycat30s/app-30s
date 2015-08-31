package com.yahoo.apps.thirty;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TumblrApi;

import java.io.File;
import java.io.IOException;

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
        String api_url = getApiUrl(String.format("blog/%s.tumblr.com/post?type=video", blog));

        RequestParams params = new RequestParams();

        params.put("type", "video");
        try {
            File f = new File(file_path);
            params.put("data", f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getClient().post(api_url, params, handler);
    }

    public void postText (String blog, String text, AsyncHttpResponseHandler handler)
    {
        String api_url = getApiUrl(String.format("blog/%s.tumblr.com/post", blog));

        RequestParams params = new RequestParams();

        params.put("type", "text");
        params.put("body", text);

        getClient().post(api_url, params, handler);
    }

}
