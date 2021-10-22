package com.example.online_photo;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//使用单例模式
public class VolleySingtel {
    private static VolleySingtel INSTANCE;
    private Context context;

    public VolleySingtel(Context context) {
        this.context = context;
    }
    public static synchronized VolleySingtel getInstance(Context context){
        if (INSTANCE==null){
            INSTANCE=new VolleySingtel(context);
        }
        return INSTANCE;
    }
    public RequestQueue requestQueue(){
        return Volley.newRequestQueue(context);
    }

}
