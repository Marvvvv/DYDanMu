package com.yukari.utils;

import com.yukari.client.DyBulletScreenClient;
import com.yukari.msg.MsgView;
import com.yukari.service.MsgService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class MsgUtil {

    private static Logger logger = Logger.getLogger(MsgUtil.class);

    // type类型
    private static final String ERROR = "error";    // 服务器反馈错误信息
    private static final String BULLET = "chatmsg";  // 弹幕信息
    private static final String GIFT = "dgb";       // 当前房间礼物信息
    private static final String ENTER = "uenter";   // 用户进入房间信息
    private static final String ANCHOR_ONLINE = "rss"; // 开关播信息
    private static final String BIG_BULLET = "ssd"; // 超级弹幕信息
    private static final String GIFT_RADIO = "spbc"; // 礼物广播
    private static final String SHUT_UP = "newblackres"; // 禁言
    private static final String NOBLE = "online_noble_list"; // 贵族详情
    private static final String NOBLE_NUM_INFO = "noble_num_info"; // 贵族数量
    private static final String OPEN_UP_NOBLE = "anbc";  // 开通贵族
    private static final String RENEW_NOBLE = "rnewbc";  // 续费贵族
    private static final String GIFT_HIT_RADIO = "bgbc"; // 礼物连击广播



    @Autowired
    private MsgService msgService;

    private static MsgUtil msgUtil;

    @PostConstruct
    public void init() {
        msgUtil = this;
    }


    public static void msgHandle(byte[] buffer, int len) {
        List<String> listMsg = new ArrayList<>();

        try {
            listMsg = splitResponse(Arrays.copyOf(buffer, len));
        } catch (UnsupportedEncodingException e) {
            logger.error("utf-8解码异常!");
        }
        for (String dataStr : listMsg) {
            MsgView msgView = new MsgView(dataStr);
            msgHandle(msgView.getMessageList());
        }
    }


    public static List<String> splitResponse(byte[] buffer) throws UnsupportedEncodingException {
        if (buffer == null || buffer.length <= 0) {return null;}

        List<String> resList = new ArrayList<>();
        String byteArray = HexUtil.bytes2HexString(buffer).toLowerCase();
        String[] responseStrings = byteArray.split("b2020000");
        int end;
        for (int i = 1; i < responseStrings.length; i++) {
            if (!responseStrings[i].contains("00")) {continue;}
            end = responseStrings[i].indexOf("00");
            byte[] bytes = HexUtil.hexString2Bytes(responseStrings[i].substring(0, end));
            if (bytes != null) {resList.add(new String(bytes, "UTF-8"));}
        }

        return resList;
    }


    /**
     * 处理从服务端接受到的消息
     *
     * @param msg 服务器返回的消息
     */
    public static void msgHandle(Map<String, Object> msg) {
        if (msg != null && msg.containsKey("type")) {
            String type = String.valueOf(msg.get("type"));

            //服务器反馈错误信息
            if (ERROR.equals(type)) {
                logger.error("错误信息===>" + msg.toString());
                DyBulletScreenClient client = DyBulletScreenClient.getInstance();

                // 结束心跳和获取弹幕线程
                client.setReadyFlag(false);
                // 重新连接服务器及房间
                logger.info("服务器发生错误，重新连接...");
                client.reConnectServer();
            }

            /* 根据消息类型进行处理 */

            try {
                switch (type) {
                    case BULLET:
                        // 弹幕消息
                        msgUtil.msgService.bulletMsgHandle(msg);
//                        logger.debug("普通弹幕消息==>" + msg.toString());
                        break;
                    case GIFT:
                        // 礼物消息
                        msgUtil.msgService.giftMsgHandle(msg);
//                        logger.debug("赠送礼物消息==>" + msg.toString());
                        break;
                    case ENTER:
                        // 用户进入房间
                        msgUtil.msgService.enterMsgHandle(msg);
//                        logger.debug("用户进房消息==>" + msg.toString());
                        break;
                    case ANCHOR_ONLINE:
                        // 房间开关播
                        msgUtil.msgService.anchorOnlineMsgHandle(msg);
                        logger.debug("开播状态消息==>" + msg.toString());
                        break;
                    case BIG_BULLET:
                        // 超级弹幕
                        msgUtil.msgService.bigBulletMsgHandle(msg);
                        logger.debug("超级弹幕消息==>" + msg.toString());
                        break;
                    case GIFT_RADIO:
                        // 礼物广播
                        msgUtil.msgService.giftRadioMsgHandle(msg);
                        logger.debug("礼物广播消息==>" + msg.toString());
                        break;
                    case SHUT_UP:
                        // 禁言
                        msgUtil.msgService.shutUpMsgHandle(msg);
                        logger.debug("用户禁言消息==>" + msg.toString());
                        break;
                    case NOBLE:
                        // 贵族列表
//                        logger.debug("贵族列表消息==>" + msg.toString());
                        break;
                    case NOBLE_NUM_INFO:
                        // 贵族数量
//                        logger.debug("贵族数量消息==>" + msg.toString());
                        break;
                    case OPEN_UP_NOBLE:
                        msgUtil.msgService.openOrRenewNobleMsgHandle(msg);
                        logger.debug("开通贵族消息==>" + msg.toString());
                        break;
                    case RENEW_NOBLE:
                        msgUtil.msgService.openOrRenewNobleMsgHandle(msg);
                        logger.debug("续费贵族消息==>" + msg.toString());
                        break;
                    case GIFT_HIT_RADIO:
                        msgUtil.msgService.giftHitRadioMsgHandle(msg);
                        logger.debug("礼物连击消息==>" + msg.toString());
                        break;
                    default:
                        logger.info("其它消息==>" + msg.toString());
                }
            } catch (Exception e) {
                logger.error("ERROR:", e);
                logger.error("ERROR MESSAGE : " + msg.toString());
            }
        }
    }
}
