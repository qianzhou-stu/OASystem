package com.andreas.oa.service;

import com.andreas.oa.pojo.LeaveForm;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 */
public interface LeaveFormService {
    LeaveForm createLeaveForm(LeaveForm form);

    List<Map> getLeaveFormList(String process, Long employeeId);

    public void audit(Long formId , Long operatorId , String result , String reason);
}
