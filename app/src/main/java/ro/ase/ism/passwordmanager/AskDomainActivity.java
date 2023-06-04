package ro.ase.ism.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import ro.ase.ism.passwordmanager.entities.Account;

public class AskDomainActivity extends AppCompatActivity {

    private EditText etDomain;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_domain);

        etDomain = findViewById(R.id.etDomain);
        btnSearch = findViewById(R.id.btnAction);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String domain = etDomain.getText().toString().trim();

                Account account = ReadActivity.getAccounts(domain);

                // Create an Intent to start DisplayActivity
                Intent intent = new Intent(AskDomainActivity.this, DisplayActivity.class);
                intent.putExtra("account", account);

                // Start DisplayActivity
                startActivity(intent);
            }
        });
    }
}
