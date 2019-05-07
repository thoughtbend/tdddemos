package com.wiredbrain.order.dao.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.persistence.EntityTransaction;

import org.apache.commons.collections.CollectionUtils;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.XlsDataFileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wiredbrain.order.dao.impl.OrderDaoJpaImpl;
import com.wiredbrain.order.model.entity.OrderEntity;
import com.wiredbrain.order.model.entity.OrderItemEntity;

public class OrderDaoJpaImplTest_Demo3 extends BaseDBUnitTestForJPADao {
	
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
		
		final String dataFile = DataFiles.XLS_DATA_SET;
		
		// Add data set initialization
		InputStream is =
			ClassLoader.getSystemResourceAsStream(dataFile);
		
		switch (dataFile) {
		case DataFiles.XML_DATA_SET:
			dataSet = new XmlDataSet(is);
			break;
		case DataFiles.FLAT_XML_DATA_SET:
			FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
			dataSet = builder.build(is);
			break;
		case DataFiles.XLS_DATA_SET:
			dataSet = new XlsDataSet(is);
			break;
		}
		
		DatabaseOperation.INSERT.execute(CONN, dataSet);
	}
	
	@AfterEach
	public void teardown() throws Exception {
		DatabaseOperation.DELETE.execute(CONN, dataSet);
	}
	
	@Test
	public void test_insert() throws Exception {
		
		EntityTransaction transaction = this.entityManager.getTransaction();
		transaction.begin();
		
		OrderEntity orderEntityFixture = this.entityManager.find(OrderEntity.class, Long.valueOf(1));
		
		OrderItemEntity newOrderItem = new OrderItemEntity();
		newOrderItem.setId(101L);
		newOrderItem.setOwningOrder(orderEntityFixture);
		newOrderItem.setQuantity(2);
		newOrderItem.setSellingPrice(new BigDecimal("10.00"));
		newOrderItem.setPromotionCode("ABC");
		newOrderItem.setDiscount(new BigDecimal(".50"));
		newOrderItem.setTax(new BigDecimal(".25"));
		newOrderItem.setSku("Item1SKU");
		
		if (orderEntityFixture.getOrderItemList() == null) {
			orderEntityFixture.setOrderItemList(new ArrayList<>());
		}
		orderEntityFixture.getOrderItemList().add(newOrderItem);

		this.entityManager.merge(newOrderItem);
		this.entityManager.persist(orderEntityFixture);
		transaction.commit();
		
		QueryDataSet orderItemDataSet = new QueryDataSet(CONN);
		//orderItemDataSet.addTable("OrderEntity_OrderItemEntity", "select * from OrderEntity_OrderItemEntity");
		String queryString = "select * from OrderItemEntity where owningOrder_Id = 1";
		orderItemDataSet.addTable("OrderItemEntity", queryString);
		
		QueryDataSet orderItemRelDataSet = new QueryDataSet(CONN);
		orderItemRelDataSet.addTable("OrderEntity_OrderItemEntity", "select * from OrderEntity_OrderItemEntity where OrderEntity_id = 1");
		
		try {
			DatabaseOperation.REFRESH.execute(CONN, orderItemDataSet);
			DatabaseOperation.REFRESH.execute(CONN, orderItemRelDataSet);
			
			ITable orderItemTable = orderItemDataSet.getTable("OrderItemEntity");
			Assertions.assertEquals(1, orderItemTable.getRowCount());
			assertOrderItemTable(newOrderItem, orderItemTable, 0);
			
			ITable orderItemRelTable = orderItemRelDataSet.getTable("OrderEntity_OrderItemEntity");
			Assertions.assertEquals(1, orderItemRelTable.getRowCount());
		}
		finally {
			DatabaseOperation.DELETE.execute(CONN, orderItemRelDataSet);
			DatabaseOperation.DELETE.execute(CONN, orderItemDataSet);
		}
	}
	
	private void assertOrderItemTable(OrderItemEntity orderItemEntity, ITable orderItemTable, int row) throws DataSetException {
		
		Assertions.assertAll("",
			() -> Assertions.assertEquals(String.valueOf(orderItemEntity.getId()), orderItemTable.getValue(row, "id").toString()),
			() -> Assertions.assertEquals(String.valueOf(orderItemEntity.getOwningOrder().getId()), orderItemTable.getValue(row, "owningOrder_id").toString()),
			() -> Assertions.assertEquals(Integer.valueOf(orderItemEntity.getQuantity()), orderItemTable.getValue(row, "quantity")),
			() -> Assertions.assertEquals(orderItemEntity.getSellingPrice(), (BigDecimal) orderItemTable.getValue(row, "sellingPrice")),
			() -> Assertions.assertEquals(orderItemEntity.getDiscount(), (BigDecimal) orderItemTable.getValue(row, "discount")),
			() -> Assertions.assertEquals(orderItemEntity.getTax(), (BigDecimal) orderItemTable.getValue(row, "tax")),
			() -> Assertions.assertEquals(orderItemEntity.getPromotionCode(), orderItemTable.getValue(row, "promotionCode")),
			() -> Assertions.assertEquals(orderItemEntity.getSku(), orderItemTable.getValue(row, "sku"))
		);
	}
	
	private void assertOrderItemTableWithErrors(OrderItemEntity orderItemEntity, ITable orderItemTable, int row) throws DataSetException {
		
		Assertions.assertAll("",
			() -> Assertions.assertEquals(String.valueOf(orderItemEntity.getId()), orderItemTable.getValue(row, "id").toString()),
			() -> Assertions.assertEquals(String.valueOf(orderItemEntity.getOwningOrder().getId()), orderItemTable.getValue(row, "owningOrder_id").toString()),
			() -> Assertions.assertEquals(Integer.valueOf(orderItemEntity.getQuantity()), orderItemTable.getValue(row, "quantity")),
			() -> Assertions.assertEquals(/*orderItemEntity.getSellingPrice()*/new BigDecimal("0.00"), (BigDecimal) orderItemTable.getValue(row, "sellingPrice")),
			() -> Assertions.assertEquals(orderItemEntity.getDiscount(), (BigDecimal) orderItemTable.getValue(row, "discount")),
			() -> Assertions.assertEquals(orderItemEntity.getTax(), (BigDecimal) orderItemTable.getValue(row, "tax")),
			() -> Assertions.assertEquals(/*orderItemEntity.getPromotionCode()*/"XX", orderItemTable.getValue(row, "promotionCode")),
			() -> Assertions.assertEquals(orderItemEntity.getSku(), orderItemTable.getValue(row, "sku"))
		);
	}
}
