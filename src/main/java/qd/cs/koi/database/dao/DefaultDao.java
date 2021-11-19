package qd.cs.koi.database.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import qd.cs.koi.database.entity.DefaultDO;
import qd.cs.koi.database.mapper.DefaultMapper;

@Repository
public class DefaultDao extends ServiceImpl<DefaultMapper, DefaultDO> {
}
