package com.bshealthcare.healthonrent_delivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.*;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ByteArrayPool;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TodaysDetails extends AppCompatActivity {
String oid,skuid,pname,address,type,name,phone;
    TextView phonev,namev,oidv,addressv,typev;
    RequestQueue requestQueue;
    String fetchedstring,flag;
    Button drop;
    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_details);
        qrScan = new IntentIntegrator(this);
        qrScan.setBeepEnabled(false);
        requestQueue = Volley.newRequestQueue(TodaysDetails.this);
        Intent g=getIntent();
        oid=g.getStringExtra("oid");
        skuid=g.getStringExtra("skuid");
        pname=g.getStringExtra("pname");
        address=g.getStringExtra("address");
        type=g.getStringExtra("type");
        name=g.getStringExtra("name");
        phone=g.getStringExtra("phone");
        drop=(Button) findViewById(R.id.drop);

        oidv=(TextView) findViewById(R.id.oid);
        addressv=(TextView) findViewById(R.id.address);
        typev=(TextView) findViewById(R.id.type);
        namev=(TextView) findViewById(R.id.name);
        phonev=(TextView) findViewById(R.id.phone);
        oidv.setText(oid);
        addressv.setText(address);
        if(type.equalsIgnoreCase("pick up")){
           typev.setBackgroundColor(getResources().getColor(R.color.pickup));
            drop.setText("Pick Up");
            flag="pickup";
        }else {
            typev.setBackgroundColor(getResources().getColor(R.color.deliver));
            drop.setText("Make Payment");
            flag="drop";
        }
        typev.setText(type);
        namev.setText(name);
        phonev.setText(phone);

    }

    public void track(View view){
         Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + address));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }


    }

    public void drop(View view){
        if(drop.getText().toString().equals("Make Payment")) {
            Intent g = new Intent(TodaysDetails.this, Payments.class);
            g.putExtra("oid",oid);
            g.putExtra("skuid",skuid);
            startActivity(g);
            finish();
        }else {
            qrScan.initiateScan();
        }
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json

                    //setting values to textviews
                    fetchedstring=  result.getContents();

                    if(fetchedstring.equals(skuid)) {
                        if(flag.equals("drop")) {
                            JSONObject params = new JSONObject();
                            try {
                                params.put("skuid", fetchedstring);

                            } catch (JSONException e) {

                            }
                            String load_url = "http://139.59.34.12/admin/app/warehouse/drop.php";

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        if (response.getString("data").equals("done")) {
                                            Toast.makeText(TodaysDetails.this, "Order Droped !", Toast.LENGTH_SHORT).show();
                                            Intent g=new Intent(TodaysDetails.this,Signature.class);
                                            g.putExtra("oid",oid);
                                            g.putExtra("skuid",skuid);
                                            startActivity(g);
                                            finish();
                                        } else {
                                            Toast.makeText(TodaysDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(TodaysDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                        if(flag.equals("pickup")){
                            JSONObject params = new JSONObject();
                            try {
                                params.put("skuid", fetchedstring);

                            } catch (JSONException e) {

                            }
                            String load_url = "http://139.59.34.12/admin/app/warehouse/pickup.php";

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        if (response.getString("data").equals("done")) {
                                            Toast.makeText(TodaysDetails.this, "Order Picked !", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(TodaysDetails.this, WareHouseOut.class));
                                            finish();
                                        } else {
                                            Toast.makeText(TodaysDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(TodaysDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                    }else {
                        Toast.makeText(this, "Skuid Not Matched ! ", Toast.LENGTH_SHORT).show();
                    }





                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }


                //  Intent g=new Intent(this,Details.class);
                //     g.putExtra("skuid",fetchedstring);
                //  startActivity(g);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TodaysDetails.this,Today.class));
        finish();
    }
}
