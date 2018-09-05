package com.yukari.client;


import com.yukari.dao.UEnterMapper;
import com.yukari.entity.ServerInfo;
import com.yukari.msg.DyMessage;
import com.yukari.msg.MsgView;
import com.yukari.utils.MD5Util;
import com.yukari.utils.MsgUtil;
import com.yukari.utils.ServerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 封装弹幕客户端
 */
public class DyBulletScreenClient {
    Logger logger = Logger.getLogger(DyBulletScreenClient.class);

    //第三方弹幕协议服务器地址
    private String hostName;

    //第三方弹幕协议服务器端口
    private int port;

    //设置字节获取buffer的最大值
    private static final int MAX_BUFFER_LENGTH = 8*1024;

    //获取弹幕线程及心跳线程运行和停止标记
    private boolean readyFlag = false;

    private Socket socket;
    private BufferedInputStream bis;
    private BufferedOutputStream bos;

    private static DyBulletScreenClient client;

    /**
     * 单例获取方法，客户端单例模式访问
     * @return
     */
    public static DyBulletScreenClient getInstance(){
        if(null == client){
            client = new DyBulletScreenClient();
        }
        return client;
    }


    public void init (int roomId,int groupId) {
        // 获取房间的弹幕服务器

        ServerUtil util = new ServerUtil();
        List<ServerInfo> serverList = util.getServers(606118);
        ServerInfo danmuServer = util.getDanmuServers(serverList,606118);

        this.hostName = danmuServer.getHost();
        this.port = danmuServer.getPort();


        // 连接至弹幕服务器
        this.connectionServer();
        // 连接到指定直播间，以及弹幕分组
        this.connectionRoom(roomId,groupId);
        // 设置客户端就绪标记为就绪状态
        readyFlag = true;
    }

    public boolean getReadyFlag() {
        return readyFlag;
    }

    public void setReadyFlag(boolean readyFlag) {
        this.readyFlag = readyFlag;
    }


    public void connectionServer () {
        try {
            //获取弹幕服务器访问host
            String host = InetAddress.getByName(hostName).getHostAddress();
            socket = new Socket(host,port);
            bis = new BufferedInputStream(socket.getInputStream());
            bos = new BufferedOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            logger.error("Server connetion error,host error",e);
        } catch (IOException e) {
            logger.error("Server connetion error,can not connection!",e);
        }
        logger.debug("Server connetion success!");
    }


    public void connectionRoom (int roomId,int groupId) {
        //获取弹幕服务器登陆请求数据包
        byte[] loginRequestData = DyMessage.getLoginRequestData(roomId);

        try {
            // 发送登陆请求至服务器
            bos.write(loginRequestData,0,loginRequestData.length);
            bos.flush();

            //初始化弹幕服务器返回值读取包大小
            byte[] recvByte = new byte[MAX_BUFFER_LENGTH];
            //获取弹幕服务器返回值
            bis.read(recvByte, 0, recvByte.length);

            //解析服务器返回的登录信息
            if(DyMessage.parseLoginRespond(recvByte)){
                logger.debug("Receive login response successfully!");

                // 登陆成功，加入弹幕分组
                //获取弹幕服务器加弹幕池请求数据包
                byte[] joinGroupRequest = DyMessage.getJoinGroupRequest(roomId, groupId);

                //想弹幕服务器发送加入弹幕池请求数据
                try {
                    bos.write(joinGroupRequest, 0, joinGroupRequest.length);
                    bos.flush();
                    logger.debug("Send join group request successfully!");
                } catch (IOException e) {
                    logger.error("Send join group request error!");
                }
            } else {
                logger.error("Receive login response failed!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送心跳连接数据包
     */
    public void keepAlive()
    {
        //获取与弹幕服务器保持心跳的请求数据包
        byte[] keepAliveRequest = DyMessage.getKeepAliveData();

        try{
            //向弹幕服务器发送心跳请求数据包
            bos.write(keepAliveRequest, 0, keepAliveRequest.length);
            bos.flush();
            logger.debug("Send keep alive request successfully!");
        } catch(Exception e){
            // 服务器重连
            logger.error("Send keep alive request failed!",e);
            logger.error("心跳包发送失败，重新连接服务器...");
            this.readyFlag = false;
            this.init(196,-9999);
        }
    }


    /**
     * 获取服务器返回信息
     */
    public void getServerMsg(){
        //初始化获取弹幕服务器返回信息包大小
//        byte[] recvByte = new byte[MAX_BUFFER_LENGTH];
        //定义服务器返回信息的字符串
//        String dataStr;
        try {
            /*//读取服务器返回信息，并获取返回信息的整体字节长度
            int recvLen = bis.read(recvByte, 0, recvByte.length);

            //根据实际获取的字节数初始化返回信息内容长度
            byte[] realBuf = new byte[recvLen];
            //按照实际获取的字节长度读取返回信息
            System.arraycopy(recvByte, 0, realBuf, 0, recvLen);
            //根据TCP协议获取返回信息中的字符串信息

            dataStr = new String(realBuf, 12, realBuf.length - 12,"UTF-8");


            //循环处理socekt黏包情况
            while(dataStr.lastIndexOf("type@=") > 5){
                //对黏包中最后一个数据包进行解析
                MsgView msgView = new MsgView(StringUtils.substring(dataStr, dataStr.lastIndexOf("type@=")));
                //分析该包的数据类型，以及根据需要进行业务操作
                MsgUtil.msgHandle(msgView.getMessageList());
                //处理黏包中的剩余部分
                dataStr = StringUtils.substring(dataStr, 0, dataStr.lastIndexOf("type@=") - 12);
            }
            //对单一数据包进行解析
            MsgView msgView = new MsgView(StringUtils.substring(dataStr, dataStr.lastIndexOf("type@=")));
            //分析该包的数据类型，以及根据需要进行业务操作
            MsgUtil.msgHandle(msgView.getMessageList());*/

            InputStream in = socket.getInputStream();
            int len;
            byte[] buffer = new byte[MAX_BUFFER_LENGTH];
            while (socket.isConnected() //链接结束
                    && (len = in.read(buffer)) != -1 //输入流结束
                    ) {
                MsgUtil.msgHandle(buffer,len);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
