package qd.cs.koi.database.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import qd.cs.koi.database.entity.BorrowHistoryDO;
import qd.cs.koi.database.interfaces.Book.*;
import qd.cs.koi.database.interfaces.ListReqDTO;
import qd.cs.koi.database.service.book.BookService;
import qd.cs.koi.database.service.borrow.BorrowService;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.entity.ResponseResult;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;
import qd.cs.koi.database.utils.web.PageResult;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    BorrowService borrowService;

    @PostMapping("/list")
    @ResponseBody
    public PageResult<BookListDTO> list(@RequestBody ListReqDTO listReqDTO) {
        return bookService.list(listReqDTO);
    }

    @PostMapping("/search")
    @ResponseBody
    public PageResult<BookListDTO> search(@RequestBody BookListReqDTO reqDTO,
                                          @UserSession UserSessionDTO userSessionDTO) {
        return bookService.search(reqDTO, userSessionDTO);
    }

    @GetMapping("/queryDetail")
    @ResponseBody
    public ResponseResult<BookDetailDTO> queryDetail(@RequestParam("bookId") Long bookId) {
        BookDetailDTO detail = bookService.detail(bookId);
        return ResponseResult.ok(detail);
    }

    @GetMapping("/getBorrowHistory")
    @ResponseBody
    public BorrowHistoryDO getBorrowHistoryById(@UserSession UserSessionDTO userSessionDTO,
                                                @RequestParam("borrowHistoryId") Long borrowHistoryId) {
        return bookService.getHistoryById(userSessionDTO, borrowHistoryId);
    }

    @GetMapping("/ReBorrow")
    @ResponseBody
    public Boolean reBorrowBook(@UserSession UserSessionDTO userSessionDTO,
                                @RequestParam("borrowHistoryId") Long borrowHistoryId, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm") @RequestParam("newDate") Date newDate) {
        return borrowService.reBorrow(userSessionDTO, borrowHistoryId, newDate);
    }

    @PostMapping("/borrow")
    @ResponseBody
    public void borrowBook(@UserSession UserSessionDTO userSessionDTO,
                           @RequestBody BookBorrowDTO bookBorrowDTO) {
        Date start = bookBorrowDTO.getStart();
        Date shouldTime = bookBorrowDTO.getShouldTime();

        Period between = null;
        try {
            LocalDate startDate = LocalDate.of(start.getYear(),
                    start.getMonth(), start.getDay());

            LocalDate shouldDate = LocalDate.of(shouldTime.getYear(),
                    shouldTime.getMonth(), shouldTime.getDay());

            between = Period.between(startDate, shouldDate);
        } catch (Exception e) {

        }

        if (between != null) {
            //借书时间不允许超过6个月
            AssertUtils.isTrue(between.getYears() < 1 && between.getMonths() < 6
                    , ApiExceptionEnum.TIME_TOO_LONG);
        }


        bookService.borrow(userSessionDTO, bookBorrowDTO);
    }

    @PostMapping("/borrowList")
    @ResponseBody
    public PageResult<BorrowHistoryDO> getMyBorrow(@UserSession UserSessionDTO userSessionDTO,
                                                   @RequestBody ListReqDTO listReqDTO) {
        return borrowService.getBorrowList(userSessionDTO, listReqDTO);
    }

    @GetMapping("/getBookName")
    @ResponseBody
    public String getBookName(@RequestParam("bookId") Long bookId) {
        return bookService.getNameById(bookId);
    }

    @GetMapping("/return")
    @ResponseBody
    public Date returnBook(@UserSession UserSessionDTO userSessionDTO,
                           @RequestParam("borrowHistoryId") Long borrowHistoryId) {
        return bookService.returnBook(userSessionDTO, borrowHistoryId);
    }
}
