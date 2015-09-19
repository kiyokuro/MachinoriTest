package com.example.kuro.machinori;

/**
 * Created by keinon on 2015/09/19.
 */
public class Data {
    private String latitude;//緯度
    private String longitude;//経度
    private String accelerationX;//加速度x軸
    private String accelerationY;//加速度y軸
    private String accelerationZ;//加速度z軸
    private String deviceId;//デバイスID

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(String accelerationX) {
        this.accelerationX = accelerationX;
    }

    public String getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(String accelerationY) {
        this.accelerationY = accelerationY;
    }

    public String getAccelerationZ() {
        return accelerationZ;
    }

    public void setAccelerationZ(String accelerationZ) {
        this.accelerationZ = accelerationZ;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
