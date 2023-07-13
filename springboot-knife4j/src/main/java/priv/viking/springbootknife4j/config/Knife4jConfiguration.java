package priv.viking.springbootknife4j.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Created By Viking on 2023/7/13
 * knife4j接口文档配置类
 */
@Configuration
//老版注解(下面三个)
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
// 新版注解
//@EnableSwagger2WebMvc
public class Knife4jConfiguration {
    // 新版配置
//    @Bean(value = "dockerBean")
//    public Docket docketBean() {
//        //指定使用Swagger2规范
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(new ApiInfoBuilder()
//                        //描述字段支持Markdown语法
//                        .description("#Knife4j RESTful APIs, knife4j文档demo")
//                        .termsOfServiceUrl("https://doc.xiaominfo.com/")
//                        .contact("viking_yan@163.com")
//                        .version("v1.0")
//                        .build())
//                //分组名称
//                .groupName("首页服务")
//                .select()
//                //这里指定Controller扫描包路径
//                .apis(RequestHandlerSelectors.basePackage("priv.viking.springbootknife4j.controller"))
//                .paths(PathSelectors.any())
//                .build();
//        return docket;
//    }
    // 老版配置
    @Bean(value = "docketBean")
    public Docket docketBean(){
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        // 文档标题
                        .title("knife4j测试接口文档")
                        .description("接口文档简介内容")
                        // 服务URL
                        .termsOfServiceUrl("http://localhost:88888/")
                        .contact(new Contact("viking","http://localhost:88888/","viking_yan@163.com"))
                        .version("v1.0")
                        .build())
                //分组名称
                .groupName("首页模块")
                .select()
                //这里指定Controller扫描包路径
//                .apis(RequestHandlerSelectors.basePackage("priv.viking"))
                // 通过扫描@ApiOperation注解的方法导入
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
