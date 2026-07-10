package recuperacion.ConcaFlores.service;

import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.CarreraDTO;
import recuperacion.ConcaFlores.entity.Carrera;
import recuperacion.ConcaFlores.exception.BusinessException;
import recuperacion.ConcaFlores.exception.ResourceNotFoundException;
import recuperacion.ConcaFlores.repository.CarreraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository carreraRepository;

    @Transactional(readOnly = true)
    public List<CarreraDTO> listarTodos() {
        return carreraRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CarreraDTO> listarActivos() {
        return carreraRepository.findByEstadoTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CarreraDTO buscarPorId(Integer id) {
        Carrera carrera = carreraRepository.findById(id)
                .filter(Carrera::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera", id));
        return toDTO(carrera);
    }

    @Transactional
    public CarreraDTO crear(CarreraDTO dto) {
        if (carreraRepository.existsByCodigoCarrera(dto.getCodigoCarrera())) {
            throw new BusinessException("Ya existe una carrera con el código: " + dto.getCodigoCarrera());
        }
        Carrera carrera = toEntity(dto);
        carrera.setEstado(true);
        return toDTO(carreraRepository.save(carrera));
    }

    @Transactional
    public CarreraDTO actualizar(Integer id, CarreraDTO dto) {
        Carrera carrera = carreraRepository.findById(id)
                .filter(Carrera::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera", id));

        carrera.setCodigoCarrera(dto.getCodigoCarrera());
        carrera.setNombre(dto.getNombre());
        carrera.setDuracionCiclos(dto.getDuracionCiclos());
        carrera.setInversion(dto.getInversion());
        return toDTO(carreraRepository.save(carrera));
    }

    @Transactional
    public void eliminar(Integer id) {
        Carrera carrera = carreraRepository.findById(id)
                .filter(Carrera::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera", id));
        carrera.setDeletedAt(LocalDateTime.now());
        carrera.setEstado(false);
        carreraRepository.save(carrera);
    }

    @Transactional
    public void restaurar(Integer id) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera", id));
        carrera.setDeletedAt(null);
        carrera.setRestoredAt(LocalDateTime.now());
        carrera.setEstado(true);
        carreraRepository.save(carrera);
    }

    private CarreraDTO toDTO(Carrera c) {
        return CarreraDTO.builder()
                .idCarrera(c.getIdCarrera())
                .codigoCarrera(c.getCodigoCarrera())
                .nombre(c.getNombre())
                .duracionCiclos(c.getDuracionCiclos())
                .inversion(c.getInversion())
                .estado(c.getEstado())
                .deletedAt(c.getDeletedAt())
                .restoredAt(c.getRestoredAt())
                .build();
    }

    private Carrera toEntity(CarreraDTO dto) {
        return Carrera.builder()
                .codigoCarrera(dto.getCodigoCarrera())
                .nombre(dto.getNombre())
                .duracionCiclos(dto.getDuracionCiclos())
                .inversion(dto.getInversion())
                .build();
    }
}
