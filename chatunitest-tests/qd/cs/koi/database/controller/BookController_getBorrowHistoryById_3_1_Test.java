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
import qd.cs.koi.database.utils.entity.ResponseResult;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;
import qd.cs.koi.database.utils.web.PageResult;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import org.junit.jupiter.api.Timeout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import qd.cs.koi.database.entity.BorrowHistoryDO;
import qd.cs.koi.database.service.book.BookService;
import qd.cs.koi.database.utils.entity.UserSessionDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookController_getBorrowHistoryById_3_1_Test {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Timeout(8000)
    void testGetBorrowHistoryById() {
        UserSessionDTO userSessionDTO = new UserSessionDTO();
        Long borrowHistoryId = 1L;

        BorrowHistoryDO expectedBorrowHistory = new BorrowHistoryDO();

        when(bookService.getHistoryById(userSessionDTO, borrowHistoryId)).thenReturn(expectedBorrowHistory);

        BorrowHistoryDO actualBorrowHistory = bookController.getBorrowHistoryById(userSessionDTO, borrowHistoryId);

        assertEquals(expectedBorrowHistory, actualBorrowHistory);
        verify(bookService, times(1)).getHistoryById(userSessionDTO, borrowHistoryId);
    }
}