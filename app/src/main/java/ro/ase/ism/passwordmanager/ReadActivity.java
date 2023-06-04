package ro.ase.ism.passwordmanager;

import static ro.ase.ism.passwordmanager.MainActivity.properties;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.ase.ism.passwordmanager.entities.Account;

public class ReadActivity extends AppCompatActivity {

    private Button btnGetAllAccounts;
    private Button btnGetAccountsByTags;
    private Button btnGetAccountsByDomain;
    private Button btnGetAccountsByID;

    public static Account getAccounts(String domain) {

        String bearerToken = "Bearer " + properties.getBearerToken();
        String url = domain != null ? "https://password-manager-api.us-e2.cloudhub.io/v1/accounts?return_full_response=true&domain=" + domain : "https://password-manager-api.us-e2.cloudhub.io/v1/accounts?return_full_response=true";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", bearerToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            ObjectMapper objectMapper = new ObjectMapper();
            Account account = objectMapper.readValue(responseBody, Account.class);
            return account;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        // Initialize the buttons
        btnGetAllAccounts = findViewById(R.id.btnGetAllAccounts);
        btnGetAccountsByTags = findViewById(R.id.btnGetAccountsByTags);
        btnGetAccountsByDomain = findViewById(R.id.btnGetAccountsByDomain);
        btnGetAccountsByID = findViewById(R.id.btnGetAccountsByID);

        // Set click listeners for the buttons
        btnGetAllAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account = ReadActivity.getAccounts(null);

                // Create an Intent to start DisplayActivity
                Intent intent = new Intent(ReadActivity.this, DisplayActivity.class);
                intent.putExtra("account", account);

                // Start DisplayActivity
                startActivity(intent);
            }
        });

        btnGetAccountsByTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReadActivity.this, AskTagActivity.class);

                // Start DisplayActivity
                startActivity(intent);
            }
        });

        btnGetAccountsByDomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadActivity.this, AskDomainActivity.class);

                // Start DisplayActivity
                startActivity(intent);
            }
        });

        btnGetAccountsByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadActivity.this, AskIdActivity.class);
                intent.putExtra("action", "read");
                intent.putExtra("buttonAction", "Search");
                startActivity(intent);
            }
        });
    }
}
