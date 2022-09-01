package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            // 1. 定义sql
            String sql = "select * from tab_user where username = ?";
            // 2. 执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {

        }
        return user;
    }

    /**
     * 保存
     *
     * @param user
     */
    @Override
    public void save(User user) {
        // 1. 定义sql
        String sql = "insert into tab_user(username, password, name, birthday, sex, telephone, email, status, code) " +
                " values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // 2. 执行sql
        template.update(sql, user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    /**
     * 根据code查询用户
     *
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            // 1. 定义sql
            String sql = "select * from tab_user where code = ?";
            // 2. 执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 更改status
     *
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        // 1. 定义sql
        String sql = "update tab_user set status = 'Y' where code = ?";
        // 2. 执行sql
        template.update(sql, user.getCode());
    }

    /**
     * 根据用户名和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        String sql = "select * from tab_user where username = ? and password = ?";
        User user = null;
        try {
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

}
