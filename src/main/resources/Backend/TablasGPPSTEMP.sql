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
    archivo_pdf LONGBLOB, -- Contenido del PDF
    fecha DATE NOT NULL,
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

-- Usuarios y Roles
INSERT INTO Usuarios (username, password, nombreCompleto, email, idRol, activo) VALUES
('estudiante', 'contrasena123', 'Juan Perez', 'juan.perez@mail.com', 1, true),
('docente', 'contrasena123', 'Maria Gomez', 'maria.gomez@mail.com', 2, true),
('entidad', 'contrasena123', 'Entidad ABC', 'entidad.abc@mail.com', 3, true),
('tutor', 'contrasena123', 'Carlos Torres', 'carlos.torres@mail.com', 4, true),
('director', 'contrasena123', 'Ana Rodriguez', 'ana.rodriguez@mail.com', 5, true),
('admin', 'contrasena123', 'Administrador General', 'admin@mail.com', 6, true);

-- Estudiantes
INSERT INTO Estudiantes (idUsuario, matricula, carrera) VALUES
(1, 'MAT123', 'Ingeniería de Software');

-- Docentes
INSERT INTO Docentes (idUsuario, legajo) VALUES
(2, 'LEG456');

-- Entidades Colaborativas
INSERT INTO EntidadesColaborativas (idUsuario, nombreEntidad, cuit, direccionEntidad) VALUES
(3, 'Entidad ABC', '30123456789', 'Calle Falsa 123');

-- Tutores Externos
INSERT INTO TutoresExternos (idUsuario, nombreEntidadColaborativa) VALUES
(4, 'Entidad ABC');

-- Directores de Carrera
INSERT INTO DirectoresCarrera (idUsuario, nombreCompleto) VALUES
(5, 'Ana Rodriguez');


-- ----------Carga Elementos PPS--------------

-- Proyectos
INSERT INTO Proyectos (titulo, habilitado, descripcion, areaDeInteres, ubicacion, objetivos, requisitos, idTutor) VALUES
('Proyecto Solar', true, 'Proyecto de energías renovables', 'Energía', 'Campus Universitario', 'Desarrollar paneles solares', 'Conocimientos en energías renovables', 1),
('Proyecto IoT', true, 'Proyecto de dispositivos inteligentes', 'Tecnología', 'Edificio de Informática', 'Crear dispositivos inteligentes', 'Conocimientos en electrónica', 1);

-- Actividades
INSERT INTO Actividades (titulo, duracion, descripcion, fecha_fin, calificacion) VALUES
('Actividad Inicial', 20, 'Introducción al proyecto', '2024-12-15', NULL),
('Actividad Avanzada', 30, 'Desarrollo de funcionalidades', '2024-12-20', NULL);


-- Convenios PPS
INSERT INTO ConveniosPPS (titulo, descripcion, habilitado, idProyecto, idDocente, idEstudiante, idDirector, idEntidad) VALUES
('Convenio Energía Renovable', 'Convenio para desarrollar paneles solares', true, 1, 1, 1, 1, 1),
('Convenio IoT Inteligente', 'Convenio para dispositivos inteligentes', true, 2, 1, 1, 1, 1);


-- Relación entre Convenio y Actividades
INSERT INTO Relacion_Convenio_Actividades (idConvenio, idActividad) VALUES
(1, 1), -- Convenio 1 con Actividad Inicial
(2, 2); -- Convenio 2 con Actividad Avanzada
