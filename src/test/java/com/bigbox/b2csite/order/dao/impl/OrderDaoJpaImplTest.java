package com.bigbox.b2csite.order.dao.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bigbox.b2csite.order.model.entity.OrderEntity;

public class OrderDaoJpaImplTest extends BaseDBUnitTestForJPADao {
	
	private OrderDaoJpaImpl target = null;
	private DefaultDataSet dataSet = null;
	
	@BeforeEach
	public void setup() throws Exception {
		
		this.target = new OrderDaoJpaImpl();
		this.target.setEntityManager(this.entityManager);
		
		// Date Setup
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
	
	@Test
	public void test_findById_success() throws Exception {
		
		OrderEntity result = this.target.findById(1L);
		
		assertNotNull(result, "result should not be null");
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
