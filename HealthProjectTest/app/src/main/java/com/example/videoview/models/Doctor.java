package com.example.videoview.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable {
    private String name;
    private String bmdc_no;
    private String nid_no;
    private String mobile_no;
    private String mail;
    private String imageUrl;
    private String password;

    public Doctor(String name, String bmdc_no, String nid_no, String mobile_no, String mail, String imageUrl, String password) {
        this.name = name;
        this.bmdc_no = bmdc_no;
        this.nid_no = nid_no;
        this.mobile_no = mobile_no;
        this.mail = mail;
        this.imageUrl = imageUrl;
        this.password = password;
    }

    protected Doctor(Parcel in) {
        name = in.readString();
        bmdc_no = in.readString();
        nid_no = in.readString();
        mobile_no = in.readString();
        mail = in.readString();
        imageUrl = in.readString();
        password = in.readString();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(bmdc_no);
        parcel.writeString(nid_no);
        parcel.writeString(mobile_no);
        parcel.writeString(mail);
        parcel.writeString(imageUrl);
        parcel.writeString(password);
    }


    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBmdc_no() {
        return bmdc_no;
    }

    public void setBmdc_no(String bmdc_no) {
        this.bmdc_no = bmdc_no;
    }

    public String getNid_no() {
        return nid_no;
    }

    public void setNid_no(String nid_no) {
        this.nid_no = nid_no;
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

