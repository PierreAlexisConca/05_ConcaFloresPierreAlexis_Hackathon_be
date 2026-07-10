-- ============================================================
-- ELIMINAR BASE DE DATOS SI EXISTE
-- ============================================================

USE master;
GO

IF DB_ID('EduFuturo') IS NOT NULL
BEGIN
    ALTER DATABASE EduFuturo SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE EduFuturo;
END
GO

-- ============================================================
-- DATABASE: EduFuturo - Matrícula Cloud 360
-- ============================================================

CREATE DATABASE EduFuturo;
GO

USE EduFuturo;
GO

-- ============================================================
-- TABLA: Promotores
-- ============================================================
CREATE TABLE Promotores (
    id_promotor         INT IDENTITY(1,1) PRIMARY KEY,
    codigo_promotor     VARCHAR(10) NOT NULL UNIQUE,
    dni                 CHAR(8) NOT NULL UNIQUE,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    telefono            VARCHAR(15) NOT NULL,
    correo              VARCHAR(100) NOT NULL UNIQUE,
    estado              BIT NOT NULL DEFAULT 1,
    deleted_at          DATETIME NULL,
    restored_at         DATETIME NULL,
    -- Restricciones
    CONSTRAINT CHK_Promotor_DNI CHECK(LEN(dni) = 8),
    CONSTRAINT CHK_Promotor_Correo CHECK(correo LIKE '%@%'),
    CONSTRAINT CHK_Promotor_Estado CHECK(estado IN (0, 1)),
    CONSTRAINT CHK_Promotor_Telefono CHECK(LEN(telefono) >= 7)
);
GO

-- ============================================================
-- TABLA: Estudiantes
-- ============================================================
CREATE TABLE Estudiantes (
    id_estudiante       INT IDENTITY(1,1) PRIMARY KEY,
    dni                 CHAR(8) NOT NULL UNIQUE,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    correo              VARCHAR(100) NOT NULL UNIQUE,
    telefono            VARCHAR(15) NOT NULL,
    direccion           VARCHAR(150) NULL,
    fecha_nacimiento    DATE NULL,
    estado              BIT NOT NULL DEFAULT 1,
    deleted_at          DATETIME NULL,
    restored_at         DATETIME NULL,
    -- Restricciones
    CONSTRAINT CHK_Estudiante_DNI CHECK(LEN(dni) = 8),
    CONSTRAINT CHK_Estudiante_Correo CHECK(correo LIKE '%@%'),
    CONSTRAINT CHK_Estudiante_Estado CHECK(estado IN (0, 1)),
    CONSTRAINT CHK_Estudiante_Telefono CHECK(LEN(telefono) >= 7)
);
GO

-- ============================================================
-- TABLA: Sedes
-- ============================================================
CREATE TABLE Sedes (
    id_sede             INT IDENTITY(1,1) PRIMARY KEY,
    codigo_sede         VARCHAR(10) NOT NULL UNIQUE,
    nombre              VARCHAR(100) NOT NULL,
    direccion           VARCHAR(150) NOT NULL,
    ciudad              VARCHAR(50) NOT NULL,
    estado              BIT NOT NULL DEFAULT 1,
    deleted_at          DATETIME NULL,
    restored_at         DATETIME NULL,
    -- Restricciones
    CONSTRAINT CHK_Sede_Estado CHECK(estado IN (0, 1)),
    CONSTRAINT CHK_Sede_Nombre CHECK(LEN(nombre) > 0),
    CONSTRAINT CHK_Sede_Ciudad CHECK(LEN(ciudad) > 0),
    CONSTRAINT CHK_Sede_Codigo CHECK(LEN(codigo_sede) > 0)
);
GO

