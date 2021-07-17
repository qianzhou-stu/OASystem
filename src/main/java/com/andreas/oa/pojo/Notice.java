package com.andreas.oa.pojo;

import java.util.Date;

public class Notice {
    private Long noticeId;

    private Long receiverId;

    private String content;

    private Date createTime;

    public Notice(){}
    public Notice(Long receiverId, String content, Date date) {
        this.receiverId = receiverId;
        this.content = content;
        this.createTime = date;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "noticeId=" + noticeId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}