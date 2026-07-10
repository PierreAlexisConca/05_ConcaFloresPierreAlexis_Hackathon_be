package recuperacion.ConcaFlores.service;

import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.CursoDTO;
import recuperacion.ConcaFlores.entity.Carrera;
import recuperacion.ConcaFlores.entity.Curso;
import recuperacion.ConcaFlores.entity.Profesor;
import recuperacion.ConcaFlores.exception.BusinessException;
import recuperacion.ConcaFlores.exception.ResourceNotFoundException;
import recuperacion.ConcaFlores.repository.CarreraRepository;
import recuperacion.ConcaFlores.repository.CursoRepository;
import recuperacion.ConcaFlores.repository.ProfesorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CarreraRepository carreraRepository;
    private final ProfesorRepository profesorRepository;

    @Transactional(readOnly = true)
    public List<CursoDTO> listarTodos() {
        return cursoRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> listarActivos() {
        return cursoRepository.findByEstadoTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> listarPorCarrera(Integer idCarrera) {
        return cursoRepository.findByCarrera_IdCarreraAndEstadoTrue(idCarrera)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CursoDTO buscarPorId(Integer id) {
        Curso curso = cursoRepository.findById(id)
                .filter(Curso::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
        return toDTO(curso);
    }

    @Transactional
    public CursoDTO crear(CursoDTO dto) {
        if (cursoRepository.existsByCodigoCurso(dto.getCodigoCurso())) {
            throw new BusinessException("Ya existe un curso con el código: " + dto.getCodigoCurso());
        }
        Carrera carrera = carreraRepository.findById(dto.getIdCarrera())
                .orElseThrow(() -> new ResourceNotFoundException("Carrera", dto.getIdCarrera()));
        Profesor profesor = profesorRepository.findById(dto.getIdProfesor())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", dto.getIdProfesor()));

        Curso curso = Curso.builder()
                .codigoCurso(dto.getCodigoCurso())
                .nombre(dto.getNombre())
                .creditos(dto.getCreditos())
                .carrera(carrera)
                .profesor(profesor)
                .estado(true)
                .build();
        return toDTO(cursoRepository.save(curso));
    }

    @Transactional
    public CursoDTO actualizar(Integer id, CursoDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .filter(Curso::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));

        Carrera carrera = carreraRepository.findById(dto.getIdCarrera())
                .orElseThrow(() -> new ResourceNotFoundException("Carrera", dto.getIdCarrera()));
        Profesor profesor = profesorRepository.findById(dto.getIdProfesor())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", dto.getIdProfesor()));

        curso.setCodigoCurso(dto.getCodigoCurso());
        curso.setNombre(dto.getNombre());
        curso.setCreditos(dto.getCreditos());
        curso.setCarrera(carrera);
        curso.setProfesor(profesor);
        return toDTO(cursoRepository.save(curso));
    }

    @Transactional
    public void eliminar(Integer id) {
        Curso curso = cursoRepository.findById(id)
                .filter(Curso::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
        curso.setDeletedAt(LocalDateTime.now());
        curso.setEstado(false);
        cursoRepository.save(curso);
    }

    @Transactional
    public void restaurar(Integer id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
        curso.setDeletedAt(null);
        curso.setRestoredAt(LocalDateTime.now());
        curso.setEstado(true);
        cursoRepository.save(curso);
    }

    private CursoDTO toDTO(Curso c) {
        return CursoDTO.builder()
                .idCurso(c.getIdCurso())
                .codigoCurso(c.getCodigoCurso())
                .nombre(c.getNombre())
                .creditos(c.getCreditos())
                .idCarrera(c.getCarrera().getIdCarrera())
                .nombreCarrera(c.getCarrera().getNombre())
                .idProfesor(c.getProfesor().getIdProfesor())
                .nombreProfesor(c.getProfesor().getNombres() + " " + c.getProfesor().getApellidos())
                .estado(c.getEstado())
                .deletedAt(c.getDeletedAt())
                .restoredAt(c.getRestoredAt())
                .build();
    }
}
