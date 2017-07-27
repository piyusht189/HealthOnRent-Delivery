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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Today extends AppCompatActivity {
    ListView lv;

    String[] skuid,oid,pname,type,address,phone,name;
    String fetchedstring,flag;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        lv=(ListView) findViewById(R.id.lv);
        requestQueue = Volley.newRequestQueue(Today.this);

        final JSONObject params = new JSONObject();
        String load_url = "http://139.59.34.12/admin/app/warehouse/fetchmyordersapproved.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr=response.getJSONArray("data");
                    skuid=new String[arr.length()];
                    oid=new  String[arr.length()];
                    pname=new String[arr.length()];
                    address=new String[arr.length()];
                    type=new String[arr.length()];
                    phone=new String[arr.length()];
                    name=new String[arr.length()];
                    for(int i=0;i<arr.length();i++)
                    {
                        JSONObject ob=arr.getJSONObject(i);
                        skuid[i]=ob.getString("skuid");
                        oid[i]=ob.getString("oid");
                        pname[i]=ob.getString("pname");
                        address[i]=ob.getString("address");
                        type[i]=ob.getString("type");
                        name[i]=ob.getString("name");
                        phone[i]=ob.getString("phone");
                    }
                    TodaysOrderAdapter adp=new TodaysOrderAdapter(Today.this,oid,skuid,pname,address,type);
                    lv.setAdapter(adp);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                           TextView skuidtv=(TextView) view.findViewById(R.id.skuid);
                            TextView oidv=(TextView) view.findViewById(R.id.oid);
                            TextView pnamev=(TextView) view.findViewById(R.id.pname);
                            TextView addressv=(TextView) view.findViewById(R.id.address);
                            TextView type=(TextView) view.findViewById(R.id.type);
                            Intent g=new Intent(Today.this,TodaysDetails.class);
                            g.putExtra("skuid",skuidtv.getText().toString());
                            g.putExtra("oid",oidv.getText().toString());
                            g.putExtra("pname",pnamev.getText().toString());
                            g.putExtra("address",addressv.getText().toString());
                            g.putExtra("type",type.getText().toString());
                            g.putExtra("phone",phone[position]);
                            g.putExtra("name",name[position]);
                            startActivity(g);
                            finish();

                        }
                    });

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Today.this, error.toString(), Toast.LENGTH_SHORT).show();
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
