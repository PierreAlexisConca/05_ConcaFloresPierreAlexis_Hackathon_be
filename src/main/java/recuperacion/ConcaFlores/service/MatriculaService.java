package recuperacion.ConcaFlores.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.CursoDTO;
import recuperacion.ConcaFlores.dto.MatriculaRequestDTO;
import recuperacion.ConcaFlores.dto.MatriculaResponseDTO;
import recuperacion.ConcaFlores.entity.*;
import recuperacion.ConcaFlores.exception.BusinessException;
import recuperacion.ConcaFlores.exception.ResourceNotFoundException;
import recuperacion.ConcaFlores.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final PromotorRepository promotorRepository;
    private final SedeRepository sedeRepository;
    private final CarreraRepository carreraRepository;
    private final CursoRepository cursoRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listar() {
        return matriculaRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MatriculaResponseDTO buscarPorId(Integer id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula", id));
        return toResponseDTO(matricula);
    }

    /**
     * Registra una matrícula invocando el procedimiento almacenado sp_RegistrarMatricula
     * para respetar la regla de negocio de no duplicados.
     * Luego persiste el detalle de cursos desde Java.
     */
    @Transactional
    public MatriculaResponseDTO registrar(MatriculaRequestDTO dto) {

        // 1. Validar que las entidades relacionadas existan y estén activas
        Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                .filter(Estudiante::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", dto.getIdEstudiante()));

        Promotor promotor = promotorRepository.findByIdPromotorAndDeletedAtIsNull(dto.getIdPromotor())
                .orElseThrow(() -> new ResourceNotFoundException("Promotor", dto.getIdPromotor()));

        Sede sede = sedeRepository.findById(dto.getIdSede())
                .filter(Sede::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", dto.getIdSede()));

        Carrera carrera = carreraRepository.findById(dto.getIdCarrera())
                .filter(Carrera::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera", dto.getIdCarrera()));

        // 2. Validar duplicado en backend antes de llamar al SP
        boolean duplicado = matriculaRepository
                .existsByEstudiante_IdEstudianteAndCarrera_IdCarreraAndPeriodo(
                        dto.getIdEstudiante(), dto.getIdCarrera(), dto.getPeriodo());
        if (duplicado) {
            throw new BusinessException(
                    "El estudiante ya está matriculado en esa carrera durante el período " + dto.getPeriodo());
        }

        // 3. Llamar al procedimiento almacenado sp_RegistrarMatricula
        StoredProcedureQuery sp = entityManager
                .createStoredProcedureQuery("sp_RegistrarMatricula");
        sp.registerStoredProcedureParameter("codigo",   String.class,          ParameterMode.IN);
        sp.registerStoredProcedureParameter("fecha",    java.sql.Date.class,   ParameterMode.IN);
        sp.registerStoredProcedureParameter("periodo",  String.class,          ParameterMode.IN);
        sp.registerStoredProcedureParameter("monto",    java.math.BigDecimal.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("id_estudiante", Integer.class,    ParameterMode.IN);
        sp.registerStoredProcedureParameter("id_promotor",   Integer.class,    ParameterMode.IN);
        sp.registerStoredProcedureParameter("id_sede",        Integer.class,   ParameterMode.IN);
        sp.registerStoredProcedureParameter("id_carrera",     Integer.class,   ParameterMode.IN);

        sp.setParameter("codigo",       dto.getCodigoMatricula());
        sp.setParameter("fecha",        java.sql.Date.valueOf(dto.getFechaMatricula()));
        sp.setParameter("periodo",      dto.getPeriodo());
        sp.setParameter("monto",        dto.getMontoTotal());
        sp.setParameter("id_estudiante", dto.getIdEstudiante());
        sp.setParameter("id_promotor",   dto.getIdPromotor());
        sp.setParameter("id_sede",        dto.getIdSede());
        sp.setParameter("id_carrera",     dto.getIdCarrera());

        sp.execute();

        // 4. Recuperar la matrícula recién creada para agregar el detalle
        Matricula matricula = matriculaRepository
                .findByCodigoMatricula(dto.getCodigoMatricula())
                .orElseThrow(() -> new BusinessException(
                        "No se pudo recuperar la matrícula registrada: " + dto.getCodigoMatricula()));

        // 5. Registrar detalle de cursos
        List<Curso> cursos = dto.getIdCursos().stream()
                .map(idCurso -> cursoRepository.findById(idCurso)
                        .filter(Curso::getEstado)
                        .orElseThrow(() -> new ResourceNotFoundException("Curso", idCurso)))
                .collect(Collectors.toList());

        for (Curso curso : cursos) {
            DetalleMatricula detalle = DetalleMatricula.builder()
                    .matricula(matricula)
                    .curso(curso)
                    .build();
            detalleMatriculaRepository.save(detalle);
        }

        return toResponseDTO(matricula);
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listarPorPromotor(Integer idPromotor) {
        return matriculaRepository.findByPromotor_IdPromotor(idPromotor)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listarPorEstudiante(Integer idEstudiante) {
        return matriculaRepository.findByEstudiante_IdEstudiante(idEstudiante)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public MatriculaResponseDTO actualizar(Integer id, MatriculaRequestDTO dto) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula", id));

        // Validar que las entidades relacionadas existan y estén activas
        Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                .filter(Estudiante::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", dto.getIdEstudiante()));

        Promotor promotor = promotorRepository.findById(dto.getIdPromotor())
                .orElseThrow(() -> new ResourceNotFoundException("Promotor", dto.getIdPromotor()));

        Sede sede = sedeRepository.findById(dto.getIdSede())
                .filter(Sede::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", dto.getIdSede()));

        Carrera carrera = carreraRepository.findById(dto.getIdCarrera())
                .filter(Carrera::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera", dto.getIdCarrera()));

        // Actualizar campos
        matricula.setFechaMatricula(dto.getFechaMatricula());
        matricula.setPeriodo(dto.getPeriodo());
        matricula.setMontoTotal(dto.getMontoTotal());
        matricula.setEstudiante(estudiante);
        matricula.setPromotor(promotor);
        matricula.setSede(sede);
        matricula.setCarrera(carrera);

        // Actualizar detalles de cursos
        detalleMatriculaRepository.deleteByMatricula_IdMatricula(id);
        List<Curso> cursos = dto.getIdCursos().stream()
                .map(idCurso -> cursoRepository.findById(idCurso)
                        .filter(Curso::getEstado)
                        .orElseThrow(() -> new ResourceNotFoundException("Curso", idCurso)))
                .collect(Collectors.toList());

        for (Curso curso : cursos) {
            DetalleMatricula detalle = DetalleMatricula.builder()
                    .matricula(matricula)
                    .curso(curso)
                    .build();
            detalleMatriculaRepository.save(detalle);
        }

        return toResponseDTO(matriculaRepository.save(matricula));
    }

    /**
     * Borrado lógico: marca deleted_at y estado=false.
     * El registro NUNCA se elimina físicamente.
     */
    @Transactional
    public void eliminar(Integer id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula", id));

        matricula.setDeletedAt(java.time.LocalDateTime.now());
        matricula.setEstado(false);
        matriculaRepository.save(matricula);
    }

    @Transactional
    public void restaurar(Integer id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula", id));
        matricula.setDeletedAt(null);
        matricula.setRestoredAt(java.time.LocalDateTime.now());
        matricula.setEstado(true);
        matriculaRepository.save(matricula);
    }

    // -------------------------------------------------------
    // Mapeo
    // -------------------------------------------------------

    private MatriculaResponseDTO toResponseDTO(Matricula m) {
        List<CursoDTO> cursos = detalleMatriculaRepository
                .findByMatricula_IdMatricula(m.getIdMatricula())
                .stream()
                .map(d -> CursoDTO.builder()
                        .idCurso(d.getCurso().getIdCurso())
                        .codigoCurso(d.getCurso().getCodigoCurso())
                        .nombre(d.getCurso().getNombre())
                        .creditos(d.getCurso().getCreditos())
                        .idCarrera(d.getCurso().getCarrera().getIdCarrera())
                        .nombreCarrera(d.getCurso().getCarrera().getNombre())
                        .idProfesor(d.getCurso().getProfesor().getIdProfesor())
                        .nombreProfesor(d.getCurso().getProfesor().getNombres()
                                + " " + d.getCurso().getProfesor().getApellidos())
                        .estado(d.getCurso().getEstado())
                        .build())
                .collect(Collectors.toList());

        return MatriculaResponseDTO.builder()
                .idMatricula(m.getIdMatricula())
                .codigoMatricula(m.getCodigoMatricula())
                .fechaMatricula(m.getFechaMatricula())
                .periodo(m.getPeriodo())
                .montoTotal(m.getMontoTotal())
                .estado(m.getEstado())
                .deletedAt(m.getDeletedAt())
                .restoredAt(m.getRestoredAt())
                .idEstudiante(m.getEstudiante().getIdEstudiante())
                .nombreEstudiante(m.getEstudiante().getNombres() + " " + m.getEstudiante().getApellidos())
                .dniEstudiante(m.getEstudiante().getDni())
                .idPromotor(m.getPromotor().getIdPromotor())
                .nombrePromotor(m.getPromotor().getNombres() + " " + m.getPromotor().getApellidos())
                .codigoPromotor(m.getPromotor().getCodigoPromotor())
                .idSede(m.getSede().getIdSede())
                .nombreSede(m.getSede().getNombre())
                .idCarrera(m.getCarrera().getIdCarrera())
                .nombreCarrera(m.getCarrera().getNombre())
                .cursos(cursos)
                .build();
    }
}
