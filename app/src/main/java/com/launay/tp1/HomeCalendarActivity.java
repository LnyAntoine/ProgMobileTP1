
package com.launay.tp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HomeCalendarActivity extends Activity {
    private HashMap<String, List<String>> eventsByDate = new HashMap<>();
    private EventAdapter adapter;
    private List<String> currentEvents = new ArrayList<>();
    private EditText event;
    private RecyclerView listEvent;
    private String selectedDateStr;
    private Calendar selectedDate = null;
    private List<EventDay> eventDays = new ArrayList<>();

    List<Calendar> highlightedDays = new ArrayList<>();
    Calendar specialDay = Calendar.getInstance();
    private CalendarView calendar;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.home_calendar_layout);


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



        ArrayList<Object> daysColor = new ArrayList<>();



        calendar = findViewById(R.id.calendar);

        ImageButton addEvent = findViewById(R.id.addBtn);

        event = findViewById(R.id.eventField);

        listEvent = findViewById(R.id.eventList);

        listEvent.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(this, currentEvents, this::removeEvent);
        listEvent.setAdapter(adapter);



        // Sélection du jour
        calendar.setOnDayClickListener(eventDay -> {

            Calendar selectedCalendar = eventDay.getCalendar();
            selectedDateStr = selectedCalendar.get(Calendar.YEAR) + "-" +
                    (selectedCalendar.get(Calendar.MONTH) + 1) + "-" +
                    selectedCalendar.get(Calendar.DAY_OF_MONTH);
            loadEventsForDate();
            calendar.setHighlightedDays(Collections.singletonList(selectedCalendar));

            // Met à jour les événements affichés
            updateCalendarView(calendar);
        });

        addEvent.setOnClickListener(v -> addEvent());
    }

    private void addEvent() {
        String eventText = event.getText().toString().trim();
        if (!eventText.isEmpty() && selectedDateStr != null) {
            if (!eventsByDate.containsKey(selectedDateStr)) {
                eventsByDate.put(selectedDateStr, new ArrayList<>());
            }
            Objects.requireNonNull(eventsByDate.get(selectedDateStr)).add(eventText);
            event.setText("");
            loadEventsForDate();
        }
        updateCalendarView(calendar);
    }

    private void removeEvent(int position) {
        if (selectedDateStr != null && eventsByDate.containsKey(selectedDateStr)) {
            Objects.requireNonNull(eventsByDate.get(selectedDateStr)).remove(position);
            loadEventsForDate();
        }
        updateCalendarView(calendar);
    }

    private void loadEventsForDate() {
        currentEvents.clear();
        if (eventsByDate.containsKey(selectedDateStr)) {
            currentEvents.addAll(Objects.requireNonNull(eventsByDate.get(selectedDateStr)));
        }
        adapter.notifyDataSetChanged();
    }

    private void updateCalendarView(CalendarView calendar) {
        List<EventDay> updatedEvents = new ArrayList<>();
        eventsByDate.forEach((k,v)->{
            if (!v.isEmpty()){
                updatedEvents.add(new EventDay(Objects.requireNonNull(utils.convertStringToCalendar(k)),R.drawable.image_btn_bg));
            }
        });
        calendar.setEvents(updatedEvents);



    }

    public static class EventAdapter extends RecyclerView.Adapter<utilsClass.EventViewHolder> {
        private List<String> events;
        private Context context;
        private utilsClass.OnRemoveClickListener onRemoveClickListener;

        public EventAdapter(Context context, List<String> events, utilsClass.OnRemoveClickListener listener) {
            this.context = context;
            this.events = events;
            this.onRemoveClickListener = listener;
        }

        @NonNull
        @Override
        public utilsClass.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.calendar_event, parent, false);
            return new utilsClass.EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull utilsClass.EventViewHolder holder, int position) {
            String event = events.get(position);
            holder.eventText.setText(event);

            holder.removeButton.setOnClickListener(v -> {
                if (onRemoveClickListener != null) {
                    onRemoveClickListener.onRemoveClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return events.size();
        }


    }



}
