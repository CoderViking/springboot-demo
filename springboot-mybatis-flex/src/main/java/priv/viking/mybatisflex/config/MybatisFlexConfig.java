package priv.viking.mybatisflex.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by viking on 2023/7/30
 * mybatis-flex配置文件
 */
@Configuration
@MapperScan("priv.viking.mybatisflex.mapper")
public class MybatisFlexConfig {
}
