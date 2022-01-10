package qd.cs.koi.database.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qd.cs.koi.database.converter.BookCreateConverter;
import qd.cs.koi.database.entity.BookDO;
import qd.cs.koi.database.interfaces.Book.BookCreateDTO;
import qd.cs.koi.database.interfaces.Book.BookExcelDTO;
import qd.cs.koi.database.service.book.BookManageService;
import qd.cs.koi.database.service.book.BookService;
import qd.cs.koi.database.utils.annations.ApiResponseBody;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.entity.ResponseResult;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.util.ExcelUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/manage/book")
public class BookManageController {

    @Autowired
    BookService bookService;

    @Autowired
    BookManageService bookManageService;

    @Autowired
    BookCreateConverter bookCreateConverter;

    @PostMapping("/create")
    @ResponseBody
    public Long createBook(@RequestBody @Valid BookCreateDTO bookCreateDTO,
                           @UserSession UserSessionDTO userSessionDTO) {
        return bookManageService.create(userSessionDTO,bookCreateDTO);
    }



    @GetMapping("/changeNumber")
    public int changeNumber(Long bookId,int number){
        return bookManageService.changeNumber(bookId,number);
    }

    @PostMapping("/update")
    @ResponseBody
    public Long updateBook(@RequestBody @Valid BookCreateDTO bookCreateDTO) {
        return bookManageService.updateBook(bookCreateDTO);
    }

    @GetMapping("/download/book")
    public void downloadUser(HttpServletResponse response){

        String fileName = "书籍";
        String sheetName="书籍";
        List<BookDO> list = bookManageService.list();
        List<BookExcelDTO> bookExcelDTOS = list.stream().map(o -> {
            return BookExcelDTO.builder()
                    .bookname(o.getBookName())
                    .author(o.getAuthor())
                    .price(o.getPrice())
                    .classification(o.getDescription())
                    .keyWord(o.getKeyWord())
                    .build();
        }).collect(Collectors.toList());

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            //String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");


            ExcelUtil.writeExcel(response,bookExcelDTOS,fileName,sheetName,BookExcelDTO.class);
            //response.setHeader("Content-Disposition", "attachment; filename=se.xlsx");
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    /*下载模板*/
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response){
        String fileName = "书籍导入模板";
        String sheetName="书籍导入模板";
        List<BookExcelDTO> bookExcelList = new ArrayList<>();

        BookExcelDTO bookExcelDTO = BookExcelDTO.builder()
                .bookname("乡土中国")
                .author("费孝通")
                .keyWord("人文,社科")
                .classification("社会科学")
                .description("《乡土中国》是费孝通著述的一部研究中国农村的作品。 全书由14篇文章组成，涉及乡土社会人文环境、传统社会结构、权力分配、道德体系、法礼、血缘地缘等各方面。")
                .price(50)
                .build();
        bookExcelList.add(bookExcelDTO);

        try {
            //TeacherExcel.class对应你的模板类
            //teacherExcelList模板的例子
            //也可以使用这种方式导出你查询出数据excel文件
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            //String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");


            ExcelUtil.writeExcel(response,bookExcelList,fileName,sheetName,BookExcelDTO.class);
            //response.setHeader("Content-Disposition", "attachment; filename=se.xlsx");
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    @PostMapping("/importBook")
    @ApiResponseBody
    public int plUpdate(@RequestParam("file") MultipartFile file) throws IOException {
        //String Originalfilename = file.getOriginalFilename();
        // String fileName = file.getName();
        //  System.out.println("orname="+Originalfilename+";"+"filename"+file.getName());
        //TeacherExcel.class对应的是和模板一样的实体类，
        //eduTeacherService对应持久层的接口
        bookManageService.loadBook(file);
        return 1;
    }

}
