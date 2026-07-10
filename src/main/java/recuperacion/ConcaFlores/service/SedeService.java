package recuperacion.ConcaFlores.service;

import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.SedeDTO;
import recuperacion.ConcaFlores.entity.Sede;
import recuperacion.ConcaFlores.exception.BusinessException;
import recuperacion.ConcaFlores.exception.ResourceNotFoundException;
import recuperacion.ConcaFlores.repository.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SedeService {

    private final SedeRepository sedeRepository;

    @Transactional(readOnly = true)
    public List<SedeDTO> listarTodos() {
        return sedeRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SedeDTO> listarActivos() {
        return sedeRepository.findByEstadoTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SedeDTO buscarPorId(Integer id) {
        Sede sede = sedeRepository.findById(id)
                .filter(Sede::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", id));
        return toDTO(sede);
    }

    @Transactional
    public SedeDTO crear(SedeDTO dto) {
        if (sedeRepository.existsByCodigoSede(dto.getCodigoSede())) {
            throw new BusinessException("Ya existe una sede con el código: " + dto.getCodigoSede());
        }
        Sede sede = toEntity(dto);
        sede.setEstado(true);
        return toDTO(sedeRepository.save(sede));
    }

    @Transactional
    public SedeDTO actualizar(Integer id, SedeDTO dto) {
        Sede sede = sedeRepository.findById(id)
                .filter(Sede::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", id));

        sede.setCodigoSede(dto.getCodigoSede());
        sede.setNombre(dto.getNombre());
        sede.setDireccion(dto.getDireccion());
        sede.setCiudad(dto.getCiudad());
        return toDTO(sedeRepository.save(sede));
    }

    @Transactional
    public void eliminar(Integer id) {
        Sede sede = sedeRepository.findById(id)
                .filter(Sede::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", id));
        sede.setDeletedAt(LocalDateTime.now());
        sede.setEstado(false);
        sedeRepository.save(sede);
    }

    @Transactional
    public void restaurar(Integer id) {
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", id));
        sede.setDeletedAt(null);
        sede.setRestoredAt(LocalDateTime.now());
        sede.setEstado(true);
        sedeRepository.save(sede);
    }

    private SedeDTO toDTO(Sede s) {
        return SedeDTO.builder()
                .idSede(s.getIdSede())
                .codigoSede(s.getCodigoSede())
                .nombre(s.getNombre())
                .direccion(s.getDireccion())
                .ciudad(s.getCiudad())
                .estado(s.getEstado())
                .deletedAt(s.getDeletedAt())
                .restoredAt(s.getRestoredAt())
                .build();
    }

    private Sede toEntity(SedeDTO dto) {
        return Sede.builder()
                .codigoSede(dto.getCodigoSede())
                .nombre(dto.getNombre())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .build();
    }
}
