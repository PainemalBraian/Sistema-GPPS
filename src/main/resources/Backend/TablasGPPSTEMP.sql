Create database AlgoLab_GPPS; 
USE AlgoLab_GPPS; 
CREATE TABLE Roles (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL
);

CREATE TABLE Usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombreCompleto VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    idRol INT NOT NULL,
    activo BOOLEAN NOT NULL,
    FOREIGN KEY (idRol) REFERENCES Roles(idRol)
);

CREATE TABLE Estudiantes(
    idEstudiante INT auto_increment PRIMARY KEY,
    idUsuario INT NOT NULL,
    matricula VARCHAR(255) NOT NULL,
    carrera VARCHAR(255) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);
CREATE TABLE Docentes(
    idDocente INT auto_increment PRIMARY KEY,
    idUsuario INT NOT NULL,
    legajo VARCHAR(50) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);
CREATE TABLE Relacion_Docentes_Estudiantes(
    idEstudiante INT NOT NULL,
    idDocente INT NOT NULL,
    FOREIGN KEY (idEstudiante) REFERENCES Estudiantes(idEstudiante),
    FOREIGN KEY (idDocente) REFERENCES Docentes(idDocente),
    PRIMARY KEY (idEstudiante, idDocente)  -- Evita duplicados
);
CREATE TABLE EntidadesColaborativas(
    idEntidad INT auto_increment PRIMARY KEY,
    idUsuario INT NOT NULL,
    nombreEntidad VARCHAR(250) NOT NULL,
    cuit VARCHAR(50) NOT NULL,
    direccionEntidad VARCHAR(50) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);

CREATE TABLE TutoresExternos(
    idTutor INT auto_increment PRIMARY KEY,
    idUsuario INT NOT NULL,
    nombreEntidadColaborativa VARCHAR(250) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);

CREATE TABLE Proyectos(
    idProyecto INT auto_increment PRIMARY KEY,
    titulo VARCHAR(250) NOT NULL,
    descripcion VARCHAR(250) NOT NULL,
    areaDeInteres VARCHAR(250) NOT NULL,
    ubicacion VARCHAR(250) NOT NULL,
    objetivos VARCHAR(250) NOT NULL,
    requisitos VARCHAR(250) NOT NULL,
    idTutor INT NOT NULL,
    FOREIGN KEY (idTutor) REFERENCES TutoresExternos(idTutor)
);



CREATE TABLE Relacion_Tutores_Proyectos(
    idTutor INT NOT NULL,
    idProyecto INT NOT NULL,
    FOREIGN KEY (idTutor) REFERENCES TutoresExternos(idTutor),
    FOREIGN KEY (idProyecto) REFERENCES Proyectos(idProyecto),
    PRIMARY KEY (idTutor, idProyecto)  -- Evita duplicados
);

CREATE TABLE Relacion_Entidades_Proyectos(
    idEntidad INT NOT NULL,
    idProyecto INT NOT NULL,
    FOREIGN KEY (idEntidad) REFERENCES EntidadesColaborativas(idEntidad),
    FOREIGN KEY (idProyecto) REFERENCES Proyectos(idProyecto),
    PRIMARY KEY (idEntidad, idProyecto)  -- Evita duplicados
);

INSERT INTO Roles (idRol, nombre, activo) VALUES
(1, 'Estudiante', true),
(2, 'Docente', true),
(3, 'Representante de Entidad Colaboradora', true),
(4, 'Tutor externo', true),
(5, 'Director de Carrera', true),
(6, 'Administrador', true);
