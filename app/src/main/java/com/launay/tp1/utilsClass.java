
package com.launay.tp1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDivider;

import java.util.List;

public class utilsClass {
    public static class Journey {
        private String departure;
        private String arrival;
        private String departureTime;
        private String arrivalTime;
        private int nbTransfers;
        private List<Step> steps;

        private boolean expanded;

        public Journey(String departure, String arrival, String departureTime, String arrivalTime, int nbTransfers, List<Step> steps) {
            this.departure = departure;
            this.arrival = arrival;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.nbTransfers = nbTransfers;
            this.steps = steps;
            this.expanded = false;
        }

        public String getDeparture() { return departure; }
        public String getArrival() { return arrival; }
        public String getDepartureTime() { return departureTime; }
        public String getArrivalTime() { return arrivalTime; }
        public int getNbTransfers() { return nbTransfers; }
        public List<Step> getSteps() { return steps; }

        public Boolean isExpanded(){
            return expanded;
        }
        public void setExpanded(boolean expanded) { this.expanded = expanded; }

        @NonNull
        @Override
        public String toString(){
            return "Departure : "+this.departure+"\n"+
                    "Arrival : "+this.arrival+"\n"+
                    "Departure Time : "+this.departureTime+"\n"+
                    "Arrival Time : "+this.arrivalTime+"\n"+
                    "Nb transfer : "+this.nbTransfers+"\n"+
                    "steps : "+steps.toString();
        }
    }
    public static class Step {
        private String departure;
        private String arrival;
        private String departureTime;
        private String arrivalTime;

        private String mode;

        public Step(String departure, String arrival, String departureTime, String arrivalTime,String mode) {
            this.departure = departure;
            this.arrival = arrival;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.mode = mode;
        }

        public String getDeparture() { return departure; }
        public String getArrival() { return arrival; }
        public String getDepartureTime() { return departureTime; }
        public String getArrivalTime() { return arrivalTime; }

        public String toString(){
            return "Departure : "+this.departure+"\n"+
                    "Arrival : "+this.arrival+"\n"+
                    "Departure Time : "+this.departureTime+"\n"+
                    "Arrival Time : "+this.arrivalTime+"\n"+
                    "Mode : "+this.mode+"\n";
        }
    }







    public static class JourneyViewHolder extends RecyclerView.ViewHolder {
        TextView departureTextView, arrivalTextView, departureTimeTextView, arrivalTimeTextView, nbTransfersTextView;

        LinearLayout stepsLinearLayout;

        LinearLayout detailsTrain;
        MaterialDivider detailsDivider;

        ImageButton detailBtn;



        public JourneyViewHolder(@NonNull View itemView) {
            super(itemView);
            detailsTrain = itemView.findViewById(R.id.detailsTrain);
            detailsDivider = itemView.findViewById(R.id.detailsDivider);
            detailBtn = itemView.findViewById(R.id.detailsBtn);
            departureTextView = itemView.findViewById(R.id.departureTextView);
            arrivalTextView = itemView.findViewById(R.id.arrivalTextView);
            departureTimeTextView = itemView.findViewById(R.id.departureTimeTextView);
            arrivalTimeTextView = itemView.findViewById(R.id.arrivalTimeTextView);
            nbTransfersTextView = itemView.findViewById(R.id.nb_steps);
            stepsLinearLayout = itemView.findViewById(R.id.list_steps);
        }

        public void bind(Journey journey, final HomeTrainActivity.JourneyAdapter adapter){


            this.departureTextView.setText(journey.getDeparture());
            this.arrivalTextView.setText(journey.getArrival());
            this.departureTimeTextView.setText(journey.getDepartureTime());
            this.arrivalTimeTextView.setText(journey.getArrivalTime());
            this.nbTransfersTextView.setText("Correspondances: " + journey.getNbTransfers());

            detailsTrain.setVisibility(journey.isExpanded() ? View.VISIBLE : View.GONE);
            detailsDivider.setVisibility(journey.isExpanded() ? View.VISIBLE : View.GONE);

            detailBtn.setOnClickListener(v -> {
                journey.setExpanded(!journey.isExpanded());
                adapter.notifyItemChanged(getAdapterPosition());
            });


        }

    }
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventText;
        ImageButton removeButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventText = itemView.findViewById(R.id.eventTxt);
            removeButton = itemView.findViewById(R.id.removeBtn);
        }
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }


}
