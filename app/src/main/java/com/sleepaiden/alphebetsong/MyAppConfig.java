package com.sleepaiden.alphebetsong;

import com.sleepaiden.androidcommonutils.config.AppConfig;

/**
 * Created by huiche on 6/16/17.
 */

public class MyAppConfig extends AppConfig{

    private static MyAppConfig instance;
    private MyAppConfig() {}

    public synchronized static AppConfig getAppConfig() {
        if (instance == null) {
            instance = new MyAppConfig();
        }

        return instance;
    }
    @Override
    public String getAppName() {
        return Constants.APP_NAME;
    }

    @Override
    public String getAppVersion() {
        return Constants.APP_VERSION;
    }
}
