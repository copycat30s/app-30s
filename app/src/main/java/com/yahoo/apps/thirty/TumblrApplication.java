package com.yahoo.apps.thirty;

import android.content.Context;

public class TumblrApplication extends com.activeandroid.app.Application
{

    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        TumblrApplication.context = this;
    }

    public static TumblrClient getRestClient() {
        return (TumblrClient) TumblrClient.getInstance(TumblrClient.class, TumblrApplication.context);
    }

}
