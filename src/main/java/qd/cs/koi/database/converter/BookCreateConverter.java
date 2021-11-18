package qd.cs.koi.database.converter;

import qd.cs.koi.database.entity.BookDO;
import qd.cs.koi.database.interfaces.Book.BookCreateDTO;

@org.mapstruct.Mapper(
        componentModel = "spring",
        imports = {BaseConvertUtils.class}
)
public interface BookCreateConverter extends BaseConverter<BookDO, BookCreateDTO>{


    @org.mapstruct.Mapping(
            target = "keyWord",
            expression = "java( BaseConvertUtils.listToString(source.getKeyWord()) )"
    )
    @org.mapstruct.Mapping(
            target = "classification",
            expression = "java( BaseConvertUtils.listToString(source.getClassification()) )"
    )
    BookDO from(BookCreateDTO source);

}
