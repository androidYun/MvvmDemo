package com.xhs.em_doctor.view;

/**
 * @ author guiyun.li
 * @ Email xyz_6776.@163.com
 * @ date 05/08/2019.
 * description:
 */
public class MapLocationBean {
    private String lon;
    private String lat;
    private String speed;
    private String direction;

    public String getLon() {
        return lon==null?"":lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat==null?"":lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getSpeed() {
        return speed==null?"":speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction==null?"":direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
