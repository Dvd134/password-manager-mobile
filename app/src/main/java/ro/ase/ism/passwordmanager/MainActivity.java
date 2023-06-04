package ro.ase.ism.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ro.ase.ism.passwordmanager.crypto.AESUtils;
import ro.ase.ism.passwordmanager.entities.Properties;
import ro.ase.ism.passwordmanager.entities.Token;
import ro.ase.ism.passwordmanager.services.PropertiesService;

public class MainActivity extends AppCompatActivity {

    private EditText editPassphrase;
    private Button btnSend;

    public static Properties properties;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        editPassphrase = findViewById(R.id.editPassphrase);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String passphrase = editPassphrase.getText().toString();
                properties = PropertiesService.getProperties(context);

                AESUtils aesUtils = new AESUtils();
                String basicToken = null;
                try {
                    basicToken = "Basic " + Base64.getEncoder().encodeToString((aesUtils.decrypt(passphrase, properties.getClientId()) + ":" + aesUtils.decrypt(passphrase, properties.getClientSecret())).getBytes());
                } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException |
                         InvalidKeySpecException | NoSuchAlgorithmException | BadPaddingException |
                         InvalidKeyException e) {
                    throw new RuntimeException(e);
                }

                Log.d("PasswordManager", "basic: " + basicToken);

                OkHttpClient client = new OkHttpClient();
                String responseBody = null;

                String url = "https://password-manager-api.us-e2.cloudhub.io/v1/actions/auth";
                String jsonBody = "{\n    \"passphrase\": \"" + passphrase + "\"\n}";
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(jsonBody, mediaType);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("grant_type", "CLIENT_CREDENTIALS")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", basicToken)
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    responseBody = response.body().string();
                    // Process the response body as needed
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ObjectMapper objectMapper = new ObjectMapper();
                Token token = null;
                try {
                    token = objectMapper.readValue(responseBody, Token.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                properties.setBearerToken(token.getData().getAccessToken());
                properties.setPassphrase(passphrase);

                Log.d("PasswordManager", "bearer: " + token.getData().getAccessToken());
                Intent intent = new Intent(MainActivity.this, SuggestionsActivity.class);
                //intent.putExtra("properties", properties);
                startActivity(intent);
            }
        });
    }
}