-- ============================================================
-- TABLA: Carreras
-- ============================================================
CREATE TABLE Carreras (
    id_carrera          INT IDENTITY(1,1) PRIMARY KEY,
    codigo_carrera      VARCHAR(10) NOT NULL UNIQUE,
    nombre              VARCHAR(100) NOT NULL,
    duracion_ciclos     TINYINT NOT NULL,
    inversion           DECIMAL(10, 2) NOT NULL,
    estado              BIT NOT NULL DEFAULT 1,
    deleted_at          DATETIME NULL,
    restored_at         DATETIME NULL,
    -- Restricciones
    CONSTRAINT CHK_Carrera_Estado CHECK(estado IN (0, 1)),
    CONSTRAINT CHK_Carrera_Duracion CHECK(duracion_ciclos > 0 AND duracion_ciclos <= 12),
    CONSTRAINT CHK_Carrera_Inversion CHECK(inversion > 0),
    CONSTRAINT CHK_Carrera_Nombre CHECK(LEN(nombre) > 0)
);
GO

-- ============================================================
-- TABLA: Profesores
-- ============================================================
CREATE TABLE Profesores (
    id_profesor         INT IDENTITY(1,1) PRIMARY KEY,
    codigo_profesor     VARCHAR(10) NOT NULL UNIQUE,
    dni                 CHAR(8) NOT NULL UNIQUE,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    especialidad        VARCHAR(100) NOT NULL,
    correo              VARCHAR(100) NOT NULL UNIQUE,
    telefono            VARCHAR(15) NOT NULL,
    estado              BIT NOT NULL DEFAULT 1,
    deleted_at          DATETIME NULL,
    restored_at         DATETIME NULL,
    -- Restricciones
    CONSTRAINT CHK_Profesor_DNI CHECK(LEN(dni) = 8),
    CONSTRAINT CHK_Profesor_Correo CHECK(correo LIKE '%@%'),
    CONSTRAINT CHK_Profesor_Estado CHECK(estado IN (0, 1)),
    CONSTRAINT CHK_Profesor_Telefono CHECK(LEN(telefono) >= 7)
);
GO

-- ============================================================
-- TABLA: Cursos
-- ============================================================
CREATE TABLE Cursos (
    id_curso            INT IDENTITY(1,1) PRIMARY KEY,
    codigo_curso        VARCHAR(10) NOT NULL UNIQUE,
    nombre              VARCHAR(100) NOT NULL,
    creditos            TINYINT NOT NULL,
    id_carrera          INT NOT NULL,
    id_profesor         INT NOT NULL,
    estado              BIT NOT NULL DEFAULT 1,
    deleted_at          DATETIME NULL,
    restored_at         DATETIME NULL,
    -- Restricciones
    CONSTRAINT CHK_Curso_Estado CHECK(estado IN (0, 1)),
    CONSTRAINT CHK_Curso_Creditos CHECK(creditos BETWEEN 1 AND 6),
    CONSTRAINT CHK_Curso_Nombre CHECK(LEN(nombre) > 0),
    CONSTRAINT FK_Curso_Carrera FOREIGN KEY(id_carrera) REFERENCES Carreras(id_carrera),
    CONSTRAINT FK_Curso_Profesor FOREIGN KEY(id_profesor) REFERENCES Profesores(id_profesor)
);
GO

-- ============================================================
-- TABLA: Matrículas
-- ============================================================
CREATE TABLE Matriculas (
    id_matricula        INT IDENTITY(1,1) PRIMARY KEY,
    codigo_matricula    VARCHAR(15) NOT NULL UNIQUE,
    fecha_matricula     DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    periodo             VARCHAR(10) NOT NULL,
    monto_total         DECIMAL(10, 2) NOT NULL,
    id_estudiante       INT NOT NULL,
    id_promotor         INT NOT NULL,
    id_sede             INT NOT NULL,
    id_carrera          INT NOT NULL,
    estado              BIT NOT NULL DEFAULT 1,
    deleted_at          DATETIME NULL,
    restored_at         DATETIME NULL,
    -- Restricciones
    CONSTRAINT CHK_Matricula_Estado CHECK(estado IN (0, 1)),
    CONSTRAINT CHK_Matricula_Monto CHECK(monto_total > 0),
    CONSTRAINT CHK_Matricula_Periodo CHECK(LEN(periodo) > 0),
    CONSTRAINT UQ_Matricula UNIQUE(id_estudiante, id_carrera, periodo),
    CONSTRAINT FK_Matricula_Estudiante FOREIGN KEY(id_estudiante) REFERENCES Estudiantes(id_estudiante),
    CONSTRAINT FK_Matricula_Promotor FOREIGN KEY(id_promotor) REFERENCES Promotores(id_promotor),
    CONSTRAINT FK_Matricula_Sede FOREIGN KEY(id_sede) REFERENCES Sedes(id_sede),
    CONSTRAINT FK_Matricula_Carrera FOREIGN KEY(id_carrera) REFERENCES Carreras(id_carrera)
);
GO

