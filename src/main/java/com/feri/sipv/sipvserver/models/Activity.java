package com.feri.sipv.sipvserver.models;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "activity")
public class Activity {
    private UUID id;
    private UUID employeeId;
    private Long timestamp;
    private String activity;
    private boolean success;

    public Activity() { }

    public Activity(UUID employeeId, Long timestamp, String activity, boolean success) {
        this.id = UUID.randomUUID();
        this.employeeId = employeeId;
        this.timestamp = timestamp;
        this.activity = activity;
        this.success = success;
    }

    public Activity(UUID employeeId, String activity, boolean success) {
        this.id = UUID.randomUUID();
        this.employeeId = employeeId;
        this.timestamp = System.currentTimeMillis()/1000;
        this.activity = activity;
        this.success = success;
    }

    @Id
    @Column(name = "id", nullable = false)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Column(name = "employee_id")
    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    @Column(name = "timestamp", nullable = false)
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    @Column(name = "activity", nullable = false)
    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    @Column(name = "success", nullable = false)
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
