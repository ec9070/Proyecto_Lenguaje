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


/************************************************* COMIENZAN SEQUENCIAS *****************************************/
--Sequencia para la tabla credito
create sequence creditos
start with 1
increment by 1;

--Sequencia puestos
create sequence puestos
start with 1
increment by 1;

--Sequencia sucursales
create sequence sucursales
start with 1
increment by 1;
/************************************************* FIN SEQUENCIAS *****************************************/


/************************************************* COMIENZAN TRIGGERS *****************************************/
--Trigger que actualiza el cliente con el credito
create or replace trigger ingresar_credito
after insert on credito
for each row
begin
    update Cliente
    set id_credito=:new.id_credito
    where id_cliente=:new.id_cliente;
end;

--Trigger que pone en 0 el id del credito cuando se elimina
create or replace trigger eliminar_credito
after delete on credito
for each row
begin
    update Cliente
    set id_credito=0
    where id_cliente=:old.id_cliente;
end;
/************************************************* FIN TRIGGERS *****************************************/


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



/************************************************* COMIENZA PAQUETE CREDITO *****************************************/

create or replace package paquete_credito
as
procedure insertar_credito(id_c Cliente.id_cliente%type,limite_c Credito.limite%type);
procedure buscar_credito(id_c in Cliente.id_cliente%type,id_cre out Credito.id_credito%type,limite_c out Credito.limite%type,
nombre_c out Cliente.nombre%type,apellidos_c out Cliente.apellidos%type);
end;

create or replace package body paquete_credito
as
procedure insertar_credito(id_c Cliente.id_cliente%type,limite_c Credito.limite%type)
is
begin
    insert into credito(id_credito,id_cliente,limite)
    values(creditos.nextval,id_c,limite_c);
    commit;
end insertar_credito;

procedure buscar_credito(id_c in Cliente.id_cliente%type,id_cre out Credito.id_credito%type,limite_c out Credito.limite%type,
nombre_c out Cliente.nombre%type,apellidos_c out Cliente.apellidos%type)
is 
begin
    select credito.id_credito,limite,nombre,apellidos into id_cre,limite_c,nombre_c,apellidos_c
    from credito,cliente
    where credito.id_cliente=id_c and credito.id_credito=cliente.id_credito;
end buscar_credito;

end paquete_credito;
/************************************************* FIN DE PAQUETE CREDITO *****************************************/


/************************************************* COMIENZA PAQUETE PUESTO *****************************************/

create or replace package paquete_puesto
as
function repetido_puesto(nombre in Puesto.nombre_puesto%type) return number; 
procedure insertar_puesto(nombre_p Puesto.nombre_puesto%type,min_salario_p Puesto.min_salario%type,max_salario_p Puesto.max_salario%type);
procedure actualizar_puesto(id_p Puesto.id_puesto%type,min_salario_p Puesto.min_salario%type,max_salario_p Puesto.max_salario%type);
end;

create or replace package body paquete_puesto
as
function repetido_puesto(nombre in Puesto.nombre_puesto%type) return number
is
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
end repetido_puesto;

procedure insertar_puesto(nombre_p Puesto.nombre_puesto%type,min_salario_p Puesto.min_salario%type,max_salario_p Puesto.max_salario%type)
is
begin
    insert into puesto(id_puesto,nombre_puesto,min_salario,max_salario)
    values(puestos.nextval,nombre_p,min_salario_p,max_salario_p);
    commit;
end insertar_puesto;

procedure actualizar_puesto(id_p Puesto.id_puesto%type,min_salario_p Puesto.min_salario%type,max_salario_p Puesto.max_salario%type)
is
begin
    update puesto
    set min_salario=min_salario_p,max_salario=max_salario_p
    where id_puesto=id_p;
    commit;
end actualizar_puesto;

end paquete_puesto;

/************************************************* FIN PAQUETE PUESTO *****************************************/


/************************************************* COMIENZA PAQUETE SUCURSAL *****************************************/

