package com.feri.sipv.sipvserver.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "sessions")
public class Session {
    private UUID userId;
    private UUID sessionId;
    private String sessionSecret;
    private String sessionToken;
    private long sessionTimestamp;
    private boolean sessionValidity;
    private String sessionInfo;

    public Session(){

    }

    public Session(UUID userId, UUID sessionId, String sessionSecret, String sessionToken) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.sessionSecret = sessionSecret;
        this.sessionToken = sessionToken;
        this.sessionTimestamp = System.currentTimeMillis() / 1000L;
        this.sessionValidity = true;
        this.sessionInfo = "";
    }

    @Id
    @Column(name = "session_id", nullable = false)
    public UUID getSessionId() {
        return sessionId;
    }
    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    @Column(name = "user_id", nullable = false)
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Column(name = "session_secret", nullable = false)
    public String getSessionSecret() {
        return sessionSecret;
    }
    public void setSessionSecret(String sessionSecret) {
        this.sessionSecret = sessionSecret;
    }

    @Column(name = "session_token", nullable = false)
    public String getSessionToken() {
        return sessionToken;
    }
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Column(name = "session_timestamp")
    public long getSessionTimestamp() {
        return sessionTimestamp;
    }
    public void setSessionTimestamp(long sessionTimestamp) { this.sessionTimestamp = sessionTimestamp; }

    @Column(name = "session_validity")
    public boolean getSessionValidity() {
        return sessionValidity;
    }
    public void setSessionValidity(boolean sessionValidity) { this.sessionValidity = sessionValidity; }

    @Column(name = "session_info")
    public String getSessionInfo() { return sessionInfo; }
    public void setSessionInfo(String sessionInfo) { this.sessionInfo = sessionInfo; }
}
