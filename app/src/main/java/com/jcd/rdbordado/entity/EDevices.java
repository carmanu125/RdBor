package com.jcd.rdbordado.entity;

/**
 * Created by Argosoft03 on 22/03/2017.
 */

public class EDevices {

    private int id;
    private String imei;
    private String brand;
    private String device;
    private String hardware;
    private String model;
    private String serial;
    private String user;
    private String versionSdk;

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVersionSdk() {
        return versionSdk;
    }

    public void setVersionSdk(String versionSdk) {
        this.versionSdk = versionSdk;
    }
}