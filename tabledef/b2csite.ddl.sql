/*
    alter table OrderEntity 
        drop constraint FKAC62CCB199C4EA3E;

    alter table OrderEntity_OrderItemEntity 
        drop constraint FK2748E316708713A3;

    alter table OrderEntity_OrderItemEntity 
        drop constraint FK2748E316BAE9F53E;

    alter table OrderItemEntity 
        drop constraint FKA76A42A450155C1D;

    drop table OrderEntity if exists;

    drop table OrderEntity_OrderItemEntity if exists;

    drop table OrderItemEntity if exists;

    drop table OrderSource if exists;
*/
    create table OrderEntity (
        id bigint generated by default as identity,
        orderLabel varchar(255),
        orderNumber varchar(255) not null,
        billingAddressId bigint,
        completionDate timestamp,
        customerId bigint,
        fulfillmentDate timestamp,
        shippingAddressId bigint,
        orderSourceEntity_id tinyint,
        primary key (id)
    );

    create table OrderEntity_OrderItemEntity (
        OrderEntity_id bigint not null,
        orderItemList_id bigint not null,
        primary key (orderItemList_id)
    );

    create table OrderItemEntity (
        id bigint not null,
        addedToOrderDateTime timestamp,
        quantity integer,
        sellingPrice numeric,
        promotionCode varchar(30),
        discount numeric,
        tax numeric,
        sku varchar(255),
        owningOrder_id bigint,
        primary key (id)
    );

    create table OrderSourceEntity (
        id tinyint not null,
        code varchar(255),
        description varchar(255),
        lastModifiedBy varchar(255),
        lastModifiedOn timestamp,
        primary key (id)
    );

    alter table OrderEntity 
        add constraint FKAC62CCB199C4EA3E 
        foreign key (orderSourceEntity_id) 
        references OrderSourceEntity;

    alter table OrderEntity_OrderItemEntity 
        add constraint FK2748E316708713A3 
        foreign key (orderItemList_id) 
        references OrderItemEntity;

    alter table OrderEntity_OrderItemEntity 
        add constraint FK2748E316BAE9F53E 
        foreign key (OrderEntity_id) 
        references OrderEntity;

    alter table OrderItemEntity 
        add constraint FKA76A42A450155C1D 
        foreign key (owningOrder_id) 
        references OrderEntity;
