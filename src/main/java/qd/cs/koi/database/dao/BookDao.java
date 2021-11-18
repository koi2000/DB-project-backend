package qd.cs.koi.database.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import qd.cs.koi.database.entity.BookDO;
import qd.cs.koi.database.mapper.BookMapper;

@Repository
public class BookDao extends ServiceImpl<BookMapper, BookDO> {
}
