package com.andreas.oa.service;

import com.andreas.oa.pojo.Notice;

import java.util.List;

/**
 * 描述：notice service的使用
 */
public interface NoticeService {
    List<Notice> getNoticeList(Long receiverId);
}