-- ============================================================
-- TABLA: Detalle_Matrícula
-- ============================================================
CREATE TABLE Detalle_Matricula (
    id_detalle          INT IDENTITY(1,1) PRIMARY KEY,
    id_matricula        INT NOT NULL,
    id_curso            INT NOT NULL,
    -- Restricciones
    CONSTRAINT FK_Detalle_Matricula FOREIGN KEY(id_matricula) REFERENCES Matriculas(id_matricula),
    CONSTRAINT FK_Detalle_Curso FOREIGN KEY(id_curso) REFERENCES Cursos(id_curso),
    CONSTRAINT UQ_Detalle_Matricula_Curso UNIQUE(id_matricula, id_curso)
);
GO

-- ============================================================
-- DATOS: Promotores
-- ============================================================
INSERT INTO Promotores (codigo_promotor, dni, nombres, apellidos, telefono, correo, estado)
VALUES
    ('PRO-001', '71234567', 'Carlos', 'Ramirez', '987654321', 'carlos@edufuturo.com', 1),
    ('PRO-002', '71234568', 'María', 'Lopez', '987654322', 'maria@edufuturo.com', 1),
    ('PRO-003', '71234569', 'José', 'Perez', '987654323', 'jose@edufuturo.com', 1),
    ('PRO-004', '71234570', 'Ana', 'Torres', '987654324', 'ana@edufuturo.com', 1),
    ('PRO-005', '71234571', 'Luis', 'Soto', '987654325', 'luis@edufuturo.com', 1),
    ('PRO-006', '71234572', 'Rosa', 'Vargas', '987654326', 'rosa@edufuturo.com', 1),
    ('PRO-007', '71234573', 'Pedro', 'Flores', '987654327', 'pedro@edufuturo.com', 1),
    ('PRO-008', '71234574', 'Lucía', 'Diaz', '987654328', 'lucia@edufuturo.com', 1),
    ('PRO-009', '71234575', 'Miguel', 'Rojas', '987654329', 'miguel@edufuturo.com', 1),
    ('PRO-010', '71234576', 'Sandra', 'Castillo', '987654330', 'sandra@edufuturo.com', 1);
GO

-- ============================================================
-- DATOS: Sedes
-- ============================================================
INSERT INTO Sedes (codigo_sede, nombre, direccion, ciudad, estado)
VALUES
    ('SED-001', 'Lima Centro', 'Av. Arequipa 120', 'Lima', 1),
    ('SED-002', 'Lima Norte', 'Av. Túpac Amaru 560', 'Lima', 1),
    ('SED-003', 'Arequipa', 'Av. Ejército 450', 'Arequipa', 1),
    ('SED-004', 'Trujillo', 'Av. España 120', 'Trujillo', 1),
    ('SED-005', 'Chiclayo', 'Av. Balta 880', 'Chiclayo', 1);
GO

-- ============================================================
-- DATOS: Carreras
-- ============================================================
INSERT INTO Carreras (codigo_carrera, nombre, duracion_ciclos, inversion, estado)
VALUES
    ('CAR-101', 'Desarrollo de Software', 6, 8500.00, 1),
    ('CAR-102', 'Administración de Empresas', 6, 7600.00, 1),
    ('CAR-103', 'Marketing y Publicidad Digital', 6, 7800.00, 1),
    ('CAR-104', 'Animación Digital 3D', 6, 9100.00, 1);
