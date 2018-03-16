package com.foxhound.rideon.common;

import com.foxhound.rideon.remote.IGoogleAPI;
import com.foxhound.rideon.remote.RetroFitClient;

/**
 * Created by yaser on 3/11/18.
 */

public class Common {
    public static final String baseUrl = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleAPI(){
        return RetroFitClient.getClient(baseUrl).create(IGoogleAPI.class);
    }
}
