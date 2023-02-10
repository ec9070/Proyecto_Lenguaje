create database Residuos;

use Residuos;

create table Zonas(
numeroZona int not null primary key,
nombreZona varchar(50) not null,
descripcion varchar(500) not null,
direccion varchar(200) not null,
cantidad_maxima int not null)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

insert into zonas values(1,'Rio San Jose','Lugar con mucho turismo, nesecita limpieza','La esperanza Sarapiqui',9);

create table Consejos(
numeroConsejo int not null primary key,
consejo varchar(200) not null)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

insert into consejos values(1,'Utilize bolsas de papel');

create table Capacitaciones(
numero int not null primary key,
nombre varchar(50) not null,
direccion varchar(200) not null,
hora varchar(20) not null,
fecha varchar(20) not null,
cupo int not null)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

insert into capacitaciones values(1,'Manejo de desechos','Santa Barbara Heredia Salon Comunal','01:00 PM','07/02/2023',19);

create table Premios(
numero int not null primary key,
nombre varchar(50) not null,
puntos int not null)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

insert into premios values(1,'Camisa con Logo',6);

create table Usuarios(
correo varchar(100) not null,
zona varchar(50) not null,
primary key(correo,zona))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

create table Donaciones(
numeroDonacion int not null primary key,
correo varchar(100) not null,
numeroTarjeta varchar(30) not null,
codigo int not null,
monto double not null,
fecha varchar(20) not null) 
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

create table Users_Capacitaciones(
correo varchar(100) not null,
nombre varchar(50) not null,
fecha varchar(20) not null,
hora varchar(20) not null)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

create user 'administrador'@'%' identified by 'admin123';
grant all privileges on Residuos.* to 'administrador'@'%';
flush privileges;

