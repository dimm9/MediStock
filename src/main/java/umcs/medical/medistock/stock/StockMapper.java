package umcs.medical.medistock.stock;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockDTO toDto(Stock stock);

    Stock toEntity(StockDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(StockDTO dto, @MappingTarget Stock entity);
}
