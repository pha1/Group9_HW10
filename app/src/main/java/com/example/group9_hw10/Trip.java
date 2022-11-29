package com.example.group9_hw10;

import java.io.Serializable;

public class Trip implements Serializable {
    String name, started_At, completed_At, status;
    Double distance, start_latitude, start_longitude, end_latitude, end_longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarted_At() {
        return started_At;
    }

    public void setStarted_At(String started_At) {
        this.started_At = started_At;
    }

    public String getCompleted_At() {
        return completed_At;
    }

    public void setCompleted_At(String completed_At) {
        this.completed_At = completed_At;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(Double start_latitude) {
        this.start_latitude = start_latitude;
    }

    public Double getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(Double start_longitude) {
        this.start_longitude = start_longitude;
    }

    public Double getEnd_latitude() {
        return end_latitude;
    }

    public void setEnd_latitude(Double end_latitude) {
        this.end_latitude = end_latitude;
    }

    public Double getEnd_longitude() {
        return end_longitude;
    }

    public void setEnd_longitude(Double end_longitude) {
        this.end_longitude = end_longitude;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "name='" + name + '\'' +
                ", started_At='" + started_At + '\'' +
                ", completed_At='" + completed_At + '\'' +
                ", status='" + status + '\'' +
                ", distance=" + distance +
                ", start_latitude=" + start_latitude +
                ", start_longitude=" + start_longitude +
                ", end_latitude=" + end_latitude +
                ", end_longitude=" + end_longitude +
                '}';
    }
}
