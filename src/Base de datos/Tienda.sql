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

create table empleado(
id_empleado varchar(9) not null,
nombre varchar(50) not null,
apellidos varchar(50) not null,
correo varchar(50) not null,
fecha_contratado date not null,
id_puesto int not null,
salario int not null,
id_sucursal int not null,
primary key(id_empleado),
foreign key(id_puesto) references puesto(id_puesto),
foreign key(id_sucursal) references sucursal(id_sucursal));

create table pago_salario(
num_pago int not null,
fecha date not null,
descripcion varchar(100) not null,
id_empleado varchar(9) not null,
total int not null,
primary key(num_pago),
foreign key(id_empleado) references empleado(id_empleado));

create table proveedor(
id_proveedor int not null,
nom_proveedor varchar(50) not null,
telefono varchar(50) not null,
correo varchar(50) not null,
primary key(id_proveedor));

create table producto(
codigo varchar(5) not null,
modelo varchar(50) not null,
id_proveedor int not null,
precio int not null,
existencias int not null,
id_sucursal int not null,
primary key(codigo),
foreign key(id_proveedor) references proveedor(id_proveedor),
foreign key(id_sucursal) references sucursal(id_sucursal));

create table factura(
num_factura int not null,
fecha date not null,
id_cliente varchar(9) not null,
total int not null,
primary key(num_factura),
foreign key(id_cliente)references Cliente(id_cliente));

create table detalle_factura(
num_linea int not null,
num_factura int not null,
codigo varchar(5) not null,
cantidad int not null,
total_linea int not null,
primary key(num_linea,num_factura),
foreign key(num_factura) references factura(num_factura));

create table pedidos(
num_pedido int not null,
fecha date not null,
codigo varchar(5) not null,
unidades int not null,
primary key (num_pedido),
foreign key(codigo) references producto(codigo));

create table mantenimiento(
num_mantenimiento int not null,
fecha_ingreso date not null,
descripcion varchar (50) not null,
id_cliente varchar(9) not null,
total int not null,
fecha_salida date not null,
primary key(num_mantenimiento),
foreign key (id_cliente) references cliente(id_cliente));

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

--Sequencia para pagos
create sequence pagos
start with 1
increment by 1;

--Sequencia para proveedores
create sequence proveedores
start with 1
increment by 1;

--Sequencia para facturas
create sequence facturas
start with 1
increment by 1;

--Sequencia para pedidos
create sequence pedidos_s
start with 1
increment by 1;

--Sequencia para mantenimientos
create sequence mant_s
start with 1
increment by 1;


/************************************************* FIN SEQUENCIAS *****************************************/


/************************************************* COMIENZAN TRIGGERS *****************************************/
--Trigger que controla el credito
create or replace trigger accion_credito
after insert or delete on credito
for each row
begin
    if INSERTING THEN
        update Cliente
        set id_credito=:new.id_credito
        where id_cliente=:new.id_cliente;
    end if;
    if DELETING THEN
         update Cliente
        set id_credito=0
        where id_cliente=:old.id_cliente;
    end if;
end;

--Trigger que controla el la cant_empleados
create or replace trigger accion_empleado
after insert or update or delete on empleado
for each row
begin
    if INSERTING THEN
        update sucursal
        set cant_empleados=cant_empleados+1
        where id_sucursal=:new.id_sucursal;
    end if;
    if UPDATING THEN
        update sucursal
        set cant_empleados=cant_empleados-1
        where id_sucursal=:old.id_sucursal;
        update sucursal
        set cant_empleados=cant_empleados+1
        where id_sucursal=:new.id_sucursal;
    end if;
    if DELETING THEN
        update sucursal
        set cant_empleados=cant_empleados-1
        where id_sucursal=:old.id_sucursal;
    end if;
end;

create or replace trigger accion_factura
after insert on detalle_factura
for each row
begin
    update factura
    set total=total+:new.total_linea
    where num_factura=:new.num_factura;
    update producto
    set existencias=existencias-:new.cantidad
    where codigo=:new.codigo;
end;


