package qd.cs.koi.database.controller;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import qd.cs.koi.database.interfaces.Book.*;
import qd.cs.koi.database.interfaces.ListReqDTO;
import qd.cs.koi.database.service.borrow.BorrowService;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import org.junit.jupiter.api.Timeout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import qd.cs.koi.database.entity.BorrowHistoryDO;
import qd.cs.koi.database.interfaces.Book.BookListDTO;
import qd.cs.koi.database.interfaces.Book.BookListReqDTO;
import qd.cs.koi.database.service.book.BookService;
import qd.cs.koi.database.utils.entity.ResponseResult;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.web.PageResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookController_search_1_1_Test {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookListReqDTO reqDTO;
    private UserSessionDTO userSessionDTO;

    @BeforeEach
    void setUp() {
        reqDTO = new BookListReqDTO();
        userSessionDTO = new UserSessionDTO();
    }

    @Test
    @Timeout(8000)
    void testSearch() {
        // Arrange
        PageResult<BookListDTO> expectedResult = new PageResult<>();
        when(bookService.search(reqDTO, userSessionDTO)).thenReturn(expectedResult);

        // Act
        PageResult<BookListDTO> actualResult = bookController.search(reqDTO, userSessionDTO);

        // Assert
        assertEquals(expectedResult, actualResult);
        Mockito.verify(bookService).search(reqDTO, userSessionDTO);
    }
}