package com.example.mapper;

import com.example.entity.Collects;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CollectsMapper {
    @Select("SELECT COUNT(*) FROM collects WHERE fid = #{fid} AND module = #{module}")
    Integer selectCollectsNum(Integer fid, String module);

    @Select("SELECT * FROM collects WHERE fid = #{fid} AND module = #{module} and user_id = #{userId}")
    Collects selectCollects(Collects collects);

    @Insert("insert into collects(fid, module, user_id) values(#{fid}, #{module}, #{userId})")
    void insertCollects(Collects collects);

    @Delete("delete from collects where fid = #{fid} and module = #{module} and user_id = #{userId}")
    void deleteCollects(Collects collectsdb);


}
