package com.gyasistory.project1moviedatabase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by gyasistory on 7/30/15.
 */
public class NetworkConnections {

    /**
     *  This method checks for a network connection.
     * @param context
     * @return Boolean value
     */
    public static Boolean networkcheck(Context context){

        Boolean returnValue = false; // Initial Value

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            returnValue = true;

        }

        return returnValue;

    }
}
