package com.creditsuisse.task.service.mapper;

import com.creditsuisse.task.domain.*;
import com.creditsuisse.task.service.dto.InventoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inventory} and its DTO {@link InventoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { BookMapper.class })
public interface InventoryMapper extends EntityMapper<InventoryDTO, Inventory> {
    @Mapping(target = "book", source = "book", qualifiedByName = "id")
    InventoryDTO toDto(Inventory s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InventoryDTO toDtoId(Inventory inventory);
}
