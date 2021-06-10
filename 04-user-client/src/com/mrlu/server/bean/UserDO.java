package com.mrlu.server.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author 简单de快乐（陆朝基）
 * @date 2021-06-07 11:54
 */
public class UserDO implements Serializable {
    private static final long serialVersionUID = 88888888L;

    private Integer id;
    private String name;
    private Integer age;
    private String email;
    private String departmentName;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public UserDO() {

    }

    public UserDO(Integer id, String name, Integer age, String email, String departmentName) {
        this(id, name, age, email, departmentName, null, null);
    }

    public UserDO(Integer id, String name, Integer age, String email, String departmentName, LocalDateTime gmtCreate, LocalDateTime gmtModified) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.departmentName = departmentName;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDO userDO = (UserDO) o;
        return id.equals(userDO.id) &&
                name.equals(userDO.name) &&
                age.equals(userDO.age) &&
                email.equals(userDO.email) &&
                departmentName.equals(userDO.departmentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, email, departmentName);
    }

}
