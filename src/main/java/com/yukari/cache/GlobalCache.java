package com.yukari.cache;

/**
 * 全局缓存类
 */
public class GlobalCache {

    private static GlobalCache globalCache = new GlobalCache();

    private boolean isOnline;
    private int roomId;

    public static GlobalCache getGlobalCache() {
        return globalCache;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
