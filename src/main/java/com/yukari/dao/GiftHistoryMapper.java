package com.yukari.dao;

import com.yukari.entity.GiftHistory;

import java.util.List;

public interface GiftHistoryMapper {

    void insert (GiftHistory giftHistory);

    void insertBatch (List<GiftHistory> generalGifts);


}
