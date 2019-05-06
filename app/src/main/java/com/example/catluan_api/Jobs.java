package com.example.catluan_api;

import java.io.Serializable;
import java.util.Date;

public class Jobs implements Serializable {
    String id, projectName;
    Date startDate, finishDate;

    public Jobs() {
    }

    public Jobs(String id, String projectName, Date startDate, Date finishDate) {
        this.id = id;
        this.projectName = projectName;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
}
