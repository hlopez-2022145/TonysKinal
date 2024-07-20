/*
	Hernán Misael López Pérez
    IN5AV
    2022145
    
    Inicio: 28-03-2023
    
    Modificaciones: 28-03-2023,31-05-2023
*/

Drop database if exists DBTonysKinal2023;
Create database DBTonysKinal2023;

Use DBTonysKinal2023;

Create table Empresas(
	codigoEmpresa int not null auto_increment,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(8) not null,
    primary key PK_codigoEmpresa (codigoEmpresa)
);

Create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
    descripcionTipo varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

Create table Productos(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null,
    cantidadProducto int not null,
    primary key PK_codigoProducto (codigoProducto)
);

Create table Empleados (
    codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    nombresEmpleados varchar(150) not null,
    apellidosEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado foreign key
		(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado)
);

Create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(8),
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key
		(codigoEmpresa) references Empresas (codigoEmpresa)
); 

Create table Presupuesto(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal (10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa)
		references Empresas (codigoEmpresa)
);

Create table Platos(
	codigoPlato int not null auto_increment,
    cantidad int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal (10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato foreign key (codigoTipoPlato)
		references TipoPlato (codigoTipoPlato)
);

Create table Productos_has_Platos(
	Productos_codigoProducto int not null,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos foreign key (codigoProducto)
		references Productos (codigoProducto)
);

Create table Servicios_has_Platos(
	Servicios_codigoProducto int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoProducto (Servicios_codigoProducto),
    constraint FK_Servicios_has_Platos_Servicios foreign key (codigoServicio)
		references Servicios (codigoServicio),
	constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato)
		references Platos (codigoPlato)
);

Create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicio_has_Empleados_Servicios foreign key (codigoServicio)
		references Servicios (codigoServicio),
    constraint FK_Servicio_has_Empleados_Empleados foreign key (codigoEmpleado)
		references Empleados (codigoEmpleado)
);

Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);

Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);

-- ----------------------------- PROCEDIMINETOS ALMACENADOS --------------------------------------
-- --------------------------- EMPRESAS -----------------------------------------------------------

-- AGREGAR EMPRESA ----------------------
Delimiter $$
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150),
		in direccion varchar(150),in telefono varchar(8))
        Begin
			Insert into Empresas (nombreEmpresa,direccion,telefono) 
				values (nombreEmpresa,direccion,telefono);
        End$$
Delimiter ;

call sp_AgregarEmpresa ('Pepsi','27 Av. A 18-15 Z.10','20254573');
call sp_AgregarEmpresa ('El Rincón de la Finca','7 AV. Zona 1', '23568974');
call sp_AgregarEmpresa ('Santa Crepa','KM 18.5 Carretera al salvador', '45698701');
call sp_AgregarEmpresa ('Restaurante Verde Limón','8Av. Zona 1 Guatemala', '45780195');
call sp_AgregarEmpresa ('Finca San Cayetano','6ta.Av Ciudad de Gautemala', '25479630');
call sp_AgregarEmpresa ('McDonald s','7Av Zona 10', '10452174');
call sp_AgregarEmpresa ('Los Cebollines','3Av San José Pinula', '89740123');
call sp_AgregarEmpresa ('Burger king','4Av. Zona 10', '14213536');
call sp_AgregarEmpresa ('Wendy s','7Av. Zona 10 Proceres', '48410752');
call sp_AgregarEmpresa ('Subway','5Av. Zona 1 Guatemala', '23654987');

-- EDITAR EMPRESA ----------------------
Delimiter $$
	Create procedure sp_EditarEmpresa(in codEmpresa int, in NombreEmpresa varchar(150), 
		in Direccion varchar(150), in Telefono varchar(8))
		Begin
			Update Empresas E
				set E.nombreEmpresa = NombreEmpresa, 
					E.direccion = Direccion,
					E.telefono = Telefono
                    where E.codigoEmpresa = codEmpresa;
        End$$
Delimiter ;

call sp_EditarEmpresa(1,'Coca Cola','8 Av. Zona 12','56984710');

-- ELIMINAR EMPRESA -------------------
Delimiter $$
	Create procedure sp_EliminarEmpresa(in codEmpresa int)
		Begin
			Delete from Empresas
				where codigoEmpresa = codEmpresa;
        End$$
Delimiter ;

-- call sp_EliminarEmpresa(2);

-- LISTAR EMPRESAS --------------------
Delimiter $$
	Create procedure sp_ListarEmpresas()
		Begin
			select 
				E.codigoEmpresa,
                E.nombreEmpresa,
                E.direccion,
                E.telefono
                from Empresas E;
        End$$
Delimiter ;

call sp_ListarEmpresas();

-- BUSCAR EMPRESA -----------------
Delimiter $$
	Create procedure sp_BuscarEmpresa(in codEmpresa int)
		Begin
			select
				E.codigoEmpresa,
                E.nombreEmpresa,
                E.direccion,
                E.telefono
				From Empresas E
					where E.codigoEmpresa = codEmpresa; 
        End$$
