package recuperacion.ConcaFlores.service;

import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.ProfesorDTO;
import recuperacion.ConcaFlores.entity.Profesor;
import recuperacion.ConcaFlores.exception.BusinessException;
import recuperacion.ConcaFlores.exception.ResourceNotFoundException;
import recuperacion.ConcaFlores.repository.ProfesorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    @Transactional(readOnly = true)
    public List<ProfesorDTO> listarTodos() {
        return profesorRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProfesorDTO> listarActivos() {
        return profesorRepository.findByEstadoTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfesorDTO buscarPorId(Integer id) {
        Profesor profesor = profesorRepository.findById(id)
                .filter(Profesor::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", id));
        return toDTO(profesor);
    }

    @Transactional
    public ProfesorDTO crear(ProfesorDTO dto) {
        if (profesorRepository.existsByDni(dto.getDni())) {
            throw new BusinessException("Ya existe un profesor con el DNI: " + dto.getDni());
        }
        if (profesorRepository.existsByCorreo(dto.getCorreo())) {
            throw new BusinessException("Ya existe un profesor con el correo: " + dto.getCorreo());
        }
        Profesor profesor = toEntity(dto);
        profesor.setEstado(true);
        return toDTO(profesorRepository.save(profesor));
    }

    @Transactional
    public ProfesorDTO actualizar(Integer id, ProfesorDTO dto) {
        Profesor profesor = profesorRepository.findById(id)
                .filter(Profesor::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", id));

        if (profesorRepository.existsByDniAndIdProfesorNot(dto.getDni(), id)) {
            throw new BusinessException("El DNI " + dto.getDni() + " ya está en uso por otro profesor");
        }
        if (profesorRepository.existsByCorreoAndIdProfesorNot(dto.getCorreo(), id)) {
            throw new BusinessException("El correo " + dto.getCorreo() + " ya está en uso por otro profesor");
        }

        profesor.setCodigoProfesor(dto.getCodigoProfesor());
        profesor.setDni(dto.getDni());
        profesor.setNombres(dto.getNombres());
        profesor.setApellidos(dto.getApellidos());
        profesor.setEspecialidad(dto.getEspecialidad());
        profesor.setCorreo(dto.getCorreo());
        profesor.setTelefono(dto.getTelefono());

        return toDTO(profesorRepository.save(profesor));
    }

    @Transactional
    public void eliminar(Integer id) {
        Profesor profesor = profesorRepository.findById(id)
                .filter(Profesor::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", id));
        profesor.setDeletedAt(LocalDateTime.now());
        profesor.setEstado(false);
        profesorRepository.save(profesor);
    }

    @Transactional
    public void restaurar(Integer id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", id));
        profesor.setDeletedAt(null);
        profesor.setRestoredAt(LocalDateTime.now());
        profesor.setEstado(true);
        profesorRepository.save(profesor);
    }

    private ProfesorDTO toDTO(Profesor p) {
        return ProfesorDTO.builder()
                .idProfesor(p.getIdProfesor())
                .codigoProfesor(p.getCodigoProfesor())
                .dni(p.getDni())
                .nombres(p.getNombres())
                .apellidos(p.getApellidos())
                .especialidad(p.getEspecialidad())
                .correo(p.getCorreo())
                .telefono(p.getTelefono())
                .estado(p.getEstado())
                .deletedAt(p.getDeletedAt())
                .restoredAt(p.getRestoredAt())
                .build();
    }

    private Profesor toEntity(ProfesorDTO dto) {
        return Profesor.builder()
                .codigoProfesor(dto.getCodigoProfesor())
                .dni(dto.getDni())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .especialidad(dto.getEspecialidad())
                .correo(dto.getCorreo())
                .telefono(dto.getTelefono())
                .build();
    }
}
