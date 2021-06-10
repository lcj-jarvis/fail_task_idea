package com.mrlu.server.dao;

import com.mrlu.server.bean.UserDO;
import fai.comm.util.FaiList;

import java.util.List;

/**
 * @author 简单de快乐（陆朝基）
 * @date 2021-06-07 11:58
 */
public interface UserDao {

    /**
     * 查询所有的用户
     * @return
     */
    List<UserDO> listsUserDo();


    /**
     * 新增用户
     * @param userDO
     */
    void insertUserDo(UserDO userDO);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    UserDO getUserDoById(Integer id);

    /**
     * 根据姓名查询用户
     * @param name
     * @return
     */
    UserDO getUserDoByName(String name);

    /**
     * 根据id更新用户
     * @param userDO
     */
    void updateUserById(UserDO userDO);

    /**
     * 根据姓名更新用户
     * @param userDO
     */
    void updateUserByName(UserDO userDO);


}
