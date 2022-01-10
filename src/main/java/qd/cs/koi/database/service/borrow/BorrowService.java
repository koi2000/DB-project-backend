package qd.cs.koi.database.service.borrow;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qd.cs.koi.database.dao.BorrowHistoryDao;
import qd.cs.koi.database.entity.BorrowHistoryDO;
import qd.cs.koi.database.interfaces.ListReqDTO;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;
import qd.cs.koi.database.utils.web.PageResult;

import java.util.Date;

@Service
public class BorrowService {

    @Autowired
    BorrowHistoryDao borrowHistoryDao;

    public PageResult<BorrowHistoryDO> getBorrowList(@UserSession UserSessionDTO userSessionDTO, ListReqDTO listReqDTO){

        LambdaQueryChainWrapper<BorrowHistoryDO> query = borrowHistoryDao.lambdaQuery();
        query = query.eq(BorrowHistoryDO::getUsername, userSessionDTO.getUsername());
        if(listReqDTO.getType()!=null){
            if(listReqDTO.getType().equals("FINISH")){
                //query.eq(BorrowHistoryDO::getRealTime,null);
                query.isNotNull(BorrowHistoryDO::getRealTime);
            }else if(listReqDTO.getType().equals("UNFINISH")){
                query.isNull(BorrowHistoryDO::getRealTime);
            }
        }

        //根据时间进行排序
        if(listReqDTO.getSortBy()!=null){
            if(listReqDTO.getSortBy().equals("ASC")){
                query.orderByAsc(BorrowHistoryDO::getShouldTime);
            }else if(listReqDTO.getSortBy().equals("DESC")){
                query.orderByDesc(BorrowHistoryDO::getShouldTime);
            }
        }

        Page<BorrowHistoryDO> pageResult = query.page(new Page<>(listReqDTO.getPageNow(), listReqDTO.getPageSize()));

        if (pageResult.getSize() == 0) {
            return new PageResult<>();
        }

        return new PageResult<>(pageResult.getPages(), pageResult.getRecords());


    }

    public Boolean reBorrow(UserSessionDTO userSessionDTO, Long borrowHistoryId, Date newDate) {
        BorrowHistoryDO historyDO = borrowHistoryDao.getById(borrowHistoryId);
        AssertUtils.notNull(historyDO, ApiExceptionEnum.HISTORY_NOT_FOUND);

        //对时间进行校验
        AssertUtils.isTrue(historyDO.getShouldTime().before(newDate),ApiExceptionEnum.REBORROW_TIME_ERROR);
        AssertUtils.isTrue(historyDO.getUsername().equals(userSessionDTO.getUsername()), ApiExceptionEnum.USER_NOT_MATCHING);
        historyDO.setShouldTime(newDate);
        AssertUtils.isTrue(borrowHistoryDao.updateById(historyDO),ApiExceptionEnum.UNKNOWN_ERROR);
        return true;
    }
}
