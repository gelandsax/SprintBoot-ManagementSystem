package com.neusoft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.model.VacationTypes;

import java.util.List;

public interface VacationTypesMapper extends BaseMapper<VacationTypes> {
    List<VacationTypes> findAll();
}
