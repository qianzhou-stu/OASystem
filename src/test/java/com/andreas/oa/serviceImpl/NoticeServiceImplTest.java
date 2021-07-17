package com.andreas.oa.serviceImpl;

import com.andreas.oa.dao.NoticeMapper;
import com.andreas.oa.pojo.Notice;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application.xml", "classpath:spring-mvc.xml"})
public class NoticeServiceImplTest extends TestCase {
    @Resource
    private NoticeServiceImpl noticeServiceImpl;

    @Resource
    private NoticeMapper noticeMapper;

    @Test
    public void getList() {
        List<Notice> notices = noticeServiceImpl.getNoticeList(Long.valueOf(1));
        System.out.println(notices);
        Notice notice = noticeMapper.selectByPrimaryKey(5L);
        System.out.println(notice);
    }
}