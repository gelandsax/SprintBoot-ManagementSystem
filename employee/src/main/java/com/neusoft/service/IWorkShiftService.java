package com.neusoft.service;

import com.neusoft.dto.WorkShiftDto;
import com.neusoft.vo.WorkShiftVo;

import java.util.List;

public interface IWorkShiftService {
    List<WorkShiftVo> getAllShift();
    boolean addShift(WorkShiftDto workShiftDto);
    WorkShiftVo getShift(Integer id);
    boolean updateShift(WorkShiftDto workShiftDto, Integer id);
    boolean deleteShift(Integer id);
}
