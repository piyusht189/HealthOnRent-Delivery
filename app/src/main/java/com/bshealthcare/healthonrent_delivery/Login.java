package com.bshealthcare.healthonrent_delivery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
TextView pass,ph;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pass=(TextView) findViewById(R.id.password);
        ph=(TextView) findViewById(R.id.phone);
        requestQueue = Volley.newRequestQueue(this);
        sharedpreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        if(sharedpreferences.contains("did")){

            startActivity(new Intent(this,MainActivity.class));
            finish();

        }
    }

    public void login(View view){
        if(!ph.getText().toString().equals("") && !pass.getText().toString().equals("")) {
            JSONObject params = new JSONObject();
            try {
                params.put("phone", ph.getText().toString());
                params.put("password", pass.getText().toString());

            } catch (JSONException e) {

            }
            String load_url = "http://139.59.34.12/admin/app/warehouse/deliverylogin.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getString("data").equals("done")) {
                            editor.putString("did",response.getString("did"));
                            editor.commit();
                            startActivity(new Intent(Login.this,MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Invalid Phone/Password !", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        }else {
            Toast.makeText(this, "Phone/Password Empty!", Toast.LENGTH_SHORT).show();
        }



    }
}
