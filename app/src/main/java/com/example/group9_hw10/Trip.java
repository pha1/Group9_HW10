package com.example.group9_hw10;

import java.io.Serializable;

public class Trip implements Serializable {
    String name, started_At, completed_At, status;
    Double distance;

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

    @Override
    public String toString() {
        return "Trip{" +
                "name='" + name + '\'' +
                ", started_At='" + started_At + '\'' +
                ", completed_At='" + completed_At + '\'' +
                ", status='" + status + '\'' +
                ", distance=" + distance +
                '}';
    }
}
