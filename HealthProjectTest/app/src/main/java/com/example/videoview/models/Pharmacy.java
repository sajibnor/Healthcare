package com.example.videoview.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Pharmacy implements Parcelable {
    private String name;
    private String mobile_no;
    private String image;
    private String license_no;
    private String mail;
    private String password;

    public Pharmacy(String name, String mobile_no, String image, String license_no, String mail, String password) {
        this.name = name;
        this.mobile_no = mobile_no;
        this.image = image;
        this.license_no = license_no;
        this.mail = mail;
        this.password = password;
    }

    protected Pharmacy(Parcel in) {
        name = in.readString();
        mobile_no = in.readString();
        image = in.readString();
        license_no = in.readString();
        mail = in.readString();
        password = in.readString();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(mobile_no);
        parcel.writeString(image);
        parcel.writeString(license_no);
        parcel.writeString(mail);
        parcel.writeString(password);
    }

    public static final Creator<Pharmacy> CREATOR = new Creator<Pharmacy>() {
        @Override
        public Pharmacy createFromParcel(Parcel in) {
            return new Pharmacy(in);
        }

        @Override
        public Pharmacy[] newArray(int size) {
            return new Pharmacy[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLicense_no() {
        return license_no;
    }

    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
