package com.saad.mydiary.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saad.mydiary.AddEventActivity;
import com.saad.mydiary.Model.ChainEvent;
import com.saad.mydiary.Model.EventType;
import com.saad.mydiary.Model.Venue;
import com.saad.mydiary.R;

import java.util.ArrayList;

import static com.saad.mydiary.MainActivity.URL;

public class RecyclerviewShowChainEvents extends RecyclerView.Adapter <RecyclerviewShowChainEvents.ViewHolder>{
    Context context;
    ArrayList<ChainEvent> chainEventArrayList;
    ArrayList<Venue> venueArrayList;
    ArrayList<EventType> eventTypeArrayList;

    public RecyclerviewShowChainEvents(Context context, ArrayList<ChainEvent> chainEventArrayList, ArrayList<Venue> venueArrayList, ArrayList<EventType> eventTypeArrayList) {
        this.context = context;
        this.chainEventArrayList = chainEventArrayList;
        this.venueArrayList = venueArrayList;
        this.eventTypeArrayList = eventTypeArrayList;
    }

    public RecyclerviewShowChainEvents.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recyclerview_show_chain_events,parent,false);
        return new RecyclerviewShowChainEvents.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewShowChainEvents.ViewHolder holder, int position) {
        holder.Title.setText(chainEventArrayList.get(position).getEvent_Title());
        for(int i=0; i< venueArrayList.size(); i++)
        {
            if(venueArrayList.get(i).getVenue_ID() == chainEventArrayList.get(position).getVenue_ID())
            {
                holder.Venue.setText(venueArrayList.get(i).getVenue_Name());
            }
        }
        for(int i=0; i< eventTypeArrayList.size(); i++)
        {
            if(eventTypeArrayList.get(i).getEventType_ID() == chainEventArrayList.get(position).getEvent_ID())
            {
                holder.Category.setText(eventTypeArrayList.get(i).getEventType_Name());
            }
        }
        holder.Date.setText(chainEventArrayList.get(position).getEvent_Date());
        holder.Time.setText(chainEventArrayList.get(position).getTime());
        if(chainEventArrayList.get(position).getReminder() == true)
        {
            holder.toggleButton.setActivated(true);
        }
        else
        {
            holder.toggleButton.setActivated(false);
        }

        holder.toggleButton.setChecked(chainEventArrayList.get(position).getReminder());
        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String RemainderURL = URL + "setChainToogleButton?EventID=" + chainEventArrayList.get(position).getEvent_ID()
                        +"&ToogleButton=" + b;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, RemainderURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DeleteEvent = URL + "deleteChainEvent?EventID=" + chainEventArrayList.get(position).getEvent_ID();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, DeleteEvent, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        chainEventArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, chainEventArrayList.size());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });
    }


    @Override
    public int getItemCount() {
        return chainEventArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title, Venue, Category, Date, Time;
        ToggleButton toggleButton;
        Button Edit, Delete;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            Title = itemView.findViewById(R.id.TextView_Title);
            Venue = itemView.findViewById(R.id.TextView_Venue);
            Category = itemView.findViewById(R.id.TextView_Category);
            Date = itemView.findViewById(R.id.TextView_Date);
            Time = itemView.findViewById(R.id.TextView_Time);
            toggleButton = itemView.findViewById(R.id.ToogleButton);
            Edit = itemView.findViewById(R.id.Button_Edit);
            Delete = itemView.findViewById(R.id.Button_Delete);
        }
    }
}
