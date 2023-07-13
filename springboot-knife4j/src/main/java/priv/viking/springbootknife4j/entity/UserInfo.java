package priv.viking.springbootknife4j.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created By Viking on 2023/7/13
 */
@ApiModel(value = "用户模型")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

    @ApiModelProperty(value = "id", notes = "userId")
    private Long id;

    @ApiModelProperty(value = "用户名", notes = "用户名", required = true)
    private String name;

    @ApiModelProperty(value = "性别", notes = "性别，(男：male，女：female)")
    private String gender;

    @ApiModelProperty(value = "年龄", notes = "年龄")
    private Integer age;

    @ApiModelProperty(value = "创建时间", notes = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", notes = "更新时间")
    private Date updateTime;
}