Delimiter ;

call sp_BuscarEmpresa(1);

-- --------------------------- TIPO EMPLEADO ---------------------------------------------

-- AGREGAR TIPO EMPLEADO -------------------
Delimiter $$
	Create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
		Begin
			Insert into TipoEmpleado (descripcion)
				values(descripcion);
        End$$
Delimiter ;

call sp_AgregarTipoEmpleado('Panadero');
call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Ayudante Camarero');
call sp_AgregarTipoEmpleado('Hostess o Recepcionista');
call sp_AgregarTipoEmpleado('Maitre o Administrador');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Gourmet o gastrónomo');
call sp_AgregarTipoEmpleado('Cantador o somelier');

-- EDITAR TIPO EMPLEADO -------------------
Delimiter $$
	Create procedure sp_EditarTipoEmpleado(in codTipoEmpleado int,in descrip varchar(50))
		Begin
			Update TipoEmpleado T
				set T.descripcion = descrip
                where T.codigoTipoEmpleado = codTipoEmpleado; 
        End$$
Delimiter ;

call sp_EditarTipoEmpleado(1,'Chef'); 

-- ELIMINAR TIPO EMPLEADO ---------------
Delimiter $$
	Create procedure sp_EliminarTipoEmpleado(in codTipoEmpleado int)
		Begin
			Delete from TipoEmpleado
				where codigoTipoEmpleado = codTipoEmpleado;
        End$$
Delimiter ;

-- call sp_EliminarTipoEmpleado(1);

-- LISTAR TIPO EMPLEADOS ---------------
Delimiter $$
	Create procedure sp_ListarTipoEmpleados()
		Begin
			select 
				T.codigoTipoEmpleado,
                T.descripcion
                From TipoEmpleado T;
        End$$
Delimiter ;

call sp_ListarTipoEmpleados();

--  BUSCAR TIPO EMPLEADO -----------------
Delimiter $$
	Create procedure sp_BuscarTipoEmpleado(in codTipoEmpleado int)
		Begin
			select 
				T.codigoTipoEmpleado,
                T.descripcion
                From TipoEmpleado T
					where T.codigoTipoEmpleado = codTipoEmpleado;
        End$$
Delimiter ;

call sp_BuscarTipoEmpleado(2);

-- ---------------------------- TIPO PLATO ----------------------------------------------------

-- AGREGAR TIPO PLATO ---------------
Delimiter $$
	Create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(100))
		Begin 
			Insert into TipoPlato(descripcionTipo)
				values(descripcionTipo);
        End$$
Delimiter ;

call sp_AgregarTipoPlato('Plato llano');
call sp_AgregarTipoPlato('Plato hondo');
call sp_AgregarTipoPlato('Plato de postre');
call sp_AgregarTipoPlato('Platos de café');
call sp_AgregarTipoPlato('Plato de Pan');
call sp_AgregarTipoPlato('Plato de Presentación');
call sp_AgregarTipoPlato('Ensaladera');
call sp_AgregarTipoPlato('Plato de Aperitivos');
call sp_AgregarTipoPlato('Plato de Sopa');
call sp_AgregarTipoPlato('Plato Ovalado');

-- EDITAR TIPO PLATO -----------------
Delimiter $$ 
	Create procedure sp_EditarTipoPlato(in codTipoPlato int, in descrip varchar(100))
		Begin
			Update TipoPlato T 
				set T.descripcionTipo = descrip
                where T.codigoTipoPlato = codTipoPlato;
        End$$
Delimiter ;

call sp_EditarTipoPlato(2,'Papas');

-- ELIMINAR TIPO PLATO --------------
Delimiter $$
	Create procedure sp_EliminarTipoPlato(in codTipoPlato int)
		Begin
			Delete from TipoPlato
				where codigoTipoPlato = codTipoPlato;
        End$$
Delimiter ;


-- call sp_EliminarTipoPlato(1);

-- LISTAR TIPO PLATOS -----------------
Delimiter $$
	Create procedure sp_ListarTipoPlatos()
		Begin
			Select 
				T.codigoTipoPlato,
				T.descripcionTipo
				From TipoPlato T;
        End$$
Delimiter ;

call sp_ListarTipoPlatos();

-- BUSCAR TIPO PLATO ----------------
Delimiter $$
	Create procedure sp_BuscarTipoPlato(in codTipoPlato int)
		Begin
			Select
				T.codigoTipoPlato,
                T.descripcionTipo
                From TipoPlato T
					where T.codigoTipoPlato = codTipoPlato;
        End$$
Delimiter ; 

call sp_BuscarTipoPlato(2);

-- ----------------------------- PRODUCTOS -------------------------------
-- AGREGAR PRODUCTO ----------------
Delimiter $$
	Create procedure sp_AgregarProducto(in nombreProducto varchar(150),in cantidadProducto int)
		Begin
			Insert into Productos(nombreProducto,cantidadProducto)
				values(nombreProducto,cantidadProducto);
        End$$
