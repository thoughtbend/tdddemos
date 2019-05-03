package com.wiredbrain.order.dao.impl;

import java.io.FileReader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.h2.Driver;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseDBUnitTestForJPADao {

	protected static EntityManagerFactory EMF = null;
	protected static IDatabaseConnection CONN = null;
	protected EntityManager entityManager = null;
	
	@BeforeAll
	public static void setupTestClass() throws Exception {
		
		Properties dbProps = new Properties();
		dbProps.put("user", DBInfo.USER);
		dbProps.put("password", DBInfo.PASSWORD);
		
		Connection jdbcConn = Driver.load().connect(DBInfo.URL, dbProps);
		
		CONN = new DatabaseConnection(jdbcConn);
		
		CONN.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
		
		RunScript.execute(CONN.getConnection(), new FileReader("tabledef/b2csite.ddl.sql"));
		
		final Map<Object, Object> props = new HashMap<>();
		props.put("javax.persistence.jdbc.url", DBInfo.URL);
		//props.put("hibernate.hbm2ddl.auto", "create-drop");
		EMF = Persistence
				.createEntityManagerFactory("orderPersistenceUnit", props);
		
	}
	
	@AfterAll
	public static void teardownTestClass() throws Exception {
		
		try {
			if (EMF != null) {
				if (EMF.isOpen()) {
					EMF.close();
				}
				EMF = null;
			}
		}
		finally {
			if (CONN != null) {
				CONN.close();
				CONN = null;
			}
		}
	}
	
	@BeforeEach
	public void baseSetup() throws Exception {
		this.entityManager = EMF.createEntityManager();
	}
	
	@AfterEach
	public void baseTeardown() throws Exception {
		
		if (entityManager != null) {
			if (entityManager.isOpen()) {
				entityManager.close();
			}
			entityManager = null;
		}
	}
}
