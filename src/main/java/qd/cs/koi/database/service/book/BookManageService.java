package qd.cs.koi.database.service.book;

import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import qd.cs.koi.database.converter.BookCreateConverter;
import qd.cs.koi.database.dao.BookDao;
import qd.cs.koi.database.dao.StorageDao;
import qd.cs.koi.database.dao.UserDao;
import qd.cs.koi.database.entity.BookDO;
import qd.cs.koi.database.entity.StorageDO;
import qd.cs.koi.database.interfaces.Book.BookCreateDTO;
import qd.cs.koi.database.interfaces.Book.BookExcelDTO;
import qd.cs.koi.database.service.file.FileService;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.util.BookDataListener;
import qd.cs.koi.database.utils.util.UserDataListener;
import qd.cs.koi.database.utils.web.ApiException;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

import java.util.List;

@Service
public class BookManageService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private BookCreateConverter bookCreateConverter;

    @Autowired
    private StorageDao storageDao;

    @Autowired
    private FileService fileService;

    @Transactional
    public Long create(UserSessionDTO userSessionDTO, BookCreateDTO bookCreateDTO) {
        BookDO bookDO = bookCreateConverter.from(bookCreateDTO);
        AssertUtils.isTrue(bookDao.save(bookDO), ApiExceptionEnum.UNKNOWN_ERROR);
        StorageDO storageDO = StorageDO.builder()
                .bookId(bookDO.getBookId())
                .number(bookCreateDTO.getNumber())
                .build();
        AssertUtils.isTrue(storageDao.save(storageDO), ApiExceptionEnum.UNKNOWN_ERROR);
        if (bookCreateDTO.getImage() != null) {
            fileService.upload(bookCreateDTO.getImage(), userSessionDTO.getUserId(), bookCreateDTO.getBookId());
        }
        return bookDO.getBookId();
    }


    public int changeNumber(Long bookId, int number) {
        BookDO one = bookDao.getById(bookId);
        AssertUtils.notNull(one, ApiExceptionEnum.BOOK_NOT_FOUND);
        StorageDO storageDO = storageDao.lambdaQuery().eq(StorageDO::getBookId, bookId).one();
        storageDO.setNumber(number);
        AssertUtils.isTrue(storageDao.save(storageDO), ApiExceptionEnum.UNKNOWN_ERROR);
        return number;
    }

    public Long updateBook(BookCreateDTO bookCreateDTO) {
        BookDO bookDO = bookDao.getById(bookCreateDTO);
        AssertUtils.notNull(bookDO, ApiExceptionEnum.BOOK_NOT_FOUND);
        BookDO update = bookCreateConverter.from(bookCreateDTO);
        update.setBookId(bookDO.getBookId());
        AssertUtils.notNull(bookDao.updateById(update), ApiExceptionEnum.UNKNOWN_ERROR);
        return bookDO.getBookId();
    }

    public void loadBook(MultipartFile file) {
        try {
            //文件输入流
            BookDao bookDao = new BookDao();
            //调用方法进行读取
            EasyExcel.read(file.getInputStream(), BookExcelDTO.class, new BookDataListener(bookDao)).sheet().doRead();
        } catch (Exception e) {
            throw new ApiException(ApiExceptionEnum.UNKNOWN_ERROR);
        }
    }

    public List<BookDO> list() {
        List<BookDO> list = bookDao.lambdaQuery().list();
        return list;
    }
}
