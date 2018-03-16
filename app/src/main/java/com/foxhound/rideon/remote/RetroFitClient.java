package com.foxhound.rideon.remote;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by yaser on 3/11/18.
 */

public class RetroFitClient {

    private static Retrofit retrofit = null;


    public static Retrofit getClient(String baseUrl)
    {
        if(retrofit == null){

              retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
