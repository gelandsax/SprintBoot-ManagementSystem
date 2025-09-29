package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.neusoft.dto.AddEmployeeDto;
import com.neusoft.dto.ModifyDeptDto;
import com.neusoft.dto.ModifyJobDto;
import com.neusoft.dto.UpdateEmployeeDto;
import com.neusoft.mapper.*;
import com.neusoft.model.*;
import com.neusoft.service.IProfileService;
import com.neusoft.vo.DepartmentHistory;
import com.neusoft.vo.JobHistory;
import com.neusoft.vo.Profile;
import com.neusoft.vo.ProfileVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProfileServiceImpl implements IProfileService {
    @Resource
    EmployeesMapper employeesMapper;
    @Resource
    UsersMapper usersMapper;
    @Resource
    DepartmentsMapper departmentsMapper;
    @Resource
    JobsMapper jobsMapper;
    @Resource
    RolesPermissionMapper rolesPermissionMapper;
    @Resource
    UserRolesMapper userRolesMapper;
    @Resource
    employee_historyMapper employee_historyMapper;
    @Resource
    RolesMapper rolesMapper;
    @Resource
    EmployeeShiftsMapper employeeShiftsMapper;
    @Resource
    WorkShiftsMapper workShiftsMapper;

    @Override
    public List<WorkShifts> getAllWorkShift() {
        List<WorkShifts> workShiftsList = workShiftsMapper.selectList(null);
        return workShiftsList;
    }

    @Override
    public List<Departments> getAllDept() {
        List<Departments> departmentsList = departmentsMapper.selectList(null);
        return departmentsList;
    }

    @Override
    public List<Jobs> getAllJob() {
        List<Jobs> jobsList = jobsMapper.selectList(null);
        return jobsList;
    }

    @Override
    public Profile getProfileByUserId(String username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Users user = usersMapper.selectOne(queryWrapper);
        int employeeId = user.getEmployeeId();

        Employees employees;
        QueryWrapper<Employees> wrapper = new QueryWrapper<>();
        wrapper.eq("id", employeeId);
        employees = employeesMapper.selectOne(wrapper);
        Profile profile = new Profile();
        profile.setId(employeeId);
        profile.setSalary(employees.getSalary());
        profile.setUser_name(user.getUsername());
        profile.setEmp_name(employees.getName());
//        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
//        usersQueryWrapper.eq("employee_id", employeeId);
//        Users user=usersMapper.selectOne(usersQueryWrapper);
        profile.setEmail(user.getEmail());
        profile.setGender(employees.getGender());
        profile.setBirthday(employees.getBirthday());
        profile.setMaritalStatus(employees.getMaritalStatus());
        profile.setFirstWorkDate(employees.getFirstWorkDate());
        profile.setHireDate(employees.getHireDate());
        profile.setStatus(employees.getStatus());
        //获取用户角色
        getUserRoles(employeeId, profile);

        getDepart(employees, profile);

        getJob(employees, profile);

        //获得职位变动历史
        List<JobHistory> jobHistory = getJobHistories(employeeId);
        profile.setJobHistory(jobHistory);

        //获得部门变动历史
        List<DepartmentHistory> deptHistory = getDepartmentHistories(employeeId);
        profile.setDepartmentHistory(deptHistory);
        return profile;
    }

    private void getUserRoles(int employeeId, Profile profile) {
        QueryWrapper<UserRoles> queryUserRolesWrapper = new QueryWrapper<>();
        queryUserRolesWrapper.eq("user_id", employeeId);
        UserRoles userRoles = userRolesMapper.selectOne(queryUserRolesWrapper);
        QueryWrapper<Roles> queryRolesWrapper = new QueryWrapper<>();
        queryRolesWrapper.eq("id",userRoles.getRoleId());
        Roles roles = rolesMapper.selectOne(queryRolesWrapper);
        profile.setPermission(roles.getRoleName());
    }

    private List<DepartmentHistory> getDepartmentHistories(int employeeId) {
        List<EmployeeHistory> deptHistoryList= employee_historyMapper.getDeptHistoryByEmpId(employeeId);
        List<DepartmentHistory> deptHistory = new ArrayList<>();
        QueryWrapper<Employees> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", employeeId);
        Employees employees = employeesMapper.selectOne(queryWrapper);
        for(EmployeeHistory employeeHistory:deptHistoryList){
            DepartmentHistory deptHistory1 = new DepartmentHistory();
            deptHistory1.setUsername(employees.getName());
            deptHistory1.setOld_department(employeeHistory.getOldValue());
            deptHistory1.setStart_date(employeeHistory.getChangeDate());
            deptHistory1.setEnd_date(employeeHistory.getEndTime());
            deptHistory.add(deptHistory1);
        }
        return deptHistory;
    }

    private List<JobHistory> getJobHistories(int employeeId) {
        QueryWrapper<Employees> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", employeeId);
        Employees employees = employeesMapper.selectOne(queryWrapper);
        List<EmployeeHistory> jobHistoryList= employee_historyMapper.getJobsHistoryByEmpId(employeeId);
        List<JobHistory> jobHistory = new ArrayList<>();
        for(EmployeeHistory employeeHistory:jobHistoryList){
            JobHistory jobHistory1 = new JobHistory();
            jobHistory1.setUsername(employees.getName());
            jobHistory1.setOld_job(employeeHistory.getOldValue());
            jobHistory1.setStart_date(employeeHistory.getChangeDate());
            jobHistory1.setEnd_date(employeeHistory.getEndTime());
            jobHistory.add(jobHistory1);
        }
        return jobHistory;
    }

    private void getJob(Employees employees, Profile profile) {
        QueryWrapper<Jobs> jobsQueryWrapper = new QueryWrapper<>();
        int job_id= employees.getJobId();
        jobsQueryWrapper.eq("id", job_id);
        Jobs job=jobsMapper.selectOne(jobsQueryWrapper);
        profile.setJob(job.getJobName());
    }

    private void getDepart(Employees employees, Profile profile) {
        QueryWrapper<Departments> departmentsQueryWrapper = new QueryWrapper<>();
        int dept_id= employees.getDeptId();
        departmentsQueryWrapper.eq("id", dept_id);
        Departments departments=departmentsMapper.selectOne(departmentsQueryWrapper);
        profile.setDepartment(departments.getDeptName());
    }

    @Override
    public List<ProfileVo> getAllProfiles() {
        List<Employees> employeesList =  employeesMapper.selectList(null);
        List<ProfileVo> profileVoList = new ArrayList<>();
       //  遍历员工列表，进行对象转换
        for (Employees employee : employeesList) {
            Profile profile = new Profile();
            QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("employee_id",employee.getId());
            Users user = usersMapper.selectOne(queryWrapper);
           profile.setUser_name(user.getUsername());
            profile.setEmp_name(employee.getName());
            profile.setId(employee.getId());
            profile.setEmail(user.getEmail());
            profile.setGender(employee.getGender());
            profile.setBirthday(employee.getBirthday());
            profile.setMaritalStatus(employee.getMaritalStatus());
            profile.setFirstWorkDate(employee.getFirstWorkDate());
            profile.setHireDate(employee.getHireDate());
            profile.setSalary(employee.getSalary());
            profile.setStatus(employee.getStatus());
            getUserRoles(employee.getId(),profile);
            getDepart(employee, profile);
            getJob(employee, profile);
            //获得职位变动历史
            List<JobHistory> jobHistory = getJobHistories(employee.getId());
            profile.setJobHistory(jobHistory);

            //获得部门变动历史
            List<DepartmentHistory> deptHistory = getDepartmentHistories(employee.getId());
            profile.setDepartmentHistory(deptHistory);

            // 创建ProfileVo并关联Profile
            ProfileVo profileVo = new ProfileVo();
            profileVo.setProfile(profile);


            profileVoList.add(profileVo);
        }

        return profileVoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addEmployee(AddEmployeeDto addEmployeeDto) {
        if (addEmployeeDto == null || addEmployeeDto.getEmployee() == null || addEmployeeDto.getUser() == null) {
            throw new IllegalArgumentException("员工信息或用户信息不能为空");
        }
        // 1. 获取DTO中的员工和用户信息
        com.neusoft.dto.Employee employeeDto = addEmployeeDto.getEmployee();
        com.neusoft.dto.UserLogin userLoginDto = addEmployeeDto.getUser();
        // 2. 基础数据校验
        // 校验用户名是否存在
        if (isUsernameExists(userLoginDto.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        // 校验邮箱是否存在
        if (isEmailExists(userLoginDto.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        // 校验入职日期是否有效
        if(!isEntryDateValid(employeeDto.getHireDate(),employeeDto.getFirstWorkDate())){
            throw new RuntimeException("入职日期不能早于初次参加工作日期");
        }

        Employees employee = new Employees();
        employee.setName(employeeDto.getName());
        employee.setGender(employeeDto.getGender());
        employee.setBirthday(employeeDto.getBirthday());
        employee.setHireDate(employeeDto.getHireDate());
        employee.setFirstWorkDate(new java.sql.Timestamp(employeeDto.getFirstWorkDate().getTime()));
        employee.setSalary(employeeDto.getSalary());
        employee.setMaritalStatus(employeeDto.getMaritalStatus());
        employee.setDeptId(employeeDto.getDeptId());
        employee.setJobId(employeeDto.getJobId());
        employee.setStatus("active");
        // 4. 保存员工信息
        int empInsertResult = employeesMapper.insert(employee);
        if (empInsertResult <= 0) {
            return false;
        }

        // 5. 关联员工ID到用户表
        Users user = new Users();
        user.setUsername(userLoginDto.getUsername());
        user.setPassword(userLoginDto.getPassword());
        user.setEmail(userLoginDto.getEmail());
        user.setEmployeeId(employee.getId());
        int userInsertResult = usersMapper.insert(user);
        if (userInsertResult <= 0) {
            return false;
        }
        //6. 关联员工角色
        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(user.getId());
        QueryWrapper<Roles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name",employeeDto.getPermisson());
        Roles roles = rolesMapper.selectOne(queryWrapper);
        userRoles.setRoleId(roles.getId());
        int userRolesInsertResult = userRolesMapper.insert(userRoles);
        if(userRolesInsertResult <=0 ){
            return false;
        }
        //班次
        EmployeeShifts employeeShifts = new EmployeeShifts();
        employeeShifts.setEmployeeId(employee.getId());
        QueryWrapper<WorkShifts> queryWorkShiftsWrapper = new QueryWrapper<>();
        queryWorkShiftsWrapper.eq("shift_name",employeeDto.getWorkShifts());
        WorkShifts workShifts = workShiftsMapper.selectOne(queryWorkShiftsWrapper);
        employeeShifts.setShiftId(workShifts.getId());
        employeeShifts.setEffectiveDate(new Date());
        int workShiftInsertResult = employeeShiftsMapper.insert(employeeShifts);
        if(workShiftInsertResult <=0 ){
            return false;
        }
        return true;
    }

    @Override
    public boolean isUsernameExists(String username) {

            QueryWrapper<Employees> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username.trim());
        return employeesMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isEmailExists(String email) {

        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email.trim());
        return usersMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isEntryDateValid(Date hireDate, Date firstWorkDate) {
        // 检查两个日期都不为null
        if (hireDate == null || firstWorkDate == null) {
            return false;
        }
        // 验证雇佣日期不早于首次工作日期
        // 如果hireDate在firstWorkDate之前，则返回false
        return !hireDate.before(firstWorkDate);
    }

    @Override
    public boolean updateEmployee(UpdateEmployeeDto updateEmployeeDto, Integer id) {

        if (updateEmployeeDto.getLoginName() == null || updateEmployeeDto.getLoginName().isEmpty()) {
            throw new IllegalArgumentException("登录名不能为空");
        }

        QueryWrapper<Employees> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Employees employees = employeesMapper.selectOne(queryWrapper);
        employees.setName(updateEmployeeDto.getEmployeeName());
        employees.setGender(updateEmployeeDto.getGender());
        employees.setBirthday(updateEmployeeDto.getBirthday());
        employees.setHireDate(updateEmployeeDto.getHiredDate());
        employees.setSalary(updateEmployeeDto.getSalary());
        employeesMapper.updateById(employees);

        QueryWrapper<Users> queryUsersWrapper = new QueryWrapper<>();
        queryUsersWrapper.eq("employee_id",employees.getId());
        Users user = usersMapper.selectOne(queryUsersWrapper);
        user.setUsername(updateEmployeeDto.getLoginName());
        usersMapper.updateById(user);

        QueryWrapper<Roles> queryRolesWrapper = new QueryWrapper<>();
        queryRolesWrapper.eq("role_name",updateEmployeeDto.getPermission());
        Roles roles = rolesMapper.selectOne(queryRolesWrapper);
        QueryWrapper<UserRoles> queryUserRolesWrapper = new QueryWrapper<>();
        queryUserRolesWrapper.eq("user_id",user.getId());
        UserRoles userRoles = userRolesMapper.selectOne(queryUserRolesWrapper);
        UpdateWrapper<UserRoles> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", user.getId()); // 假设 userId 是要更新的用户 ID
        userRoles.setRoleId(roles.getId());
        userRolesMapper.update(userRoles, updateWrapper);
        return true;
    }

    @Override
    public boolean statusUpdate(Integer id) {
        QueryWrapper<Employees> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Employees employees = employeesMapper.selectOne(queryWrapper);
        if(employees.getStatus().equals("在职")){
            employees.setStatus("离职");
        }
        else {
            employees.setStatus("在职");
        }
        int r= employeesMapper.updateById(employees);
        if(r <= 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyDept(ModifyDeptDto modifyDeptDto, Integer id) {
        QueryWrapper<Employees> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Employees employees = employeesMapper.selectOne(queryWrapper);
        int dept_id = employees.getDeptId();//old
        QueryWrapper<Departments> queryDepartmentsWrapper = new QueryWrapper<>();
        queryDepartmentsWrapper.eq("id",dept_id);
        Departments departments = departmentsMapper.selectOne(queryDepartmentsWrapper);
        String dept_name = departments.getDeptName();//old
        QueryWrapper<Departments> queryDepartmentsWrapper2 = new QueryWrapper<>();
        queryDepartmentsWrapper2.eq("id",modifyDeptDto.getNewDepartmentId());
        Departments departments2 = departmentsMapper.selectOne(queryDepartmentsWrapper2);//new
        if(departments2 == null){
            throw new RuntimeException("新部门不存在，无法调转部门");
        }
        //添加新记录
        EmployeeHistory employeeHistory = new EmployeeHistory();
        if(dept_id != modifyDeptDto.getNewDepartmentId()){
            employees.setDeptId(modifyDeptDto.getNewDepartmentId());
            employeesMapper.updateById(employees);

            employeeHistory.setEmployeeId(id);
            employeeHistory.setChangeType("dept");
            employeeHistory.setOldValue(dept_name);
            employeeHistory.setNewValue(departments2.getDeptName());
            employeeHistory.setChangeDate(new Timestamp(System.currentTimeMillis()));
            employee_historyMapper.insert(employeeHistory);
        }
        else {
            throw new RuntimeException("部门未发生变化，无法调转部门");
        }

        // 更新上一条部门变更记录的end_time
        // 查询该员工最近的一条部门变更记录（未设置end_time的）
        QueryWrapper<EmployeeHistory> historyQuery = new QueryWrapper<>();
        historyQuery.eq("employee_id", id)
                .eq("change_type", "dept")
                .isNull("end_time") // 假设未结束的记录end_time为null
                .orderByDesc("change_date") // 按时间倒序
                .last("LIMIT 1"); // 只取最近的一条

        EmployeeHistory lastHistory = employee_historyMapper.selectOne(historyQuery);
        if (lastHistory != null) {
            lastHistory.setEndTime(employeeHistory.getChangeDate()); // 用当前时间作为上一条记录的结束时间
            employee_historyMapper.updateById(lastHistory);
        }

        return true;
    }

    @Override
    public boolean modifyJob(ModifyJobDto modifyJobDto, Integer id) {

        QueryWrapper<Employees> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Employees employees = employeesMapper.selectOne(queryWrapper);
        int job_id = employees.getJobId();//old
        QueryWrapper<Jobs> queryJobsWrapper = new QueryWrapper<>();
        queryJobsWrapper.eq("id",job_id);
        Jobs jobs = jobsMapper.selectOne(queryJobsWrapper);
        String job_name = jobs.getJobName();//old
        QueryWrapper<Jobs> queryJobsWrapper2 = new QueryWrapper<>();
        queryJobsWrapper2.eq("id",modifyJobDto.getNewJobId());
        Jobs jobs2 = jobsMapper.selectOne(queryJobsWrapper2);//new
        if(jobs2 == null){
            throw new RuntimeException("新职位不存在，无法调转");
        }
        //添加新记录
        EmployeeHistory employeeHistory = new EmployeeHistory();
        if(job_id != modifyJobDto.getNewJobId()){
            employees.setJobId(modifyJobDto.getNewJobId());
            employeesMapper.updateById(employees);

            employeeHistory.setEmployeeId(id);
            employeeHistory.setChangeType("job");
            employeeHistory.setOldValue(job_name);
            employeeHistory.setNewValue(jobs2.getJobName());
            employeeHistory.setChangeDate(new Timestamp(System.currentTimeMillis()));
            employee_historyMapper.insert(employeeHistory);
        }
        else {
            throw new RuntimeException("部门未发生变化，无法调转部门");
        }

        // 更新上一条部门变更记录的end_time
        // 查询该员工最近的一条部门变更记录（未设置end_time的）
        QueryWrapper<EmployeeHistory> historyQuery = new QueryWrapper<>();
        historyQuery.eq("employee_id", id)
                .eq("change_type", "job")
                .isNull("end_time") // 假设未结束的记录end_time为null
                .orderByDesc("change_date") // 按时间倒序
                .last("LIMIT 1"); // 只取最近的一条

        EmployeeHistory lastHistory = employee_historyMapper.selectOne(historyQuery);
        if (lastHistory != null) {
            lastHistory.setEndTime(employeeHistory.getChangeDate()); // 用当前时间作为上一条记录的结束时间
            employee_historyMapper.updateById(lastHistory);
        }

        return true;
    }


}
