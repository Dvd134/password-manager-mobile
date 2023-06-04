package ro.ase.ism.passwordmanager.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Meta implements Parcelable {

    private String status;
    private ArrayList<MetaMessage> messages;

    public Meta() {}

    public Meta(String status, ArrayList<MetaMessage> messages) {
        this.status = status;
        this.messages = messages;
    }

    protected Meta(Parcel in) {
        status = in.readString();
        messages = in.createTypedArrayList(MetaMessage.CREATOR);
    }

    public static final Creator<Meta> CREATOR = new Creator<Meta>() {
        @Override
        public Meta createFromParcel(Parcel in) {
            return new Meta(in);
        }

        @Override
        public Meta[] newArray(int size) {
            return new Meta[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MetaMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MetaMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeTypedList(messages);
    }
}