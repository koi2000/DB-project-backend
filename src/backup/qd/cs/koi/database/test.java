package qd.cs.koi.database;


import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import qd.cs.koi.database.utils.util.EncrypDES;
import qd.cs.koi.database.utils.util.MD5;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseApplication.class)
public class test {

    @Autowired
    EncrypDES encrypDES;

    @Test
    public void md5Test() throws Exception {
        String keyword="i love you";
        String encrypt = encrypDES.encrypt(keyword);
        System.out.println(encrypt);
        String decrypt = encrypDES.decrypt(encrypt);
        System.out.println(decrypt);
    }
}

