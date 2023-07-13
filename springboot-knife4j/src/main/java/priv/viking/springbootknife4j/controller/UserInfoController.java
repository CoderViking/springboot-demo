package priv.viking.springbootknife4j.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import priv.viking.springbootknife4j.entity.UserInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created By Viking on 2023/7/13
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    private List<UserInfo> userInfos = new ArrayList<>();

    @ApiOperation(value = "获取用户信息接口", notes = "获取用户信息接口")
    @ApiImplicitParam(name = "id", value = "用户userId", required = true, dataType = "int")
    @GetMapping("/get/{id}")
    public UserInfo getUserInfoById(@PathVariable("id") Long id){
        UserInfo userInfo = userInfos.stream().filter(bean -> bean.getId().equals(id)).findFirst().orElse(null);
        return userInfo;
    }

    @ApiOperation(value = "插入用户信息接口", notes = "插入用户信息接口")
    @PostMapping("/insert")
    public UserInfo getUserInfoById(@RequestBody UserInfo userInfo){
        boolean present = userInfos.stream().max(Comparator.comparing(UserInfo::getId)).isPresent();
        UserInfo maxIdUser = UserInfo.builder().id(0L).build();
        if (present) {
            maxIdUser = userInfos.stream().max(Comparator.comparing(UserInfo::getId)).get();
        }
        userInfo.setId(maxIdUser.getId() + 1);
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfos.add(userInfo);
        return userInfo;
    }


}
