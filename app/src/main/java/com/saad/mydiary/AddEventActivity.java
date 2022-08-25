package com.saad.mydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saad.mydiary.Adapter.RecyclerViewAllEventsAdapter;
import com.saad.mydiary.Adapter.RecyclerviewEventOptionsAdapter;
import com.saad.mydiary.Model.EventType;
import com.saad.mydiary.Model.Events;
import com.saad.mydiary.Model.Venue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.saad.mydiary.MainActivity.URL;

public class AddEventActivity extends AppCompatActivity {

    Button Button_Save;
    EditText EditText_Title, EditText_Date, EditText_Time, EditText_Description;
    Spinner Spinner_Venue, Spinner_Event;
    EditText AddVenue, AddEventOption;
    ArrayList<Venue> venueArrayList = new ArrayList<>();
    List<String> Venue = new ArrayList<>();
    ArrayList<EventType> eventTypeArrayList = new ArrayList<>();
    List<String> Event = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Button_Save = findViewById(R.id.Button_Save);
        EditText_Title = findViewById(R.id.EditText_Title);
        EditText_Date = findViewById(R.id.EditText_Date);
        EditText_Time = findViewById(R.id.EditText_Time);
        EditText_Description = findViewById(R.id.EditText_Description);
        Spinner_Event = findViewById(R.id.Spinner_Event);
        Spinner_Venue = findViewById(R.id.Spinner_Venue);
        AddVenue = findViewById(R.id.EditText_Add_Venue);
        AddEventOption = findViewById(R.id.EditText_Add_Event);

        Intent intent = getIntent();
        int EventID = intent.getIntExtra("EventID", 0);

        String GetAllEventOptionUrl = URL +"getEventOptions";
        String GetAllVenueUrl = URL +"getVenue";
        StringRequest stringRequestVenue = new StringRequest(Request.Method.GET, GetAllVenueUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                        Venue venue = new Venue();
                        venue.setVenue_ID(jsonObject.getInt("Venue_ID"));
                        venue.setVenue_Name(jsonObject.getString("Venue_Name"));
                        venueArrayList.add(venue);
                        Venue.add(venue.getVenue_Name());

                    }
                    Venue.add("Other");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_list_item_1, Venue);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner_Venue.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddEventActivity.this, "Error While Getting Venue Information", Toast.LENGTH_SHORT).show();
            }
        });
        StringRequest stringRequestEventOption = new StringRequest(Request.Method.GET, GetAllEventOptionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                        EventType eventType = new EventType();
                        eventType.setEventType_ID(jsonObject.getInt("EventType_ID"));
                        eventType.setEventType_Name(jsonObject.getString("EventType_Name"));
                        eventTypeArrayList.add(eventType);
                        Event.add(eventType.getEventType_Name());
                    }
                    Event.remove("All");
                    Event.add("Other");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_list_item_1, Event);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner_Event.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddEventActivity.this, "Error While Getting Event Options Information", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequestVenue.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequestEventOption.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequestEventOption);
        requestQueue.add(stringRequestVenue);

        if(EventID != 0)
        {
            String GetEvent = URL + "getEvent?EventID=" + EventID;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, GetEvent, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        EditText_Title.setText(jsonObject.getString("Event_Title"));
                        EditText_Description.setText(jsonObject.getString("Event_Description"));
                        EditText_Date.setText(jsonObject.getString("Event_Date"));
                        EditText_Time.setText(jsonObject.getString("Time"));
                        int SelectedEvent = Event.indexOf(jsonObject.getString("EventTypeName"));
                        int SelectedVenue = Venue.indexOf(jsonObject.getString("VenueName"));
                        Spinner_Event.setSelection(SelectedEvent);
                        Spinner_Venue.setSelection(SelectedVenue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(stringRequest);
        }

        Spinner_Venue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(Spinner_Venue.getSelectedItem() == "Other")
                {
                    AddVenue.setVisibility(View.VISIBLE);
                }
                else
                {
                    AddVenue.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner_Event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(Spinner_Venue.getSelectedItem() == "Other")
                {
                    AddEventOption.setVisibility(View.VISIBLE);
                }
                else
                {
                    AddEventOption.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EditText_Title.length()>0 && EditText_Date.length()>0 && EditText_Description.length()>0 && EditText_Time.length()>0)
                {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        Events events = new Events();
                        events.setEvent_Title(EditText_Title.getText().toString());
                        jsonObject.put("Event_Title", events.getEvent_Title());
                        events.setTime(EditText_Time.getText().toString());
                        jsonObject.put("Time", events.getTime());
                        events.setEvent_Date(EditText_Date.getText().toString());
                        jsonObject.put("Event_Date", events.getEvent_Date());
                        events.setEvent_Description(EditText_Description.getText().toString());
                        jsonObject.put("Event_Description", events.getEvent_Description());
                        events.setFavourite(false);
                        jsonObject.put("Favourite", false);
                        events.setReminder(false);
                        jsonObject.put("Reminder", false);
                        if(Spinner_Event.getSelectedItem() != "Other" && Spinner_Venue.getSelectedItem() != "Other")
                        {
                            events.setEventType_ID(Spinner_Event.getSelectedItemPosition());
                            events.setVenue_ID(Spinner_Event.getSelectedItemPosition());
                            jsonObject.put("Venue_ID", events.getVenue_ID());
                            jsonObject.put("EventType_ID", events.getEventType_ID());
                            if(EventID == 0)
                            {
                                String AddEvent = URL + "setEvent";
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AddEvent, jsonObject, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        finish();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(AddEventActivity.this);
                                requestQueue1.add(jsonObjectRequest);
                            }
                            else
                            {
                                jsonObject.put("Event_ID", EventID);
                                String AddEvent = URL + "setEventUpdate";
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AddEvent, jsonObject, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        finish();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(AddEventActivity.this);
                                requestQueue1.add(jsonObjectRequest);
                            }

                        }
                        else
                        {
                            events.setEventType_ID(0);
                            events.setVenue_ID(0);
                            if(EventID == 0)
                            {
                                String AddEvent = URL + "setEventWithOption?StringVenue="+AddVenue.getText().toString()+"&StringEventOption="+AddEventOption.getText().toString();
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AddEvent, jsonObject, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        finish();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(AddEventActivity.this);
                                requestQueue1.add(jsonObjectRequest);
                            }
                            else
                            {
                                jsonObject.put("Event_ID", EventID);
                                String AddEvent = URL + "setEventWithOptionUpdate?StringVenue="+AddVenue.getText().toString()+"&StringEventOption="+AddEventOption.getText().toString();
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AddEvent, jsonObject, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        finish();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(AddEventActivity.this);
                                requestQueue1.add(jsonObjectRequest);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(AddEventActivity.this, "Please Fill the Required Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}