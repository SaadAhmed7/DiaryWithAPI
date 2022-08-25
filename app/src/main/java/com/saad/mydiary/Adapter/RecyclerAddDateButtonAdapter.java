package com.saad.mydiary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saad.mydiary.Interface.OnOptionClick;
import com.saad.mydiary.MainActivity;
import com.saad.mydiary.Model.EventType;
import com.saad.mydiary.Model.Events;
import com.saad.mydiary.Model.Venue;
import com.saad.mydiary.R;

import java.util.ArrayList;

public class RecyclerAddDateButtonAdapter extends RecyclerView.Adapter <RecyclerAddDateButtonAdapter.ViewHolder> {
    Context context;
    ArrayList<Integer> integerArrayList;
    private OnOptionClick mCallback;

    public RecyclerAddDateButtonAdapter(Context context, ArrayList<Integer> integerArrayList, OnOptionClick listener) {
        this.context = context;
        this.integerArrayList = integerArrayList;
        mCallback = listener;
    }

    public RecyclerAddDateButtonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recyclerview_buttons,parent,false);
        return new RecyclerAddDateButtonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAddDateButtonAdapter.ViewHolder holder, int position) {
        holder.Date.setText(String.valueOf(integerArrayList.get(position)));
        holder.Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.DateOptions = integerArrayList.get(position);
                SelectDataAccordingToOptions(integerArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return integerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        Button Date;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            Date = itemView.findViewById(R.id.Button_Date);
        }
    }

    private void SelectDataAccordingToOptions(int eventType_id)
    {
        mCallback.onClick(eventType_id);
    }
}