GO

-- ============================================================
-- DATOS: Estudiantes
-- ============================================================
INSERT INTO Estudiantes (dni, nombres, apellidos, correo, telefono, direccion, fecha_nacimiento, estado)
VALUES
    ('75111111', 'Juan', 'Pérez', 'juan@gmail.com', '900000001', 'Los Olivos', '2004-02-15', 1),
    ('75111112', 'Andrea', 'Ruiz', 'andrea@gmail.com', '900000002', 'Comas', '2003-04-10', 1),
    ('75111113', 'Kevin', 'Salas', 'kevin@gmail.com', '900000003', 'SJL', '2005-01-20', 1),
    ('75111114', 'Lucía', 'Mendoza', 'lucia@gmail.com', '900000004', 'Callao', '2004-11-01', 1),
    ('75111115', 'Diego', 'Ríos', 'diego@gmail.com', '900000005', 'Lince', '2005-03-12', 1),
    ('75111116', 'Valeria', 'Castro', 'valeria@gmail.com', '900000006', 'Ate', '2004-05-16', 1),
    ('75111117', 'Marco', 'Silva', 'marco@gmail.com', '900000007', 'Surco', '2003-07-25', 1),
    ('75111118', 'Paola', 'León', 'paola@gmail.com', '900000008', 'Breña', '2005-02-09', 1),
    ('75111119', 'Jorge', 'Campos', 'jorge@gmail.com', '900000009', 'SMP', '2004-08-30', 1),
    ('75111120', 'Camila', 'Ortega', 'camila@gmail.com', '900000010', 'Independencia', '2005-06-01', 1);
GO

-- ============================================================
-- DATOS: Profesores
-- ============================================================
INSERT INTO Profesores (codigo_profesor, dni, nombres, apellidos, especialidad, correo, telefono, estado)
VALUES
    ('DOC-001', '72111111', 'Fernando', 'Gomez', 'Programación', 'fernando@edufuturo.com', '955000001', 1),
    ('DOC-002', '72111112', 'Patricia', 'Vega', 'Base de Datos', 'patricia@edufuturo.com', '955000002', 1),
    ('DOC-003', '72111113', 'Ricardo', 'Paredes', 'Redes', 'ricardo@edufuturo.com', '955000003', 1),
    ('DOC-004', '72111114', 'Daniel', 'Sánchez', 'Administración', 'daniel@edufuturo.com', '955000004', 1),
    ('DOC-005', '72111115', 'Julia', 'Navarro', 'Marketing', 'julia@edufuturo.com', '955000005', 1),
    ('DOC-006', '72111116', 'Oscar', 'Luna', 'Diseño 3D', 'oscar@edufuturo.com', '955000006', 1),
    ('DOC-007', '72111117', 'Mónica', 'Suárez', 'UX/UI', 'monica@edufuturo.com', '955000007', 1),
    ('DOC-008', '72111118', 'César', 'Pinto', 'Ciberseguridad', 'cesar@edufuturo.com', '955000008', 1);
GO

-- ============================================================
-- DATOS: Cursos
-- ============================================================
INSERT INTO Cursos (codigo_curso, nombre, creditos, id_carrera, id_profesor, estado)
VALUES
    ('CUR-001', 'Programación I', 4, 1, 1, 1),
    ('CUR-002', 'Base de Datos', 4, 1, 2, 1),
    ('CUR-003', 'Redes', 3, 1, 3, 1),
    ('CUR-004', 'UX/UI', 3, 1, 7, 1),
    ('CUR-005', 'Administración I', 4, 2, 4, 1),
    ('CUR-006', 'Contabilidad', 4, 2, 4, 1),
    ('CUR-007', 'Economía', 3, 2, 4, 1),
    ('CUR-008', 'Gestión Empresarial', 3, 2, 4, 1),
    ('CUR-009', 'Marketing Digital', 4, 3, 5, 1),
    ('CUR-010', 'Publicidad', 3, 3, 5, 1),
    ('CUR-011', 'Community Manager', 3, 3, 5, 1),
    ('CUR-012', 'SEO', 3, 3, 5, 1),
    ('CUR-013', 'Modelado 3D', 4, 4, 6, 1),
    ('CUR-014', 'Animación', 4, 4, 6, 1),
    ('CUR-015', 'Renderizado', 3, 4, 6, 1),
    ('CUR-016', 'Post Producción', 3, 4, 6, 1);