create or replace trigger accion_pedido
after insert on pedidos
for each row
begin
    update producto
    set existencias=existencias+:new.unidades
    where codigo=:new.codigo;
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
procedure actualizar_credito(id_c Credito.id_credito%type,limite_c Credito.limite%type);
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

procedure actualizar_credito(id_c Credito.id_credito%type,limite_c Credito.limite%type)
is
begin
    update credito
    set limite=limite_c
    where id_credito=id_c;
    commit;
end actualizar_credito;

end paquete_credito;
/************************************************* FIN DE PAQUETE CREDITO *****************************************/


/************************************************* COMIENZA PAQUETE PUESTO *****************************************/

create or replace package paquete_puesto
as
function repetido_puesto(nombre in Puesto.nombre_puesto%type) return number; 
procedure insertar_puesto(nombre_p Puesto.nombre_puesto%type,min_salario_p Puesto.min_salario%type,max_salario_p Puesto.max_salario%type);
procedure actualizar_puesto(id_p Puesto.id_puesto%type,min_salario_p Puesto.min_salario%type,max_salario_p Puesto.max_salario%type);
function existe_puestos return number;
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

function existe_puestos return number
is
existe int;
id_c Puesto.id_puesto%type;
cursor lista is
select id_puesto
from puesto;
begin
    open lista;
    loop
        fetch lista into id_c;
        exit when lista%NOTFOUND; 
    end loop;
    if lista%ROWCOUNT>0 then
        existe:=1;
    else
        existe:=0;
    end if;
    close lista;
    return existe;
end existe_puestos;

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
function existe_sucursales return number;
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

function existe_sucursales return number
is
existe int;
id_s Sucursal.id_sucursal%type;
cursor lista is
select id_sucursal
from sucursal;
begin
    open lista;
    loop
        fetch lista into id_s;
        exit when lista%NOTFOUND; 
    end loop;
    if lista%ROWCOUNT>0 then
        existe:=1;
    else
        existe:=0;
    end if;
    close lista;
    return existe;
end existe_sucursales;
    
end paquete_sucursal;

/************************************************* FIN PAQUETE SUCURSAL *****************************************/


/************************************************* COMIENZA PAQUETE EMPLEADO *****************************************/

create or replace package paquete_empleado
as
function repetido_empleado(id_e Empleado.id_empleado%type)return number;
procedure insertar_empleado(id_e Empleado.id_empleado%type,n Empleado.nombre%type,a Empleado.apellidos%type,c Empleado.correo%type,f varchar,
id_p Empleado.id_puesto%type,s Empleado.salario%type,id_s Empleado.id_sucursal%type);
procedure actualizar_empleado(id_e Empleado.id_empleado%type,n Empleado.nombre%type,a Empleado.apellidos%type,c Empleado.correo%type,id_p Empleado.id_puesto%type,
s Empleado.salario%type,id_s Empleado.id_sucursal%type);
end;

create or replace package body paquete_empleado
as
function repetido_empleado(id_e Empleado.id_empleado%type)return number
is
existe int;
id_e2 Empleado.id_empleado%type;
begin
    select id_empleado into id_e2
    from Empleado
    where id_empleado=id_e;
    existe:=1;
    return existe;
    EXCEPTION
    WHEN  NO_DATA_FOUND THEN
          existe:=0;
          return existe;
end repetido_empleado;

procedure insertar_empleado(id_e Empleado.id_empleado%type,n Empleado.nombre%type,a Empleado.apellidos%type,c Empleado.correo%type,f varchar,
id_p Empleado.id_puesto%type,s Empleado.salario%type,id_s Empleado.id_sucursal%type)
is
begin
    insert into Empleado(id_empleado,nombre,apellidos,correo,fecha_contratado,id_puesto,salario,id_sucursal)
    values(id_e,n,a,c,to_date(f,'DD/MM/YYYY'),id_p,s,id_s);
    commit;
end insertar_empleado;

