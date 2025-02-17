
package com.launay.tp1;
import static java.nio.file.Files.newBufferedReader;
import android.content.res.Resources;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class utils {
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    private static final String API_KEY = "9c9dc7f1-befe-4911-b1e1-6902c4b2fcc9"; // Remplacez par votre clé API
    private static final String BASE_URL_JOURNEYS = "https://api.sncf.com/v1/coverage/sncf/journeys";
    private static final String BASE_URL_PLACES = "https://api.sncf.com/v1/coverage/sncf/places";
    private static final String BASE_URL_AREAS = "https://api.sncf.com/v1/coverage/sncf/stop_areas";

    public static String  getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(calendar.getTime());
    }

    public static String getFurthestDate(String date1, String date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date d1 = dateFormat.parse(date1);
            Date d2 = dateFormat.parse(date2);

            assert d1 != null;
            if (d1.after(d2)) {
                return date1;
            } else {
                return date2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Calendar convertStringToCalendar(String selectedDateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date date = format.parse(selectedDateStr);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getTrainJourneys(String from, String to, String dateTime) {
            OkHttpClient client = new OkHttpClient();

            String url = BASE_URL_JOURNEYS + "?from=" + from + "&to=" + to + "&datetime=" + dateTime +"&count=20";

            Request request = new Request.Builder()
                    .url(url+"&key="+API_KEY)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    return response.body().string(); // Retourner la réponse JSON
                } else {
                    return "Erreur : " + response.code();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Erreur lors de l'appel API";
            }
        }
    public static JsonObject parseResponse(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            Log.e("JourneyParser", "Réponse JSON vide ou nulle !");
            return null;
        }

        try {
            return JsonParser.parseString(jsonResponse).getAsJsonObject();
        } catch (Exception e) {
            Log.e("JourneyParser", "Erreur de parsing JSON", e);
            return null;
        }
    }
    public static String getStopAreaCode(String stationName) {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL_PLACES+ "?q=" + stationName;

        Request request = new Request.Builder()
                .url(url+"&key="+API_KEY)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string(); // Retourner la réponse JSON
            } else {
                return "Erreur : " + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public static String extractHourFromISO8601(String isoDate) {
        try {
            Log.d("Horaire :", isoDate);

            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Important pour éviter les erreurs de fuseau horaire

            Date date = isoFormat.parse(isoDate);

            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return hourFormat.format(date);
        } catch (ParseException e) {
            Log.e("Error horaire", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    public static Date strToDate(String selectedDate){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatter.parse(selectedDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String toIso(Date date){
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC")); // Définir le fuseau horaire à UTC
        return iso8601Format.format(date);
    }
    public static String getFirstPlaceId(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            // Accéder à l'array "places"
            JsonNode placesNode = rootNode.get("places");
            if (placesNode != null && placesNode.isArray() && !placesNode.isEmpty()) {
                // Récupérer l'ID de la première place
                JsonNode firstPlace = placesNode.get(0);
                JsonNode idNode = firstPlace.get("id");

                return idNode != null ? idNode.asText() : "ID non trouvé";
            }
        } catch (IOException e) {
            Log.e("JsonUtils", "Erreur lors de la lecture du JSON", e);
        }
        return "Aucune place trouvée";
    }

    public static List<utilsClass.Journey> parseJourneysList(JsonObject jsonObject) {
        List<utilsClass.Journey> journeyList = new ArrayList<>();

        if (jsonObject == null || !jsonObject.has("journeys")) {
            Log.e("JourneyParser", "Aucun trajet trouvé !");
            return journeyList;
        }


        JsonArray journeys = jsonObject.getAsJsonArray("journeys");
        //On récupère les différents trajets

        for (JsonElement journeyElement : journeys) {
            JsonObject journeyObj = journeyElement.getAsJsonObject();

            String departure =  "Inconnu";
            String arrival = "Inconnu";

            //On récupère les informations sur le trajet

            String departureTime = journeyObj.has("departure_date_time") ? journeyObj.get("departure_date_time").getAsString() : "Inconnu";
            departureTime = extractHourFromISO8601(departureTime);
            String arrivalTime = journeyObj.has("arrival_date_time") ? journeyObj.get("arrival_date_time").getAsString() : "Inconnu";
            arrivalTime = extractHourFromISO8601(arrivalTime);
            int nbTransfers = journeyObj.has("nb_transfers") ? journeyObj.get("nb_transfers").getAsInt() : 0;
            List<utilsClass.Step> steps = new ArrayList<>();

            if (journeyObj.has("sections")) {

                //On récupère les sections du voyage
                JsonArray sections = journeyObj.getAsJsonArray("sections");
                //On récupère le premier départ et la dernière arrivée
                departure = sections.get(0).getAsJsonObject().getAsJsonObject("from").get("name").getAsString();
                arrival = sections.get(sections.size()-1).getAsJsonObject().getAsJsonObject("to").get("name").getAsString();

                for (JsonElement sectionElement : sections) {

                    //Pour chaque section on vérifie qu'elles sont de type "public_transport" pour exclure les "sections" d'attentes et de marche

                    JsonObject section = sectionElement.getAsJsonObject();
                    if (!section.has("mode")){
                        if (section.has("type")){
                            //On récupère toutes les informations utiles de la section et on les ajoute dans steps
                            if (Objects.equals(section.get("type").getAsString(), "public_transport")){

                                String stepDeparture = section.has("from") ? section.getAsJsonObject("from").get("name").getAsString() : "Inconnu";
                                String stepArrival = section.has("to") ? section.getAsJsonObject("to").get("name").getAsString() : "Inconnu";
                                String stepDepartureTime = section.has("departure_date_time") ? section.get("departure_date_time").getAsString() : "Inconnu";
                                stepDepartureTime = extractHourFromISO8601(stepDepartureTime);
                                String stepArrivalTime = section.has("arrival_date_time") ? section.get("arrival_date_time").getAsString() : "Inconnu";
                                stepArrivalTime = extractHourFromISO8601(stepArrivalTime);
                                String mode = section.has("type") ? section.get("type").getAsString() : "Inconnu";
                                steps.add(new utilsClass.Step(stepDeparture, stepArrival, stepDepartureTime, stepArrivalTime,mode));

                            }
                        }
                    }

                }
            }

            journeyList.add(new utilsClass.Journey(departure, arrival, departureTime, arrivalTime, nbTransfers, steps));
        }

        return journeyList;
    }
    }