GO

-- ============================================================
-- STORED PROCEDURE: Registrar Matrícula
-- ============================================================
CREATE PROCEDURE sp_RegistrarMatricula
    @codigo VARCHAR(15),
    @fecha DATE,
    @periodo VARCHAR(10),
    @monto DECIMAL(10, 2),
    @id_estudiante INT,
    @id_promotor INT,
    @id_sede INT,
    @id_carrera INT
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (
        SELECT 1
        FROM Matriculas
        WHERE id_estudiante = @id_estudiante
            AND id_carrera = @id_carrera
            AND periodo = @periodo
            AND deleted_at IS NULL
    )
    BEGIN
        PRINT 'ERROR: El estudiante ya está matriculado en esa carrera durante ese período.';
        RETURN;
    END

    INSERT INTO Matriculas (
        codigo_matricula,
        fecha_matricula,
        periodo,
        monto_total,
        id_estudiante,
        id_promotor,
        id_sede,
        id_carrera,
        estado
    )
    VALUES (
        @codigo,
        @fecha,
        @periodo,
        @monto,
        @id_estudiante,
        @id_promotor,
        @id_sede,
        @id_carrera,
        1
    );

    PRINT 'Matrícula registrada correctamente.';
END;
GO

-- ============================================================
-- STORED PROCEDURES: Restaurar
-- ============================================================
CREATE PROCEDURE sp_RestaurarPromotor
    @id_promotor INT
AS
BEGIN
    UPDATE Promotores 
    SET deleted_at = NULL, restored_at = GETDATE(), estado = 1
    WHERE id_promotor = @id_promotor;
END;
GO

CREATE PROCEDURE sp_RestaurarEstudiante
    @id_estudiante INT
AS
BEGIN
    UPDATE Estudiantes 
    SET deleted_at = NULL, restored_at = GETDATE(), estado = 1
    WHERE id_estudiante = @id_estudiante;
END;
GO

CREATE PROCEDURE sp_RestaurarSede
    @id_sede INT
AS
BEGIN
    UPDATE Sedes 
    SET deleted_at = NULL, restored_at = GETDATE(), estado = 1
    WHERE id_sede = @id_sede;
END;
GO

CREATE PROCEDURE sp_RestaurarCarrera
    @id_carrera INT
AS
BEGIN
    UPDATE Carreras 
    SET deleted_at = NULL, restored_at = GETDATE(), estado = 1
    WHERE id_carrera = @id_carrera;
END;
GO

CREATE PROCEDURE sp_RestaurarProfesor
    @id_profesor INT
AS
BEGIN
    UPDATE Profesores 
    SET deleted_at = NULL, restored_at = GETDATE(), estado = 1
    WHERE id_profesor = @id_profesor;
END;
GO

CREATE PROCEDURE sp_RestaurarCurso
    @id_curso INT
AS
BEGIN
    UPDATE Cursos 
    SET deleted_at = NULL, restored_at = GETDATE(), estado = 1
    WHERE id_curso = @id_curso;
END;
GO

-- ============================================================
-- VERIFICACIÓN DE DATOS
-- ============================================================
SELECT * FROM Promotores;
SELECT * FROM Estudiantes;
SELECT * FROM Sedes;
SELECT * FROM Carreras;
SELECT * FROM Profesores;
SELECT * FROM Cursos;
SELECT * FROM Matriculas;
SELECT * FROM Detalle_Matricula;
