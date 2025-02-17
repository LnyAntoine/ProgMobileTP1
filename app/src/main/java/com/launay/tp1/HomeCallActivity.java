
package com.launay.tp1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class HomeCallActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        Intent intent = new Intent(this , ContactActivity.class);

        Intent toCallIntent = new Intent(this,HomeCallActivity.class);
        Intent toTrainIntent = new Intent(this,HomeTrainActivity.class);
        Intent toCalendarIntent = new Intent(this,HomeCalendarActivity.class);


        //setContentView(R.layout.home_call_layout);
        setContentView(R.layout.home_call_layout);


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


        Button btn = findViewById(R.id.validate_button);

        EditText ETName = findViewById(R.id.name_field);
        EditText ETFName = findViewById(R.id.first_name_field);
        EditText ETMultiLine = findViewById(R.id.expertise_field);
        EditText ETTel = findViewById(R.id.phone_field);
        EditText ETDate = findViewById(R.id.bday_field);

        imageView = findViewById(R.id.profile_picture);

        imageView.setOnClickListener(view -> openGallery());



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(res.getString(R.string.dialog_sure));
                builder.setPositiveButton(res.getString(R.string.dialog_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intent.putExtra("snd_name",ETName.getText().toString());
                                intent.putExtra("fst_name",ETFName.getText().toString());
                                intent.putExtra("expertise",ETMultiLine.getText().toString());
                                intent.putExtra("phone_number",ETTel.getText().toString());
                                intent.putExtra("bday_date",ETDate.getText().toString());
                                intent.putExtra("pfp",imageUri.toString());
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton(res.getString(R.string.dialog_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create();
                builder.show();
            }
        });

    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private Activity getActivity(){
        return this;
    }
    public void addListenerOnButton(){

    }
}
