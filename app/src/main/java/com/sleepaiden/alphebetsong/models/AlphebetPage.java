package com.sleepaiden.alphebetsong.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Created by huiche on 6/10/17.
 */
@Data
public class AlphebetPage implements Parcelable {
    private int imageId;
    private String word;
    private int sound;
    private String customSoundSource;

    public AlphebetPage(int imageId, String word, int sound, String customSoundSource) {
        this.imageId = imageId;
        this.word = word;
        this.sound = sound;
        this.customSoundSource = customSoundSource;
    }

    public static final Parcelable.Creator<AlphebetPage> CREATOR = new Parcelable.Creator<AlphebetPage>() {
        public AlphebetPage createFromParcel(Parcel in) {
            return new AlphebetPage(in.readInt(), in.readString(), in.readInt(), in.readString());
        }

        public AlphebetPage[] newArray(int size) {
            return new AlphebetPage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeString(word);
        dest.writeInt(sound);
        dest.writeString(customSoundSource);
    }
}
