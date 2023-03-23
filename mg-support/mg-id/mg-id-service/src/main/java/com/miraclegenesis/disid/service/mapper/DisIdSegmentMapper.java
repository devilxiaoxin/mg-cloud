package com.miraclegenesis.disid.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miraclegenesis.disid.service.model.entity.IdSegment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author robert
 */
public interface DisIdSegmentMapper extends BaseMapper<IdSegment> {


    /**
     * 自增增加号段
     * @param id 号段id
     * @return
     */
    @Update("update id_segment set auto_increment = auto_increment + #{segmentCount} where id = #{id} ")
    Boolean updateDisIdSegmentAutoIncrement(@Param("id") Long id, @Param("segmentCount") Long segmentCount);
}
