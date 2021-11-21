package qd.cs.koi.database.service.defaults;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qd.cs.koi.database.converter.BaseConvertUtils;
import qd.cs.koi.database.dao.DefaultDao;
import qd.cs.koi.database.entity.BookDO;
import qd.cs.koi.database.entity.DefaultDO;
import qd.cs.koi.database.interfaces.Book.BookListDTO;
import qd.cs.koi.database.interfaces.ListReqDTO;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.web.PageResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultService {

    @Autowired
    DefaultDao defaultDao;

    public PageResult<DefaultDO> list(UserSessionDTO userSessionDTO, ListReqDTO reqDTO){
        LambdaQueryChainWrapper<DefaultDO> query = defaultDao.lambdaQuery().eq(DefaultDO::getUserName, userSessionDTO.getUsername());

        Page<DefaultDO> pageResult = query.page(new Page<>(reqDTO.getPageNow(), reqDTO.getPageSize()));
        if (pageResult.getSize() == 0) {
            return new PageResult<>();
        }

        return new PageResult<>(pageResult.getPages(), pageResult.getRecords());
    }
}
