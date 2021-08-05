package com.creditsuisse.task.service.mapper;

import com.creditsuisse.task.domain.*;
import com.creditsuisse.task.service.dto.BookDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Book} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BookMapper extends EntityMapper<BookDTO, Book> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookDTO toDtoId(Book book);
}
