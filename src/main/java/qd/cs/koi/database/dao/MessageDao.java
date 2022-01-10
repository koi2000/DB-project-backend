package qd.cs.koi.database.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import qd.cs.koi.database.entity.MessageDO;
import qd.cs.koi.database.entity.StorageDO;
import qd.cs.koi.database.mapper.MessageDOMapper;
import qd.cs.koi.database.mapper.StorageMapper;

@Repository
public class MessageDao  extends ServiceImpl<MessageDOMapper, MessageDO> {
}
