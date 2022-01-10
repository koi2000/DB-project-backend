package qd.cs.koi.database.service.file;


import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import qd.cs.koi.database.converter.FileConverter;
import qd.cs.koi.database.dao.BookDao;
import qd.cs.koi.database.dao.FileDao;
import qd.cs.koi.database.entity.BookDO;
import qd.cs.koi.database.entity.FileDO;
import qd.cs.koi.database.interfaces.File.FileDTO;
import qd.cs.koi.database.service.book.BookService;
import qd.cs.koi.database.utils.util.CodecUtils;
import org.apache.commons.io.FileUtils;
import qd.cs.koi.database.utils.util.SnowflakeIdWorker;
import qd.cs.koi.database.utils.web.ApiException;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Service
@EnableConfigurationProperties(FileSystemProperties.class)
public class FileService {
    @Autowired
    FileDao fileDao;

    @Autowired
    private FileSystemProperties fileSystemProperties;

    @Autowired
    private FileConverter fileConverter;

    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    @Autowired
    BookDao bookDao;

    @Transactional
    public FileDTO upload(MultipartFile file, Long userId, Long bookId) {
        //首先查看书籍是否存在
        BookDO bookDO = bookDao.getById(bookId);
        AssertUtils.notNull(bookDO, ApiExceptionEnum.BOOK_NOT_FOUND);

        // 计算文件 md5
        String md5;
        try {
            md5 = CodecUtils.md5(file.getInputStream());
        } catch (Exception e) {
            throw new ApiException(ApiExceptionEnum.FILE_WRITE_ERROR);
        }
        // 查询是否已有相同 MD5
        FileDO fileDO = fileDao.lambdaQuery().eq(FileDO::getMd5, md5).one();
        if (fileDO != null) {
            AssertUtils.notNull(fileDO, ApiExceptionEnum.FILE_MD5_EXISTS);
            return fileConverter.to(fileDO);
        }

        Long id = snowflakeIdWorker.nextId();
        fileDO = FileDO.builder()
                .id(id)
                .userId(userId)
                .name(file.getOriginalFilename())
                .extensionName(Files.getFileExtension(file.getOriginalFilename()))
                .md5(md5)
                .size(file.getSize())
                .build();
        AssertUtils.isTrue(fileDao.save(fileDO), ApiExceptionEnum.SERVER_BUSY);

        bookDO.setImg(id.toString());
        AssertUtils.isTrue(bookDao.updateById(bookDO), ApiExceptionEnum.UNKNOWN_ERROR);

        try {
            File writeFile = new File(Paths.get(fileSystemProperties.getBaseDir(), id.toString()).toString() + "." + fileDO.getExtensionName());
            //File writeFile = new File(Paths.get(fileSystemProperties.getBaseDir(), id.toString()).toString());
            byte[] bytes = file.getBytes();
            FileUtils.writeByteArrayToFile(writeFile, bytes);
        } catch (Exception e) {
            throw new ApiException(ApiExceptionEnum.FILE_WRITE_ERROR);
        }
        return fileConverter.to(fileDO);
    }

    public FileDTO queryByMd5(String md5) {
        FileDO fileDO = fileDao.lambdaQuery().eq(FileDO::getMd5, md5).one();
        return fileConverter.to(fileDO);
    }
}
