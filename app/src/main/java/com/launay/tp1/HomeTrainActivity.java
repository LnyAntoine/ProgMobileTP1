
package com.launay.tp1;

import static com.launay.tp1.utils.getFirstPlaceId;
import static com.launay.tp1.utils.parseJourneysList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeTrainActivity extends Activity {
    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.home_train_layout);

        Intent toCallIntent = new Intent(this,HomeCallActivity.class);
        Intent toTrainIntent = new Intent(this,HomeTrainActivity.class);
        Intent toCalendarIntent = new Intent(this,HomeCalendarActivity.class);


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

        EditText departureField = findViewById(R.id.departure_field);
        EditText arrivalField = findViewById(R.id.arrival_field);
        Button switchBtn = findViewById(R.id.switch_btn);
        Button searchBtn = findViewById(R.id.search_btn);
        TextView dateField = findViewById(R.id.date_picker);

        dateField.setText(utils.getTodayDate());

        dateField.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                dateField.setText(utils.getFurthestDate(selectedDate,utils.getTodayDate()));
            }, year, month, day);

            datePickerDialog.show();
        });

        switchBtn.setOnClickListener(v -> {
            String temp = departureField.getText().toString();
            departureField.setText(arrivalField.getText());
            arrivalField.setText(temp);
        });

        searchBtn.setOnClickListener(v->{
            String from = departureField.getText().toString();
            String to = arrivalField.getText().toString();
            Date date = utils.strToDate(dateField.getText().toString());

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String jsonResponse = utils.getStopAreaCode(from);

            String uicCodeDeparture = getFirstPlaceId(jsonResponse);

            String jsonResponse2 = utils.getStopAreaCode(to);
            String uicCodeArrival = getFirstPlaceId(jsonResponse2);

            String strDate = utils.toIso(date);


            String json = utils.getTrainJourneys(uicCodeDeparture,uicCodeArrival,strDate);

            JsonObject jsonObject = utils.parseResponse(json);

            List<utilsClass.Journey> journeyList = parseJourneysList(jsonObject);

            RecyclerView recyclerView = findViewById(R.id.rcv_train);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            JourneyAdapter adapter = new JourneyAdapter(this, journeyList);
            recyclerView.setAdapter(adapter);

        });
    }

    public static class JourneyAdapter extends RecyclerView.Adapter<utilsClass.JourneyViewHolder> {
        private List<utilsClass.Journey> journeys;
        private Context context;

        public JourneyAdapter(Context context, List<utilsClass.Journey> journeys) {
            this.context = context;
            this.journeys = journeys;
        }

        @NonNull
        @Override
        public utilsClass.JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.train_item, parent, false);
            return new utilsClass.JourneyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull utilsClass.JourneyViewHolder holder, int position) {
            utilsClass.Journey journey = journeys.get(position);
            holder.stepsLinearLayout.removeAllViews();
            for (utilsClass.Step step : journey.getSteps()) {

                View stepView = LayoutInflater.from(context).inflate(R.layout.train_step_item,
                        holder.stepsLinearLayout, false);

                TextView departureTextView = stepView.findViewById(R.id.departureText);
                TextView arrivalTextView = stepView.findViewById(R.id.arrivalText);
                TextView departureTimeTextView = stepView.findViewById(R.id.departure_time);
                TextView arrivalTimeTextView = stepView.findViewById(R.id.arrival_time);

                departureTextView.setText(step.getDeparture());
                arrivalTextView.setText(step.getArrival());
                departureTimeTextView.setText(step.getDepartureTime());
                arrivalTimeTextView.setText(step.getArrivalTime());

                holder.stepsLinearLayout.addView(stepView);
            }

            holder.bind(journey,this);
        }

        @Override
        public int getItemCount() {
            return journeys.size();
        }
    }
}