procedure actualizar_empleado(id_e Empleado.id_empleado%type,n Empleado.nombre%type,a Empleado.apellidos%type,c Empleado.correo%type,id_p Empleado.id_puesto%type,
s Empleado.salario%type,id_s Empleado.id_sucursal%type)
is
begin
    update empleado
    set nombre=n,apellidos=a,correo=c,id_puesto=id_p,salario=s,id_sucursal=id_s
    where id_empleado=id_e;
    commit;
end actualizar_empleado;

end paquete_empleado;

/************************************************* FIN PAQUETE EMPLEADO *****************************************/

/************************************************* COMIENZA PAQUETE PAGO*****************************************/

create or replace package paquete_pago
as
function pago_fecha(id_e Pago_salario.id_empleado%type) return number;
procedure insertar_pago(des Pago_salario.descripcion%type,id_e Pago_salario.id_empleado%type,total_p Pago_salario.total%type);
end;

create or replace package body paquete_pago
as
function pago_fecha(id_e Pago_salario.id_empleado%type) return number
is
fecha_p pago_salario.fecha%type;
existe int;
begin
    select fecha into fecha_p
    from pago_salario
    where TRUNC(fecha) = TRUNC(sysdate) and id_empleado=id_e;
    existe:=1;
    return existe;
    EXCEPTION
    WHEN  NO_DATA_FOUND THEN
          existe:=0;
          return existe;
end pago_fecha;

procedure insertar_pago(des Pago_salario.descripcion%type,id_e Pago_salario.id_empleado%type,total_p Pago_salario.total%type)
is
begin
    insert into pago_salario(num_pago,fecha,descripcion,id_empleado,total)
    values(pagos.nextval,sysdate,des,id_e,total_p);
    commit;
end insertar_pago;

end paquete_pago;

/************************************************* FIN PAQUETE PAGO*****************************************/

/************************************************* COMIENZA PAQUETE PROVEEDOR*****************************************/

create or replace package paquete_proveedor
as
function repetido_proveedor(nombre_p Proveedor.nom_proveedor%type) return number;
procedure insertar_proveedor(nom Proveedor.nom_proveedor%type,tel Proveedor.telefono%type,correo_p Proveedor.correo%type);
procedure actualizar_proveedor(id_p Proveedor.id_proveedor%type,tel Proveedor.telefono%type,correo_p Proveedor.correo%type);
function existe_proveedores return number;
end;

create or replace package body paquete_proveedor
as
function repetido_proveedor(nombre_p Proveedor.nom_proveedor%type) return number
is
existe int;
nombre_2 Proveedor.nom_proveedor%type;
begin
    select nom_proveedor into nombre_2
    from Proveedor
    where nom_proveedor=nombre_p;
    existe:=1;
    return existe;
    EXCEPTION
    WHEN  NO_DATA_FOUND THEN
          existe:=0;
          return existe;
end repetido_proveedor;

procedure insertar_proveedor(nom Proveedor.nom_proveedor%type,tel Proveedor.telefono%type,correo_p Proveedor.correo%type)
is
begin
    insert into proveedor(id_proveedor,nom_proveedor,telefono,correo)
    values(proveedores.nextval,nom,tel,correo_p);
    commit;
end insertar_proveedor;

procedure actualizar_proveedor(id_p Proveedor.id_proveedor%type,tel Proveedor.telefono%type,correo_p Proveedor.correo%type)
is
begin
    update proveedor
    set telefono=tel,correo=correo_p
    where id_proveedor=id_p;
    commit;
end actualizar_proveedor;

function existe_proveedores return number
is
existe int;
id_p Proveedor.id_proveedor%type;
cursor lista is
select id_proveedor
from proveedor;
begin
    open lista;
    loop
        fetch lista into id_p;
        exit when lista%NOTFOUND; 
    end loop;
    if lista%ROWCOUNT>0 then
        existe:=1;
    else
        existe:=0;
    end if;
    close lista;
    return existe;
end existe_proveedores;

end paquete_proveedor;

/************************************************* FIN PAQUETE PROVEEDOR*****************************************/

/************************************************* COMIENZA PAQUETE PRODUCTO*****************************************/

