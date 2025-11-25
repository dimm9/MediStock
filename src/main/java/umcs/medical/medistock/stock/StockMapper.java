package umcs.medical.medistock.stock;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(source = "hospital.id", target = "hospitalId")
    StockDTO toDto(Stock stock);

    @Mapping(source = "hospitalId", target = "hospital.id")
    Stock toEntity(StockDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "hospitalId", target = "hospital.id")
    void updateEntityFromDto(StockDTO dto, @MappingTarget Stock entity);
}
