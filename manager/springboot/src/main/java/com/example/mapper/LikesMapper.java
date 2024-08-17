package com.example.mapper;

import com.example.entity.Likes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikesMapper {
    @Select("SELECT COUNT(*) FROM likes WHERE fid = #{fid} AND module = #{module}")
    Integer selectLikesNum(Integer fid, String module);

    @Select("SELECT * FROM likes WHERE fid = #{fid} AND module = #{module} and user_id = #{userId}")
    Likes selectLikes(Likes likes);

    @Insert("insert into likes(fid, module, user_id) values(#{fid}, #{module}, #{userId})")
    void insertLikes(Likes likes);

    @Delete("delete from likes where fid = #{fid} and module = #{module} and user_id = #{userId}")
    void deleteLikes(Likes likesdb);

}
