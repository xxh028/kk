package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Bj;
import com.xiaoshu.entity.BjExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BjMapper extends BaseMapper<Bj> {
    long countByExample(BjExample example);

    int deleteByExample(BjExample example);

    List<Bj> selectByExample(BjExample example);

    int updateByExampleSelective(@Param("record") Bj record, @Param("example") BjExample example);

    int updateByExample(@Param("record") Bj record, @Param("example") BjExample example);
}