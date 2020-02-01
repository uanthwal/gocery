package com.mobilecomputing.one_sec.activities;




import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


//Singleton class to add requests
public class RequestQueueSingleton {

//    Variables for context and RequestQueue
    private static RequestQueueSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

//    Constructor initialising context and request queue
    private RequestQueueSingleton(Context context) {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }


    //get singleton Instance of the Class
    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (mInstance == null){
            mInstance = new RequestQueueSingleton(context.getApplicationContext());
        }
        //The same instance is returned if it already exists
        return mInstance;
    }

    //get Request Queue
    public RequestQueue getRequestQueue(){
        //Request Queue of JSON Objects
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    //add request to request queue
    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
