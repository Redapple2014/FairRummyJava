package com.skillengine.db.config;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.skillengine.dao.mapper.TableDetailsMapper;
import com.skillengine.dao.mapper.UserStatMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource
{
	static SqlSessionFactory sessionFactory;
	static
	{
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl( "jdbc:postgresql://localhost:5433/gamedb" );
		config.setUsername( "gameuser" );
		config.setPassword( "tester" );
		config.setDriverClassName( "org.postgresql.Driver" );
		config.setMaximumPoolSize( 10 );
		config.setMinimumIdle( 2 );
		config.setIdleTimeout( 30000 );
		config.setConnectionTimeout( 30000 );
		config.setPoolName( "GameEnginePool" );
		HikariDataSource dataSource = new HikariDataSource( config );
		Environment environment = new Environment( "dev", new JdbcTransactionFactory(), dataSource );
		Configuration configuration = new Configuration( environment );
		configuration.setMapUnderscoreToCamelCase( true );
		configuration.addMapper( TableDetailsMapper.class );
		configuration.addMapper( UserStatMapper.class );
		sessionFactory = new SqlSessionFactoryBuilder().build( configuration );
	}

	public static SqlSessionFactory getSqlSessionFactory()
	{
		return sessionFactory;
	}
}
