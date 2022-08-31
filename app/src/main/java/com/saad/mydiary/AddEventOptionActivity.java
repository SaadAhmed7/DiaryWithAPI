package com.saad.mydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddEventOptionActivity extends AppCompatActivity {

    Button AddEvent, AddChainEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_option);
        AddEvent = findViewById(R.id.Button_AddEvent);
        AddChainEvent = findViewById(R.id.Button_AddChainEvent);

        AddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventOptionActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

        AddChainEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventOptionActivity.this, AddChainEventActivity.class);
                startActivity(intent);
            }
        });

    }
}