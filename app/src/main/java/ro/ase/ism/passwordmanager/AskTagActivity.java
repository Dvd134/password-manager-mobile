package ro.ase.ism.passwordmanager;

import static ro.ase.ism.passwordmanager.MainActivity.properties;
import static ro.ase.ism.passwordmanager.ReadActivity.getAccounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class AskTagActivity extends AppCompatActivity {

    private EditText etTag;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_tag);

        etTag = findViewById(R.id.etTag);
        btnSearch = findViewById(R.id.btnAction);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chosenTag = etTag.getText().toString().trim();

                AESUtils aesUtils = new AESUtils();
                Account accounts = getAccounts(null);
                Account filteredAccounts = new Account();
                ArrayList<AccountData> filteredAccountData = new ArrayList<>();

                for(AccountData account: accounts.getData()) {

                    for(String tag: account.getTags()) {

                        String decryptedTag = null;
                        try {
                            decryptedTag = aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), tag);
                        } catch (InvalidAlgorithmParameterException | InvalidKeyException |
                                 BadPaddingException | NoSuchAlgorithmException |
                                 InvalidKeySpecException | IllegalBlockSizeException e) {
                            throw new RuntimeException(e);
                        }
                        if(decryptedTag.equals(chosenTag)) {

                            filteredAccountData.add(account);
                            break;
                        }
                    }
                }
                filteredAccounts.setMeta(accounts.getMeta());
                filteredAccounts.setData(filteredAccountData);

                // Create an Intent to start DisplayActivity
                Intent intent = new Intent(AskTagActivity.this, DisplayActivity.class);
                intent.putExtra("account", filteredAccounts);

                // Start DisplayActivity
                startActivity(intent);
            }
        });
    }
}