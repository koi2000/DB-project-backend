package qd.cs.koi.database.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import qd.cs.koi.database.converter.BookCreateConverter;
import qd.cs.koi.database.interfaces.Book.BookCreateDTO;
import qd.cs.koi.database.service.book.BookManageService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/manage/book")
public class BookManageController {

    @Autowired
    BookManageService bookManageService;

    @Autowired
    BookCreateConverter bookCreateConverter;

    @PostMapping("/create")
    @ResponseBody
    public Long createBook(@RequestBody @Valid BookCreateDTO bookCreateDTO) {
        return bookManageService.create(bookCreateDTO);
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
}
