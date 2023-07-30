package priv.viking.mybatisflex;

import com.mybatisflex.core.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import priv.viking.mybatisflex.entity.Account;
import priv.viking.mybatisflex.mapper.AccountMapper;

@SpringBootTest
class MybatisFlexApplicationTests {

    @Autowired
    private AccountMapper accountMapper;
    @Test
    void contextLoads() {
        QueryWrapper queryWrapper = QueryWrapper.create().where(Account::getAge).eq(18);
        Account account = accountMapper.selectOneByQuery(queryWrapper);
        System.out.println(account);
    }

}
