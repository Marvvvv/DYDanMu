package com.yukari.service;

import java.util.Map;

public interface MsgService {

    void bulletMsgHandle (Map<String, Object> msg);

    void giftMsgHandle (Map<String, Object> msg);

    void enterMsgHandle (Map<String, Object> msg);

    void anchorOnlineMsgHandle (Map<String, Object> msg);

    void bigBulletMsgHandle (Map<String, Object> msg);

    void giftRadioMsgHandle (Map<String, Object> msg);

    void shutUpMsgHandle (Map<String, Object> msg);

    void openOrRenewNobleMsgHandle (Map<String, Object> msg);

    void giftHitRadioMsgHandle (Map<String, Object> msg);


}
