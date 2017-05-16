create table if not exists users (username varchar(50) not null primary key, password varchar(50) not null, enabled boolean not null)
create table if not exists authorities (username varchar(50) not null, authority varchar(50) not null, constraint FK_AUTHORITIES_USERS foreign key(username) references users(username))

insert into users (username, password, enabled) values ('admin', 'admin', true);
insert into authorities (username, authority) values ('admin', 'ROLE_ADMIN');
insert into users (username, password, enabled) values ('user1', 'user1', true);
insert into authorities (username, authority) values ('user1', 'ROLE_USER1');
insert into authorities (username, authority) values ('user1', 'ROLE_ADMIN');
