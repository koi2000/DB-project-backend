package qd.cs.koi.database.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qd.cs.koi.database.dao.MessageDao;
import qd.cs.koi.database.entity.MessageDO;
import qd.cs.koi.database.websocket.entity.Message;

import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;


    public List<MessageDO> queryMessage(String username, String chatUser) {
        List<MessageDO> list = messageDao.lambdaQuery().eq(MessageDO::getFromUser, username).eq(MessageDO::getToUser, chatUser)
                .or(o -> o.eq(MessageDO::getFromUser, chatUser).eq(MessageDO::getToUser, username)).list();
        list.sort(new Comparator<MessageDO>() {
            @Override
            public int compare(MessageDO o1, MessageDO o2) {
                return (int) (o1.getGmtCreate().getTime() - o2.getGmtCreate().getTime());
            }
        });
        return list;
    }
}
