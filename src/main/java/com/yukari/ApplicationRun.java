package com.yukari;

import com.yukari.client.DyBulletScreenClient;

import com.yukari.entity.ServerInfo;
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
        client.init(988, -9999);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
        // 每45秒发送一次心跳包
        scheduledExecutorService.scheduleAtFixedRate(new KeepAlive(), 2L, 45000L, TimeUnit.MILLISECONDS);

        // 发送心跳包线程
        KeepAlive alive = new KeepAlive();
        alive.start();

        // 接收弹幕服务器的消息
        KeepGetMsg getMsg = new KeepGetMsg();
        getMsg.start();



        /*ServerUtil util = new ServerUtil();
        List<ServerInfo> serverList = util.getServers(606118);
        ServerInfo danmuServer = util.getDanmuServers(serverList,606118);
        System.out.println(1);*/

    }

}
