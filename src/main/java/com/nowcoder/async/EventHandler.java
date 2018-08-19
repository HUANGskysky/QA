package com.nowcoder.async;

import java.util.List;

/**
 * Created by Huangsky on 2018/8/18.
 */
public interface EventHandler {

    void doHandler(EventModel eventModel);
    List<EventType> getSupportEventTypes();

}
