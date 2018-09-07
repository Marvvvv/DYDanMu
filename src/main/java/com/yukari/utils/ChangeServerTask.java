package com.yukari.utils;

import com.yukari.client.DyBulletScreenClient;
import org.apache.log4j.Logger;


public class ChangeServerTask {

    Logger logger = Logger.getLogger(ChangeServerTask.class);

    public void run() {
        // 更换服务器重连
        logger.info("更换弹幕服务器...");
        DyBulletScreenClient client = DyBulletScreenClient.getInstance();
        client.setReadyFlag(false);
        client.init(196, -9999);
    }

}
