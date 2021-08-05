package com.creditsuisse.task.service.mapper;

import com.creditsuisse.task.domain.*;
import com.creditsuisse.task.service.dto.PatronDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Patron} and its DTO {@link PatronDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PatronMapper extends EntityMapper<PatronDTO, Patron> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatronDTO toDtoId(Patron patron);
}
