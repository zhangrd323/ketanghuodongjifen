package com.example.meeting.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Group implements Serializable, Parcelable {
    private int id;
    private String name;
    private int star;
    private int score;
    private String userIds="";

    public Group(String name, int star) {
        this.name = name;
        this.star = star;
    }

    public Group(String name, int star, int score, String userIds) {
        this.name = name;
        this.star = star;
        this.score = score;
        this.userIds = userIds;
    }

    public Group(int id, String name, int star, int score, String userIds) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.score = score;
        this.userIds = userIds;
    }

    protected Group(Parcel in) {
        id = in.readInt();
        name = in.readString();
        star = in.readInt();
        score = in.readInt();
        userIds = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(star);
        dest.writeInt(score);
        dest.writeString(userIds);
    }
}
