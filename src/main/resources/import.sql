create table if not exists users (username varchar(50) not null primary key, password varchar(50) not null, enabled boolean not null)
create table if not exists authorities (username varchar(50) not null, authority varchar(50) not null, constraint FK_AUTHORITIES_USERS foreign key(username) references users(username))

insert into users (username, password, enabled) values ('admin', 'admin', true);
insert into authorities (username, authority) values ('admin', 'ROLE_ADMIN');
insert into users (username, password, enabled) values ('user1', 'user1', true);
insert into users (username, password, enabled) values ('user2', 'user2', true);
insert into authorities (username, authority) values ('user1', 'ROLE_USER1');
insert into authorities (username, authority) values ('user1', 'ROLE_ADMIN');
insert into authorities (username, authority) values ('user2', 'ROLE_MAINTENANCE_TL');


insert into plant_inventory_entry (id, name, description, price) values (1, 'Mini excavator', '1.5 Tonne Mini excavator', 150);
insert into plant_inventory_entry (id, name, description, price) values (2, 'Mini excavator', '3 Tonne Mini excavator', 200);
insert into plant_inventory_entry (id, name, description, price) values (3, 'Midi excavator', '5 Tonne Midi excavator', 250);
insert into plant_inventory_entry (id, name, description, price) values (4, 'Midi excavator', '8 Tonne Midi excavator', 300);
insert into plant_inventory_entry (id, name, description, price) values (5, 'Maxi excavator', '15 Tonne Large excavator', 400);
insert into plant_inventory_entry (id, name, description, price) values (6, 'Maxi excavator', '20 Tonne Large excavator', 450);
insert into plant_inventory_entry (id, name, description, price) values (7, 'HS dumper', '1.5 Tonne Hi-Swivel Dumper', 150);
insert into plant_inventory_entry (id, name, description, price) values (8, 'FT dumper', '2 Tonne Front Tip Dumper', 180);
insert into plant_inventory_entry (id, name, description, price) values (9, 'FT dumper', '2 Tonne Front Tip Dumper', 200);
insert into plant_inventory_entry (id, name, description, price) values (10, 'FT dumper', '2 Tonne Front Tip Dumper', 300);
insert into plant_inventory_entry (id, name, description, price) values (11, 'FT dumper', '3 Tonne Front Tip Dumper', 400);
insert into plant_inventory_entry (id, name, description, price) values (12, 'Loader', 'Hewden Backhoe Loader', 200);
insert into plant_inventory_entry (id, name, description, price) values (13, 'D-Truck', '15 Tonne Articulating Dump Truck', 250);
insert into plant_inventory_entry (id, name, description, price) values (14, 'D-Truck', '30 Tonne Articulating Dump Truck', 300);

insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition, plant_status) values (1, 1, 'A01', 'SERVICEABLE', 'RETURNED');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition, plant_status) values (2, 2, 'A02', 'SERVICEABLE', 'RETURNED');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition, plant_status) values (3, 3, 'A03', 'UNSERVICEABLE_REPAIRABLE', 'RETURNED');

insert into plant_reservation (id, plant_id, start_date, end_date) values (1, 1, '2017-03-22', '2017-03-24');
insert into purchase_order values ('1', '2017-03-27', '2017-03-28', 'OPEN', 100, 1);