package qd.cs.koi.database.service.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qd.cs.koi.database.converter.BookCreateConverter;
import qd.cs.koi.database.dao.BookDao;
import qd.cs.koi.database.entity.BookDO;
import qd.cs.koi.database.interfaces.Book.BookCreateDTO;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

@Service
public class BookManageService {

    @Autowired
    BookDao bookDao;

    @Autowired
    BookCreateConverter bookCreateConverter;

    public Long create(BookCreateDTO bookCreateDTO){
        BookDO bookDO = bookCreateConverter.from(bookCreateDTO);
        AssertUtils.isTrue(bookDao.save(bookDO), ApiExceptionEnum.UNKNOWN_ERROR);
        return bookDO.getBookId();
    }
}
