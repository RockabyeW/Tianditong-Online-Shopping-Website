package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite isFavorite(int rid, int uid) {
        String sql = "SELECT * FROM tab_favorite WHERE rid= ? and uid = ?";
        Favorite favorite = null;
        try {
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int getCountByRid(String rid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid = ?";
        return template.queryForObject(sql, Integer.class, rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "INSERT INTO tab_favorite values(?, ?, ?)";
        template.update(sql, rid, new Date(), uid);
    }
}