Delimiter ;

call sp_AgregarProducto('Litros de Leche', 20);
call sp_AgregarProducto('Pescados', 26);
call sp_AgregarProducto('lb de Arroz', 30);
call sp_AgregarProducto('Pescados', 26);
call sp_AgregarProducto('lb de Café', 25);
call sp_AgregarProducto('Alitas', 200);
call sp_AgregarProducto('lb de Frijol', 27);
call sp_AgregarProducto('lb de Papas', 15);
call sp_AgregarProducto('lb de Tomate', 23);
call sp_AgregarProducto('Aguacates', 40);


-- EDITAR PRODUCTO ----------------
Delimiter $$
	Create procedure sp_EditarProducto(in codProducto int, in nomProducto varchar(150),in cant int)
		Begin 
			Update Productos P 
				set P.nombreProducto = nomProducto,
					P.cantidadProducto = cant
                    where P.codigoProducto = codProducto;
        End$$
Delimiter ;

call sp_EditarProducto(1,'Mariscos',30);

-- ELIMINAR PRODUCTO -----------------
Delimiter $$
	Create procedure sp_EliminarProducto(in codProducto int)
		Begin
			Delete from Productos
				where codigoProducto = codProducto;
        End$$
Delimiter ;

-- call sp_EliminarProducto(1);

-- LISTAR PRODUCTOS -------------------
Delimiter $$
	Create procedure sp_ListarProductos()
		Begin
			Select
				P.codigoProducto,
                P.nombreProducto,
                P.cantidadProducto
                From Productos P;
        End$$
Delimiter ;

call sp_ListarProductos();

-- BUSCAR PRODUCTO ----------------
Delimiter $$
	Create procedure sp_BuscarProducto(in codProducto int)
		Begin
			Select
				P.codigoProducto,
                P.nombreProducto,
                P.cantidadProducto
                From Productos P 
					where P.codigoProducto = codProducto;
        End$$
Delimiter ;

call sp_BuscarProducto(2);

-- ----------------------------------- EMPLEADOS ----------------------------------------
-- AGREAGAR EMPLEADO -------------------
Delimiter $$
	Create procedure sp_AgregarEmpleado(in numeroEmpleado int,in nombresEmpleados varchar(150), in apellidosEmpleado varchar(150),
		in direccionEmpleado varchar(150), telefonoContacto varchar(8),in gradoCocinero varchar(50),in codigoTipoEmpleado int)
        Begin
			Insert into Empleados (numeroEmpleado,nombresEmpleados, apellidosEmpleado,  direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado)
				values (numeroEmpleado,nombresEmpleados, apellidosEmpleado,  direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado);
        End$$
Delimiter ;

call sp_AgregarEmpleado(10,'José Luis','Pérez Ramírez','1ra Av Zona 1','23257898','Chef Ejecutivo',1);
call sp_AgregarEmpleado(6,'Oscar Rodolfo','Mundo Sil','5ta Av Zona 10','21659847','Sous Chef',2);
call sp_AgregarEmpleado(1,'Fernando','Ramírez','5ta Av Zona 1','45207894','Camarero',10);
call sp_AgregarEmpleado(5,'Juan','López','8Av. San José Pnula','48706134','Gerente General',7);
call sp_AgregarEmpleado(8,'Luis','Mundo','7Av Lote4. Santa Catarina Pnula','20198356','Chef Repostero',6);
call sp_AgregarEmpleado(2,'Rodolfo','Hernández','7Av Zona 9','21562278','Asistente',5);
call sp_AgregarEmpleado(3,'Alejandro','García','5ta Zona 14','48795474','Chef de Cocina',4);
call sp_AgregarEmpleado(17,'José','López','10Av. boca del Monto','48920147','Chef garde manager',3);
call sp_AgregarEmpleado(4,'Oscar','Pineda','9Av. Zona 12','45698710','Pantry, prep cook',9);
call sp_AgregarEmpleado(9,'Carlos','Martínez','5Av Colonia landivar','56301478','Expenditer',8);

-- EDITAR EMPLEADO ------------------
Delimiter $$
	Create procedure sp_EditarEmpleado(in codEmpleado int,in numEmpleado int,in nombEmpleados varchar(150), in apellEmpleado varchar(150),
		in direccEmpleado varchar(150), telContacto varchar(8),in graCocinero varchar(50))
        Begin
			Update Empleados E
				Set E.numeroEmpleado = numEmpleado,
					E.nombresEmpleados = nombEmpleados,
					E.apellidosEmpleado = apellEmpleado,
                    E.direccionEmpleado = direccEmpleado,
                    E.telefonoContacto = telContacto,
                    E.gradoCocinero = graCocinero
                    where E.codigoEmpleado = codEmpleado;
        End$$
