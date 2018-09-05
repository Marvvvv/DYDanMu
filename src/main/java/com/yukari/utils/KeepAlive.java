package com.yukari.utils;


import com.yukari.client.DyBulletScreenClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Summary: 服务器心跳保持线程
 * @author: FerroD     
 * @date:   2016-3-12   
 * @version V1.0
 */

public class KeepAlive extends Thread {

    public void run() {
        DyBulletScreenClient client = DyBulletScreenClient.getInstance();
        while (client.getReadyFlag()) {
            client.keepAlive();
            try {
                Thread.sleep(45000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
