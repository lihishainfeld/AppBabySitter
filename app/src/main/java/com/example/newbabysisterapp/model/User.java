package com.example.newbabysisterapp.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {
    private transient Bitmap bitmap;
    private String photoLink;
    static public final String WORKER_KIND = "Worker";
    static public final String CLIENT_KIND = "Client";
    static public final String APPROVED = "APPROVED";
    static public final String DECLINED = "DECLINED";
    static public final String NOT_CHECKED = "NOT_CHECKED";
    private String userID;
    private String privateName;
    private String familyName;
    private String phone;
    private String email;
    private String password;
    private String facebookLink;
    private String address;
    private String description;
    private String kind;
    private String approved = NOT_CHECKED;

    public User() {}

    public String getPrivateName() {
        return privateName;
    }

    public void setPrivateName(String privateName) {
        this.privateName = privateName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public Bitmap theBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
