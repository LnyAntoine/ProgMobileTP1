
package com.launay.tp1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CallActivity extends Activity {
    private Bundle extras;
    private Resources res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent toCallIntent = new Intent(this,HomeCallActivity.class);
        Intent toTrainIntent = new Intent(this,HomeTrainActivity.class);
        Intent toCalendarIntent = new Intent(this,HomeCalendarActivity.class);


        setContentView(R.layout.call_layout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.phone) {
                    startActivity(toCallIntent);
                    return true;
                } else if (id == R.id.train) {
                    startActivity(toTrainIntent);
                    return true;
                } else if (id == R.id.calendar) {
                    startActivity(toCalendarIntent);
                    return true;
                }
                return false;
            }
        });

        extras = getIntent().getExtras();
        res = getResources();

        TextView tvPhoneNumber = findViewById(R.id.pnb_tv);
        TextView tvName = findViewById(R.id.tv_fst_name);
        Button callButton = findViewById(R.id.validate_button);
        ImageView iv = findViewById(R.id.profile_picture);

        if (extras != null) {
            tvPhoneNumber.setText(extras.getString("phone_number"));
            tvName.setText(String.format("%s %s", extras.getString("fname"), extras.getString("name")));
            String imageURIString = extras.getString("pfp");
            if (imageURIString!=null){
                Uri imageURI = Uri.parse(imageURIString);
                iv.setImageURI(imageURI);
            }

            callButton.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
            });
        } else {
            tvPhoneNumber.setText(res.getString(R.string.error_phone_number));
            callButton.setEnabled(false);
        }
    }

    private void call() {
        if (extras != null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + extras.getString("phone_number")));
            startActivity(callIntent);
        } else {
            Toast.makeText(this, res.getString(R.string.error_phone_number), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call();
            } else {
                Toast.makeText(this, res.getString(R.string.denied_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
