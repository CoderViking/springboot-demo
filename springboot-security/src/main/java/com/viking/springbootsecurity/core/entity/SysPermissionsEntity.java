package com.viking.springbootsecurity.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("sys_permissions")
public class SysPermissionsEntity implements Serializable {
    private static final long serialVersionID = 1L;

    @TableId(type = IdType.ID_WORKER)
    private Long rid;   //权限ID
    private String name;    //权限名称
    private String permissions; //权限标识


    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
