package com.bshealthcare.healthonrent_delivery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by SuperNova on 12-06-2017.
 */

public class TodaysOrderAdapter extends ArrayAdapter<String> {

    private Activity context;
    private String[] oid,skuid,pname,address,type;
    private TextView oidv,skuidv,pnamev,addressv,typev;


    public TodaysOrderAdapter(Activity context, String[] oid, String[] skuid, String[] pname,String[] address,String[] type) {
        super(context, R.layout.todaycard, oid);
        this.context = context;
        this.oid = oid;
        this.skuid = skuid;
        this.pname= pname;
        this.address=address;
        this.type=type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"InflateParams", "ViewHolder"}) View view = inflater.inflate(R.layout.todaycard, null, true);
        //listview implementation
        oidv = (TextView) view.findViewById(R.id.oid);
        skuidv = (TextView) view.findViewById(R.id.skuid);
        pnamev=(TextView) view.findViewById(R.id.pname);
       addressv=(TextView) view.findViewById(R.id.address);
        typev=(TextView) view.findViewById(R.id.type);
        oidv.setText(oid[position]);
        skuidv.setText(skuid[position]);
        pnamev.setText(pname[position]);
        addressv.setText(address[position]);
        typev.setText(type[position]);
        if(type[position].equalsIgnoreCase("pick up")){
            typev.setBackgroundColor(getContext().getResources().getColor(R.color.pickup));
        }else {
            typev.setBackgroundColor(getContext().getResources().getColor(R.color.deliver));

        }
        return view;
    }
}
