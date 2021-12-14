package qd.cs.koi.database.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @CrossOrigin
    @PostMapping("/list")
    @ResponseBody
    public PageResult<BookListDTO> list(@RequestBody ListReqDTO listReqDTO){
        return bookService.list(listReqDTO);
    }

    @GetMapping("/queryDetail")
    @ResponseBody
    public ResponseResult<BookDetailDTO> queryDetail(@RequestParam("bookId") Long bookId) {
        BookDetailDTO detail = bookService.detail(bookId);
        return ResponseResult.ok(detail);
    }

    @PostMapping("/borrow")
    @ResponseBody
    public void borrowBook(@UserSession UserSessionDTO userSessionDTO,
                           @RequestBody BookBorrowDTO bookBorrowDTO){
        Date start = bookBorrowDTO.getStart();
        Date shouldTime = bookBorrowDTO.getShouldTime();

        LocalDate startDate = LocalDate.of(start.getYear(),
                start.getMonth(),start.getDay());

        LocalDate shouldDate = LocalDate.of(shouldTime.getYear(),
                shouldTime.getMonth(),shouldTime.getDay());

        Period between = Period.between(startDate, shouldDate);

        //借书时间不允许超过6个月
        AssertUtils.isTrue(between.getYears()<1&&between.getMonths()<6
                , ApiExceptionEnum.TIME_TOO_LONG);

        bookService.borrow(userSessionDTO,bookBorrowDTO);
    }

    @PostMapping("/borrowList")
    @ResponseBody
    public PageResult<BorrowHistoryDO> getMyBorrow(@UserSession UserSessionDTO userSessionDTO,
                                                   @RequestBody   ListReqDTO listReqDTO){
        return borrowService.getBorrowList(userSessionDTO,listReqDTO);
    }

    @GetMapping("/getBookName")
    @ResponseBody
    public String getBookName(@RequestParam("bookId") Long bookId){
        return bookService.getNameById(bookId);
    }

    @GetMapping("/return")
    @ResponseBody
    public Date returnBook(@UserSession UserSessionDTO userSessionDTO,
            @RequestParam("borrowHistoryId") Long borrowHistoryId){
        return bookService.returnBook(userSessionDTO,borrowHistoryId);
    }
}
