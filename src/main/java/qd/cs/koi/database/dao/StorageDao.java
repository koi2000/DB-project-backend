package qd.cs.koi.database.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import qd.cs.koi.database.entity.StorageDO;
import qd.cs.koi.database.entity.UserDO;
import qd.cs.koi.database.mapper.StorageMapper;
import qd.cs.koi.database.mapper.UserMapper;

@Repository
public class StorageDao extends ServiceImpl<StorageMapper, StorageDO> {
}
