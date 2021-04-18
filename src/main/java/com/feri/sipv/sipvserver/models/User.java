package com.feri.sipv.sipvserver.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String passwordHash;
    private String permissions;
    private int carbs;
    private int proteins;
    private int fats;

    public User() {}

    public User(String firstName, String lastName, String username, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.permissions = "";
        this.carbs = 0;
        this.proteins = 0;
        this.fats = 0;
    }

    @Id
    @Column(name = "id", nullable = false)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Column(name = "first_name")
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    @Column(name = "last_name")
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    @Column(name = "username")
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Column(name = "password_hash")
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    @Column(name = "permissions")
    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }

    @Column(name = "carbs")
    public int getCarbs() { return carbs; }
    public void setCarbs(int carbs) { this.carbs = carbs; }

    @Column(name = "proteins")
    public int getProteins() {  return proteins; }
    public void setProteins(int proteins) { this.proteins = proteins; }

    @Column(name = "fats")
    public int getFats() { return fats; }
    public void setFats(int fats) { this.fats = fats; }
}
