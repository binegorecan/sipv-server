package com.feri.sipv.sipvserver.models.JWT;

import java.io.Serializable;
import java.util.UUID;

public class JwtResponse  implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;

    private final String jwttoken;
    private String username;
    private String name;
    private UUID id;
    private String permissions;


    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public JwtResponse(String jwttoken, String username, String name, UUID id, String permissions) {
        this.jwttoken = jwttoken;
        this.username = username;
        this.name = name;
        this.id = id;
        this.permissions = permissions;
    }

    public String getToken() { return this.jwttoken; }
    public String getUsername() { return username; }
    public String getName() {  return name; }
    public UUID getId() { return id; }
    public String getPermissions() { return permissions; }
}
