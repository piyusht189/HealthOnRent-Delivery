package com.bshealthcare.healthonrent_delivery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }
    public void warehouseout(View view){
        startActivity(new Intent(this,WareHouseOut.class));
    }
    public void today(View view){
        startActivity(new Intent(this,Today.class));
    }
    public void rentcollection(View view){
        startActivity(new Intent(this,RentCollection.class));
    }

    public void logout(View view){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Logout...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want Log Out?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.delete);
        // Setting Icon to Dialog

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                editor.clear();
                editor.commit();
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
}
