package org.wlcp.wlcpapi.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.wlcp.wlcpapi.helper.HelperMethods;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "archiveEntityManagerFactory", transactionManagerRef = "archiveTransactionManager", basePackages = {
		"org.wlcp.wlcpapi.archive.repository" })
@EnableTransactionManagement
@Profile("!test")
public class ArchiveDataSource {

    @Bean
    @ConfigurationProperties(prefix="spring.archive-datasource")
    public DataSource dataSourceArchive() {
        return DataSourceBuilder.create().build();
    }
	
	Properties additionalProperties() {
	    Properties properties = new Properties();
	    properties.setProperty("hibernate.hbm2ddl.auto", "none");
	    properties.setProperty("hibernate.event.merge.entity_copy_observer", "allow");
	    properties.setProperty("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
	    properties.setProperty("hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
	    return properties;
	}
	
	@Bean
    public LocalContainerEntityManagerFactoryBean archiveEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
          = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSourceArchive());
        em.setPackagesToScan(new String[] { "org.wlcp.wlcpapi" });
        em.setJpaProperties(additionalProperties());
        
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        return em;
    }
	
	@Bean
	public PlatformTransactionManager archiveTransactionManager() {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(archiveEntityManagerFactory().getObject());

	    return transactionManager;
	}
    
    @Bean
    @ConfigurationProperties(prefix = "spring.archive-liquibase")
    public LiquibaseProperties archiveLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase archiveLiquibase() {
        return HelperMethods.springLiquibase(dataSourceArchive(), archiveLiquibaseProperties());
    }
    
}
