package com.andreas.oa.serviceImpl;

import com.andreas.oa.common.Constant;
import com.andreas.oa.dao.EmployeeMapper;
import com.andreas.oa.dao.LeaveFormMapper;
import com.andreas.oa.dao.NoticeMapper;
import com.andreas.oa.dao.ProcessFlowMapper;
import com.andreas.oa.exception.BussinessException;
import com.andreas.oa.pojo.Employee;
import com.andreas.oa.pojo.LeaveForm;
import com.andreas.oa.pojo.Notice;
import com.andreas.oa.pojo.ProcessFlow;
import com.andreas.oa.service.LeaveFormService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：
 */
@Service
public class LeaveFormServiceImpl implements LeaveFormService {
    /*下面是请假的提交流程的数据*/
    // 1.持久化form表单数据，8级以下员工表单状态为processing，8级（领导）状态为approved

    // 2.增加第一条流程数据，说明表单已提交，状态为completed，增加一条数据

    // 3.分情况创建其余流程数据
    // 3.1 7级以下人员，生成辅导员审批任务，请假时间大于36小时，还需要生成领导审批任务。

    // 3.2 7级人员，生成辅导员审批任务。

    // 3，3 8级人员，生成领导审批任务，系统会自动通过。
    @Resource
    private EmployeeMapper employeeMapper;
    @Resource
    private LeaveFormMapper leaveFormMapper;
    @Resource
    private ProcessFlowMapper processFlowMapper;
    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public LeaveForm createLeaveForm(LeaveForm form) {
        Employee employee = employeeMapper.selectByPrimaryKey(form.getEmployeeId());
        if (employee.getLevel() == 8) {
            form.setState("approved");
        } else {
            form.setState("processing");
        }
        int i = leaveFormMapper.insertSelective(form);
        if (i == 0) {
            throw new BussinessException(3001, "数据插入失败");
        }
        /*插入了一条数据，但是我们获取到的form.getFormId为null*/
        /*
         * 因此在插入了数据库之后我们要查询出刚刚查出的数据
         * */

//        LeaveForm leaveForm = leaveFormMapper.selectByAllParams(form.getEmployeeId(), form.getFormType(), form.getStartTime(), form.getEndTime(), form.getReason(), form.getCreateTime(), form.getState());
        List<LeaveForm> leaveFormList = leaveFormMapper.selectByEmployeeId(form.getEmployeeId());
        int size = leaveFormList.size();
        LeaveForm leaveForm = leaveFormList.get(size - 1);
        //2.增加第一条流程数据,说明表单已提交,状态为complete
        ProcessFlow processFlow = new ProcessFlow();
        processFlow.setFormId(leaveForm.getFormId());
        processFlow.setOperatorId(employee.getEmployeeId());
        processFlow.setAction("apply");
        processFlow.setCreateTime(new Date());
        processFlow.setOrderNo(1);
        processFlow.setState("complete");
        processFlow.setIsLast(0);
        int insert = processFlowMapper.insertSelective(processFlow);
        if (insert == 0) {
            throw new BussinessException(3001, "数据插入失败");
        }
        //3.分情况创建其余流程数据
        //3.1 7级以下员工,生成部门经理审批任务,请假时间大于72小时,还需生成总经理审批任务
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
        if (employee.getLevel() < 7) {
            Employee instructor = employeeMapper.selectLeader(employee);
            ProcessFlow processFlow1 = new ProcessFlow();
            processFlow1.setFormId(leaveForm.getFormId());
            processFlow1.setOperatorId(instructor.getEmployeeId());
            processFlow1.setAction("audit");
            processFlow1.setCreateTime(new Date());
            processFlow1.setOrderNo(2);
            processFlow1.setState("process");
            long diff = leaveForm.getEndTime().getTime() - leaveForm.getStartTime().getTime();
            float hours = diff / (1000 * 60 * 60) * 1f;
            if (hours >= Constant.MANAGER_AUDIT_HOURS) {
                processFlow1.setIsLast(0);
                processFlowMapper.insert(processFlow1);
                Employee leader = employeeMapper.selectLeader(instructor);
                ProcessFlow processFlow2 = new ProcessFlow();
                processFlow2.setFormId(leaveForm.getFormId());
                processFlow2.setOperatorId(leader.getEmployeeId());
                processFlow2.setAction("audit");
                processFlow2.setCreateTime(new Date());
                processFlow2.setState("ready");
                processFlow2.setOrderNo(3);
                processFlow2.setIsLast(1);
                processFlowMapper.insert(processFlow2);
            } else {
                processFlow1.setIsLast(1);
                processFlowMapper.insert(processFlow1);
            }
            //请假单已提交消息
            String noticeContent = String.format("您的请假申请[%s-%s]已提交,请等待上级审批."
                    , sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
            noticeMapper.insert(new Notice(employee.getEmployeeId(), noticeContent, new Date()));

            //通知部门经理审批消息
            noticeContent = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",
                    employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));

            noticeMapper.insert(new Notice(instructor.getEmployeeId(), noticeContent, new Date()));
        } else if (employee.getLevel() == 7) { //部门经理
            //3.2 7级员工,生成总经理审批任务
            Employee manager = employeeMapper.selectLeader(employee);
            ProcessFlow flow = new ProcessFlow();
            flow.setFormId(leaveForm.getFormId());
            flow.setOperatorId(manager.getEmployeeId());
            flow.setAction("audit");
            flow.setCreateTime(new Date());
            flow.setState("process");
            flow.setOrderNo(2);
            flow.setIsLast(1);
            processFlowMapper.insert(flow);
            //请假单已提交消息
            String noticeContent = String.format("您的请假申请[%s-%s]已提交,请等待上级审批."
                    , sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
            noticeMapper.insert(new Notice(employee.getEmployeeId(), noticeContent, new Date()));

            //通知总经理审批消息
            noticeContent = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",
                    employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
            noticeMapper.insert(new Notice(manager.getEmployeeId(), noticeContent, new Date()));

        } else if (employee.getLevel() == 8) {
            //3.3 8级员工,生成总经理审批任务,系统自动通过
            ProcessFlow flow = new ProcessFlow();
            flow.setFormId(leaveForm.getFormId());
            flow.setOperatorId(employee.getEmployeeId());
            flow.setAction("audit");
            flow.setResult("approved");
            flow.setReason("自动通过");
            flow.setCreateTime(new Date());
            flow.setAuditTime(new Date());
            flow.setState("complete");
            flow.setOrderNo(2);
            flow.setIsLast(1);
            processFlowMapper.insert(flow);
            String noticeContent = String.format("您的请假申请[%s-%s]系统已自动批准通过.",
                    sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
            noticeMapper.insert(new Notice(employee.getEmployeeId(), noticeContent, new Date()));
        }
        return leaveForm;
    }

    @Override
    public List<Map> getLeaveFormList(String pfState, Long operatorId) {
        List<Map> mapList = leaveFormMapper.selectByParams(pfState, operatorId);
        return mapList;
    }

    @Override
    public void audit(Long formId, Long operatorId, String result, String reason) {
        /*
         * 审核请假单
         * formId 表单编号
         * operatorId 经办人
         * result 审批结果
         * reason 审批意见
         * */
        //1.无论同意/驳回,当前任务状态变更为complete
        List<ProcessFlow> processFlows = processFlowMapper.selectByFormId(formId);
        if (processFlows.size() == 0) {
            throw new BussinessException(8001, "无效的审批流程");
        }
        //获取当前任务ProcessFlow对象
        List<ProcessFlow> processList = processFlows.stream().filter(p -> p.getOperatorId().equals(operatorId) && p.getState().equals("process")).collect(Collectors.toList());
        ProcessFlow processFlow = null;
        if (processList.size() == 0) {
            throw new BussinessException(8002, "未找到待处理任务");
        } else {
            processFlow = processList.get(0);
            processFlow.setState("complete");
            processFlow.setResult(result);
            processFlow.setReason(reason);
            processFlow.setAuditTime(new Date());
            processFlowMapper.updateByPrimaryKeySelective(processFlow);
        }
        LeaveForm leaveForm = leaveFormMapper.selectByPrimaryKey(formId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
        //表单提交人信息
        Employee employee = employeeMapper.selectByPrimaryKey(leaveForm.getEmployeeId());
        //任务经办人信息
        Employee operator = employeeMapper.selectByPrimaryKey(operatorId);
        //2.如果当前任务是最后一个节点,代表流程结束,更新请假单状态为对应的approved/refused
        if (processFlow.getIsLast() == 1) {
            leaveForm.setState(result);
            leaveFormMapper.updateByPrimaryKeySelective(leaveForm);
            String strResult = null;
            if (result.equals("approved")) {
                strResult = "批准";
            } else if (result.equals("refused")) {
                strResult = "驳回";
            }
            String noticeContent = String.format("您的请假申请[%s-%s]%s%s已%s,审批意见:%s,审批流程已结束",
                    sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()),
                    operator.getTitle(), operator.getName(),
                    strResult, reason);//发给表单提交人的通知
            noticeMapper.insertSelective(new Notice(leaveForm.getEmployeeId(), noticeContent, new Date()));

            noticeContent = String.format("%s-%s提起请假申请[%s-%s]您已%s,审批意见:%s,审批流程已结束",
                    employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()),
                    strResult, reason);//发给审批人的通知
            noticeMapper.insertSelective(new Notice(operator.getEmployeeId(), noticeContent, new Date()));
        } else {
            //3.如果当前任务不是最后一个节点且审批通过,那下一个节点的状态从ready变为process
            //readyList包含所有后续任务节点
            List<ProcessFlow> readyList = processFlows.stream().filter(p -> p.getState().equals("ready")).collect(Collectors.toList());
            //3.如果当前任务不是最后一个节点且审批通过,那下一个节点的状态从ready变为process
            if (readyList.size() == 0) {
                throw new BussinessException(8002, "未找到待处理任务");
            }
            if (result.equals("approved")) {
                ProcessFlow readyProcess = readyList.get(0);
                readyProcess.setState("process");
                processFlowMapper.updateByPrimaryKeySelective(readyProcess);
                //消息1: 通知表单提交人,部门经理已经审批通过,交由上级继续审批
                String noticeContent1 = String.format("您的请假申请[%s-%s]%s%s已批准,审批意见:%s ,请继续等待上级审批",
                        sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()),
                        operator.getTitle(), operator.getName(), reason);
                noticeMapper.insertSelective(new Notice(leaveForm.getEmployeeId(), noticeContent1, new Date()));

                //消息2: 通知总经理有新的审批任务
                String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",
                        employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
                noticeMapper.insertSelective(new Notice(readyProcess.getOperatorId(), noticeContent2, new Date()));

                //消息3: 通知部门经理(当前经办人),员工的申请单你已批准,交由上级继续审批
                String noticeContent3 = String.format("%s-%s提起请假申请[%s-%s]您已批准,审批意见:%s,申请转至上级领导继续审批",
                        employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()), reason);
                noticeMapper.insertSelective(new Notice(operator.getEmployeeId(), noticeContent3, new Date()));
            } else if (result.equals("refused")) {
                //4.如果当前任务不是最后一个节点且审批驳回,则后续所有任务状态变为cancel,请假单状态变为refused
                for (ProcessFlow p : readyList) {
                    p.setState("cancel");
                    processFlowMapper.updateByPrimaryKeySelective(p);
                }
                leaveForm.setState("refused");
                leaveFormMapper.updateByPrimaryKeySelective(leaveForm);
                //消息1: 通知申请人表单已被驳回
                String noticeContent1 = String.format("您的请假申请[%s-%s]%s%s已驳回,审批意见:%s,审批流程已结束",
                        sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()),
                        operator.getTitle(), operator.getName(), reason);
                noticeMapper.insertSelective(new Notice(leaveForm.getEmployeeId(), noticeContent1, new Date()));

                //消息2: 通知经办人表单"您已驳回"
                String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s]您已驳回,审批意见:%s,审批流程已结束",
                        employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()), reason);
                noticeMapper.insertSelective(new Notice(operator.getEmployeeId(), noticeContent2, new Date()));
            }
        }
    }
}
