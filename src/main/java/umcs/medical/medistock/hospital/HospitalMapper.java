package umcs.medical.medistock.hospital;

import org.mapstruct.*;
import umcs.medical.medistock.hospital.HospitalDto;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

    HospitalDto toDto(Hospital hospital);

    Hospital toEntity(HospitalDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(HospitalDto dto, @MappingTarget Hospital entity);
}
