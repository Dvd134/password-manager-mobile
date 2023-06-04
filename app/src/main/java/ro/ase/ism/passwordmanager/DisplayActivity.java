package ro.ase.ism.passwordmanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import ro.ase.ism.passwordmanager.entities.Account;
import ro.ase.ism.passwordmanager.entities.AccountData;

public class DisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AccountAdapter accountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Retrieve the account object from the intent
        Account account = getIntent().getParcelableExtra("account");

        // Bind the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        ArrayList<AccountData> accountList = new ArrayList<AccountData>();
        // Create and set the adapter for the RecyclerView
        accountAdapter = new AccountAdapter(accountList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(accountAdapter);

        // Retrieve the list of accounts
        accountList = account.getData();

        // Update the adapter with the new account list
        accountAdapter.updateData(accountList);
    }

    // Custom adapter for the RecyclerView
    private class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

        private ArrayList<AccountData> data;

        public AccountAdapter(ArrayList<AccountData> data) {
            this.data = data;
        }

        public void updateData(ArrayList<AccountData> newData) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
            return new AccountViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
            AccountData account = data.get(position);
            try {
                holder.bind(account);
            } catch (InvalidAlgorithmParameterException | InvalidKeyException |
                     BadPaddingException | NoSuchAlgorithmException | InvalidKeySpecException |
                     IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        // ViewHolder class for each account item
        private class AccountViewHolder extends RecyclerView.ViewHolder {
            private TextView textUsername, textPassword, textTags, textDomain;

            public AccountViewHolder(@NonNull View itemView) {
                super(itemView);
                textUsername = itemView.findViewById(R.id.textUsername);
                textPassword = itemView.findViewById(R.id.textPassword);
                textTags = itemView.findViewById(R.id.textTags);
                textDomain = itemView.findViewById(R.id.textDomain);
            }

            public void bind(AccountData account) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
                textUsername.setText("Username: " + account.getDecryptedUsername());
                textPassword.setText("Password: " + account.getDecryptedPassword());
                textTags.setText("Tags: " + TextUtils.join(", ", account.getDecryptedTags()));
                textDomain.setText("Domain: " + account.getDecryptedDomain());
            }
        }
    }
}

