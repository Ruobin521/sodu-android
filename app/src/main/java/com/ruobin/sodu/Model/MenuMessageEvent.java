package com.ruobin.sodu.Model;

import java.util.Objects;

/**
 * Created by ruobi on 17/10/14.
 */

public class MenuMessageEvent {

    public static enum EventType {
        Login,
        Logout,

        AddOnline,
        SetOnlineHadRead,
        RemoverOnline,

        AddLocal,
        SetLocalHadRead,
        RemoveLoacal,
        CheckLocalUpdate
    }
    public Object data;
    public EventType eventType;

    public MenuMessageEvent(EventType e, Object obj) {
        eventType = e;
        data = obj;
    }
}