Delimiter ;

call sp_EditarEmpleado(1,5,'Jorge Rodrigo','Segura López','2da Av Zona 7','12365897','Senior Chef');

-- ELIMINAR EMPLEADO --------------------
Delimiter $$
	Create procedure sp_EliminarEmpleado(in codEmpleado int)
		Begin 
			Delete from Empleados
				where codigoEmpleado = codEmpleado;
        End$$
Delimiter ;

-- call sp_EliminarEmpleado(2);

-- LISTAR EMPLEADOS ---------------------
Delimiter $$
	Create procedure sp_ListarEmpleados()
		Begin
			Select
				P.codigoEmpleado, 
                P.numeroEmpleado,
                P.nombresEmpleados, 
                P.apellidosEmpleado, 
                P.direccionEmpleado, 
                P.telefonoContacto, 
                P.gradoCocinero, 
                P.codigoTipoEmpleado
				From Empleados P;
        End$$
Delimiter ;

call sp_ListarEmpleados();

-- BUSCAR EMPLEADO ---------------------
Delimiter $$
	Create procedure sp_BuscarEmpleado(in codEmpleado int)
		Begin
			Select
				P.codigoEmpleado, 
                P.numeroEmpleado,
                P.nombresEmpleados, 
                P.apellidosEmpleado, 
                P.direccionEmpleado, 
                P.telefonoContacto, 
                P.gradoCocinero, 
                P.codigoTipoEmpleado
				From Empleados P
					where P.codigoEmpleado = codEmpleado;
        End$$
Delimiter ;

call sp_BuscarEmpleado(1);

-- ---------------- BUSCAR POR LLAVE FORANEA ---------------------
Delimiter $$
	Create procedure sp_BuscarEmpleadoTipo(in codTipoEmpleado int)
		Begin
			Select
				P.codigoEmpleado, 
                P.numeroEmpleado,
                P.nombresEmpleados, 
                P.apellidosEmpleado, 
                P.direccionEmpleado, 
                P.telefonoContacto, 
                P.gradoCocinero, 
                P.codigoTipoEmpleado
				From Empleados P
					where P.codigoTipoEmpleado = codTipoEmpleado;
        End$$
Delimiter ;

