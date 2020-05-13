package com.ciber.foodieshoot.applications.detection.Auxiliar.Network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance = null;
    public RequestQueue request_queue;

    private NetworkManager(Context context){
        request_queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkManager getInstance(Context context){
        if(instance == null)
            instance = new NetworkManager(context);
        return instance;
    }

    public static synchronized NetworkManager getInstance(){
        if(instance == null)
            throw new IllegalStateException(NetworkManager.class.getSimpleName() + " is not initialized, call getInstance(...) first");
        return instance;
    }

    public void postRequest(String endpoint, Map<String,? extends Object> params, final RestListener listener){
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                endpoint,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ":", "Url: " + endpoint + " response: " + response.toString());
                        if (response.toString() != null)
                            listener.parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null){
                            Log.d(TAG + ": ","Error response code: " + error.networkResponse.statusCode);
                            listener.handleError(error);
                        }
                    }
                }
        );
        //request.setRetryPolicy(new DefaultRetryPolicy(3000,
          //      DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            //    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request_queue.add(request);
    }
}
