package com.example.mvc.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class JPAConfigMockitoTest {

    private JPAConfig jpaConfig;

    @Mock
    private Environment environment;

    @BeforeEach
    void setUp() throws Exception {
        jpaConfig = new JPAConfig();

        // Use reflection to set the @Value fields
        setField(jpaConfig, "jdbcUrl", "jdbc:mysql://localhost:3306/testdb");
        setField(jpaConfig, "jdbcUsername", "root");
        setField(jpaConfig, "jdbcPassword", "password");
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testDataSourceBean() {
        DriverManagerDataSource ds = (DriverManagerDataSource) jpaConfig.dataSource();
        assertNotNull(ds);
        assertNotNull(ds.getUrl());
        assertNotNull(ds.getUsername());
        assertNotNull(ds.getPassword());

        System.out.println("URL: " + ds.getUrl());
        System.out.println("Username: " + ds.getUsername());
        System.out.println("Password: " + ds.getPassword());
    }

    @Test
    void testJpaVendorAdapterBean() {
        JpaVendorAdapter adapter = jpaConfig.jpaVendorAdapter();
        assertNotNull(adapter);
        assert(adapter instanceof HibernateJpaVendorAdapter);
    }

    @Test
    void testEntityManagerFactoryBean() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean emf = jpaConfig.entityManagerFactory(ds, adapter, environment);
        assertNotNull(emf);
        assertNotNull(emf.getJpaVendorAdapter());
    }

    @Test
    void testTransactionManagerBean() {
        EntityManagerFactory emfMock = mock(EntityManagerFactory.class);
        JpaTransactionManager txManager = (JpaTransactionManager) jpaConfig.transactionManager(emfMock);
        assertNotNull(txManager);
        assert(txManager.getEntityManagerFactory() == emfMock);
    }

    @Test
    void testExceptionTranslationBean() {
        PersistenceExceptionTranslationPostProcessor postProcessor = jpaConfig.exceptionTranslation();
        assertNotNull(postProcessor);
    }
}