create or replace package paquete_producto
as
function repetido_producto(codigo_p Producto.codigo%type) return number;
procedure insertar_producto(codigo_p Producto.codigo%type,m Producto.modelo%type,id_p Producto.id_proveedor%type,p Producto.precio%type,e Producto.existencias%type,
id_s Producto.id_sucursal%type);
function lista_productos return varchar;
procedure actualizar_producto(c Producto.codigo%type,p Producto.precio%type,id_s Producto.id_sucursal%type);
function existe_productos return number;
end;

create or replace package body paquete_producto
as
function repetido_producto(codigo_p Producto.codigo%type) return number
is
existe int;
codigo_2 Producto.codigo%type;
begin
    select codigo into codigo_2
    from Producto
    where codigo=codigo_p;
    existe:=1;
    return existe;
    EXCEPTION
    WHEN  NO_DATA_FOUND THEN
          existe:=0;
          return existe;
end repetido_producto;

procedure insertar_producto(codigo_p Producto.codigo%type,m Producto.modelo%type,id_p Producto.id_proveedor%type,p Producto.precio%type,e Producto.existencias%type,
id_s Producto.id_sucursal%type)
is
begin 
    insert into producto(codigo,modelo,id_proveedor,precio,existencias,id_sucursal)
    values(codigo_p,m,id_p,p,e,id_s);
    commit;
end insertar_producto;

function lista_productos return varchar
is
s varchar(500):='';
v_id Producto.codigo%type;
v_modelo Producto.modelo%type;
v_proveedor Proveedor.nom_proveedor%type;
cursor lista is 
select codigo,modelo,nom_proveedor
from Producto,Proveedor
where Producto.id_proveedor=Proveedor.id_proveedor;
begin
    open lista;
    loop
        fetch lista into v_id,v_modelo,v_proveedor;
        exit when lista%NOTFOUND;
        s:=s||v_id||'. '||v_proveedor||' '||v_modelo||Chr(10);
    end loop;
    close lista;
    return s;
end lista_productos;

procedure actualizar_producto(c Producto.codigo%type,p Producto.precio%type,id_s Producto.id_sucursal%type)
is
begin
    update producto
    set precio=p,id_sucursal=id_s
    where codigo=c;
    commit;
end actualizar_producto;

function existe_productos return number
is
existe int;
id_p Producto.codigo%type;
cursor lista is
select codigo
from producto;
begin
    open lista;
    loop
        fetch lista into id_p;
        exit when lista%NOTFOUND; 
    end loop;
    if lista%ROWCOUNT>0 then
        existe:=1;
    else
        existe:=0;
    end if;
    close lista;
    return existe;
end existe_productos;

end paquete_producto;

/************************************************* FIN PAQUETE PRODUCTO*****************************************/

/************************************************* COMIENZA PAQUETE FACTURA*****************************************/

create or replace package paquete_factura
as
procedure insertar_factura(id_c Factura.id_cliente%type,n out Factura.num_factura%type);
function nombre_producto(c Producto.codigo%type) return varchar;
procedure insertar_linea_factura(n_l detalle_factura.num_linea%type,num_fact detalle_factura.num_factura%type,c detalle_factura.codigo%type,cant detalle_factura.cantidad%type,
total_l detalle_factura.total_linea%type);
end;

create or replace package body paquete_factura
as
procedure insertar_factura(id_c Factura.id_cliente%type,n out Factura.num_factura%type)
is
begin
    insert into factura(num_factura,fecha,id_cliente,total)
    values(facturas.nextval,sysdate,id_c,0);
    select facturas.currval into n from dual;
end insertar_factura;

function nombre_producto(c Producto.codigo%type) return varchar
is
nombre varchar(100);
begin
    select nom_proveedor||' '||modelo into nombre
    from proveedor,producto
    where producto.codigo=c and producto.id_proveedor=proveedor.id_proveedor;
    return nombre;
end nombre_producto;

procedure insertar_linea_factura(n_l detalle_factura.num_linea%type,num_fact detalle_factura.num_factura%type,c detalle_factura.codigo%type,cant detalle_factura.cantidad%type,
total_l detalle_factura.total_linea%type)
is
begin
    insert into detalle_factura(num_linea,num_factura,codigo,cantidad,total_linea)
    values(n_l,num_fact,c,cant,total_l);
    commit;
