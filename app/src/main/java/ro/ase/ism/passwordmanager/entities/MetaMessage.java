package ro.ase.ism.passwordmanager.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class MetaMessage implements Parcelable {

    private String code;
    private String text;
    private String level;

    public MetaMessage() {}

    public MetaMessage(String code, String text, String level) {
        this.code = code;
        this.text = text;
        this.level = level;
    }

    protected MetaMessage(Parcel in) {
        code = in.readString();
        text = in.readString();
        level = in.readString();
    }

    public static final Parcelable.Creator<MetaMessage> CREATOR = new Creator<MetaMessage>() {
        @Override
        public MetaMessage createFromParcel(Parcel in) {
            return new MetaMessage(in);
        }

        @Override
        public MetaMessage[] newArray(int size) {
            return new MetaMessage[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(text);
        dest.writeString(level);
    }
}