package de.sopamo.triangula.android.levels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by moe on 03.10.14.
 */
public class BaseOnlineLevel extends BaseLevel implements Level,Parcelable {
    private int nextLevel;

    //this is a template for onlineLevels. Just set creatorTag and levelName in the constructor, and the JSON string later

    public BaseOnlineLevel(String creatorTag, String levelName, String levelUrl,int nextLevel) {
        this.creatorTag=creatorTag;
        this.levelName = levelName;
        this.isOnlineLevel=true;
        this.levelUrl=levelUrl;
        this.nextLevel = nextLevel;
    }


    private BaseOnlineLevel(Parcel in) {
        boolean[] booleanData = new boolean[1];
        in.readBooleanArray(booleanData);
        nextLevel = in.readInt();
        creatorTag = in.readString();
        levelName = in.readString();
        levelUrl = in.readString();
        levelString = in.readString();
        isOnlineLevel = booleanData[0];
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBooleanArray(new boolean[] {
                isOnlineLevel
        });
        parcel.writeInt(nextLevel);
        parcel.writeString(creatorTag);
        parcel.writeString(levelName);
        parcel.writeString(levelUrl);
        parcel.writeString(levelString);
    }

    public static final Parcelable.Creator<BaseOnlineLevel> CREATOR
            = new Parcelable.Creator<BaseOnlineLevel>() {
        public BaseOnlineLevel createFromParcel(Parcel in) {
            return new BaseOnlineLevel(in);
        }

        public BaseOnlineLevel[] newArray(int size) {
            return new BaseOnlineLevel[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public int getNextLevel() {
        return nextLevel;
    }
}
