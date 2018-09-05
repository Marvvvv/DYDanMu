package com.yukari.dao;

import com.yukari.entity.Gift;

import java.util.List;

public interface GiftMapper {

    void insert (Gift gift);

    void insertBatch (List<Gift> generalGifts);


}
