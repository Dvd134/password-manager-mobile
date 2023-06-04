package ro.ase.ism.passwordmanager;

import static ro.ase.ism.passwordmanager.MainActivity.properties;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ro.ase.ism.passwordmanager.crypto.AESUtils;
import ro.ase.ism.passwordmanager.entities.Account;

public class UpdateActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextTags;
    private EditText editTextDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Retrieve the account object from the intent
        Account account = getIntent().getParcelableExtra("account");
        AESUtils aesUtils = new AESUtils();

        // Initialize the views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextTags = findViewById(R.id.editTextTags);
        editTextDomain = findViewById(R.id.editTextDomain);

        // Set the current values of the parameters in the corresponding EditText fields
        try {
            editTextUsername.setText(account.getData().get(0).getDecryptedUsername());
            editTextPassword.setText(account.getData().get(0).getDecryptedPassword());
            ArrayList<String> tagsArray = account.getData().get(0).getTags();
            StringBuilder formattedTags = new StringBuilder();
            for(String tag: tagsArray) {

                formattedTags.append(aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), tag));
                if(!tag.equals(tagsArray.get(tagsArray.size()-1))) {
                    formattedTags.append(",");
                }
            }
            editTextTags.setText(formattedTags);
            editTextDomain.setText(account.getData().get(0).getDecryptedDomain());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 NoSuchAlgorithmException | InvalidKeySpecException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }

        // Set the update button click listener
        Button buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated values from the EditText fields
                String newUsername = editTextUsername.getText().toString().trim();
                String newPassword = editTextPassword.getText().toString().trim();
                String newTags = editTextTags.getText().toString().trim();
                String newDomain = editTextDomain.getText().toString().trim();

                String bearerToken = "Bearer " + properties.getBearerToken();

                // Compute request body
                boolean isPreviousField = false;
                StringBuilder requestBody = new StringBuilder("{\n    ");
                if(!newUsername.isEmpty()) {

                    requestBody.append("\"username\": \"").append(newUsername).append("\"");
                    isPreviousField = true;
                }
                if(!newPassword.isEmpty()) {

                    if(isPreviousField) {
                        requestBody.append(",\n    ");
                    } else {
                        isPreviousField = true;
                    }
                    requestBody.append("\"password\": \"").append(newPassword).append("\"");
                }
                if(!newTags.isEmpty()) {

                    if(isPreviousField) {
                        requestBody.append(",\n    ");
                    } else {
                        isPreviousField = true;
                    }

                    String[] newTagsArray = newTags.split(",");
                    StringBuilder newFormattedTags = new StringBuilder("[\n        ");
                    for(String tag: newTagsArray) {

                        newFormattedTags.append("\"" + tag + "\"");
                        if(Objects.equals(tag, newTagsArray[newTagsArray.length-1])) {

                            newFormattedTags.append("\n        ");
                        } else {

                            newFormattedTags.append(",\n        ");
                        }
                    }
                    newFormattedTags.append("]");

                    requestBody.append("\"tags\": ").append(newFormattedTags);
                }
                if(!newDomain.isEmpty()) {

                    if(isPreviousField) {
                        requestBody.append(",\n    ");
                    }
                    requestBody.append("\"domain\": \"").append(newDomain).append("\"");
                }

                requestBody.append("\n}");

                OkHttpClient client = new OkHttpClient();

                String accountId = null;
                try {
                    accountId = aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), account.getData().get(0).getId());
                } catch (InvalidAlgorithmParameterException | InvalidKeyException |
                         BadPaddingException | NoSuchAlgorithmException | InvalidKeySpecException |
                         IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                }
                String url = "https://password-manager-api.us-e2.cloudhub.io/v1/accounts/" + accountId;

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody jsonBody = RequestBody.create(String.valueOf(requestBody), mediaType);

                Request request = new Request.Builder()
                        .url(url)
                        .put(jsonBody)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", bearerToken)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    // Handle the response as needed
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Return to SuggestionsActivity
                finish();
            }
        });
    }
}

