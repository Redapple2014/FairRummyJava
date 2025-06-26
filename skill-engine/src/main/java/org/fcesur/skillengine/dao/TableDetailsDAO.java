package org.fcesur.skillengine.dao;

import org.fcesur.skillengine.dao.mapper.TableDetailsMapper;
import org.fcesur.skillengine.dao.model.TableDetails;
import org.fcesur.skillengine.db.config.DataSource;
import org.apache.ibatis.session.SqlSession;

public class TableDetailsDAO {
    public Long insertTableDetails(TableDetails tableDetails) {
        try (SqlSession session = DataSource.getSqlSessionFactory().openSession(true)) {
            TableDetailsMapper mapper = session.getMapper(TableDetailsMapper.class);
            mapper.insertTableDetails(tableDetails);
            return tableDetails.getTableId();
        }
    }
}
