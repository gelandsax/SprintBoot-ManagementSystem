package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.dto.WorkShiftDto;
import com.neusoft.mapper.EmployeeShiftsMapper;
import com.neusoft.mapper.WorkShiftsMapper;
import com.neusoft.model.EmployeeShifts;
import com.neusoft.model.WorkShifts;
import com.neusoft.service.IWorkShiftService;
import com.neusoft.vo.WorkShiftVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkShiftServiceImpl implements IWorkShiftService {
    @Resource
    WorkShiftsMapper workShiftsMapper;
    @Resource
    EmployeeShiftsMapper employeeShiftsMapper;
    @Override
    public List<WorkShiftVo> getAllShift() {
        List<WorkShifts> workShiftsList = workShiftsMapper.selectList(null);
        List<WorkShiftVo> workShiftVoList = new ArrayList<>();
        for (WorkShifts workShift: workShiftsList){
            WorkShiftVo workShiftVo = new WorkShiftVo();
            workShiftVo.setShiftName(workShift.getShiftName());
            workShiftVo.setStartTime(workShift.getStartTime());
            workShiftVo.setEndTime(workShift.getEndTime());
            workShiftVo.setId(workShift.getId());
            workShiftVoList.add(workShiftVo);

        }
        return workShiftVoList;
    }

    @Override
    public boolean addShift(WorkShiftDto workShiftDto) {
        WorkShifts workShifts = new WorkShifts();
        String name = workShiftDto.getShiftName();
        QueryWrapper<WorkShifts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shift_name",name);
        int count = workShiftsMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new RuntimeException("排班名称已经存在");
        }

        workShifts.setShiftName(workShiftDto.getShiftName());
        workShifts.setStartTime(workShiftDto.getStartTime());
        workShifts.setEndTime(workShiftDto.getEndTime());
        workShiftsMapper.insert(workShifts);
        return true;
    }

    @Override
    public WorkShiftVo getShift(Integer id) {
        QueryWrapper<WorkShifts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        WorkShifts workShifts = workShiftsMapper.selectOne(queryWrapper);
        WorkShiftVo workShiftVo = new WorkShiftVo();
        workShiftVo.setShiftName(workShifts.getShiftName());
        workShiftVo.setStartTime(workShifts.getStartTime());
        workShiftVo.setEndTime(workShifts.getEndTime());
        return workShiftVo;
    }

    @Override
    public boolean updateShift(WorkShiftDto workShiftDto, Integer id) {
        QueryWrapper<WorkShifts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        WorkShifts workShifts = workShiftsMapper.selectOne(queryWrapper);

        String name = workShiftDto.getShiftName();
        QueryWrapper<WorkShifts> queryWrapperName = new QueryWrapper<>();
        queryWrapperName.eq("shift_name",name);
        int count = workShiftsMapper.selectCount(queryWrapperName);
        if(count > 0){
            throw new RuntimeException("排班名称已经存在");
        }

        workShifts.setShiftName(workShiftDto.getShiftName());
        workShifts.setStartTime(workShiftDto.getStartTime());
        workShifts.setEndTime(workShiftDto.getEndTime());
        workShiftsMapper.updateById(workShifts);
        return true;
    }

    @Override
    public boolean deleteShift(Integer id) {
        //先判断是否可以删除，需要确保该类型下无在职员工使用
        QueryWrapper<EmployeeShifts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shift_id",id);
        int count = employeeShiftsMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new RuntimeException("该类型下有在职员工使用,无法删除");
        }
        //删除
        int result = workShiftsMapper.deleteById(id);

        if(result <= 0){
            return false;
        }
        return true;
    }
}
