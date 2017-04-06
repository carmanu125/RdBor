package com.jcd.rdbordado.entity;

import java.io.Serializable;

/**
 * Created by Argosoft03 on 02/03/2017.
 */

public class EPlaces implements Serializable{

    private int id ;
    private String name ;
    private String short_description ;
    private String description ;
    private String address ;
    private String phone ;
    private String email ;
    private String latLong ;
    private String Ranking ;
    private String urlImage;
    private String urlLogo;
    private String urlFace;
    private String urlInsta;
    private String urlTwit;


    public String getUrlFace() {
        return urlFace;
    }

    public void setUrlFace(String urlFace) {
        this.urlFace = urlFace;
    }

    public String getUrlInsta() {
        return urlInsta;
    }

    public void setUrlInsta(String urlInsta) {
        this.urlInsta = urlInsta;
    }

    public String getUrlTwit() {
        return urlTwit;
    }

    public void setUrlTwit(String urlTwit) {
        this.urlTwit = urlTwit;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getRanking() {
        return Ranking;
    }

    public void setRanking(String ranking) {
        Ranking = ranking;
    }
}
