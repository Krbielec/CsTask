package com.creditsuisse.task.service.mapper;

import com.creditsuisse.task.domain.*;
import com.creditsuisse.task.service.dto.RentalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rental} and its DTO {@link RentalDTO}.
 */
@Mapper(componentModel = "spring", uses = { PatronMapper.class, InventoryMapper.class })
public interface RentalMapper extends EntityMapper<RentalDTO, Rental> {
    @Mapping(target = "patron", source = "patron", qualifiedByName = "id")
    @Mapping(target = "inventory", source = "inventory", qualifiedByName = "id")
    RentalDTO toDto(Rental s);
}
