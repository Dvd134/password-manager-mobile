package ro.ase.ism.passwordmanager.entities;

import static ro.ase.ism.passwordmanager.MainActivity.properties;

import android.os.Parcel;
import android.os.Parcelable;

import ro.ase.ism.passwordmanager.crypto.AESUtils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class AccountData implements Parcelable {

	private String id;
	private String username;
	private String password;
	private ArrayList<String> tags;
	private String domain;

	AESUtils aesUtils = new AESUtils();


	public AccountData() {}

	public AccountData(String id, String username, String password, ArrayList<String> tags, String domain) throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException {

		this.id = id;
		this.username = username;
		this.password = password;
		this.tags = tags;
		this.domain = domain;
	}

	public String getId() {

		return id;
	}

	public String getUsername() {

		return username;
	}

	public String getPassword() {

		return password;
	}

	public ArrayList<String> getTags() {

		return tags;
	}

	public String getDomain() {

		return domain;
	}

	public String getDecryptedId() throws InvalidAlgorithmParameterException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

		return aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), id);
	}

	public String getDecryptedUsername() throws InvalidAlgorithmParameterException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

		return aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), username);
	}

	public String getDecryptedPassword() throws InvalidAlgorithmParameterException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

		return aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), password);
	}

	public ArrayList<String> getDecryptedTags() throws InvalidAlgorithmParameterException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

		ArrayList<String> decryptedTags = new ArrayList<>();
		for(String tag: tags) {

			decryptedTags.add(aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), tag));
		}

		return decryptedTags;
	}

	public String getDecryptedDomain() throws InvalidAlgorithmParameterException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

		return aesUtils.decrypt(aesUtils.decrypt(properties.getPassphrase(), properties.getClientSecret()), domain);
	}

	// Implement the Parcelable interface methods
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(username);
		dest.writeString(password);
		dest.writeStringList(tags);
		dest.writeString(domain);
	}

	// Creator constant
	public static final Parcelable.Creator<AccountData> CREATOR = new Parcelable.Creator<AccountData>() {
		public AccountData createFromParcel(Parcel in) {
			return new AccountData(in);
		}

		public AccountData[] newArray(int size) {
			return new AccountData[size];
		}
	};

	// Read data from the Parcel constructor
	private AccountData(Parcel in) {
		id = in.readString();
		username = in.readString();
		password = in.readString();
		tags = new ArrayList<>();
		in.readStringList(tags);
		domain = in.readString();
	}
}
