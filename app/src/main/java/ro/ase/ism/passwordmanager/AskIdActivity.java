package ro.ase.ism.passwordmanager;

import static ro.ase.ism.passwordmanager.MainActivity.properties;
import static ro.ase.ism.passwordmanager.ReadActivity.getAccounts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import ro.ase.ism.passwordmanager.crypto.AESUtils;
import ro.ase.ism.passwordmanager.entities.Account;
import ro.ase.ism.passwordmanager.entities.AccountData;

public class AskIdActivity extends AppCompatActivity {

    private EditText etId;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_id);

        Button btnAction = findViewById(R.id.btnAction);

        // Set the button text dynamically based on the action
        String buttonText = getIntent().getStringExtra("buttonAction");
        btnAction.setText(buttonText);

        etId = findViewById(R.id.etId);
        btnSearch = findViewById(R.id.btnAction);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = getIntent().getExtras();
                String action = extras.getString("action");

                String chosenId = etId.getText().toString().trim();

                AESUtils aesUtils = new AESUtils();

                Account accounts = getAccounts(null);
                Account filteredAccounts = new Account();
                ArrayList<AccountData> filteredAccountData = new ArrayList<>();

                for(AccountData account: accounts.getData()) {

                    String decryptedId = null;
                    try {
                        decryptedId = aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), account.getId());
                    } catch (InvalidAlgorithmParameterException | InvalidKeyException |
                             BadPaddingException | NoSuchAlgorithmException |
                             InvalidKeySpecException | IllegalBlockSizeException e) {
                        throw new RuntimeException(e);
                    }
                    if(decryptedId.equals(chosenId)) {

                        filteredAccountData.add(account);
                        break;
                    }
                }
                filteredAccounts.setMeta(accounts.getMeta());
                filteredAccounts.setData(filteredAccountData);

                if(action.equals("read")) {

                    // Create an Intent to start DisplayActivity
                    Intent intent = new Intent(AskIdActivity.this, DisplayActivity.class);
                    intent.putExtra("account", filteredAccounts);

                    // Start DisplayActivity
                    startActivity(intent);
                } else if(action.equals("update")) {

                    // Open the UpdateActivity
                    Intent intent = new Intent(AskIdActivity.this, UpdateActivity.class);
                    intent.putExtra("account", filteredAccounts);
                    startActivity(intent);

                    finish();

                } else if(action.equals("delete")) {

                    Intent intent = new Intent(AskIdActivity.this, DeleteActivity.class);
                    intent.putExtra("account", filteredAccounts);
                    startActivity(intent);

                    // Show the "Account Deleted!" toast
                    try {
                        Toast.makeText(AskIdActivity.this, "Account" + filteredAccounts.getData().get(0).getDecryptedUsername() + "Deleted!", Toast.LENGTH_SHORT).show();
                    } catch (InvalidAlgorithmParameterException | InvalidKeyException |
                             BadPaddingException | NoSuchAlgorithmException |
                             InvalidKeySpecException | IllegalBlockSizeException e) {
                        throw new RuntimeException(e);
                    }

                    // Delay the closure of the activity to display the toast for a specific duration
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Close the activity after a delay
                            finish();
                        }
                    }, 2000); // 2000 milliseconds = 2 seconds
                }

            }
        });
    }
}