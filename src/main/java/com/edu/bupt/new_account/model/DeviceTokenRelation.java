package com.edu.bupt.new_account.model;

public class DeviceTokenRelation {
    private Integer id;

    private String ieee;

    private Integer endpoint;

    private String token;

    private String type;

    private String gatewayname;

    private String shortaddress;

    private String uuid;

    public DeviceTokenRelation(Integer id, String ieee, Integer endpoint, String token, String type, String gatewayname, String shortaddress, String uuid) {
        this.id = id;
        this.ieee = ieee;
        this.endpoint = endpoint;
        this.token = token;
        this.type = type;
        this.gatewayname = gatewayname;
        this.shortaddress = shortaddress;
        this.uuid = uuid;
    }

    public DeviceTokenRelation() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIeee() {
        return ieee;
    }

    public void setIeee(String ieee) {
        this.ieee = ieee == null ? null : ieee.trim();
    }

    public Integer getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Integer endpoint) {
        this.endpoint = endpoint;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getGatewayname() {
        return gatewayname;
    }

    public void setGatewayname(String gatewayname) {
        this.gatewayname = gatewayname == null ? null : gatewayname.trim();
    }

    public String getShortaddress() {
        return shortaddress;
    }

    public void setShortaddress(String shortaddress) {
        this.shortaddress = shortaddress == null ? null : shortaddress.trim();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }
}