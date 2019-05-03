package com.bigbox.b2csite.order.dao.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bigbox.b2csite.order.model.entity.OrderEntity;

public class OrderDaoJpaImplTest_Demo1 extends BaseDBUnitTestForJPADao {

	private OrderDaoJpaImpl target = null;
	
	private DefaultDataSet dataSet = null;
	
	@Before
	public void setup() throws Exception {
		
		target = new OrderDaoJpaImpl();
		target.setEntityManager(entityManager);
		
		dataSet = new DefaultDataSet();
		
		DefaultTable orderSourceEntityTable = 
				new DefaultTable("OrderSourceEntity", DBDataDef.ORDER_SOURCE_ENTITY_COLUMNS);
		Object[][] orderSourceRows = createOrderSourceRows();
		for (Object[] currentOrderSourceRow : orderSourceRows) {
			orderSourceEntityTable.addRow(currentOrderSourceRow);
		}
		dataSet.addTable(orderSourceEntityTable);
		
		DefaultTable orderEntityTable = 
				new DefaultTable("OrderEntity", DBDataDef.ORDER_ENTITY_COLUMNS);
		Object[][] orderRows = createOrderRowData();
		for (Object[] currentOrderRow : orderRows) {
			orderEntityTable.addRow(currentOrderRow);
		}
		dataSet.addTable(orderEntityTable);
		
		DatabaseOperation.INSERT.execute(CONN, dataSet);
	}
	
	@After
	public void teardown() throws Exception {
		DatabaseOperation.DELETE.execute(CONN, dataSet);
	}
	
	@Test
	public void test_findByOrderSource() throws Exception {
		
		// Setup
		
		
		// Execution
		List<OrderEntity> orderList = this.target.findByOrderSource("wo");
		
		// Verification
		Assert.assertNotNull(orderList);
		Assert.assertEquals(1, orderList.size());
		Assert.assertEquals("ORD1", orderList.get(0).getOrderNumber());
	}
	
	private Object[][] createOrderSourceRows() {
		
		Object[][] orderSourceRows = new Object[][] {
				new Object[] {
					1,
					"so",
					"Store Order",
					"cbrown",
					Date.from(LocalDateTime.now().withYear(2012).withMonth(12).withDayOfMonth(31).toInstant(ZoneOffset.UTC))
				},
				new Object[] {
					2,
					"wo",
					"Web Order",
					"lvanpelt",
					Date.from(LocalDateTime.now().withYear(2012).withMonth(12).withDayOfMonth(31).toInstant(ZoneOffset.UTC))
				},
				new Object[] {
					3,
					"un",
					null,
					"lvanpelt",
					Date.from(LocalDateTime.now().withYear(2013).withMonth(1).withDayOfMonth(1).toInstant(ZoneOffset.UTC))
				}
			};
		return orderSourceRows;
	}
	
	private Object[][] createOrderRowData() {
		
		Object[][] orderRows = new Object[][] {
				new Object[] {
					1,
					"Customer 1 Order 1",
					"ORD1",
					1,
					new Date(LocalDate.now().withYear(2013).withMonth(12).withDayOfMonth(23).toEpochDay()),
					250000,
					null,
					1,
					2	// Reference the web order
				},
				new Object[] {
					2,
					"Customer 1 Order 2",
					"ORD2",
					1,
					new Date(LocalDate.now().withYear(2013).withMonth(12).withDayOfMonth(23).toEpochDay()),
					250000,
					new Date(LocalDate.now().withYear(2013).withMonth(12).withDayOfMonth(26).toEpochDay()),
					1,
					1	// References the store order
				}
			};
		return orderRows;
	}
}
