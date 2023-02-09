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

create table Consejos(
numeroConsejo int not null primary key,
consejo varchar(200) not null)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

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

create table Premios(
numero int not null primary key,
nombre varchar(50) not null,
puntos int not null)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

create table Usuarios(
correo varchar(100) not null primary key,
zona varchar(50) not null)
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
correo varchar(100) not null primary key,
nombre varchar(50) not null,
fecha varchar(20) not null,
hora varchar(20) not null)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_eo_0900_ai_ci;

create user 'administrador'@'%' identified by 'admin123';
grant all privileges on Residuos.* to 'administrador'@'%';
flush privileges;

select * from Consejos;