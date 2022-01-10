package qd.cs.koi.database.service.book;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qd.cs.koi.database.converter.BaseConvertUtils;
import qd.cs.koi.database.dao.BookDao;
import qd.cs.koi.database.dao.BorrowHistoryDao;
import qd.cs.koi.database.dao.DefaultDao;
import qd.cs.koi.database.dao.StorageDao;
import qd.cs.koi.database.entity.*;
import qd.cs.koi.database.interfaces.Book.BookBorrowDTO;
import qd.cs.koi.database.interfaces.Book.BookDetailDTO;
import qd.cs.koi.database.interfaces.Book.BookListDTO;
import qd.cs.koi.database.interfaces.Book.BookListReqDTO;
import qd.cs.koi.database.interfaces.ListReqDTO;
import qd.cs.koi.database.interfaces.User.UserListReqDTO;
import qd.cs.koi.database.utils.Enums.DefaultEnum;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;
import qd.cs.koi.database.utils.web.PageResult;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    BookDao bookDao;

    @Autowired
    StorageDao storageDao;

    @Autowired
    BorrowHistoryDao borrowHistoryDao;

    @Autowired
    DefaultDao defaultDao;

    public PageResult<BookListDTO> list(ListReqDTO reqDTO) {
        LambdaQueryChainWrapper<BookDO> query = bookDao.lambdaQuery();
        Page<BookDO> pageResult = query.page(new Page<>(reqDTO.getPageNow(), reqDTO.getPageSize()));
        if (pageResult.getSize() == 0) {
            return new PageResult<>();
        }
        // 转换
        List<BookListDTO> collect = pageResult.getRecords().stream().map(o -> {
            BookListDTO build = BookListDTO.builder()
                    .bookId(o.getBookId())
                    .bookName(o.getBookName())
                    .author(o.getAuthor())
                    .keyWord(BaseConvertUtils.stringToList(o.getKeyWord()))
                    .img(o.getImg())
                    .description(o.getDescription())
                    .build();
            return build;
        }).collect(Collectors.toList());

        return new PageResult<>(pageResult.getPages(), collect);
    }

    public BookDetailDTO detail(Long bookId) {
        BookDO bookDO = bookDao.getById(bookId);
        AssertUtils.notNull(bookDO, ApiExceptionEnum.BOOK_NOT_FOUND);
        BookDetailDTO build = BookDetailDTO.builder()
                .bookId(bookId)
                .author(bookDO.getAuthor())
                .bookName(bookDO.getBookName())
                .description(bookDO.getDescription())
                .build();
        StorageDO one = storageDao.lambdaQuery().eq(StorageDO::getBookId, bookId).one();
        if (one == null) {
            build.setNumber(0);
        } else {
            build.setNumber(one.getNumber());
        }

        return build;
    }

    @Transactional
    public void borrow(UserSessionDTO userSessionDTO, BookBorrowDTO bookBorrowDTO) {
        BookDO bookDO = bookDao.getById(bookBorrowDTO.getBookId());
        AssertUtils.notNull(bookDO, ApiExceptionEnum.BOOK_NOT_FOUND);
        StorageDO storageDO = storageDao.lambdaQuery().eq(StorageDO::getBookId, bookBorrowDTO.getBookId()).one();
        if (storageDO == null) {
            storageDO = StorageDO.builder().build();
            storageDO.setBookId(bookDO.getBookId());
            storageDO.setNumber(0);
            AssertUtils.isTrue(storageDao.save(storageDO), ApiExceptionEnum.UNKNOWN_ERROR);
        }
        AssertUtils.isTrue(storageDO.getNumber() > 0, ApiExceptionEnum.BOOK_NOT_ENOUGH);

        storageDO.setNumber(storageDO.getNumber() - 1);
        AssertUtils.isTrue(storageDao.updateById(storageDO), ApiExceptionEnum.UNKNOWN_ERROR);

        BorrowHistoryDO borrowHistoryDO = BorrowHistoryDO.builder()
                .bookId(bookBorrowDTO.getBookId())
                .start(bookBorrowDTO.getStart())
                .shouldTime(bookBorrowDTO.getShouldTime())
                .username(userSessionDTO.getUsername())
                .build();
        AssertUtils.isTrue(borrowHistoryDao.save(borrowHistoryDO), ApiExceptionEnum.UNKNOWN_ERROR);
    }

    public String getNameById(Long bookId) {
        BookDO bookDO = bookDao.getById(bookId);
        AssertUtils.notNull(bookDO, ApiExceptionEnum.BOOK_NOT_FOUND);
        return bookDO.getBookName();
    }

    @Transactional
    public Date returnBook(UserSessionDTO userSessionDTO, Long bookHistoryId) {
        BorrowHistoryDO borrowHistoryDO = borrowHistoryDao.getById(bookHistoryId);
        AssertUtils.notNull(borrowHistoryDO, ApiExceptionEnum.HISTORY_NOT_FOUND);
        //根据书籍得到库存
        StorageDO storageDO = storageDao.lambdaQuery().eq(StorageDO::getBookId, borrowHistoryDO.getBookId())
                .one();
        if (storageDO == null) {
            storageDO = StorageDO.builder()
                    .bookId(borrowHistoryDO.getBookId())
                    .number(0)
                    .build();
            AssertUtils.isTrue(storageDao.save(storageDO), ApiExceptionEnum.UNKNOWN_ERROR);
        }

        storageDO.setNumber(storageDO.getNumber() + 1);
        AssertUtils.isTrue(storageDao.updateById(storageDO), ApiExceptionEnum.UNKNOWN_ERROR);
        borrowHistoryDO.setRealTime(new Date());
        //说明时间已超出
        if (borrowHistoryDO.getShouldTime().before(borrowHistoryDO.getRealTime())) {
            DefaultDO defaultDO = DefaultDO.builder()
                    .reason(DefaultEnum.OUTOFTIME.name)
                    .time(borrowHistoryDO.getRealTime())
                    .userName(userSessionDTO.getUsername())
                    .price(5)
                    .build();
            AssertUtils.isTrue(defaultDao.save(defaultDO), ApiExceptionEnum.UNKNOWN_ERROR);
        }
        AssertUtils.isTrue(borrowHistoryDao.updateById(borrowHistoryDO), ApiExceptionEnum.UNKNOWN_ERROR);
        return borrowHistoryDO.getRealTime();
    }

    public PageResult<BookListDTO> search(BookListReqDTO reqDTO, UserSessionDTO userSessionDTO) {

        LambdaQueryChainWrapper<BookDO> query = bookDao.lambdaQuery();

        Optional.of(reqDTO).map(BookListReqDTO::getBookName).ifPresent(bookname -> {
            query.like(BookDO::getBookName, bookname);
        });


        Optional.of(reqDTO).map(BookListReqDTO::getAuthor).ifPresent(author -> {
            query.like(BookDO::getAuthor, author);
        });

        Optional.of(reqDTO).map(BookListReqDTO::getClassification).ifPresent(classification -> {
            query.like(BookDO::getClassification, classification);
        });

        Optional.of(reqDTO).map(BookListReqDTO::getClassification).ifPresent(classification -> {
            query.like(BookDO::getClassification, classification);
        });


        // searchKey
        Optional.of(reqDTO).map(BookListReqDTO::getSearchKey).ifPresent(searchKey -> {
            query.or(o1 -> o1.like(BookDO::getBookName, searchKey))
                    .or(o1 -> o1.like(BookDO::getAuthor, searchKey))
                    .or(o1 -> o1.like(BookDO::getDescription, searchKey))
                    .or(o1 -> o1.like(BookDO::getClassification, searchKey))
                    .or(o1 -> o1.like(BookDO::getKeyWord, searchKey));
        });
        Page<BookDO> pageResult = query.page(new Page<>(reqDTO.getPageNow(), reqDTO.getPageSize()));
        if (pageResult.getSize() == 0) {
            return new PageResult<>();
        }
        // 转换
        List<BookListDTO> collect = pageResult.getRecords().stream().map(o -> {
            BookListDTO build = BookListDTO.builder()
                    .bookId(o.getBookId())
                    .bookName(o.getBookName())
                    .author(o.getAuthor())
                    .keyWord(BaseConvertUtils.stringToList(o.getKeyWord()))
                    .img(o.getImg())
                    .description(o.getDescription())
                    .build();
            return build;
        }).collect(Collectors.toList());

        return new PageResult<>(pageResult.getPages(), collect);
    }

    public BorrowHistoryDO getHistoryById(UserSessionDTO userSessionDTO, Long borrowHistoryId) {
        BorrowHistoryDO historyDO = borrowHistoryDao.getById(borrowHistoryId);
        AssertUtils.notNull(historyDO, ApiExceptionEnum.HISTORY_IS_NULL);

        AssertUtils.isTrue(historyDO.getUsername().equals(userSessionDTO.getUsername()), ApiExceptionEnum.USER_NOT_MATCHING);

        return historyDO;
    }
}
