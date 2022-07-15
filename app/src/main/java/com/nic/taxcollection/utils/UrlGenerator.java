package com.nic.taxcollection.utils;


import com.nic.taxcollection.R;
import com.nic.taxcollection.application.NICApplication;

/**
 * Created by Dileep on 2022.
 */
public class UrlGenerator {



    public static String getLoginUrl() {
        return NICApplication.getAppString(R.string.LOGIN_URL);
    }

    public static String getServicesListUrl() {
        return NICApplication.getAppString(R.string.BASE_SERVICES_URL);
    }
    public static String getAppMainServiceUrl() {
        return NICApplication.getAppString(R.string.APP_MAIN_SERVICES_URL);
    }




}
