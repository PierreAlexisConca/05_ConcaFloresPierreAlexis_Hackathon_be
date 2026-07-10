package recuperacion.ConcaFlores.service;

import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.PromotorDTO;
import recuperacion.ConcaFlores.entity.Promotor;
import recuperacion.ConcaFlores.exception.BusinessException;
import recuperacion.ConcaFlores.exception.ResourceNotFoundException;
import recuperacion.ConcaFlores.repository.PromotorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotorService {

    private final PromotorRepository promotorRepository;

    @Transactional(readOnly = true)
    public List<PromotorDTO> listarTodos() {
        return promotorRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PromotorDTO> listarActivos() {
        return promotorRepository.findByDeletedAtIsNull()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PromotorDTO buscarPorId(Integer id) {
        Promotor promotor = promotorRepository.findByIdPromotorAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotor", id));
        return toDTO(promotor);
    }

    @Transactional
    public PromotorDTO crear(PromotorDTO dto) {
        if (promotorRepository.existsByDni(dto.getDni())) {
            throw new BusinessException("Ya existe un promotor con el DNI: " + dto.getDni());
        }
        if (promotorRepository.existsByCorreo(dto.getCorreo())) {
            throw new BusinessException("Ya existe un promotor con el correo: " + dto.getCorreo());
        }
        Promotor promotor = toEntity(dto);
        promotor.setEstado(true);
        promotor.setDeletedAt(null);
        return toDTO(promotorRepository.save(promotor));
    }

    @Transactional
    public PromotorDTO actualizar(Integer id, PromotorDTO dto) {
        Promotor promotor = promotorRepository.findByIdPromotorAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotor", id));

        if (promotorRepository.existsByDniAndIdPromotorNot(dto.getDni(), id)) {
            throw new BusinessException("El DNI " + dto.getDni() + " ya está en uso por otro promotor");
        }
        if (promotorRepository.existsByCorreoAndIdPromotorNot(dto.getCorreo(), id)) {
            throw new BusinessException("El correo " + dto.getCorreo() + " ya está en uso por otro promotor");
        }

        promotor.setCodigoPromotor(dto.getCodigoPromotor());
        promotor.setDni(dto.getDni());
        promotor.setNombres(dto.getNombres());
        promotor.setApellidos(dto.getApellidos());
        promotor.setTelefono(dto.getTelefono());
        promotor.setCorreo(dto.getCorreo());

        return toDTO(promotorRepository.save(promotor));
    }

    /**
     * Borrado lógico: marca deleted_at y estado=false.
     * El registro NUNCA se elimina físicamente.
     */
    @Transactional
    public void eliminar(Integer id) {
        Promotor promotor = promotorRepository.findByIdPromotorAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotor", id));

        promotor.setDeletedAt(LocalDateTime.now());
        promotor.setEstado(false);
        promotorRepository.save(promotor);
    }

    @Transactional
    public void restaurar(Integer id) {
        Promotor promotor = promotorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotor", id));
        promotor.setDeletedAt(null);
        promotor.setRestoredAt(LocalDateTime.now());
        promotor.setEstado(true);
        promotorRepository.save(promotor);
    }

    // -------------------------------------------------------
    // Mapeo entidad <-> DTO
    // -------------------------------------------------------

    private PromotorDTO toDTO(Promotor p) {
        return PromotorDTO.builder()
                .idPromotor(p.getIdPromotor())
                .codigoPromotor(p.getCodigoPromotor())
                .dni(p.getDni())
                .nombres(p.getNombres())
                .apellidos(p.getApellidos())
                .telefono(p.getTelefono())
                .correo(p.getCorreo())
                .estado(p.getEstado())
                .deletedAt(p.getDeletedAt())
                .restoredAt(p.getRestoredAt())
                .build();
    }

    private Promotor toEntity(PromotorDTO dto) {
        return Promotor.builder()
                .codigoPromotor(dto.getCodigoPromotor())
                .dni(dto.getDni())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .build();
    }
}
