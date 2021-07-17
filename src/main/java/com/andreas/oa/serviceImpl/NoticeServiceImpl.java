package com.andreas.oa.serviceImpl;

import com.andreas.oa.dao.NoticeMapper;
import com.andreas.oa.pojo.Notice;
import com.andreas.oa.service.NoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：noticeServiceImp实现类
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Resource
    private NoticeMapper noticeMapper;
    @Override
    public List<Notice> getNoticeList(Long receiverId) {
        List<Notice> notices = noticeMapper.selectByReceiverId(receiverId);
        return notices;
    }
}
