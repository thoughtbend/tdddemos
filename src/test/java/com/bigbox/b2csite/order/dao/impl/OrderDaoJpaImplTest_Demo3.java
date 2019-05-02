package com.bigbox.b2csite.order.dao.impl;

import java.io.FileReader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.Column.Nullable;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.h2.Driver;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.bigbox.b2csite.order.model.entity.OrderEntity;
import com.bigbox.b2csite.order.model.entity.OrderItemEntity;

public class OrderDaoJpaImplTest_Demo3 extends BaseDBUnitTestForJPADao {
	
	private final static class DataFiles {
		private final static String XML_DATA_SET = 
				"com/bigbox/b2csite/order/dao/impl/OrderDaoJpaImplTest_XMLDataSet.xml";
		private final static String FLAT_XML_DATA_SET = 
				"com/bigbox/b2csite/order/dao/impl/OrderDaoJpaImplTest_FlatXMLDataSet.xml";
		private final static String XLS_DATA_SET = 
				"com/bigbox/b2csite/order/dao/impl/OrderDaoJpaImplTest_XlsDataSet.xls";
	}
	
	private OrderDaoJpaImpl target = null;
	
	IDataSet dataSet = null;
	
	@Before
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
	
	@After
	public void teardown() throws Exception {
		DatabaseOperation.DELETE.execute(CONN, dataSet);
	}
	
	@Test
	public void test_insert() throws Exception {
		
		EntityTransaction transaction = this.entityManager.getTransaction();
		transaction.begin();
		
		OrderEntity orderEntityFixture = this.entityManager.find(OrderEntity.class, Long.valueOf(1));
		
		OrderItemEntity newOrderItem = new OrderItemEntity();
		newOrderItem.setOwningOrder(orderEntityFixture);
		newOrderItem.setQuantity(2);
		newOrderItem.setSellingPrice(new BigDecimal("10.00"));
		newOrderItem.setSku("Item1SKU");
		
		this.entityManager.persist(newOrderItem);
		transaction.commit();
		
		QueryDataSet queryDataSet = new QueryDataSet(CONN);
		String queryString = "select * from OrderItemEntity where owningOrder_Id = 1";
		queryDataSet.addTable("OrderItemEntity", queryString);
		
		DatabaseOperation.REFRESH.execute(CONN, queryDataSet);
		
		ITable orderItemTable = queryDataSet.getTable("OrderItemEntity");
		Assert.assertEquals(1, orderItemTable.getRowCount());
		assertOrderItemTable(newOrderItem, orderItemTable, 0);
		
		DatabaseOperation.DELETE.execute(CONN, queryDataSet);
	}
	
	private void assertOrderItemTable(OrderItemEntity orderItemEntity, ITable orderItemTable, int row) throws DataSetException {
		
		Assert.assertEquals(String.valueOf(orderItemEntity.getId()), orderItemTable.getValue(row, "id").toString());
		Assert.assertEquals(String.valueOf(orderItemEntity.getOwningOrder().getId()), orderItemTable.getValue(row, "owningOrder_id").toString());
		Assert.assertEquals(Integer.valueOf(orderItemEntity.getQuantity()), orderItemTable.getValue(row, "quantity"));
		Assert.assertEquals(orderItemEntity.getSellingPrice(), (BigDecimal) orderItemTable.getValue(row, "sellingPrice"));
		Assert.assertEquals(orderItemEntity.getSku(), orderItemTable.getValue(row, "sku"));
	}
}
