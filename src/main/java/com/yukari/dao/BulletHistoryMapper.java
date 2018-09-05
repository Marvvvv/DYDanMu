package com.yukari.dao;

import com.yukari.entity.BulletHistory;

import java.util.List;

public interface BulletHistoryMapper {

    void insert (BulletHistory bullet);

    void insertBatch (List<BulletHistory> bullets);

}
