package umcs.medical.medistock.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    public List<HospitalDto> listAll() {
        return hospitalRepository.findAll()
                .stream()
                .map(hospitalMapper::toDto)
                .toList();
    }

    public HospitalDto getById(Long id) {
        return hospitalRepository.findById(id)
                .map(hospitalMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Hospital not found"));
    }

    public HospitalDto create(HospitalDto dto) {
        Hospital entity = hospitalMapper.toEntity(dto);
        Hospital saved = hospitalRepository.save(entity);
        return hospitalMapper.toDto(saved);
    }

    public HospitalDto update(Long id, HospitalDto dto) {
        Hospital entity = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found"));
        hospitalMapper.updateEntityFromDto(dto, entity);
        Hospital saved = hospitalRepository.save(entity);
        return hospitalMapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!hospitalRepository.existsById(id)) {
            throw new RuntimeException("Hospital not found");
        }
        hospitalRepository.deleteById(id);
    }
}
