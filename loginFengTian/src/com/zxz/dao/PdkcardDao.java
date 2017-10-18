package com.zxz.dao;

import com.zxz.domain.Pdkcard;

public interface PdkcardDao {
    int deleteByPrimaryKey(Integer userid);

    int insert(Pdkcard record);

    int insertSelective(Pdkcard record);

    Pdkcard selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(Pdkcard record);

    int updateByPrimaryKey(Pdkcard record);
}