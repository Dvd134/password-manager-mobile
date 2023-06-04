package ro.ase.ism.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuggestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        // Find the buttons by their IDs
        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnRead = findViewById(R.id.btnRead);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Set the click listeners for each button
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the CreateActivity
                Intent intent = new Intent(SuggestionsActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the ReadActivity
                Intent intent = new Intent(SuggestionsActivity.this, ReadActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SuggestionsActivity.this, AskIdActivity.class);
                intent.putExtra("action", "update");
                intent.putExtra("buttonAction", "Update");
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SuggestionsActivity.this, AskIdActivity.class);
                intent.putExtra("action", "delete");
                intent.putExtra("buttonAction", "Delete");
                startActivity(intent);
            }
        });
    }
}