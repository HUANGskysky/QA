package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Huangsky on 2018/8/10.
 */
@Mapper
public interface  CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type, created_date, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME , "(", INSERT_FIELDS,
            ") values (#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);


   /* @Update({"update ",TABLE_NAME,"set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("status") int status,
                      @Param("entityId") int entityId,
                      @Param("entityType") int entityType); */
    @Update({"update ",TABLE_NAME,"set status=#{status} where id=#{id} "})
    int updateStatus(@Param("id") int id,
                       @Param("status") int status);

    @Select({"select",SELECT_FIELDS," from ",TABLE_NAME,
            "where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
    List<Comment> selectByEntity( @Param("entityId") int entityId,
                                  @Param("entityType") int entityType);

    @Select({"select count(id) from ",TABLE_NAME,"where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                        @Param("entityType") int entityType);



}
