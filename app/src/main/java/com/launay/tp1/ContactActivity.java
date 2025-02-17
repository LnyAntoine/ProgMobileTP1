
package com.launay.tp1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContactActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent toCallIntent = new Intent(this,HomeCallActivity.class);
        Intent toTrainIntent = new Intent(this,HomeTrainActivity.class);
        Intent toCalendarIntent = new Intent(this,HomeCalendarActivity.class);

        setContentView(R.layout.contact_layout);

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

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        Resources res = getResources();

        TextView tvName = findViewById(R.id.name_tv);
        TextView tvFName = findViewById(R.id.fname_tv);
        TextView tvExpertise = findViewById(R.id.expertise_tv);
        TextView tvPNumber = findViewById(R.id.phone_tv);
        TextView tvBday = findViewById(R.id.bday_tv);
        Button btnAccept = findViewById(R.id.validate_button);
        Button btnCancel = findViewById(R.id.cancel_button);
        ImageView iv = findViewById(R.id.profile_picture);

        Intent intentToCall = new Intent(this, CallActivity.class);

        if (extras != null) {
            tvName.setText(String.format("%s", extras.getString("snd_name")));
            tvFName.setText(String.format("%s", extras.getString("fst_name")));
            tvExpertise.setText(String.format("%s", extras.getString("expertise")));
            tvPNumber.setText(String.format("%s", extras.getString("phone_number")));
            tvBday.setText(String.format("%s", extras.getString("bday_date")));
            String imageURIString = extras.getString("pfp");
            if (imageURIString!=null){
                Uri imageURI = Uri.parse(imageURIString);
                iv.setImageURI(imageURI);
            }

            btnAccept.setOnClickListener(v -> {
                intentToCall.putExtra("phone_number", extras.getString("phone_number"));
                intentToCall.putExtra("name",extras.getString("snd_name"));
                intentToCall.putExtra("fname",extras.getString("fst_name"));
                if (imageURIString!=null){
                    intentToCall.putExtra("pfp",imageURIString);
                }
                startActivity(intentToCall);
            });
        }

        btnCancel.setOnClickListener(v -> finish());
    }
}
