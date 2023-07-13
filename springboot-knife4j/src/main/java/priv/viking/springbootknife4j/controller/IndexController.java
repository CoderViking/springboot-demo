package priv.viking.springbootknife4j.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By Viking on 2023/7/13
 */
@Api(tags = "首页模块")
@RestController
@RequestMapping("/index")
public class IndexController {

    @ApiOperation("测试启动问候")
    @ApiImplicitParam(name = "name", value = "姓名", required = true)
    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestParam(value = "name") String name){
        System.out.println("get name = " + name);
        return ResponseEntity.ok("Hello " + name);
    }
}
