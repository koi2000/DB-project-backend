package qd.cs.koi.database.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import qd.cs.koi.database.interfaces.Book.BookCreateDTO;
import qd.cs.koi.database.interfaces.Book.BookListDTO;
import qd.cs.koi.database.interfaces.Book.BookListReqDTO;
import qd.cs.koi.database.service.book.BookService;
import qd.cs.koi.database.utils.web.PageResult;

@Slf4j
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/list")
    @ResponseBody
    public PageResult<BookListDTO> list(@RequestBody BookListReqDTO bookListReqDTO){
        return bookService.list(bookListReqDTO);
    }
}
