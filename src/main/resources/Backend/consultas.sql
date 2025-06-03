select * from usuarios;
select * from roles;

select * from estudiantes;
select * from docentes;
select * from entidadesColaborativas;
select * from tutoresExternos;
select * from directoresCarrera;


select * from proyectos;
select * from actividades;
select * from informes;
select * from conveniosPPS;
select * from planesdetrabajos;

select * from Relacion_Actividad_Informes;
select * from Relacion_PlanDeTrabajo_Actividades;
select * from Relacion_Entidad_Proyectos;
select * from Relacion_Tutor_Proyectos;
select * from Relacion_Docente_Estudiantes;


drop database algolab_gpps;
drop table estudiantes;
drop table usuarios;
drop table tutoresExternos;




DELETE E 
FROM Estudiantes E 
JOIN Usuarios U ON E.idUsuario = U.idUsuario 
WHERE E.idEstudiante IS NOT NULL;

DELETE U, E 
FROM Usuarios U 
JOIN Estudiantes E ON E.idUsuario = U.idUsuario 
WHERE U.idUsuario IS NOT NULL;

DELETE FROM `usuarios` WHERE (`idUsuario` is not 	null);
