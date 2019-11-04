/**
 * 
 */
package com.che.config.db;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.InitBinder;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author karlhell
 *
 */
@Configuration
public class DataSourceConfig {
	@Value("${ds1.jdbc.driverClassName}")
	private String driverClass;
	@Value("${ds1.jdbc.url}")
	private String jdbcUrl;
	@Value("${ds1.jdbc.username}")
	private String username;
	@Value("${ds1.jdbc.password}")
	private String password;
	@Value("${druid.pool.initialSize}")
	private Integer initialSize;
	@Value("${druid.pool.minIdle}")
	private int minIdle;
	@Value("${druid.pool.maxActive}")
	private int maxActive;
	@Value("${druid.pool.maxWait}")
	private long maxWaitMillis;
	@Value("${druid.pool.timeBetweenEvictionRunsMillis}")
	private long timeBetweenEvictionRunsMillis;
	@Value("${druid.pool.minEvictableIdleTimeMillis}")
	private long minEvictableIdleTimeMillis;
	@Value("${druid.pool.validationQuery}")
	private String validationQuery;
	@Value("${druid.pool.testWhileIdle}")
	private boolean testWhileIdle;
	@Value("${druid.pool.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${druid.pool.testOnReturn}")
	private boolean testOnReturn;
	@Value("${druid.pool.poolPreparedStatements}")
	private boolean poolPreparedStatements;
	@Value("${druid.pool.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;
	
	@Bean
	@InitBinder
	public DruidDataSource dataSource(){
		DruidDataSource druidDs = new DruidDataSource();
		druidDs.setDriverClassName(driverClass);
		druidDs.setUrl(jdbcUrl);
		druidDs.setUsername(username);
		druidDs.setPassword(password);
		druidDs.setInitialSize(initialSize);
		druidDs.setMinIdle(minIdle);
		druidDs.setMaxActive(maxActive);
		druidDs.setMaxWait(maxWaitMillis);
		druidDs.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		druidDs.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		druidDs.setValidationQuery(validationQuery);
		druidDs.setTestWhileIdle(testWhileIdle);
		druidDs.setTestOnBorrow(testOnBorrow);
		druidDs.setTestOnReturn(testOnReturn);
		druidDs.setPoolPreparedStatements(poolPreparedStatements);
		druidDs.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		return druidDs;
	}
	
	@PostConstruct
	private void init() throws SQLException{
		dataSource().init();
	}
	
	@PreDestroy
	private void destroy(){
		dataSource().close();
	}
	
}
