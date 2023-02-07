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
import com.saad.mydiary.ChainEventActivity;
import com.saad.mydiary.EditChainActivity;
import com.saad.mydiary.Model.ChainEvent;
import com.saad.mydiary.Model.ChainEventFile;
import com.saad.mydiary.Model.EventType;
import com.saad.mydiary.Model.Events;
import com.saad.mydiary.Model.Venue;
import com.saad.mydiary.R;

import java.util.ArrayList;

import static com.saad.mydiary.MainActivity.URL;

public class RecyclerviewAllChainEventsAdapter extends RecyclerView.Adapter <RecyclerviewAllChainEventsAdapter.ViewHolder> {
    Context context;
    ArrayList<ChainEventFile> chainEventArrayList;
    ArrayList<Venue> venueArrayList;
    ArrayList<EventType> eventTypeArrayList;

    public RecyclerviewAllChainEventsAdapter(Context context, ArrayList<ChainEventFile> chainEventArrayList, ArrayList<Venue> venueArrayList, ArrayList<EventType> eventTypeArrayList) {
        this.context = context;
        this.chainEventArrayList = chainEventArrayList;
        this.venueArrayList = venueArrayList;
        this.eventTypeArrayList = eventTypeArrayList;
    }

    public RecyclerviewAllChainEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recyclerview_all_chainevents,parent,false);
        return new RecyclerviewAllChainEventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAllChainEventsAdapter.ViewHolder holder, int position) {
        holder.Title.setText(chainEventArrayList.get(position).getTitlel());
        holder.File.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChainEventActivity.class);
                intent.putExtra("ChainID", chainEventArrayList.get(position).getChainID());
                context.startActivity(intent);
            }
        });
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DeleteFullChainURL = URL + "deleteFullChain?ChainID="+chainEventArrayList.get(position).getChainID();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, DeleteFullChainURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        chainEventArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, chainEventArrayList .size());
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
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChainEventActivity.class);
                intent.putExtra("ChainID", chainEventArrayList.get(position).getChainID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chainEventArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title;
        Button File;
        Button Edit, Delete;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            Title = itemView.findViewById(R.id.TextView_Event_Name);
            File = itemView.findViewById(R.id.Button_File);
            Edit = itemView.findViewById(R.id.Button_Edit);
            Delete = itemView.findViewById(R.id.Button_Delete);
        }
    }
}
