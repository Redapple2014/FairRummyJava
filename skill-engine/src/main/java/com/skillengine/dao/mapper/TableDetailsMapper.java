package com.skillengine.dao.mapper;

import com.skillengine.dao.model.TableDetails;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

public interface TableDetailsMapper {
    @Insert("Insert into table_details(template_id,status,start_time) values(#{templateId},#{status},now())")
    @Options(useGeneratedKeys = true, keyProperty = "tableId")
    void insertTableDetails(TableDetails tableDetails);

    @Update("update table_details set end_time = now(),status=4 where id = #{tableId}")
    void update(TableDetails details);
}
