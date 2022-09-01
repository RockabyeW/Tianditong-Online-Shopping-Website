package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * 用户保存
     *
     * @param user
     */
    public void save(User user);

    /**
     * 根据code查询用户
     *
     * @param code
     * @return
     */
    User findByCode(String code);

    /**
     * 激活，更改status
     *
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据用户名和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}
