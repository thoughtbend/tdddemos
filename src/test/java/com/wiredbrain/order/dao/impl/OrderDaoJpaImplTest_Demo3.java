package com.wiredbrain.order.dao.impl;

import java.io.InputStream;
import java.math.BigDecimal;

import javax.persistence.EntityTransaction;

import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.wiredbrain.order.dao.impl.OrderDaoJpaImpl;
import com.wiredbrain.order.model.entity.OrderEntity;
import com.wiredbrain.order.model.entity.OrderItemEntity;

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
