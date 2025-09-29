package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.neusoft.dto.AddDeptDto;
import com.neusoft.dto.UpdateDeptmentsDto;
import com.neusoft.mapper.DepartmentsMapper;
import com.neusoft.mapper.EmployeesMapper;
import com.neusoft.mapper.JobsMapper;
import com.neusoft.model.Departments;
import com.neusoft.model.Employees;
import com.neusoft.model.Jobs;
import com.neusoft.service.IDeptService;
import com.neusoft.vo.DeptEmployeeVo;
import com.neusoft.vo.DeptVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeptServiceImpl implements IDeptService {

    @Resource
    DepartmentsMapper departmentsMapper;
    @Resource
    EmployeesMapper employeesMapper;
    @Resource
    JobsMapper jobsMapper;
    @Override
    public DeptVo getDeptment(Integer id) {
        QueryWrapper<Departments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Departments departments = departmentsMapper.selectOne(queryWrapper);

        if (departments == null) {
            return null;
        }

        DeptVo deptVo = new DeptVo();
        //基础信息
        deptVo.setDeptId(id);
        deptVo.setDeptName(departments.getDeptName());
        deptVo.setCreateTime(departments.getCreatedAt());

        // 直接使用get方法获取值，确保不为null
        Integer parentDept = departments.getFatherDept();
//        System.out.println(parentDept);
        deptVo.setParentDept(parentDept); // 这里应该显示5而不是null

        deptVo.setDeptQuota(departments.getDeptQuota());
        deptVo.setStatus(departments.getStatus());

        //部门负责人
        deptVo.setDeptLeaderId(departments.getLeaderId());

        if (departments.getLeaderId() != null) {
            QueryWrapper<Employees> queryEmployeesWrapper = new QueryWrapper<>();
            queryEmployeesWrapper.eq("id", departments.getLeaderId());
            Employees employees = employeesMapper.selectOne(queryEmployeesWrapper);

            if (employees != null) {
                deptVo.setDeptLeaderName(employees.getName());
            } else {
                deptVo.setDeptLeaderName("");
            }
        } else {
            deptVo.setDeptLeaderName("");
        }

        //部门人数
        QueryWrapper<Employees> queryEmployeesWrapper2 = new QueryWrapper<>();
        queryEmployeesWrapper2.eq("dept_id", id);
        Integer employee_count = employeesMapper.selectCount(queryEmployeesWrapper2);
        deptVo.setEmployeeCount(employee_count != null ? employee_count : 0);

        //员工信息
        List<DeptEmployeeVo> deptEmployeeVoList = new ArrayList<>();
        List<Employees> employeesList = employeesMapper.selectList(queryEmployeesWrapper2);

        if (employeesList != null) {
            for (Employees employee : employeesList) {
                if (employee == null) {
                    continue;
                }

                DeptEmployeeVo deptEmployeeVo = new DeptEmployeeVo();
                deptEmployeeVo.setId(employee.getId());
                deptEmployeeVo.setName(employee.getName());

                if (employee.getJobId() != null) {
                    QueryWrapper<Jobs> queryJobsWrapper = new QueryWrapper<>();
                    queryJobsWrapper.eq("id", employee.getJobId());
                    Jobs jobs = jobsMapper.selectOne(queryJobsWrapper);
                    if (jobs != null) {
                        deptEmployeeVo.setJob(jobs.getJobName());
                        deptEmployeeVo.setJob_level(jobs.getJobLevel());
                    } else {
                        deptEmployeeVo.setJob("");

                    }
                } else {
                    deptEmployeeVo.setJob("");

                }

                deptEmployeeVo.setGender(employee.getGender());

                // 计算年龄
                Date birthdayDate = employee.getBirthday();
                int age = 0;
                if (birthdayDate != null) {
                    try {
                        LocalDate birthday = birthdayDate.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        LocalDate today = LocalDate.now();
                        age = Period.between(birthday, today).getYears();
                    } catch (Exception e) {
                        age = 0;
                    }
                }
                deptEmployeeVo.setAge(age);

                deptEmployeeVoList.add(deptEmployeeVo);
            }
        }

        deptVo.setDeptEmployeeVoList(deptEmployeeVoList);
        return deptVo;
    }

    @Override
    public List<DeptVo> getAllDeptment() {
        List<Departments> departmentsList = departmentsMapper.selectList(null);
        List<DeptVo> deptVoList = new ArrayList<>();
        for (Departments department: departmentsList){
            DeptVo deptVo = getDeptment(department.getId());
            deptVoList.add(deptVo);
        }

        return deptVoList;
    }

    @Override
    @Transactional  // 添加事务注解，确保数据一致性
    public boolean addDeptment(AddDeptDto addDeptDto) {

        // 1. 参数合法性校验
        if (addDeptDto == null) {
            throw new IllegalArgumentException("部门信息不能为空");
        }

        Integer deptId = addDeptDto.getDeptId();
        String deptName = addDeptDto.getDeptName();
        Integer leaderId = addDeptDto.getLeaderId();

        if (deptId == null || deptId <= 0) {
            throw new IllegalArgumentException("部门ID必须为正整数");
        }
        if (StringUtils.isBlank(deptName)) {
            throw new IllegalArgumentException("部门名称不能为空");
        }
        if (leaderId == null || leaderId <= 0) {
            throw new IllegalArgumentException("部门负责人ID必须为正整数");
        }

        // 2. 校验部门ID是否已存在
        QueryWrapper<Departments> idQuery = new QueryWrapper<>();
        idQuery.eq("id", deptId);
        int idCount = departmentsMapper.selectCount(idQuery);
        if (idCount > 0) {
            throw new RuntimeException("部门编号已存在");
        }

        isExistDeptName(deptName);

        // 4. 校验上级部门是否存在
        String fatherDeptName = addDeptDto.getFatherDeptName();
        Integer fatherDeptId = null;
        if (StringUtils.isNotBlank(fatherDeptName) && !"无".equals(fatherDeptName)) {
            fatherDeptId = getFatherLeaderId(fatherDeptName);
        }

        // 5. 校验负责人是否存在且为有效员工
        Employees leader = employeesMapper.selectById(leaderId);
        if (leader == null) {
            throw new RuntimeException("负责人不存在");
        }

        // 6. 检查该员工是否已经是其他部门的领导
        isLeader(leaderId);

        // 7. 构建部门实体
        Departments department = new Departments();
        department.setId(deptId);
        department.setDeptName(deptName);
        department.setLeaderId(leaderId);
        department.setDeptQuota(addDeptDto.getDeptSize());
        department.setFatherDept(fatherDeptId);
        department.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        department.setStatus("活动");

        // 8. 执行插入操作
        int insertResult = departmentsMapper.insert(department);
        if (insertResult <= 0) {
            throw new RuntimeException("部门创建失败");
        }

        // 9. 将负责人的部门ID更新为新部门ID
        Employees updateEmp = new Employees();
        updateEmp.setId(leaderId);
        updateEmp.setDeptId(deptId);
        int updateResult = employeesMapper.updateById(updateEmp);
        if (updateResult <= 0) {
            throw new RuntimeException("部门创建成功，但负责人部门关联失败");
        }

        return true;
    }

    private Integer getFatherLeaderId(String fatherDeptName) {
        Integer fatherDeptId;
        QueryWrapper<Departments> fatherQuery = new QueryWrapper<>();
        fatherQuery.eq("dept_name", fatherDeptName);
        Departments fatherDept = departmentsMapper.selectOne(fatherQuery);
        if (fatherDept == null) {
            throw new RuntimeException("上级部门不存在");
        }
        fatherDeptId = fatherDept.getId();
        return fatherDeptId;
    }

    private void isLeader(Integer leaderId) {
        QueryWrapper<Departments> leaderQuery = new QueryWrapper<>();
        leaderQuery.eq("leader_id", leaderId);
        int leaderCount = departmentsMapper.selectCount(leaderQuery);
        if (leaderCount > 0) {
            throw new RuntimeException("该员工已担任其他部门的负责人，不能同时担任本部门负责人");
        }
    }

    private void isExistDeptName(String deptName) {
        // 3. 校验部门名称是否已存在
        QueryWrapper<Departments> nameQuery = new QueryWrapper<>();
        nameQuery.eq("dept_name", deptName);
        int nameCount = departmentsMapper.selectCount(nameQuery);
        if (nameCount > 0) {
            throw new RuntimeException("部门名称已存在");
        }
    }

    @Override
    @Transactional  // 添加事务注解，确保数据一致性
    public boolean updateDeptments(UpdateDeptmentsDto updateDeptmentsDto, Integer id) {
        String deptName = updateDeptmentsDto.getDeptName();
        int leaderId = updateDeptmentsDto.getLeaderId();

        //检验部门名称不能为空，不能存在
        if (StringUtils.isBlank(deptName)) {
            throw new IllegalArgumentException("部门名称不能为空");
        }
        isExistDeptName(deptName);

        //  校验
        Employees leader = employeesMapper.selectById(leaderId);
        if (leader == null) {
            throw new RuntimeException("负责人不存在");
        }
        isLeader(leaderId);

        QueryWrapper<Departments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Departments departments = departmentsMapper.selectOne(queryWrapper);
        departments.setDeptName(updateDeptmentsDto.getDeptName());
        departments.setLeaderId(updateDeptmentsDto.getLeaderId());
        departments.setDeptQuota(updateDeptmentsDto.getDeptSize());
        int fatherLeaderId =getFatherLeaderId(updateDeptmentsDto.getFatherDeptName());
        departments.setFatherDept(fatherLeaderId);
        departmentsMapper.updateById(departments);

        // 9. 将负责人的部门ID更新为新部门ID
        Employees updateEmp = new Employees();
        updateEmp.setId(leaderId);
        updateEmp.setDeptId(departments.getId());
        int updateResult = employeesMapper.updateById(updateEmp);
        if (updateResult <= 0) {
            throw new RuntimeException("部门创建成功，但负责人部门关联失败");
        }
        return true;
    }

    @Override
    public boolean updateDeptStatus(Integer id) {
        QueryWrapper<Departments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Departments departments = departmentsMapper.selectOne(queryWrapper);
        if(departments.getStatus().equals("活动")){
            departments.setStatus("撤销");
        }
        else {
            departments.setStatus("活动");
        }
        int r= departmentsMapper.updateById(departments);
        if(r <= 0){
            return false;
        }
        return true;

    }


}
