package com.example.meeting.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class User implements Parcelable,Serializable{
    private int id;
    private String name;
    private String num;
    private int score;
    /**
     * 最终分
     */
    private String fScore;
    private int isSelect = 0;
    private int groupId = 0;

    public User(int id, String name, String num, int score, String fScore, int groupId) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.score = score;
        this.fScore = fScore;
        this.groupId = groupId;
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        num = in.readString();
        score = in.readInt();
        fScore = in.readString();
        isSelect = in.readInt();
        groupId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(num);
        dest.writeInt(score);
        dest.writeString(fScore);
        dest.writeInt(isSelect);
        dest.writeInt(groupId);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public User(String name, String num) {
        this.name = name;
        this.num = num;
    }
    public User(String name, String num, int score) {
        this.name = name;
        this.num = num;
        this.score = score;
    }

    public User(int id, String name, String num, int score) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.score = score;
    }

    public String getfScore() {
        return fScore;
    }

    public void setfScore(String fScore) {
        this.fScore = fScore;
    }

    public User(int id, String name, String num, int score, String fScore) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.score = score;
        this.fScore = fScore;
    }

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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
