package com.example.anton.syshelper;

import android.support.annotation.NonNull;

public class ServerInfo implements Comparable<ServerInfo>{
    private String title;
    private String name;
    private String host;
    private  String port;
    private String password;
    private String uId;

    public ServerInfo(String name, String host, String port, String password,String title,String uId) {
        this.name = name;
        this.host = host;
        this.title=title;
        this.uId=uId;
        this.port = port;
        this.password = password;
    }

    public String getuId() {
        return uId;
    }

    public ServerInfo() {
        this.name = new String();
        this.host = new String();
        this.title=new String();
        this.port = new String();
        this.password = new String();
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int compareTo(@NonNull ServerInfo serverInfo) {
        return (serverInfo.getuId().compareTo(this.getuId()));
    }
}
