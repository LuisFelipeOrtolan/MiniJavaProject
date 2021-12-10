create database starhotel;

use starhotel;

create table MenuItem (id int(10) auto_increment primary key, name varchar(100) not null, price float(10) not null, active boolean not null, dateOfLunch date not null, category varchar(100) not null, freeDelivery boolean not null);


create table Carts(idItem int(10), idBuyer int(10), amount int(10), primary key(idItem, idBuyer), foreign key(idItem) references MenuItem(id));

                                            