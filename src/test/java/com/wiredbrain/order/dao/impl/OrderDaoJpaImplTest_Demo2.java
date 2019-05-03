package com.wiredbrain.order.dao.impl;

import java.io.InputStream;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wiredbrain.order.dao.impl.OrderDaoJpaImpl;
import com.wiredbrain.order.model.entity.OrderEntity;

public class OrderDaoJpaImplTest_Demo2 extends BaseDBUnitTestForJPADao {
	
	private final static class DataFiles {
		private final static String XML_DATA_SET = 
				"data-fixtures/OrderDaoJpaImplTest_XMLDataSet.xml";
		private final static String FLAT_XML_DATA_SET = 
				"data-fixtures/OrderDaoJpaImplTest_FlatXMLDataSet.xml";
		private final static String XLS_DATA_SET = 
				"data-fixtures/OrderDaoJpaImplTest_XlsDataSet.xls";
	}
	
	private OrderDaoJpaImpl target = null;
	
	IDataSet dataSet = null;
	
	@BeforeEach
	public void setup() throws Exception {
		
		target = new OrderDaoJpaImpl();
		target.setEntityManager(entityManager);
		
		// Add data set initialization
		InputStream is =
			ClassLoader.getSystemResourceAsStream(DataFiles.FLAT_XML_DATA_SET);
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		dataSet = builder.build(is);
		
		DatabaseOperation.INSERT.execute(CONN, dataSet);
	}
	
	@AfterEach
	public void teardown() throws Exception {
		DatabaseOperation.DELETE.execute(CONN, dataSet);
	}
	
	@Test
	public void test_findByOrderSource() throws Exception {
		
		List<OrderEntity> resultList = target.findByOrderSource("wo");
		
		Assertions.assertNotNull(resultList);
		Assertions.assertEquals(1, resultList.size());
		Assertions.assertEquals("ORD1", resultList.get(0).getOrderNumber());
	}
}
