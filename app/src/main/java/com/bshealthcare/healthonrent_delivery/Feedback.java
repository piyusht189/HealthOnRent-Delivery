package com.bshealthcare.healthonrent_delivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
TextView fb;
    String oid,skuid;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Intent g=getIntent();
        requestQueue = Volley.newRequestQueue(this);
        oid=g.getStringExtra("oid");
        skuid=g.getStringExtra("skuid");
       fb=(TextView) findViewById(R.id.feedback);
        Toast.makeText(this, skuid+","+oid, Toast.LENGTH_SHORT).show();
    }
    public void skip(View view){
        Toast.makeText(this, "Order Successfully Delivered !", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Feedback.this,Today.class));
        finish();
    }
    public void submit(View view){
        if(fb.getText().toString().equals("")){
            Toast.makeText(this, "Kindly Write Feedback First!", Toast.LENGTH_SHORT).show();
        }else {
            JSONObject params = new JSONObject();
            try {
                params.put("skuid", skuid);
                params.put("oid", oid);
                params.put("feedback", fb.getText().toString());

            } catch (JSONException e) {

            }
            String load_url = "http://139.59.34.12/admin/app/warehouse/feedback.php";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getString("data").equals("done")) {
                            Toast.makeText(Feedback.this, "Order Successfully Delivered !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Feedback.this,Today.class));
                            finish();
                        } else {
                            Toast.makeText(Feedback.this, "Server Error ! Try Again or Skip", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Feedback.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        }
    }

}
