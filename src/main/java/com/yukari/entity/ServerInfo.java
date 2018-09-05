package com.yukari.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ServerInfo implements Serializable{

    private String host;
    private int port;

    public ServerInfo (){}

    public ServerInfo (String host,int port) {
        this.host = host;
        this.port = port;
    }

    public ServerInfo (String ipStr) {
        // "ip=14.17.102.74/port=12604/" ->
        ipStr = ipStr.replaceAll("@A","").replaceAll("@S","/");
        this.host = ipStr.substring(ipStr.indexOf("ip=") + 3,ipStr.indexOf("/port="));
        this.port = Integer.parseInt(ipStr.substring(ipStr.indexOf("/port=") + 6,ipStr.lastIndexOf("/")));
    }


}
