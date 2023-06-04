package ro.ase.ism.passwordmanager;

import static ro.ase.ism.passwordmanager.MainActivity.properties;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateActivity extends AppCompatActivity {

    private EditText editUsername, editPassword, editTags, editDomain;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editTags = findViewById(R.id.editTags);
        editDomain = findViewById(R.id.editDomain);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String tags = editTags.getText().toString();
                String domain = editDomain.getText().toString();

                String[] tagsArray = tags.split(",");
                StringBuilder formattedTags = new StringBuilder("[\n        ");
                for(String tag: tagsArray) {

                    formattedTags.append("\"" + tag + "\"");
                    if(Objects.equals(tag, tagsArray[tagsArray.length-1])) {

                        formattedTags.append("\n        ");
                    } else {

                        formattedTags.append(",\n        ");
                    }
                }
                formattedTags.append("]");

                String bearerToken = "Bearer " + properties.getBearerToken();

                String jsonBody = "{\n" +
                        "    \"username\": \"" + username + "\",\n" +
                        "    \"password\": \"" + password + "\",\n" +
                        "    \"tags\":" + formattedTags + ",\n" +
                        "    \"domain\": \"" + domain + "\"\n" +
                        "}";

                // Create the OkHttp client
                OkHttpClient client = new OkHttpClient();

                // Set the media type and request body
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(jsonBody, mediaType);

                // Build the request
                Request request = new Request.Builder()
                        .url("https://password-manager-api.us-e2.cloudhub.io/v1/accounts")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", bearerToken)
                        .post(body)
                        .build();

                try {
                    // Execute the request and get the response
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();

                    // Process the response as needed
                    System.out.println(responseBody);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Return to SuggestionsActivity
                finish();
            }
        });
    }
}