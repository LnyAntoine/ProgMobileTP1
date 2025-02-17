
package com.launay.tp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params1.weight = 1;

        Button callActivityBtn = new Button(this);
        callActivityBtn.setId(View.generateViewId());
        callActivityBtn.setText(R.string.call_activity_btn);
        callActivityBtn.setLayoutParams(params1);


        Button trainActivityBtn = new Button(this);
        trainActivityBtn.setId(View.generateViewId());
        trainActivityBtn.setText(R.string.train_activity_btn);
        trainActivityBtn.setLayoutParams(params1);


        Button calendarActivityBtn = new Button(this);
        calendarActivityBtn.setId(View.generateViewId());
        calendarActivityBtn.setText(R.string.calendar_activity_btn);
        calendarActivityBtn.setLayoutParams(params1);


        linearLayout.addView(callActivityBtn);
        linearLayout.addView(trainActivityBtn);
        linearLayout.addView(calendarActivityBtn);

        setContentView(linearLayout);

        Intent toCallIntent = new Intent(this,HomeCallActivity.class);
        Intent toTrainIntent = new Intent(this,HomeTrainActivity.class);
        Intent toCalendarIntent = new Intent(this,HomeCalendarActivity.class);

        callActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toCallIntent);
            }
        });

        trainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toTrainIntent);
            }
        });

        calendarActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toCalendarIntent);
            }
        });
    }

}