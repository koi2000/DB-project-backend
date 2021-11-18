package qd.cs.koi.database.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
}
