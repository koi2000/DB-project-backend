package qd.cs.koi.database.service.book;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qd.cs.koi.database.converter.BaseConvertUtils;
import qd.cs.koi.database.dao.BookDao;
import qd.cs.koi.database.dao.BorrowHistoryDao;
import qd.cs.koi.database.dao.StorageDao;
import qd.cs.koi.database.entity.BookDO;
import qd.cs.koi.database.entity.StorageDO;
import qd.cs.koi.database.interfaces.Book.BookBorrowDTO;
import qd.cs.koi.database.interfaces.Book.BookDetailDTO;
import qd.cs.koi.database.interfaces.Book.BookListDTO;
import qd.cs.koi.database.interfaces.Book.BookListReqDTO;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;
import qd.cs.koi.database.utils.web.PageResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    BookDao bookDao;

    @Autowired
    StorageDao storageDao;

    @Autowired
    BorrowHistoryDao borrowHistoryDao;

    public PageResult<BookListDTO> list(BookListReqDTO reqDTO){
        LambdaQueryChainWrapper<BookDO> query = bookDao.lambdaQuery();
        Page<BookDO> pageResult = query.page(new Page<>(reqDTO.getPageNow(), reqDTO.getPageSize()));
        if (pageResult.getSize() == 0) {
            return new PageResult<>();
        }
        // 转换
        List<BookListDTO> collect = pageResult.getRecords().stream().map(o -> {
            BookListDTO build = BookListDTO.builder()
                    .bookName(o.getBookName())
                    .author(o.getAuthor())
                    .keyWord(BaseConvertUtils.stringToList(o.getKeyWord()))
                    .build();
            return build;
        }).collect(Collectors.toList());

        return new PageResult<>(pageResult.getPages(), collect);
    }

    public BookDetailDTO detail(Long bookId){
        BookDO bookDO = bookDao.getById(bookId);
        AssertUtils.notNull(bookDO, ApiExceptionEnum.BOOK_NOT_FOUND);
        BookDetailDTO build = BookDetailDTO.builder()
                .bookId(bookId)
                .author(bookDO.getAuthor())
                .bookName(bookDO.getBookName())
                .description(bookDO.getDescription())
                .build();
        StorageDO one = storageDao.lambdaQuery().eq(StorageDO::getBookId, bookId).one();
        build.setNumber(one.getNumber());
        return build;
    }

    public void borrow(UserSessionDTO userSessionDTO, BookBorrowDTO bookBorrowDTO){

    }
}
