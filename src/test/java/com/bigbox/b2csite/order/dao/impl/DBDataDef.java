package com.bigbox.b2csite.order.dao.impl;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.datatype.DataType;

public class DBDataDef {

	public final static Column[] ORDER_SOURCE_ENTITY_COLUMNS = new Column[] {
		new Column("id", DataType.TINYINT),
		new Column("code", DataType.VARCHAR),
		new Column("description", DataType.VARCHAR),
		new Column("lastModifiedBy", DataType.VARCHAR),
		new Column("lastModifiedOn", DataType.TIMESTAMP)
	};
	
	public final static Column[] ORDER_ENTITY_COLUMNS = new Column[] {
		new Column("id", DataType.BIGINT),
		new Column("orderLabel", DataType.VARCHAR),
		new Column("orderNumber", DataType.VARCHAR),
		new Column("billingAddressId", DataType.BIGINT),
		new Column("completionDate", DataType.TIMESTAMP),
		new Column("customerId", DataType.BIGINT),
		new Column("fulfillmentDate", DataType.TIMESTAMP),
		new Column("shippingAddressId", DataType.TIMESTAMP),
		new Column("orderSourceEntity_id", DataType.TINYINT)
	};
}
