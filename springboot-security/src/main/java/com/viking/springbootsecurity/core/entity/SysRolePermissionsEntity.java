package com.viking.springbootsecurity.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("sys_role_permissions")
public class SysRolePermissionsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(type = IdType.ID_WORKER)
    private Long rid;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 权限ID
     */
    private Long permissionsId;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionsId() {
        return permissionsId;
    }

    public void setPermissionsId(Long permissionsId) {
        this.permissionsId = permissionsId;
    }
}
