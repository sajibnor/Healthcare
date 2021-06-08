package com.example.videoview.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Blood implements Parcelable {
    private String name;
    private String fb_link;
    private String mobile_no;
    private String mail;
    private String imageUrl;
    private String password;

    public Blood(String name, String fb_link, String mobile_no, String mail, String imageUrl, String password) {
        this.name = name;
        this.fb_link = fb_link;
        this.mobile_no = mobile_no;
        this.mail = mail;
        this.imageUrl = imageUrl;
        this.password = password;
    }

    protected Blood(Parcel in) {
        name = in.readString();
        fb_link = in.readString();
        mobile_no = in.readString();
        mail = in.readString();
        imageUrl = in.readString();
        password = in.readString();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(fb_link);
        parcel.writeString(mobile_no);
        parcel.writeString(mail);
        parcel.writeString(imageUrl);
        parcel.writeString(password);
    }


    public static final Creator<Blood> CREATOR = new Creator<Blood>() {
        @Override
        public Blood createFromParcel(Parcel in) {
            return new Blood(in);
        }

        @Override
        public Blood[] newArray(int size) {
            return new Blood[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFb_link() {
        return fb_link;
    }

    public void setFb_link(String fb_link) {
        this.fb_link = fb_link;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
