package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.dto.VacationProcessDto;
import com.neusoft.dto.VacationReq;
import com.neusoft.mapper.EmployeeVocationBalanceMapper;
import com.neusoft.mapper.EmployeesMapper;
import com.neusoft.mapper.VacationRequestsMapper;
import com.neusoft.mapper.VacationTypesMapper;
import com.neusoft.model.*;
import com.neusoft.service.IvocationService;
import com.neusoft.utils.UserUtils;
import com.neusoft.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class vacationServiceImpl implements IvocationService {
    @Resource
    EmployeesMapper employeesMapper;
    @Resource
    EmployeeVocationBalanceMapper employeeVocationBalanceMapper;
    @Resource
    VacationTypesMapper vacationTypesMapper;
    @Resource
    VacationRequestsMapper vacationRequestsMapper;
    @Override
    public void refreshAnnualAndFixedVacations() {
        List<Employees> employees = employeesMapper.findAll();
        int year = LocalDate.now().getYear();

        // 获取所有假期类型
        List<VacationTypes> types = vacationTypesMapper.findAll();

        for (Employees e : employees) {
            LocalDate firstWorkDate = e.getFirstWorkDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            for (VacationTypes type : types) {
                int totalDays = 0;

                // 判断是否是年假
                if (type.getId() == 1) {
                    totalDays = calculateAnnualLeave(firstWorkDate);
                } else {
                    // 其他类型不自动刷新
                    continue;
                }

                // 查询是否已存在记录
                QueryWrapper<EmployeeVacationBalance> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("employee_id", e.getId()).eq("year", year).eq("type", type.getTypeName());
                EmployeeVacationBalance existing = employeeVocationBalanceMapper.selectOne(queryWrapper);

                if (existing != null) {
                    // 更新（重置已用天数）
                    existing.setTotalDays(totalDays);
                    existing.setRemainingDays(totalDays);
                    existing.setUsedDays(0);
                    employeeVocationBalanceMapper.updateById(existing);
                } else {
                    // 插入新记录
                    EmployeeVacationBalance newBalance = new EmployeeVacationBalance();
                    newBalance.setEmployeeId(e.getId());
                    newBalance.setTypeId(type.getId());
                    newBalance.setYear(year);
                    newBalance.setTotalDays(totalDays);
                    newBalance.setRemainingDays(totalDays);
                    newBalance.setUsedDays(0);
                    employeeVocationBalanceMapper.insert(newBalance);
                }
            }
        }
    }

    // 按工龄计算年假
    @Override
    public int calculateAnnualLeave(LocalDate firstWorkDate) {
        int years = LocalDate.now().getYear() - firstWorkDate.getYear();
        if (years < 1) return 0;
        if (years <= 10) return 5;
        if (years <= 20) return 10;
        return 15;
    }

    @Override
    public VacationCountVo getVocationCount(){
        Users loginUser = new UserUtils().getLoginUser();
        int employeeId = loginUser.getEmployeeId();
        int year = LocalDate.now().getYear();

        // 查询员工当年假期余额
        List<EmployeeVacationBalance> balances = employeeVocationBalanceMapper.selectList(
                new QueryWrapper<EmployeeVacationBalance>()
                        .eq("employee_id", employeeId)
                        .eq("year", year)
        );

        // 一次性查出所有假期类型
        List<VacationTypes> types = vacationTypesMapper.selectList(null);
        Map<Integer, VacationTypes> typeMap = types.stream()
                .collect(Collectors.toMap(VacationTypes::getId, t -> t));

        List<Vacationinfo> vacationInfos = new ArrayList<>();
        for (EmployeeVacationBalance balance : balances) {
            VacationTypes type = typeMap.get(balance.getTypeId());
            if (type == null) continue;

            Vacationinfo info = new Vacationinfo();
            info.setEVBid(balance.getId());
            info.setTypename(type.getTypeName());
            info.setLimit(type.getLimited());
            info.setRemaindays(balance.getRemainingDays());

            vacationInfos.add(info);
        }

        VacationCountVo vo = new VacationCountVo();
        vo.setVacationinfo(vacationInfos);
        return vo;
    }

    @Override
    public VacationTypeListVo getVacationType(){
        List<VacationTypes> types = vacationTypesMapper.selectList(null);
        List<VacationTypeVo> vacationTypeVos = new ArrayList<>();
        for (VacationTypes type : types) {
            VacationTypeVo vo = new VacationTypeVo();
            vo.setId(type.getId());
            vo.setVacationType(type.getTypeName());
            String LimitType;
            if(type.getLimited()){
                LimitType = "true";
            }else{
                LimitType = "false";
            }
            vo.setLimitType(LimitType);
            vacationTypeVos.add(vo);
        }
        VacationTypeListVo vo = new VacationTypeListVo();
        vo.setVacationList(vacationTypeVos);
        return vo;
    }

    @Override
    public int vacationRequest(VacationReq vacationReq) {
        VacationRequests requests = new VacationRequests();
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        requests.setEmployeeId(loginUser.getEmployeeId());
        QueryWrapper<VacationTypes> vacationTypesQueryWrapper = new QueryWrapper<>();
        vacationTypesQueryWrapper.eq("type_name",vacationReq.getVocationType());
        requests.setTypeId(vacationTypesMapper.selectOne(vacationTypesQueryWrapper).getId());
        requests.setStatus("未审批");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 根据你的日期字符串格式调整

        requests.setStartDate(LocalDate.parse(vacationReq.getStartDate(), formatter));
        requests.setEndDate(LocalDate.parse(vacationReq.getEndDate(), formatter));
        requests.setTotalDays(vacationReq.getDays());
        requests.setReason(vacationReq.getReason());
        return vacationRequestsMapper.insert(requests);
    }

    @Override
    public MyVacationRequestListVo getMyVacationRequestList() {
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        int employeeId = loginUser.getEmployeeId();
        QueryWrapper<VacationRequests> vacationRequestsQueryWrapper = new QueryWrapper<>();
        vacationRequestsQueryWrapper.eq("employee_id", employeeId);
        List<VacationRequests> list = vacationRequestsMapper.selectList(vacationRequestsQueryWrapper);
        List<VacationReqVo> myVacationRequestList = new ArrayList<>();
        for(VacationRequests vacationRequests : list){
            VacationReqVo vacationReqVo = new VacationReqVo();
            vacationReqVo.setId(vacationRequests.getId());
            vacationReqVo.setUsername(loginUser.getUsername());
            QueryWrapper<VacationTypes> vacationTypesQueryWrapper = new QueryWrapper<>();
            vacationTypesQueryWrapper.eq("id", vacationRequests.getTypeId());
            vacationReqVo.setVocationType(vacationTypesMapper.selectOne(vacationTypesQueryWrapper).getTypeName());
            vacationReqVo.setDays(vacationRequests.getTotalDays());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            vacationReqVo.setStartDate(vacationRequests.getStartDate());
            vacationReqVo.setEndDate(vacationRequests.getEndDate());
            vacationReqVo.setReason(vacationRequests.getReason());
            vacationReqVo.setStatus(vacationRequests.getStatus());
            myVacationRequestList.add(vacationReqVo);
        }
        MyVacationRequestListVo myVacationRequestListVo = new MyVacationRequestListVo();
        myVacationRequestListVo.setVocationReqList(myVacationRequestList);
        return myVacationRequestListVo;
    }

    public MyVacationRequestListVo getAllVacationRequestList(){
        // 查询所有请假记录
        List<VacationRequests> list = vacationRequestsMapper.selectList(null);

        List<VacationReqVo> vacationRequestList = new ArrayList<>();
        for (VacationRequests vacationRequests : list) {
            VacationReqVo vacationReqVo = new VacationReqVo();

            // 获取对应员工信息
            Employees employee = employeesMapper.selectById(vacationRequests.getEmployeeId());
            vacationReqVo.setUsername(employee.getName());

            // 获取请假类型
            QueryWrapper<VacationTypes> vacationTypesQueryWrapper = new QueryWrapper<>();
            vacationTypesQueryWrapper.eq("id", vacationRequests.getTypeId());
            VacationTypes vacationType = vacationTypesMapper.selectOne(vacationTypesQueryWrapper);
            vacationReqVo.setVocationType(vacationType.getTypeName());

            vacationReqVo.setId(vacationRequests.getId());
            vacationReqVo.setDays(vacationRequests.getTotalDays());

            // 日期直接使用 LocalDate
            vacationReqVo.setStartDate(vacationRequests.getStartDate());
            vacationReqVo.setEndDate(vacationRequests.getEndDate());

            vacationReqVo.setReason(vacationRequests.getReason());
            vacationReqVo.setStatus(vacationRequests.getStatus());

            vacationRequestList.add(vacationReqVo);
        }

        MyVacationRequestListVo allVacationRequestListVo = new MyVacationRequestListVo();
        allVacationRequestListVo.setVocationReqList(vacationRequestList);
        return allVacationRequestListVo;
    }

    @Override
    public VacationApproveListVo getVacationApproveList(){
        QueryWrapper<VacationRequests> vacationRequestsQueryWrapper = new QueryWrapper<>();
        vacationRequestsQueryWrapper.eq("status","未审批");
        List<VacationRequests> List = vacationRequestsMapper.selectList(vacationRequestsQueryWrapper);
        List<VacationReqVo> AllVacationRequestList = new ArrayList<>();
        for(VacationRequests vacationRequests : List){
            VacationReqVo vacationReqVo = new VacationReqVo();
            vacationReqVo.setId(vacationRequests.getId());
            QueryWrapper<Employees> employeesQueryWrapper = new QueryWrapper<>();
            employeesQueryWrapper.eq("id", vacationRequests.getEmployeeId());
            Employees employee = employeesMapper.selectOne(employeesQueryWrapper);
            vacationReqVo.setUsername(employee.getName());
            QueryWrapper<VacationTypes> vacationTypesQueryWrapper = new QueryWrapper<>();
            vacationTypesQueryWrapper.eq("id", vacationRequests.getTypeId());
            vacationReqVo.setVocationType(vacationTypesMapper.selectOne(vacationTypesQueryWrapper).getTypeName());
            vacationReqVo.setDays(vacationRequests.getTotalDays());
            vacationReqVo.setStartDate(vacationRequests.getStartDate());
            vacationReqVo.setEndDate(vacationRequests.getEndDate());
            vacationReqVo.setReason(vacationRequests.getReason());
            AllVacationRequestList.add(vacationReqVo);
        }
        VacationApproveListVo vacationApproveListVo = new VacationApproveListVo();
        vacationApproveListVo.setVocationReqList(AllVacationRequestList);
        return vacationApproveListVo;
    }

    @Override
    public int agreeVacation(VacationProcessDto vacationProcessDto){
        int reqID = vacationProcessDto.getVacationRequestIds().get(0);

        // 1. 查询请假申请
        VacationRequests vacationRequests = vacationRequestsMapper.selectOne(
                new QueryWrapper<VacationRequests>().eq("id", reqID)
        );
        if (vacationRequests == null) {
            throw new RuntimeException("请假申请不存在: " + reqID);
        }

        // 2. 更新请假申请状态 + 审批人信息
        vacationRequests.setStatus("同意");
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        vacationRequests.setApproverId(loginUser.getEmployeeId());
        vacationRequestsMapper.updateById(vacationRequests);

        // 3. 查找员工假期余额
        EmployeeVacationBalance employeeVacationBalance = employeeVocationBalanceMapper.selectOne(
                new QueryWrapper<EmployeeVacationBalance>()
                        .eq("employee_id", vacationRequests.getEmployeeId())
                        .eq("type_id", vacationRequests.getTypeId())
                        .eq("year", LocalDate.now().getYear())
        );
        if (employeeVacationBalance == null) {
            throw new RuntimeException("未找到该员工的假期余额记录");
        }

        // 4. 查找假期类型
        VacationTypes vacationTypes = vacationTypesMapper.selectOne(
                new QueryWrapper<VacationTypes>().eq("id", vacationRequests.getTypeId())
        );
        if (vacationTypes == null) {
            throw new RuntimeException("假期类型不存在: " + vacationRequests.getTypeId());
        }

        // 5. 如果是假期有限制的类型，扣减余额
        if (Boolean.TRUE.equals(vacationTypes.getLimited())) {
            int usedDays = employeeVacationBalance.getUsedDays() + vacationRequests.getTotalDays();
            int remainingDays = employeeVacationBalance.getTotalDays() - usedDays;

            if (remainingDays < 0) {
                throw new RuntimeException("剩余天数不足，无法审批通过");
            }

            employeeVacationBalance.setUsedDays(usedDays);
            employeeVacationBalance.setRemainingDays(remainingDays);
        }

        // 6. 更新余额
        return employeeVocationBalanceMapper.update(employeeVacationBalance,
                new QueryWrapper<EmployeeVacationBalance>()
                        .eq("employee_id", vacationRequests.getEmployeeId())
                        .eq("type_id", vacationRequests.getTypeId())
                        .eq("year", LocalDate.now().getYear()));
    }

    @Override
    public int rejectVacation(VacationProcessDto vacationProcessDto){
        int reqID = vacationProcessDto.getVacationRequestIds().get(0);

        // 1. 查询请假申请
        VacationRequests vacationRequests = vacationRequestsMapper.selectOne(
                new QueryWrapper<VacationRequests>().eq("id", reqID)
        );
        if (vacationRequests == null) {
            throw new RuntimeException("请假申请不存在: " + reqID);
        }

        // 2. 更新状态 + 审批人信息
        vacationRequests.setStatus("驳回");
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        vacationRequests.setApproverId(loginUser.getEmployeeId());

        // 3. 更新数据库
        return vacationRequestsMapper.updateById(vacationRequests);
    }
    @Override
    public int agreeAllVacations(VacationProcessDto vacationProcessDto){
        List<Integer> requestIds = vacationProcessDto.getVacationRequestIds();
        if (requestIds == null || requestIds.isEmpty()) {
            throw new RuntimeException("请假申请ID不能为空");
        }

        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();

        int updateCount = 0;

        for (Integer reqID : requestIds) {
            // 1. 查询请假申请
            VacationRequests vacationRequests = vacationRequestsMapper.selectOne(
                    new QueryWrapper<VacationRequests>().eq("id", reqID)
            );
            if (vacationRequests == null) {
                continue; // 找不到则跳过
            }

            // 2. 更新请假申请状态 + 审批人
            vacationRequests.setStatus("同意");
            vacationRequests.setApproverId(loginUser.getEmployeeId());
            vacationRequestsMapper.updateById(vacationRequests);

            // 3. 查找员工假期余额
            EmployeeVacationBalance employeeVacationBalance = employeeVocationBalanceMapper.selectOne(
                    new QueryWrapper<EmployeeVacationBalance>()
                            .eq("employee_id", vacationRequests.getEmployeeId())
                            .eq("type_id", vacationRequests.getTypeId())
                            .eq("year", LocalDate.now().getYear())
            );
            if (employeeVacationBalance == null) {
                throw new RuntimeException("未找到员工的假期余额记录: employeeId="
                        + vacationRequests.getEmployeeId());
            }

            // 4. 查找假期类型
            VacationTypes vacationTypes = vacationTypesMapper.selectOne(
                    new QueryWrapper<VacationTypes>().eq("id", vacationRequests.getTypeId())
            );
            if (vacationTypes == null) {
                throw new RuntimeException("假期类型不存在: " + vacationRequests.getTypeId());
            }

            // 5. 如果是假期有限制的类型，扣减余额
            if (Boolean.TRUE.equals(vacationTypes.getLimited())) {
                int usedDays = employeeVacationBalance.getUsedDays() + vacationRequests.getTotalDays();
                int remainingDays = employeeVacationBalance.getTotalDays() - usedDays;

                if (remainingDays < 0) {
                    throw new RuntimeException("剩余天数不足，无法审批通过，请假ID: " + reqID);
                }

                employeeVacationBalance.setUsedDays(usedDays);
                employeeVacationBalance.setRemainingDays(remainingDays);

                employeeVocationBalanceMapper.update(employeeVacationBalance,
                        new QueryWrapper<EmployeeVacationBalance>()
                                .eq("employee_id", vacationRequests.getEmployeeId())
                                .eq("type_id", vacationRequests.getTypeId())
                                .eq("year", LocalDate.now().getYear()));
            }

            updateCount++;
        }

        return updateCount; // 返回成功审批的申请数量
    }

    @Override
    public int rejectAllVacations(VacationProcessDto vacationProcessDto){
        List<Integer> requestIds = vacationProcessDto.getVacationRequestIds();
        if (requestIds == null || requestIds.isEmpty()) {
            throw new RuntimeException("请假申请ID不能为空");
        }

        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();

        int updateCount = 0;

        for (Integer reqID : requestIds) {
            // 1. 查询请假申请
            VacationRequests vacationRequests = vacationRequestsMapper.selectOne(
                    new QueryWrapper<VacationRequests>().eq("id", reqID)
            );
            if (vacationRequests == null) {
                continue; // 找不到则跳过
            }

            // 2. 更新状态 + 审批人 + 审批时间
            vacationRequests.setStatus("驳回");
            vacationRequests.setApproverId(loginUser.getEmployeeId());

            vacationRequestsMapper.updateById(vacationRequests);
            updateCount++;
        }

        return updateCount; // 返回成功驳回的申请数量
    }

    public int VacationConfig(VacationConfigVo vacationConfigVo){
        QueryWrapper<VacationRequests> qw = new QueryWrapper<>();
        qw.eq("id", vacationConfigVo.getId());
        if(vacationRequestsMapper.selectCount(qw)>0){
            throw new RuntimeException("还有未审批申请");
        }
        else{
            VacationTypes vacationTypes = vacationTypesMapper.selectById(vacationConfigVo.getId());
            if(vacationTypes==null){
                vacationTypes.setTypeName(vacationConfigVo.getVacationType());
                return vacationTypesMapper.updateById(vacationTypes);
            }
        }
        return 0;
    }

    public int deleteVacation(VacationConfigVo vacationConfigVo){
        QueryWrapper<VacationRequests> qw = new QueryWrapper<>();
        qw.eq("id", vacationConfigVo.getId());
        if(vacationRequestsMapper.selectCount(qw)>0){
            throw new RuntimeException("还有未审批申请");
        }
        else{
            VacationTypes vacationTypes = vacationTypesMapper.selectById(vacationConfigVo.getId());
            if(vacationTypes==null){
                return vacationTypesMapper.deleteById(vacationConfigVo.getId());
            }
        }
        return 0;
    }

}
