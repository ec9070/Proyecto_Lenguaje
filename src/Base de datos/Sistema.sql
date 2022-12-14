create tablespace tbl_tienda
datafile 'C:\Proyecto_Lenguajes\tbl_tienda01.dbf' size 100M /*Recordar hacer la carpeta primero*/
default storage (initial 1m next 1m pctincrease 0);

create user tienda identified by tienda01
default tablespace tbl_tienda
temporary tablespace temp;

grant connect,resource to tienda;

alter user tienda quota unlimited on tbl_tienda;

alter session set "_ORACLE_SCRIPT" = TRUE;
