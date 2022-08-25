package com.saad.mydiary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saad.mydiary.Interface.OnItemClick;
import com.saad.mydiary.MainActivity;
import com.saad.mydiary.Model.EventType;
import com.saad.mydiary.R;

import java.util.ArrayList;

public class RecyclerviewEventOptionsAdapter extends RecyclerView.Adapter <RecyclerviewEventOptionsAdapter.ViewHolder> {
    Context context;
    ArrayList<EventType> eventTypeArrayList;
    private OnItemClick mCallback;

    public RecyclerviewEventOptionsAdapter(Context context, ArrayList<EventType> eventTypeArrayList, OnItemClick listener) {
        this.context = context;
        this.eventTypeArrayList = eventTypeArrayList;
        this.mCallback = listener;
    }

    public RecyclerviewEventOptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recyclerview_event_options,parent,false);
        return new RecyclerviewEventOptionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewEventOptionsAdapter.ViewHolder holder, int position) {
        holder.TextView_EventOption.setText(eventTypeArrayList.get(position).getEventType_Name());
        holder.TextView_EventOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.EventOptions = eventTypeArrayList.get(position).getEventType_ID();
                SelectDataAccordingToOptions(eventTypeArrayList.get(position).getEventType_ID());
            }
        });
    }


    @Override
    public int getItemCount() {
        return eventTypeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView TextView_EventOption;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            TextView_EventOption = itemView.findViewById(R.id.TextView_Options);
        }
    }

    private void SelectDataAccordingToOptions(int eventType_id)
    {
        mCallback.onClick(eventType_id);
    }
}
