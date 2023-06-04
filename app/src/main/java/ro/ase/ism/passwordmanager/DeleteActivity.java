package ro.ase.ism.passwordmanager;

import static ro.ase.ism.passwordmanager.MainActivity.properties;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.ase.ism.passwordmanager.crypto.AESUtils;
import ro.ase.ism.passwordmanager.entities.Account;

public class DeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the account object from the intent
        Account account = getIntent().getParcelableExtra("account");

        AESUtils aesUtils = new AESUtils();
        String bearerToken = "Bearer " + properties.getBearerToken();

        OkHttpClient client = new OkHttpClient();

        Request request = null;
        try {
            request = new Request.Builder()
                    .url("https://password-manager-api.us-e2.cloudhub.io/v1/accounts/" + aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), account.getData().get(0).getId()))
                    .delete()
                    .addHeader("Authorization", bearerToken)
                    .build();
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 NoSuchAlgorithmException | InvalidKeySpecException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();

        }
        finish();
    }
}
