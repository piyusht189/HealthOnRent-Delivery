package com.bshealthcare.healthonrent_delivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WareHouseOut extends AppCompatActivity {
    ListView lv;

    String[] skuid,oid,pname;
    String fetchedstring,flag;
    RequestQueue requestQueue;
    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house_out);
        lv=(ListView) findViewById(R.id.lv);
        requestQueue = Volley.newRequestQueue(WareHouseOut.this);
        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.setBeepEnabled(false);
        final JSONObject params = new JSONObject();
        String load_url = "http://139.59.34.12/admin/app/warehouse/fetchmyorders.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr=response.getJSONArray("data");
                    skuid=new String[arr.length()];
                    oid=new  String[arr.length()];
                    pname=new String[arr.length()];
                    for(int i=0;i<arr.length();i++)
                    {
                        JSONObject ob=arr.getJSONObject(i);
                        skuid[i]=ob.getString("skuid");
                        oid[i]=ob.getString("oid");
                        pname[i]=ob.getString("pname");
                    }
                    MyOrdersListAdapter adp=new MyOrdersListAdapter(WareHouseOut.this,oid,skuid,pname);
                    lv.setAdapter(adp);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            TextView skuidtv=(TextView) view.findViewById(R.id.skuid);
                            flag=skuidtv.getText().toString();
                            qrScan.initiateScan();

                        }
                    });

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WareHouseOut.this, error.toString(), Toast.LENGTH_SHORT).show();
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

                    if(fetchedstring.equals(flag)) {
                        JSONObject params = new JSONObject();
                        try {
                            params.put("skuid", fetchedstring);

                        } catch (JSONException e) {

                        }
                        String load_url = "http://139.59.34.12/admin/app/warehouse/outfordelivery.php";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.getString("data").equals("done")) {
                                        Toast.makeText(WareHouseOut.this, "Order Picked !", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(WareHouseOut.this,WareHouseOut.class));
                                        finish();
                                    } else {
                                        Toast.makeText(WareHouseOut.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(WareHouseOut.this, error.toString(), Toast.LENGTH_SHORT).show();
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

}
