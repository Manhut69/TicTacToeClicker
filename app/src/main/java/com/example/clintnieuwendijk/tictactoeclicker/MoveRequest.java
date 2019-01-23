package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MoveRequest implements Response.Listener<JSONObject>, Response.ErrorListener {
    Context context;
    MoveRequest.Callback activity;

    public MoveRequest(Context context) {
        this.context = context;
    }

    public interface Callback {
        void gotMove(JSONObject response) throws JSONException;
        void gotMoveError(String message);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        activity.gotMoveError(error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            activity.gotMove(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postMove(Callback activity, int rowPlayed, int columnPlayed, int gameID) {
        this.activity = activity;

        JSONObject postJSON = new JSONObject();

        if (rowPlayed != -1) {
            try {
                postJSON.put("gameID", gameID);
                postJSON.put("rowPlayed", rowPlayed);
                postJSON.put("columnPlayed", columnPlayed);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        String requestURL = "https://ide50.manhut.c9users.io:8080/ClickTacToeMoveHandler";
        JsonObjectRequest jsonRequest;
        if (rowPlayed != -1) {
            jsonRequest = new JsonObjectRequest(requestURL, postJSON, this, this);
        }
        else {
            jsonRequest = new JsonObjectRequest(requestURL, null, this, this);
        }
        queue.add(jsonRequest);
    }
}
