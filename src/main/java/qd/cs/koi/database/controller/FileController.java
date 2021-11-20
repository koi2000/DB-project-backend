package qd.cs.koi.database.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qd.cs.koi.database.dao.FileDao;
import qd.cs.koi.database.entity.FileDO;
import qd.cs.koi.database.interfaces.File.FileDTO;
import qd.cs.koi.database.service.file.FileService;
import qd.cs.koi.database.service.file.FileSystemProperties;
import qd.cs.koi.database.utils.annations.ApiResponseBody;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileSystemProperties fileSystemProperties;

    @Autowired
    FileService fileService;

    @Autowired
    FileDao fileDao;

    @PostMapping(value = "/upload")
    @ResponseBody
    public FileDTO upload(@RequestParam("file") @NotNull MultipartFile file,
                          @RequestParam("bookId") @NotNull Long bookId,
                          @UserSession UserSessionDTO userSessionDTO) {
        return fileService.upload(file, userSessionDTO.getUserId(),bookId);
    }

    @GetMapping("/getImage")
    @ResponseBody
    public void getByName(HttpServletResponse response, @RequestParam("id") String id) {
        String contentType = null;
        String extensionName = null;

        FileDO fileDO = fileDao.getById(Long.parseLong(id));

        BufferedImage buffImg = null;
        try {
            File file = new File(fileSystemProperties.getBaseDir(), fileDO.getId() + "." + fileDO.getExtensionName());
            FileInputStream fileInputStream = new FileInputStream(file);
            buffImg = ImageIO.read(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServletOutputStream sos = null;

        try {
            sos = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            AssertUtils.notNull(buffImg, ApiExceptionEnum.FILE_NOT_EXISTS);
            ImageIO.write(buffImg, fileDO.getExtensionName(), sos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/queryByMd5")
    @ResponseBody
    public FileDTO queryByMd5(@RequestParam("md5") String md5) {
        return fileService.queryByMd5(md5);
    }
}
