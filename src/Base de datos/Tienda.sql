--USER:TIENDA

--Crear las tablas
create table Cliente(
id_cliente varchar(9) not null,
nombre varchar(50) not null,
apellidos varchar(50) not null,
correo varchar(50) not null,
telefono varchar(50) not null,
id_credito int,
primary key(id_cliente));

/************************************************* COMIENZA PAQUETE CLIENTES *****************************************/

create or replace package paquete_clientes
as
function repetido(id_c in Cliente.id_cliente%type) return number; 
procedure insertar_cliente(id_c Cliente.id_cliente%type,nombre_c Cliente.nombre%type,apellidos_c Cliente.apellidos%type,
correo_c Cliente.correo%type,telefono_c Cliente.telefono%type);
procedure actualizar_cliente(id_c Cliente.id_cliente%type,nombre_c Cliente.nombre%type,apellidos_c Cliente.apellidos%type,correo_c Cliente.correo%type,
telefono_c Cliente.telefono%type);
end;

create or replace package body paquete_clientes
as
function repetido(id_c in Cliente.id_cliente%type) return number 
is
existe int;
id_c2 Cliente.id_cliente%type;
begin
    select id_cliente into id_c2
    from Cliente
    where id_cliente=id_c;
    existe:=1;
    return existe;
    EXCEPTION
    WHEN  NO_DATA_FOUND THEN
          existe:=0;
          return existe;
end repetido;

procedure insertar_cliente(id_c Cliente.id_cliente%type,nombre_c Cliente.nombre%type,apellidos_c Cliente.apellidos%type,
correo_c Cliente.correo%type,telefono_c Cliente.telefono%type)
is
begin
    insert into cliente(id_cliente,nombre,apellidos,correo,telefono,id_credito) 
    values(id_c,nombre_c,apellidos_c,correo_c,telefono_c,0);
    commit;
end insertar_cliente;

procedure actualizar_cliente(id_c Cliente.id_cliente%type,nombre_c Cliente.nombre%type,apellidos_c Cliente.apellidos%type,correo_c Cliente.correo%type,
telefono_c Cliente.telefono%type)
is
begin
    update Cliente
    set nombre=nombre_c,apellidos=apellidos_c,correo=correo_c,telefono=telefono_c
    where id_cliente=id_c;
    commit;
end actualizar_cliente;

end paquete_clientes;
/************************************************* FIN DE PAQUETE CLIENTES *****************************************/



/************************************************* No funciona de momento*****************************************/
insert into Cliente 
values('117990404','Emanuel','Castro Arrieta','ec9070@gmail.com','85217368',0);

insert into Credito 
values(creditos.nextval,'1',700000);

update Cliente
set nombre='Emanuel',apellidos='Castro Arrieta',correo='ec9071@gmail.com',telefono='85217362'
where id_cliente='117990404';

delete 
from Cliente
where id_cliente='117990404';

commit;

create sequence creditos
start with 1
increment by 1;

drop sequence creditos;

select * from credito;


create or replace function repetido_puesto(nombre in Puesto.nombre_puesto%type) return number 
as
existe int;
nombre_2 Puesto.nombre_puesto%type;
begin
    select nombre_puesto into nombre_2
    from Puesto
    where nombre_puesto=nombre;
    existe:=1;
    return existe;
    EXCEPTION
    WHEN  NO_DATA_FOUND THEN
          existe:=0;
          return existe;
end;

create sequence puestos
start with 1
increment by 1;

select * from puesto;

create or replace function repetido_sucursal(nombre1 in Sucursal.nombre_sucursal%type) return number 
as
existe int;
nombre_2 Sucursal.nombre_sucursal%type;
begin
    select nombre_sucursal into nombre_2
    from Sucursal
    where nombre_sucursal=nombre1;
    existe:=1;
    return existe;
    EXCEPTION
    WHEN  NO_DATA_FOUND THEN
          existe:=0;
          return existe;
end;

create sequence sucursales
start with 1
increment by 1;

drop sequence sucursales;

select * from puesto;

drop table sucursal;


insert into provincia values (1,'San Jose');
insert into provincia values (2,'Alajuela');
insert into provincia values (3,'Heredia');
insert into provincia values (4,'Cartago');
insert into provincia values (5,'Guanacaste');
insert into provincia values (6,'Puntarenas');
insert into provincia values (7,'Limon');

create table Credito(
id_credito int not null,
id_cliente varchar(9) not null,
limite int not null,
primary key(id_credito),
foreign key(id_cliente) references Cliente(id_cliente));

create table Puesto(
id_puesto int not null,
nombre_puesto varchar(50) not null,
min_salario int not null,
max_salario int not null,
primary key(id_puesto));

create table Provincia(
id_provincia int not null,
nombre varchar(50) not null,
primary key(id_provincia));

create table sucursal(
id_sucursal int not null,
nombre_sucursal varchar(50) not null,
direccion varchar(100) not null,
telefono varchar(50) not null,
cant_empleados int not null,
id_provincia int not null,
primary key(id_sucursal),
foreign key(id_provincia) references provincia(id_provincia));




execute paquete_clientes.insertar_cliente('117990404','Emanuel','Castro Arrieta','ec9070@gmail.com','85217368')