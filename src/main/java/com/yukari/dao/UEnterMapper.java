package com.yukari.dao;

import com.yukari.entity.UEnter;

import java.util.List;

public interface UEnterMapper {

    void insert (UEnter uEnter);

    void insertBatch (List<UEnter> uEnters);

}
