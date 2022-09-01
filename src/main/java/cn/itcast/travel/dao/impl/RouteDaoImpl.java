package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid, String rname) {
        // String sql = "select count(*) from tab_route where cid = ?";
        // 1. 定义sql模板
        String sql = "SELECT COUNT(*) FROM tab_route WHERE 1 = 1 ";

        // sql容器
        StringBuilder sb = new StringBuilder(sql);
        // 条件容器
        List params = new ArrayList();
        // 2. 判断传入参数是否有值
        if (cid != 0) {
            params.add(cid);
            sb.append(" and cid = ? ");
        }
        if (rname != null && rname.length() != 0) {
            params.add("%" + rname + "%");
            sb.append(" and rname like ? ");
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class, params.toArray());
    }

    @Override
    public List<Route> findByCurrentPage(int cid, int start, int pageSize, String rname) {
        // String sql = "select * from tab_route where cid = ? limit ?, ?";
        // 1. 定义sql模板
        String sql = "SELECT * FROM tab_route WHERE 1 = 1 ";

        // sql容器
        StringBuilder sb = new StringBuilder(sql);
        // 条件容器
        List params = new ArrayList();
        // 2. 判断传入参数是否有值
        if (cid != 0) {
            params.add(cid);
            sb.append(" and cid = ? ");
        }
        if (rname != null && rname.length() != 0) {
            params.add('%' + rname + '%');
            sb.append(" and rname like ? ");
        }
        params.add(start);
        params.add(pageSize);
        sb.append(" limit ?, ? ");
        sql = sb.toString();

        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
    }

    @Override
    public Route findById(int rid) {
        String sql = "SELECT * FROM tab_route WHERE rid = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
    }

}