end insertar_linea_factura;

end paquete_factura;

/************************************************* FIN PAQUETE FACTURA*****************************************/

/************************************************* EMPIEZA PAQUETE PEDIDO*****************************************/

create or replace package paquete_pedido
as
procedure insertar_pedido(c pedidos.codigo%type,u pedidos.unidades%type);
function lista_pedidos return varchar;
function existe_pedido (id_p Pedidos.num_pedido%type) return number;
end;

create or replace package body paquete_pedido
as
procedure insertar_pedido(c pedidos.codigo%type,u pedidos.unidades%type)
is
begin
    insert into pedidos(num_pedido,fecha,codigo,unidades)
    values(pedidos_s.nextval,sysdate,c,u);
    commit;
end insertar_pedido;

function lista_pedidos return varchar
is
s varchar(500):='';
v_id Pedidos.num_pedido%type;
v_nombre varchar(100);
v_fecha Pedidos.fecha%type;
cursor lista is 
select num_pedido,fecha,nom_proveedor||' '||modelo
from Pedidos,Producto,Proveedor
where Pedidos.codigo=Producto.codigo and Producto.id_proveedor=Proveedor.id_proveedor;
begin
    open lista;
    loop
        fetch lista into v_id,v_fecha,v_nombre;
        exit when lista%NOTFOUND;
        s:=s||v_id||'. '||v_fecha||' '||v_nombre||Chr(10);
    end loop;
    close lista;
    return s;
end lista_pedidos;

function existe_pedido (id_p Pedidos.num_pedido%type) return number
is
existe int;
id_2 Pedidos.num_pedido%type;
begin
    select num_pedido into id_2
    from Pedidos
    where num_pedido=id_p;
    existe:=1;
    return existe;
    EXCEPTION
    WHEN  NO_DATA_FOUND THEN
        existe:=0;
        return existe;
end existe_pedido;

end paquete_pedido;

/************************************************* FIN PAQUETE PEDIDO*****************************************/

/************************************************* EMPIEZA PAQUETE MANTANIMIENTO*****************************************/

create or replace package paquete_mantenimiento
as
procedure insertar_mant(fecha_i varchar,des mantenimiento.descripcion%type,id_c mantenimiento.id_cliente%type,
t mantenimiento.total%type,fecha_s varchar);
function existe_mant(id_c mantenimiento.id_cliente%type) return number;
end;

create or replace package body paquete_mantenimiento
as
procedure insertar_mant(fecha_i varchar,des mantenimiento.descripcion%type,id_c mantenimiento.id_cliente%type,
t mantenimiento.total%type,fecha_s varchar)
is
begin
    insert into mantenimiento(num_mantenimiento,fecha_ingreso,descripcion,id_cliente,total,fecha_salida)
    values(mant_s.nextval,to_date(fecha_i,'DD/MM/YYYY'),des,id_c,t,to_date(fecha_s,'DD/MM/YYYY'));
    commit;
end insertar_mant;

function existe_mant(id_c mantenimiento.id_cliente%type) return number
is
existe int;
id_p Mantenimiento.num_mantenimiento%type;
cursor lista is
select num_mantenimiento
from mantenimiento
where id_cliente=id_c;
begin
    open lista;
    loop
        fetch lista into id_p;
        exit when lista%NOTFOUND; 
    end loop;
    if lista%ROWCOUNT>0 then
        existe:=1;
    else
        existe:=0;
    end if;
    close lista;
    return existe;
end existe_mant;

end paquete_mantenimiento;

/************************************************* FIN PAQUETE MANTENIMINETO*****************************************/

/************ Momentanio ****************/
select * from credito;

select * from cliente;

select * from puesto;

select * from empleado;

select * from pago_salario;

select * from proveedor;

select * from producto;

select * from sucursal;

select * from factura;

select * from detalle_factura;

delete from factura;

select * from pedidos;

select * from mantenimiento;

