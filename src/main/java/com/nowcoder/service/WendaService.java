package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * Created by huangksy on 2018/8/6.
 */
@Service
public class WendaService {
    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }
}
