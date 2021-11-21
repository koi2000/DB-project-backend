package qd.cs.koi.database.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import qd.cs.koi.database.entity.BorrowHistoryDO;
import qd.cs.koi.database.entity.DefaultDO;
import qd.cs.koi.database.interfaces.ListReqDTO;
import qd.cs.koi.database.service.defaults.DefaultService;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.web.PageResult;

@Slf4j
@Controller
@RequestMapping("/default")
public class DefaultController {

    @Autowired
    DefaultService defaultService;

    @PostMapping("/list")
    @ResponseBody
    public PageResult<DefaultDO> list(@UserSession UserSessionDTO userSessionDTO,
                                      @RequestBody ListReqDTO listReqDTO){
        return defaultService.list(userSessionDTO,listReqDTO);
    }
}
