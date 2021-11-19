package qd.cs.koi.database.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import qd.cs.koi.database.entity.BorrowHistoryDO;
import qd.cs.koi.database.entity.UserDO;
import qd.cs.koi.database.mapper.BorrowHistoryMapper;
import qd.cs.koi.database.mapper.UserMapper;

@Repository
public class BorrowHistoryDao extends ServiceImpl<BorrowHistoryMapper, BorrowHistoryDO> {
}
