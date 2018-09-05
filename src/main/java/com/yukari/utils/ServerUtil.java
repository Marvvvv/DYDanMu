package com.yukari.utils;


import com.yukari.entity.Message;
import com.yukari.entity.ServerInfo;
import com.yukari.msg.MsgView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerUtil {

    private static final String REGEX_SERVER = "%7B%22ip%22%3A%22(.*?)%22%2C%22port%22%3A%22(.*?)%22%7D%2C";

    public List<ServerInfo> getServers (int roomId) {
        Connection conn = Jsoup.connect("http://www.douyu.com/" + roomId).userAgent("Mozilla").timeout(30 * 1000).ignoreContentType(true);
        try {
            Document doc = conn.get();
            Pattern pa = Pattern.compile(REGEX_SERVER);
            Matcher ma = pa.matcher(doc.html());
            List<ServerInfo> serverList = new ArrayList<>();
            while (ma.find()) {
                serverList.add(new ServerInfo(ma.group(1),Integer.parseInt(ma.group(2))));
            }
            return serverList;
        } catch (IOException e) {
            return null;
        }
    }


    public ServerInfo getDanmuServers (List<ServerInfo> serverList,int roomId) {
        if (serverList != null && !serverList.isEmpty()) {
            ServerInfo server = serverList.get(new Random().nextInt(serverList.size()));
            Socket socket = null;
            try {
                // 连接服务器
                socket = new Socket(server.getHost(),server.getPort());

                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                String vk = MD5Util.MD5(timestamp + "7oE9nPEG9xXV69phU31FYCLUagKeYtsF" + uuid);

                Message message = new Message(gid(roomId, uuid, timestamp, vk));
                OutputStream out = socket.getOutputStream();
                out.write(message.getBytes());

                int len;
                byte[] buffer = new byte[8 * 1024];
                InputStream in = socket.getInputStream();
                // 获取弹幕服务器
                while (socket.isConnected() && (len = in.read(buffer)) != -1) {
                    List<String> msgList = splitResponse(Arrays.copyOf(buffer,len));
                    for (String msg : msgList) {
                        if (msg.contains("type@=msgiplist")) {
                            MsgView msgView = new MsgView(msg);
                            Map<String,Object> list = msgView.getMessageList();
                            Map<String,Object> ipmap = (Map<String, Object>) list.get("iplist");
                            List<String> iplist = new ArrayList<>(ipmap.keySet());
                            String ipStr = iplist.get(new Random().nextInt(iplist.size()));
                            return new ServerInfo(ipStr);
                        }
                        System.out.println(msg);
                    }
                }
            } catch (IOException e) {
                return null;
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }


    public String gid(int roomId, String devid, String rt, String vk) {
        return String.format("type@=loginreq/username@=/ct@=0/password@=/roomid@=%d/devid@=%s/rt@=%s/vk@=%s/ver@=20150929/", roomId, devid, rt, vk);
    }


    List<String> splitResponse(byte[] buffer) {
        if (buffer == null || buffer.length <= 0) return null;

        List<String> resList = new ArrayList<>();
        String byteArray = HexUtil.bytes2HexString(buffer).toLowerCase();

        String[] responseStrings = byteArray.split("b2020000");
        int end;
        for (int i = 1; i < responseStrings.length; i++) {
            if (!responseStrings[i].contains("00")) continue;
            end = responseStrings[i].indexOf("00");
            byte[] bytes = HexUtil.hexString2Bytes(responseStrings[i].substring(0, end));
            if (bytes != null) resList.add(new String(bytes));
        }

        return resList;
    }


    public static void main (String[] args) {


    }


}