call sp_BuscarEmpleadoTipo(3);
-- ------------------------------------ SERVICIOS ------------------------------------------------
-- AGREGAR SERVICIO --------------------
Delimiter $$
	Create procedure sp_AgregarServicio(in fechaServicio date,in tipoServicio varchar(150),in horaServicio time,
		in lugarServicio varchar(150),in telefonoContacto varchar(8),in codigoEmpresa int)
        Begin 
			Insert into Servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				values(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
        End$$
Delimiter ;

call sp_AgregarServicio('2023-01-21','Limpieza','10:32','Restaurante','23564897', 1);
call sp_AgregarServicio('2023-02-24','Servicio de Mesa','12:20','Restaurante','13257410',2);
call sp_AgregarServicio('2022-05-29','Buffet Asistido','3:55','Restaurante','78204561',5);
call sp_AgregarServicio('2023-06-15','Autoservicio','7:30','Wendy s','13257410',10);
call sp_AgregarServicio('2023-06-2','Degustación','14:20','Cebollines','45056301',3);
call sp_AgregarServicio('2023-08-21','Servico Emplatado','13:40','Restaurante','78921365',7);
call sp_AgregarServicio('2023-09-12','Atención al cliente','15:56','Restaurante','45893017',8);
call sp_AgregarServicio('2023-06-12','Camareros','17:21','MacDonals','45963020',6);
call sp_AgregarServicio('2023-05-10','Buffet','18:45','Estancia','45103789',4);
call sp_AgregarServicio('2023-10-23','Recepcionistas','20:30','Cebollines','89741023',9);

-- EDITAR SERVICIO ------------------
Delimiter $$
	Create procedure sp_EditarServicio (in codServicio int,in feServicio date,in tiServicio varchar(150),in hoServicio time,
		in lugServicio varchar(150),in telContacto varchar(8))
        Begin
			Update Servicios S
				Set S.fechaServicio = feServicio, 
					S.tipoServicio = tiServicio,
					S.horaServicio = hoServicio, 
					S.lugarServicio = lugServicio, 
					S.telefonoContacto = telContacto
					where S.codigoServicio = codServicio;
        End$$
Delimiter ;

call sp_EditarServicio(1,'2023-03-21','Limpieza','11:45','Restaurante','25547889');

-- ELIMINAR SERVICIO -------------------
Delimiter $$
	Create procedure sp_EliminarServicio(in codServicio int)
		Begin
			Delete From Servicios
				where codigoServicio = codServicio;
        End$$
Delimiter ;

-- call sp_EliminarServicio(1);

-- LISTAR SERVICIOS ---------------------
Delimiter $$
	Create procedure sp_ListarServicios()
		Begin
			Select 
				S.codigoServicio,
                S.fechaServicio, 
                S.tipoServicio, 
                S.horaServicio, 
                S.lugarServicio,
                S.telefonoContacto, 
                S.codigoEmpresa
                From Servicios S;
        End$$
Delimiter ;

call sp_ListarServicios();

-- BUSCAR SERVICIO --------------------
Delimiter $$
	Create procedure sp_BuscarServicio(in codServicio int)
		Begin
			Select 
				S.codigoServicio,
                S.fechaServicio, 
                S.tipoServicio, 
                S.horaServicio, 
                S.lugarServicio,
                S.telefonoContacto, 
                S.codigoEmpresa
                From Servicios S
					where S.codigoServicio = codServicio;
        End$$
Delimiter ;

call sp_BuscarServicio(2)

-- ------------------------------------ PRESUPUESTO -------------------------------------
-- AGREAGAR PRESUPUESTO --------------
Delimiter $$
	Create procedure sp_AgregarPresupuesto(in fechaSolicitud date,
		in cantidadPresupuesto decimal(10,2),in codigoEmpresa int)
        Begin
			Insert into Presupuesto (fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
				values (fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
        End$$
Delimiter ;

call sp_AgregarPresupuesto('2022-11-10',55320,1);
call sp_AgregarPresupuesto('2023-12-17',454654,3);
call sp_AgregarPresupuesto('2023-01-12',444575,2);
call sp_AgregarPresupuesto('2023-04-26',6565654,7);
call sp_AgregarPresupuesto('2023-10-24',445422,4);
call sp_AgregarPresupuesto('2023-07-30',141242,5);
call sp_AgregarPresupuesto('2023-03-29',12455,9);
call sp_AgregarPresupuesto('2023-01-25',941123,10);
call sp_AgregarPresupuesto('2023-09-16',445004,6);
call sp_AgregarPresupuesto('2023-02-14',545710,8);

-- EDITAR PRESUPUESTO ------------------
Delimiter $$
	Create procedure sp_EditarPresupuesto(in codPresupuesto int,in fecSolicitud date,
		in cantPresupuesto decimal(10,2))
        Begin
			Update Presupuesto P
				Set P.fechaSolicitud = fecSolicitud,
					P.cantidadPresupuesto = cantPresupuesto
                    where P.codigoPresupuesto = codPresupuesto;
        End$$
Delimiter ;

call sp_EditarPresupuesto(1,'2019-12-23',74562);

-- ELIMINAR PRESUPUESTO -----------------
Delimiter $$
	Create procedure sp_EliminarPresupuesto(in codPresupuesto int)
		Begin
			Delete From Presupuesto
				where codigoPresupuesto = codPresupuesto;
        End$$
Delimiter ;

-- call sp_EliminarPresupuesto(1);

-- LISTAR PRESUPUESTOS -----------------
Delimiter $$
	Create procedure sp_ListarPresupuestos()
		Begin
			Select
				P.codigoPresupuesto,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
                P.codigoEmpresa
                From Presupuesto P;
        End$$
Delimiter ;

call sp_ListarPresupuestos();

-- BUSCAR PRESUPUESTO --------------------
Delimiter $$
	Create procedure sp_BuscarPresupuesto(in codPresupuesto int)
		Begin
			Select
				P.codigoPresupuesto,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
                P.codigoEmpresa
                From Presupuesto P
					where P.codigoPresupuesto = codPresupuesto;
        End$$
Delimiter ;

call sp_BuscarPresupuesto(2);

-- -------------- BUSCAR POR LLAVE FORANEA -------------
Delimiter $$
	Create procedure sp_BuscarPresupuestoEmpresa(in codEmpresa int)
		Begin
			Select
				P.codigoPresupuesto,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
                P.codigoEmpresa
                From Presupuesto P
					where P.codigoEmpresa = codEmpresa;
        End$$
Delimiter ;

call sp_BuscarPresupuestoEmpresa(2);
-- ------------------------------ PLATOS ---------------------------------------------
-- AGREGAR PLATO ---------------------
Delimiter $$
	Create procedure sp_AgregarPlato(in cantidad int,in nombrePlato varchar(50),
		in descripcionPlato varchar(150),in precioPlato decimal(10,2),in codigoTipoPlato int)
		Begin
			Insert into Platos(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
				values(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
        End$$
Delimiter ;

call sp_AgregarPlato(20,'Ceviche','Comida del mediodía',150,1);
call sp_AgregarPlato(15,'Pulpo a la galleta','Comida del mediodía',250,3);
call sp_AgregarPlato(18,'Fiambre','Comida del mediodía',150,4);
call sp_AgregarPlato(25,'Omelet','Desayuno',250,5);
call sp_AgregarPlato(19,'Huevos a la mexicana','Desayuno',70,8);
call sp_AgregarPlato(40,'Pancakes','Desayuno',40,10);
call sp_AgregarPlato(25,'Pasta ','Cena',90,9);
call sp_AgregarPlato(30,'Pechuga de pollo','Cena',120,6);
call sp_AgregarPlato(19,'Milanesa al orno','Almuerzo',130,7);
call sp_AgregarPlato(28,'Empanadas de pollo','Almuerzo',125,2);

-- EDITAR PLATO ---------------------
Delimiter $$
	Create procedure sp_EditarPlato(in codPlato int, in cant int,in nombPlato varchar(50),
		in descPlato varchar(150),in precPlato decimal(10,2))
        Begin
			Update Platos P 
				Set P.cantidad = cant,
					P.nombrePlato = nombPlato,
                    P.descripcionPlato = descPlato,
                    P.precioPlato = precPlato
                    where P.codigoPlato = codPlato;
        End$$
Delimiter ;

call sp_EditarPlato(1,10,'Sopa de pollo','Comida del mediodía',125);

-- ELIMINAR PLATO ------------------
Delimiter $$
	Create procedure sp_EliminarPlato (in codPlato int)
		Begin
			Delete from Platos
				where codigoPlato = codPlato;
        End$$
Delimiter ;

-- call sp_EliminarPlato(1);

-- LISTAR PLATOS --------------------
Delimiter $$
	Create procedure sp_ListarPlatos()
		Begin
			Select
				P.codigoPlato, 
                P.cantidad, 
                P.nombrePlato, 
                P.descripcionPlato, 
                P.precioPlato, 
                P.codigoTipoPlato
                From Platos P;
        End$$
Delimiter ;

call sp_ListarPlatos();

-- BUSCAR PLATO ---------------------
Delimiter $$
	Create procedure sp_BuscarPlato( in codPlato int)
		Begin
			Select
				P.codigoPlato, 
                P.cantidad, 
                P.nombrePlato, 
                P.descripcionPlato, 
                P.precioPlato, 
                P.codigoTipoPlato
                From Platos P
					where P.codigoPlato = codPlato;
        End$$
Delimiter ;

call sp_BuscarPlato(2);

-- ----------------- BUSCAR POR LLAVE FORANEA -------------------
Delimiter $$
	Create procedure sp_BuscarPlatoTipo( in codTipoPlato int)
		Begin
			Select
				P.codigoPlato, 
                P.cantidad, 
                P.nombrePlato, 
                P.descripcionPlato, 
                P.precioPlato, 
                P.codigoTipoPlato
                From Platos P
					where P.codigoTipoPlato = codTipoPlato;
        End$$
Delimiter ;

call sp_BuscarPlatoTipo(2);

-- ---------------------------------------- PRODUCTOS_HAS_PLATOS -----------------------------------
-- AGREGAR PRODUCTO_HAS_PLATO--------------
Delimiter $$
	Create procedure sp_AgregarProductoHasPlato(in Productos_codigoProducto int ,in codigoPlato int,in codigoProducto int)
		Begin
			Insert into Productos_has_Platos(Productos_codigoProducto,codigoPlato, codigoProducto)
				values (Productos_codigoProducto,codigoPlato, codigoProducto);
        End$$
Delimiter ;

call sp_AgregarProductoHasPlato(1,1,2);
call sp_AgregarProductoHasPlato(2,2,1);
call sp_AgregarProductoHasPlato(3,3,2);
call sp_AgregarProductoHasPlato(4,4,4);
call sp_AgregarProductoHasPlato(5,5,3);
call sp_AgregarProductoHasPlato(6,6,5);
call sp_AgregarProductoHasPlato(7,7,6);
call sp_AgregarProductoHasPlato(8,8,7);
call sp_AgregarProductoHasPlato(9,9,8);
call sp_AgregarProductoHasPlato(10,10,9);

-- EDITAR PRODUCTO_HAS_PLATO --------------
Delimiter $$
	Create procedure sp_EditarProductoHasPlato(in Produc_codProducto int,in codPlato int,in codProducto int)
		Begin
			Update Productos_has_Platos P
				set P.codigoPlato = codPlato,
					P.codigoProducto = codProducto
                    where P.Productos_codigoProducto =  Produc_codProducto;
        End$$
Delimiter ;

-- ELIMINAR PRODUCTO_HAS_PLATO -------------
Delimiter $$
	Create procedure sp_EliminarProductoHasPlato(in Produc_codProducto int)
		Begin
			Delete From Productos_has_Platos
				where Productos_codigoProducto = Produc_codProducto;
        End$$
Delimiter ;

-- call sp_EliminarProductoHasPlato(1);

-- LISTAR PRODUCTOS_HAS_PLATOS ------------------
Delimiter $$
	Create procedure sp_ListarProductosHasPlatos()
		Begin
			Select
				P.Productos_codigoProducto,
                P.codigoPlato,
                P.codigoProducto
                From Productos_has_Platos P;
        End$$
Delimiter ;

call sp_ListarProductosHasPlatos();

-- BUSCAR PRODUCTO_HAS_PLATO -----------------
Delimiter $$
	Create procedure sp_BuscarProductoHasPlato(in Productos_codProducto int)
		Begin
			Select
				P.Productos_codigoProducto,
                P.codigoPlato,
                P.codigoProducto
                From Productos_has_Platos P
					where P.Productos_codigoProducto = Productos_codProducto;
        End$$
Delimiter ;

call sp_BuscarProductoHasPlato(2);

-- ---------------------------------- SERVICIOS_HAS_PLATOS ---------------------------
-- AGREGAR SERVICIO_HAS_PLATO ----------------
Delimiter $$
	Create procedure sp_AgregarServicioHasPlato(in Servicios_codigoProducto int,in codigoPlato int,in codigoServicio int) 
		Begin
			Insert into Servicios_has_Platos(Servicios_codigoProducto, codigoPlato, codigoServicio)
				values(Servicios_codigoProducto, codigoPlato, codigoServicio);
        End$$
Delimiter ;

call sp_AgregarServicioHasPlato(1,1,1);
call sp_AgregarServicioHasPlato(2,2,2);
call sp_AgregarServicioHasPlato(3,3,3);
call sp_AgregarServicioHasPlato(4,4,4);
call sp_AgregarServicioHasPlato(5,5,5);
call sp_AgregarServicioHasPlato(6,6,6);
call sp_AgregarServicioHasPlato(7,7,7);
call sp_AgregarServicioHasPlato(8,8,8);
call sp_AgregarServicioHasPlato(9,9,9);
call sp_AgregarServicioHasPlato(10,10,10);

-- EDITAR SERVICIO_HAS_PLATO ------------------
Delimiter $$
	Create procedure sp_EditarServicioHasPlato(in Servi_codProducto int,in codPlato int,in codServicio int) 
		Begin
			Update Servicios_has_Platos S 
				Set S.codigoPlato = codPlato,
					S.codigoServicio = codServico;
        End$$
Delimiter ;

-- ELIMINAR SERVICIO_HAS_PLATO -------------------
Delimiter $$
	Create procedure sp_EliminarServicioHasPlato(in Servi_codProducto int)
		Begin
			Delete From Servicios_has_Platos
				where Servicios_codigoProducto = Servi_codProducto;
        End$$
Delimiter ;

-- call sp_EliminarServicioHasPlato(1);

-- LISTAR SERVICIOS_HAS_PLATOS -----------------
Delimiter $$
	Create procedure sp_ListarServiciosHasPlatos()
		Begin 
			Select
				S.Servicios_codigoProducto,
                S.codigoPlato,
                S.codigoServicio
                From Servicios_has_Platos S;
        End$$
Delimiter ;

call sp_ListarServiciosHasPlatos();

-- BUSCAR SERVICIO_HAS_PLATO ------------
Delimiter $$
	Create procedure sp_BuscarServicioHasPlato(in Servi_codProducto int )
		Begin 
			Select
				S.Servicios_codigoProducto,
                S.codigoPlato,
                S.codigoServicio
                From Servicios_has_Platos S
					where S.Servicios_codigoProducto = Servi_codProducto;
        End$$
Delimiter ;

call sp_BuscarServicioHasPlato(2);

-- ---------------------------------- SERVICIOS_HAS_EMPLEADOS ----------------------------------
-- AGREGAR SERVICIO_HAS_EMPLEADO ---------------
Delimiter $$
	Create procedure sp_AgregarServicioHasEmpleado (in Servicios_codigoServicio int,in codigoServicio int,in codigoEmpleado int,
		in fechaEvento date,in horaEvento time,in lugarEvento varchar(150))
        Begin
			Insert into Servicios_has_Empleados(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
				values (Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
        End$$
Delimiter ;

call sp_AgregarServicioHasEmpleado(1,2,2,'2022-10-03','10:25','Pecorino');
call sp_AgregarServicioHasEmpleado(2,1,1,'2023-11-20','15:20','Party Planet');
call sp_AgregarServicioHasEmpleado(3,4,4,'2023-12-27','18:50','Cebollines');
call sp_AgregarServicioHasEmpleado(4,5,5,'2023-09-14','9:12','Wendy s');
call sp_AgregarServicioHasEmpleado(5,6,6,'2023-06-05','10:59','MacDonalds');
call sp_AgregarServicioHasEmpleado(6,7,7,'2023-03-06','21:45','Lai Lai');
call sp_AgregarServicioHasEmpleado(7,8,8,'2023-04-24','12:39','Estancia');
call sp_AgregarServicioHasEmpleado(8,9,9,'2023-10-03','14:15','Cebollines');
call sp_AgregarServicioHasEmpleado(9,10,10,'2023-02-21','13:49','Burger King');
call sp_AgregarServicioHasEmpleado(10,9,9,'2023-08-25','17:37','Estancia');

-- EDITAR SERVICIO_HAS_EMPLEADO --------------------------
Delimiter $$
	Create procedure sp_EditarServicioHasEmpleado(in Servi_codServicio int,
		in fecEvento date,in horEvento time,in lugEvento varchar(150))
        Begin
			Update Servicios_has_Empleados S 
				Set 
                    S.fechaEvento = fecEvento,
                    S.horaEvento = horEvento,
                    S.lugarEvento = lugEvento
                    where S.Servicios_codigoServicio = Servi_codServicio;
        End$$
Delimiter ;

call sp_EditarServicioHasEmpleado(1,'2020-01-03','9:45','Pecorino');

-- ELIMINAR SERVICIO_HAS_EMPLEADO ----------------------
Delimiter $$
	Create procedure sp_EliminarServicioHasEmpleado(in Servi_codServicio int )
		Begin
			Delete From Servicios_has_Empleados
				where Servicios_codigoServicio = Servi_codServicio;
        End$$
Delimiter ;

-- call sp_EliminarServicioHasEmpleado(1);

-- LISTAR SERVICIOS_HAS_EMPLEADOS ---------------------
Delimiter $$
	Create procedure sp_ListarServiciosHasEmpleados()
		Begin
			Select 
				S.Servicios_codigoServicio, 
                S.codigoServicio, 
                S.codigoEmpleado, 
                S.fechaEvento, 
                S.horaEvento,
                S.lugarEvento
                From Servicios_has_Empleados S;
        End$$
Delimiter ;

call sp_ListarServiciosHasEmpleados();

-- BUSCAR SERVICIO_HAS_EMPLEADO -----------------------
Delimiter $$
	Create procedure sp_BuscarServicioHasEmpleado(in Servi_codServicio int)
		Begin
			Select 
				S.Servicios_codigoServicio, 
                S.codigoServicio, 
                S.codigoEmpleado, 
                S.fechaEvento, 
                S.horaEvento,
                S.lugarEvento
                From Servicios_has_Empleados S
					where S.Servicios_codigoServicio = Servi_codServicio;
        End$$
Delimiter ;

call sp_BuscarServicioHasEmpleado(2);

-- --------------------- REPORTE GENERAL --------------------------------------
Delimiter $$
	Create procedure sp_ReporteGeneral(in codEmpresa int)
		Begin
			select
				E.nombreEmpresa,
                E.direccion,
                E.telefono,
				P.fechaSolicitud,
                P.cantidadPresupuesto,
                S.FechaServicio, 
                S.tipoServicio, 
                S.horaServicio,
                S.telefonoContacto,
                SE.fechaEvento,
				SE.horaEvento,
                SE.lugarEvento,
                EM.nombresEmpleados,
                EM.apellidosEmpleado,
                EM.gradoCocinero,
                EM.direccionEmpleado,
                TE.descripcion,
                PL.cantidad,
                PL.nombrePlato,
                PL.descripcionPlato,
                PL.precioPlato,
                TP.descripcionTipo,
                PD.nombreProducto,
			    PD.cantidadProducto
				from Empresas E
				inner join Presupuesto P
				on E.codigoEmpresa = P.codigoEmpresa
				inner join Servicios S
				on S.codigoEmpresa = E.codigoEmpresa
                inner join Servicios_has_Empleados SE
                on SE.codigoServicio = S.codigoServicio
                inner join Empleados EM
                on EM.codigoEmpleado = SE.codigoEmpleado
                inner join TipoEmpleado TE
                on TE.codigoTipoEmpleado = EM.codigoTipoEmpleado
                inner join Servicios_has_Platos SP
				on SP.codigoServicio = S.codigoServicio
                inner join Platos PL
                on PL.codigoPlato = SP.codigoPlato
                inner join TipoPlato TP
                on TP.codigoTipoPlato = PL.codigoTipoPlato
                inner join Productos_has_Platos PP
                on PP.codigoPlato = PL.codigoPlato
                inner join Productos PD
                on PD.codigoProducto = PP.codigoProducto
                where E.codigoEmpresa = codEmpresa;
        End$$
Delimiter ;

call sp_ReporteGeneral(3);

-- ---------------------------
Delimiter $$
	create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), 
		in apellidoUsuario varchar(100),in usuarioLogin varchar(50),in contrasena varchar(50))
        Begin
			Insert into Usuario (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
				values(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
        end$$
Delimiter ;
call sp_AgregarUsuario('Misael','López','MisaelL','12345678');
call sp_AgregarUsuario('Pedro','Armas','parmas','parmas');
Delimiter $$
	create procedure sp_ListarUsuarios()
		Begin
			select
				U.codigoUsuario,
                U.nombreUsuario,
                U.apellidoUsuario,
                U.usuarioLogin,
                U.contrasena
			From Usuario U;
        end$$
Delimiter ;

call sp_ListarUsuarios();

-- ALTER USER 'misael'@'localhost' IDENTIFIED WITH mysql_native_password BY 'misael';

