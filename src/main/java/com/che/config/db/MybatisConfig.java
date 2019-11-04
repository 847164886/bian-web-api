package com.che.config.db;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.pagehelper.PageHelper;

/**
 * @author karlhell
 *
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.che.**.mapper")
public class MybatisConfig {
	
	@Autowired
	private DataSourceConfig dsConfig;

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
	    sessionFactory.setDataSource(dsConfig.dataSource());
	    
	    PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.put("dialect", "mysql");
        properties.put("reasonable", true);
        pageHelper.setProperties(properties);
        sessionFactory.setPlugins(new Interceptor[]{pageHelper});
	    
	    sessionFactory.getObject().getConfiguration().setLogImpl(sessionFactory.getObject().getConfiguration().getTypeAliasRegistry().resolveAlias("LOG4J2"));

	    return sessionFactory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
	    return new DataSourceTransactionManager(dsConfig.dataSource());
	}
	
}
