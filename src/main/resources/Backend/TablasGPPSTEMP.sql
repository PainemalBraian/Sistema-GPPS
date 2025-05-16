Create database AlgoLab_GPPS;
USE AlgoLab_GPPS;

-- -----------------  Usuarios ------------------------------------------------------------------
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
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) on delete cascade
);
CREATE TABLE Docentes(
    idDocente INT auto_increment PRIMARY KEY,
    idUsuario INT NOT NULL,
    legajo VARCHAR(50) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) on delete cascade
);

CREATE TABLE EntidadesColaborativas(
    idEntidad INT auto_increment PRIMARY KEY,
    idUsuario INT NOT NULL,
    nombreEntidad VARCHAR(250) NOT NULL,
    cuit VARCHAR(50) NOT NULL,
    direccionEntidad VARCHAR(50) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) on delete cascade
);

CREATE TABLE TutoresExternos(
    idTutor INT auto_increment PRIMARY KEY,
    idUsuario INT NOT NULL,
    nombreEntidadColaborativa VARCHAR(250) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) on delete cascade
);

CREATE TABLE DirectoresCarrera (
    idDirector INT AUTO_INCREMENT PRIMARY KEY,
    idUsuario INT NOT NULL,
    nombreCompleto VARCHAR(255) NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) ON DELETE CASCADE
);

-- ------------------Elementos de PPS----------------------------------------------------

CREATE TABLE Proyectos(
    idProyecto INT auto_increment PRIMARY KEY,
    titulo VARCHAR(250) NOT NULL,
    habilitado boolean not null,
    descripcion VARCHAR(250) NOT NULL,
    areaDeInteres VARCHAR(250) NOT NULL,
    ubicacion VARCHAR(250) NOT NULL,
    objetivos VARCHAR(250) NOT NULL,
    requisitos VARCHAR(250) NOT NULL,
    idTutor INT NOT NULL,
    FOREIGN KEY (idTutor) REFERENCES TutoresExternos(idTutor)
);

CREATE TABLE Actividades (
    idActividad INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    duracion int not null,
    descripcion TEXT,
    fecha_fin DATE NOT NULL,
    calificacion BOOLEAN DEFAULT NULL -- NULL indica "calificación no cargada"
);

CREATE TABLE Informes (
    idInforme INT AUTO_INCREMENT PRIMARY KEY,
    idActividad INT,
    nombre VARCHAR(255) NOT NULL,
    archivo_pdf LONGBLOB NOT NULL, -- Contenido del PDF
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idActividad) REFERENCES Actividades(idActividad)
);

CREATE TABLE ConveniosPPS (
    idConvenio INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    habilitado BOOLEAN DEFAULT FALSE,
    idProyecto INT NOT NULL,
    idDocente INT NOT NULL,
    idEstudiante INT NOT NULL,
    idDirector INT NOT NULL,
    idEntidad INT NOT NULL,
    FOREIGN KEY (idProyecto) REFERENCES Proyectos(idProyecto) ON DELETE CASCADE,
    FOREIGN KEY (idDocente) REFERENCES Docentes(idDocente) ON DELETE CASCADE,
    FOREIGN KEY (idEstudiante) REFERENCES Estudiantes(idEstudiante) ON DELETE CASCADE,
    FOREIGN KEY (idDirector) REFERENCES DirectoresCarrera(idDirector) ON DELETE CASCADE,
    FOREIGN KEY (idEntidad) REFERENCES EntidadesColaborativas(idEntidad) ON DELETE CASCADE
);


-- --------------- RELACIONES -----------------------------------------------

CREATE TABLE Relacion_Docente_Estudiantes(
    idEstudiante INT NOT NULL,
    idDocente INT NOT NULL,
    FOREIGN KEY (idEstudiante) REFERENCES Estudiantes(idEstudiante), 
    FOREIGN KEY (idDocente) REFERENCES Docentes(idDocente),
    PRIMARY KEY (idEstudiante, idDocente)  -- Evita duplicados
);

CREATE TABLE Relacion_Tutor_Proyectos(
    idTutor INT NOT NULL,
    idProyecto INT NOT NULL,
    FOREIGN KEY (idTutor) REFERENCES TutoresExternos(idTutor) on delete cascade,
    FOREIGN KEY (idProyecto) REFERENCES Proyectos(idProyecto) on delete cascade,
    PRIMARY KEY (idTutor, idProyecto)  -- Evita duplicados 
);

CREATE TABLE Relacion_Entidad_Proyectos(
    idEntidad INT NOT NULL,
    idProyecto INT NOT NULL,
    FOREIGN KEY (idEntidad) REFERENCES EntidadesColaborativas(idEntidad) on delete cascade,
    FOREIGN KEY (idProyecto) REFERENCES Proyectos(idProyecto) on delete cascade,
    PRIMARY KEY (idEntidad, idProyecto)  -- Evita duplicados
);

-- Relación entre ConvenioPPS y Actividades
CREATE TABLE Relacion_Convenio_Actividades (
    idConvenio INT NOT NULL,
    idActividad INT NOT NULL,
    PRIMARY KEY (idConvenio, idActividad),
    FOREIGN KEY (idConvenio) REFERENCES ConveniosPPS(idConvenio) ON DELETE CASCADE,
    FOREIGN KEY (idActividad) REFERENCES Actividades(idActividad) ON DELETE CASCADE
);


-- ---------------- CARGAS -------------------------------------------------------------------

INSERT INTO Roles (idRol, nombre, activo) VALUES
(1, 'Estudiante', true),
(2, 'Docente', true),
(3, 'Representante de Entidad Colaboradora', true),
(4, 'Tutor externo', true),
(5, 'Director de Carrera', true),
(6, 'Administrador', true);
