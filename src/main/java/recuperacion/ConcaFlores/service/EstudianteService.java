package recuperacion.ConcaFlores.service;

import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.EstudianteDTO;
import recuperacion.ConcaFlores.entity.Estudiante;
import recuperacion.ConcaFlores.exception.BusinessException;
import recuperacion.ConcaFlores.exception.ResourceNotFoundException;
import recuperacion.ConcaFlores.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    @Transactional(readOnly = true)
    public List<EstudianteDTO> listarTodos() {
        return estudianteRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstudianteDTO> listarActivos() {
        return estudianteRepository.findByEstadoTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstudianteDTO buscarPorId(Integer id) {
        Estudiante e = estudianteRepository.findById(id)
                .filter(Estudiante::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", id));
        return toDTO(e);
    }

    @Transactional
    public EstudianteDTO crear(EstudianteDTO dto) {
        if (estudianteRepository.existsByDni(dto.getDni())) {
            throw new BusinessException("Ya existe un estudiante con el DNI: " + dto.getDni());
        }
        if (estudianteRepository.existsByCorreo(dto.getCorreo())) {
            throw new BusinessException("Ya existe un estudiante con el correo: " + dto.getCorreo());
        }
        Estudiante estudiante = toEntity(dto);
        estudiante.setEstado(true);
        return toDTO(estudianteRepository.save(estudiante));
    }

    @Transactional
    public EstudianteDTO actualizar(Integer id, EstudianteDTO dto) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .filter(Estudiante::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", id));

        if (estudianteRepository.existsByDniAndIdEstudianteNot(dto.getDni(), id)) {
            throw new BusinessException("El DNI " + dto.getDni() + " ya está en uso por otro estudiante");
        }
        if (estudianteRepository.existsByCorreoAndIdEstudianteNot(dto.getCorreo(), id)) {
            throw new BusinessException("El correo " + dto.getCorreo() + " ya está en uso por otro estudiante");
        }

        estudiante.setDni(dto.getDni());
        estudiante.setNombres(dto.getNombres());
        estudiante.setApellidos(dto.getApellidos());
        estudiante.setCorreo(dto.getCorreo());
        estudiante.setTelefono(dto.getTelefono());
        estudiante.setDireccion(dto.getDireccion());
        estudiante.setFechaNacimiento(dto.getFechaNacimiento());

        return toDTO(estudianteRepository.save(estudiante));
    }

    @Transactional
    public void eliminar(Integer id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .filter(Estudiante::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", id));
        estudiante.setDeletedAt(LocalDateTime.now());
        estudiante.setEstado(false);
        estudianteRepository.save(estudiante);
    }

    @Transactional
    public void restaurar(Integer id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", id));
        estudiante.setDeletedAt(null);
        estudiante.setRestoredAt(LocalDateTime.now());
        estudiante.setEstado(true);
        estudianteRepository.save(estudiante);
    }

    private EstudianteDTO toDTO(Estudiante e) {
        return EstudianteDTO.builder()
                .idEstudiante(e.getIdEstudiante())
                .dni(e.getDni())
                .nombres(e.getNombres())
                .apellidos(e.getApellidos())
                .correo(e.getCorreo())
                .telefono(e.getTelefono())
                .direccion(e.getDireccion())
                .fechaNacimiento(e.getFechaNacimiento())
                .estado(e.getEstado())
                .deletedAt(e.getDeletedAt())
                .restoredAt(e.getRestoredAt())
                .build();
    }

    private Estudiante toEntity(EstudianteDTO dto) {
        return Estudiante.builder()
                .dni(dto.getDni())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .correo(dto.getCorreo())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .fechaNacimiento(dto.getFechaNacimiento())
                .build();
    }
}
