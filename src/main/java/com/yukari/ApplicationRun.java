package com.yukari;

import com.yukari.client.DyBulletScreenClient;

import com.yukari.entity.ServerInfo;
import com.yukari.utils.ChangeServer;
import com.yukari.utils.KeepAlive;
import com.yukari.utils.KeepGetMsg;
import com.yukari.utils.ServerUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class ApplicationRun {


    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:config/spring/applicationContext.xml");

        DyBulletScreenClient client = DyBulletScreenClient.getInstance();
        client.init(196, -9999);

        // 发送心跳包线程
        KeepAlive alive = new KeepAlive();
        alive.start();

        // 接收弹幕服务器的消息
        KeepGetMsg getMsg = new KeepGetMsg();
        getMsg.start();

        /*// (定时任务) 每一小时更换一次弹幕服务器？
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(new ChangeServer(),2L,50000L,TimeUnit.MILLISECONDS);*/

    }

}
