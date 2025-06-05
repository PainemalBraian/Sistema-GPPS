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
    idEstudiante INT PRIMARY KEY,
    idUsuario INT NOT NULL,
    matricula VARCHAR(255) NOT NULL,
    carrera VARCHAR(255) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) on delete cascade,
    CHECK (idEstudiante = idUsuario)
);
CREATE TABLE Docentes(
    idDocente INT PRIMARY KEY,
    idUsuario INT NOT NULL,
    legajo VARCHAR(50) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) on delete cascade,
    CHECK (idDocente = idUsuario)
);

CREATE TABLE EntidadesColaborativas(
    idEntidad INT PRIMARY KEY,
    idUsuario INT NOT NULL,
    nombreEntidad VARCHAR(250) NOT NULL,
    cuit VARCHAR(50) NOT NULL,
    direccionEntidad VARCHAR(50) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) on delete cascade,
    CHECK (idEntidad = idUsuario)
);

CREATE TABLE TutoresExternos(
    idTutor INT PRIMARY KEY,
    idUsuario INT NOT NULL,
    nombreEntidadColaborativa VARCHAR(250) NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) on delete cascade,
    CHECK (idTutor = idUsuario)
);

CREATE TABLE DirectoresCarrera (
    idDirector INT PRIMARY KEY,
    idUsuario INT NOT NULL,
    nombreCompleto VARCHAR(255) NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) ON DELETE CASCADE,
    CHECK (idDirector = idUsuario)
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
    idTutor INT,
    FOREIGN KEY (idTutor) REFERENCES TutoresExternos(idTutor)
);

CREATE TABLE Actividades (
    idActividad INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    duracion int not null,
    descripcion TEXT,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    calificacion BOOLEAN DEFAULT NULL -- NULL indica "calificación no cargada"
);

CREATE TABLE Informes (
    idInforme INT AUTO_INCREMENT PRIMARY KEY,
    idActividad INT,
    titulo VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255),
    porcentajeAvance INT,
    archivo_pdf LONGBLOB, -- Contenido del PDF
    fecha DATE NOT NULL,
    FOREIGN KEY (idActividad) REFERENCES Actividades(idActividad)
);

CREATE TABLE PlanesDeTrabajos (
    idPlanDeTrabajo INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    habilitado BOOLEAN DEFAULT TRUE,
    idDocente INT NOT NULL,
    idTutor INT NOT NULL,
    idInformeFinal INT,
--  relacion de Lista de Actividades - Plan
    FOREIGN KEY (idDocente) REFERENCES Docentes(idDocente) ON DELETE CASCADE,
    FOREIGN KEY (idTutor) REFERENCES TutoresExternos(idTutor) ON DELETE CASCADE,
    FOREIGN KEY (idInformeFinal) REFERENCES Informes(idInforme) ON DELETE CASCADE
);

