package com.yukari.dao;

import com.yukari.entity.Anchor;

import java.util.List;

public interface AnchorMapper {

    void insertBatch (List<Anchor> anchorList);

    List<Integer> quertAllRoomId ();
}
