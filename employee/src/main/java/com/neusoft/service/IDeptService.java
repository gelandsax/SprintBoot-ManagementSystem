package com.neusoft.service;

import com.neusoft.dto.AddDeptDto;
import com.neusoft.dto.UpdateDeptmentsDto;
import com.neusoft.vo.DeptVo;

import java.util.List;


public interface IDeptService {
    DeptVo getDeptment(Integer id);
    List<DeptVo> getAllDeptment();
    boolean addDeptment(AddDeptDto addDeptDto);
    boolean updateDeptments(UpdateDeptmentsDto updateDeptmentsDto, Integer id);
    boolean updateDeptStatus(Integer id);
}