CREATE TABLE ConveniosPPS (
    idConvenio INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    habilitado BOOLEAN DEFAULT TRUE,
    idProyecto INT NOT NULL,
    idEstudiante INT NOT NULL,
    idEntidad INT NOT NULL,
    idPlan INT ,
    FOREIGN KEY (idProyecto) REFERENCES Proyectos(idProyecto) ON DELETE CASCADE,
    FOREIGN KEY (idPlan) REFERENCES PlanesDeTrabajos(idPlanDeTrabajo) ON DELETE CASCADE,
    FOREIGN KEY (idEstudiante) REFERENCES Estudiantes(idEstudiante) ON DELETE CASCADE,
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

-- Relación entre Planes de Trabajos y Actividades
CREATE TABLE Relacion_PlanDeTrabajo_Actividades (
    idPlan INT NOT NULL,
    idActividad INT NOT NULL,
    PRIMARY KEY (idPlan, idActividad),
    FOREIGN KEY (idPlan) REFERENCES PlanesDeTrabajos(idPlanDeTrabajo) ON DELETE CASCADE,
    FOREIGN KEY (idActividad) REFERENCES Actividades(idActividad) ON DELETE CASCADE
);

CREATE TABLE Relacion_Actividad_Informes (
    idActividad INT NOT NULL,
    idInforme INT NOT NULL,
    PRIMARY KEY (idInforme, idActividad),
    FOREIGN KEY (idInforme) REFERENCES Informes(idInforme) ON DELETE CASCADE,
    FOREIGN KEY (idActividad) REFERENCES Actividades(idActividad) ON DELETE CASCADE
);


-- ---------------- CARGAS -------------------------------------------------------------------

INSERT INTO Roles (idRol, nombre, activo) VALUES
(-10, 'TEMPORAL', false),
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

INSERT INTO Usuarios (idUsuario,username, password, nombreCompleto, email, idRol, activo) VALUES
(-10,"TEMPORAL",123456789, 'XXXXXX','XXXXXX',-10,false);

-- Estudiantes
INSERT INTO Estudiantes (idEstudiante, idUsuario, matricula, carrera) VALUES
(-10,-10, 'XXXXXX','XXXXXX'),
(1,1, 'MAT123', 'Ingeniería de Software');

-- Docentes
INSERT INTO Docentes (idDocente, idUsuario, legajo) VALUES
(-10,-10, 'XXXXXX'),
(2,2, 'LEG456');

-- Entidades Colaborativas
INSERT INTO EntidadesColaborativas (idEntidad, idUsuario, nombreEntidad, cuit, direccionEntidad) VALUES
(-10,-10, 'TEMPORAL','00000000000','TEMPORAL'),
(3,3, 'Entidad ABC', '30123456789', 'Calle Falsa 123');

-- Tutores Externos
INSERT INTO TutoresExternos (idTutor,idUsuario, nombreEntidadColaborativa) VALUES
(-10,-10, 'TEMPORAL'),
(4,4, 'Entidad ABC');

-- Directores de Carrera
INSERT INTO DirectoresCarrera (idDirector,idUsuario, nombreCompleto) VALUES
(-10,-10, 'XXXXXX'),
(5,5, 'Ana Rodriguez');

-- ----------Carga Elementos PPS--------------

INSERT INTO PlanesDeTrabajos (titulo, descripcion, habilitado, idDocente, idTutor, idInformeFinal) VALUES
('Plan Energía Solar', 'Plan de trabajo para proyecto solar', true, 2, 4, null),
('Plan IoT', 'Plan de trabajo para proyecto IoT', true, 2, 4, null),
('Plan SiS', 'Plan de trabajo para proyecto SiS', true, 2, 4, null);

INSERT INTO PlanesDeTrabajos (idPlanDeTrabajo,titulo, descripcion, habilitado, idDocente, idTutor, idInformeFinal) VALUES
(-10,'TEMPORAL', 'TEMPORAL', false, -10, -10, null);

-- Proyectos
INSERT INTO Proyectos (titulo, habilitado, descripcion, areaDeInteres, ubicacion, objetivos, requisitos, idTutor) VALUES
('Proyecto Solar', true, 'Proyecto de energías renovables', 'Energía', 'Campus Universitario', 'Desarrollar paneles solares', 'Conocimientos en energías renovables', 4),
('Proyecto IoT', true, 'Proyecto de dispositivos inteligentes', 'Tecnología', 'Edificio de Informática', 'Crear dispositivos inteligentes', 'Conocimientos en electrónica', 4),
('Proyecto SiS', true, 'Proyecto de Afinidad de conomientos Teóricos', 'Sistemas', 'Salón de informatica', 'Planificación organizada de técnicas de la carrera', 'Conocimientos teoricos de Licenciatura en Sistemas', 4);


-- Convenios PPS
INSERT INTO ConveniosPPS (titulo, descripcion, habilitado, idProyecto, idEstudiante, idEntidad, idPlan) VALUES
('Convenio IoT Inteligente', 'Convenio para dispositivos inteligentes', true, 1, 1, 3, 1),
('Convenio Energía Renovable', 'Convenio para desarrollar paneles solares', true, 2, 1, 3, 2),
('Convenio Practicantes', 'Convenio para practicas estudiantiles', true, 3, 1, 3, 3);


-- Actividades
INSERT INTO Actividades (titulo, duracion, descripcion, fechaInicio, fechaFin, calificacion) VALUES
('Actividad Inicial', 20, 'Introducción al proyecto', '2024-12-20', '2024-12-30', NULL),
('Actividad Avanzada', 30, 'Desarrollo de funcionalidades', '2024-12-10', '2024-12-30', NULL),
('Actividad Intermedia 1', 15, 'Definición de Requerimientos', '2024-12-01', '2024-12-30', NULL),
('Actividad Intermedia 2', 20, 'Definición de Casos de Usos', '2024-12-02', '2024-12-30', NULL),
('Actividad Intermedia 3', 25, 'Definición de Diagrama de Clases', '2024-12-03', '2024-12-30', NULL);

INSERT INTO Informes (idActividad, titulo, descripcion, archivo_pdf, fecha) VALUES
(1, 'Informe Act Inicial','Descripcion A', NULL, '2024-12-31'),
(1, 'Informe Act Inicial 1','Descripcion B', NULL, '2024-12-25'),
(1, 'Informe Act Inicial 2','Descripcion C', NULL, '2024-12-20'),
(2, 'Informe Act Avanzada 1','Descripcion D', NULL, '2024-12-23'),
(2, 'Informe Act Avanzada 2','Descripcion E', NULL, '2025-01-15'),
(3, 'Informe Act Intermedia','Descripcion F', NULL, '2024-12-15');

INSERT INTO Relacion_Entidad_Proyectos (idEntidad, idProyecto) VALUES
(3, 1),
(3, 2),
(3, 3);

INSERT INTO Relacion_Tutor_Proyectos (idTutor, idProyecto) VALUES
(4, 1),
(4, 2),
(4, 3);

INSERT INTO Relacion_Docente_Estudiantes (idDocente, idEstudiante) VALUES
(2, 1);


INSERT INTO Relacion_Actividad_Informes (idActividad, idInforme) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 4),
(2, 5),
(3, 6);

INSERT INTO Relacion_PlanDeTrabajo_Actividades (idPlan, idActividad) VALUES
(1, 1), -- Plan Energía Solar incluye Actividad Inicial
(2, 2), -- Plan IoT incluye Actividad Avanzada
(3, 3), -- Plan SiS incluye Actividad Intermedia 1
(3, 4), -- Plan SiS incluye Actividad Intermedia 2
(3, 5); -- Plan SiS incluye Actividad Intermedia 3