create or replace package paquete_sucursal
as
function repetido_sucursal(nombre1 in Sucursal.nombre_sucursal%type) return number;
function existe_provincia return number;
procedure insertar_provincias;
procedure insertar_sucursal(nombre_s Sucursal.nombre_sucursal%type,direccion_s Sucursal.direccion%type,telefono_s Sucursal.telefono%type,
provincia_s Provincia.nombre%type);
function lista_sucursales return varchar;
procedure actualizar_sucursal(id_s Sucursal.id_sucursal%type,nombre_s Sucursal.nombre_sucursal%type,direccion_s Sucursal.direccion%type,telefono_s Sucursal.telefono%type,provincia_s Provincia.nombre%type);
end;

create or replace package body paquete_sucursal
as
function repetido_sucursal(nombre1 in Sucursal.nombre_sucursal%type) return number
is
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
end repetido_sucursal;

function existe_provincia return number
is
existe int;
id_p Provincia.id_provincia%type;
begin
    select id_provincia into id_p
    from provincia
    where id_provincia=1;
    existe:=1;
    return existe;
    EXCEPTION
    WHEN  NO_DATA_FOUND THEN
          existe:=0;
          return existe;
end  existe_provincia;

procedure insertar_provincias
is
begin
    insert into provincia(id_provincia,nombre) 
    values (1,'San Jose');
    insert into provincia (id_provincia,nombre)
    values (2,'Alajuela');
    insert into provincia (id_provincia,nombre) 
    values (3,'Heredia');
    insert into provincia (id_provincia,nombre) 
    values (4,'Cartago');
    insert into provincia (id_provincia,nombre)
    values (5,'Guanacaste');
    insert into provincia (id_provincia,nombre) 
    values (6,'Puntarenas');
    insert into provincia (id_provincia,nombre) 
    values (7,'Limon');
    commit;
end insertar_provincias;

procedure insertar_sucursal(nombre_s Sucursal.nombre_sucursal%type,direccion_s Sucursal.direccion%type,telefono_s Sucursal.telefono%type,
provincia_s Provincia.nombre%type)
is
id_provincia_p Sucursal.id_provincia%type;
begin
    case provincia_s
        when 'San Jose' then id_provincia_p:=1;
        when 'Alajuela' then id_provincia_p:=2;
        when 'Heredia' then id_provincia_p:=3;
        when 'Cartago' then id_provincia_p:=4;
        when 'Guanacaste' then id_provincia_p:=5;
        when 'Puntarenas' then id_provincia_p:=6;
        when 'Limon' then id_provincia_p:=7;
    end case;
    insert into sucursal(id_sucursal,nombre_sucursal,direccion,telefono,cant_empleados,id_provincia)
    values(sucursales.nextval,nombre_s,direccion_s,telefono_s,0,id_provincia_p);
    commit;
end insertar_sucursal;

function lista_sucursales return varchar
is
s varchar(500):='';
v_id Sucursal.id_sucursal%type;
v_nombre Sucursal.nombre_sucursal%type;
cursor lista is 
select id_sucursal,nombre_sucursal
from Sucursal
order by id_sucursal;
begin
    open lista;
    loop
        fetch lista into v_id,v_nombre;
        exit when lista%NOTFOUND;
        s:=s||v_id||'. '||v_nombre||Chr(10);
    end loop;
    close lista;
    return s;
end lista_sucursales;

procedure actualizar_sucursal(id_s Sucursal.id_sucursal%type,nombre_s Sucursal.nombre_sucursal%type,direccion_s Sucursal.direccion%type,telefono_s Sucursal.telefono%type,provincia_s Provincia.nombre%type)
is
id_provincia_p Sucursal.id_provincia%type;
begin
    case provincia_s
        when 'San Jose' then id_provincia_p:=1;
        when 'Alajuela' then id_provincia_p:=2;
        when 'Heredia' then id_provincia_p:=3;
        when 'Cartago' then id_provincia_p:=4;
        when 'Guanacaste' then id_provincia_p:=5;
        when 'Puntarenas' then id_provincia_p:=6;
        when 'Limon' then id_provincia_p:=7;
    end case;
    update Sucursal
    set nombre_sucursal=nombre_s,direccion=direccion_s,telefono=telefono_s,id_provincia=id_provincia_p
    where id_sucursal=id_s;
    commit;
end  actualizar_sucursal;
    
end paquete_sucursal;

/************************************************* FIN PAQUETE SUCURSAL *****************************************/




