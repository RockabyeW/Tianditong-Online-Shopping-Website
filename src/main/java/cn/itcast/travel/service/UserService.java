package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 注册
     *
     * @param user
     * @return
     */
    Boolean register(User user);

    /**
     * 激活
     *
     * @param code
     * @return
     */
    boolean active(String code);

    /**
     * 登录
     *
     * @param user
     * @return
     */
    User login(User user);
}
