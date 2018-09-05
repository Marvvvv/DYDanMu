package com.yukari.utils;

import com.yukari.client.DyBulletScreenClient;

public class ChangeServer implements Runnable {

    public void run() {
        DyBulletScreenClient client = DyBulletScreenClient.getInstance();
        client.setReadyFlag(false);
        client.init(196,-9999);
    }

}
