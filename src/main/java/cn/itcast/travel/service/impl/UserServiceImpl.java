package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @Override
    public Boolean register(User user) {
        // 1. 根据用户名查询用户
        User byUsername = userDao.findByUsername(user.getUsername());
        // 2. 判断是否存在
        if (byUsername != null)
            // 用户名存在，注册失败
            return false;
        // 3. 保存用户信息
        // 3.1 设置激活码，唯一
        user.setCode(UuidUtil.getUuid());
        // 3.2 设置激活状态
        user.setStatus("N");
        userDao.save(user);
        // 4. 发送激活邮件
        String content = "<a href='http://localhost:80/travel/user/activeUser?code=" + user.getCode() + "'>点击激活</a>";
        MailUtils.sendMail(user.getEmail(), content, "激活邮件");

        return true;
    }

    /**
     * 激活
     *
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        // 1. 根据code查询是否存在该用户
        User user = userDao.findByCode(code);
        if (user != null) {
            // 2. 激活
            userDao.updateStatus(user);
            return true;
        } else
            return false;
    }

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
