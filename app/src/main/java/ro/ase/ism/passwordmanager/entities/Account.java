package ro.ase.ism.passwordmanager.entities;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable  {

    private Meta meta;
    private ArrayList<AccountData> data;

    public Account() {}

    public Account(Meta meta, ArrayList<AccountData> data) {
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public ArrayList<AccountData> getData() {
        return data;
    }

    public void setData(ArrayList<AccountData> data) {
        this.data = data;
    }


    // Implement the Parcelable interface methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable) meta, flags);
        dest.writeTypedList(data);
    }

    // Creator constant
    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    // Read data from the Parcel constructor
    private Account(Parcel in) {
        meta = in.readParcelable(Meta.class.getClassLoader());
        data = in.createTypedArrayList(AccountData.CREATOR);
    }

}
