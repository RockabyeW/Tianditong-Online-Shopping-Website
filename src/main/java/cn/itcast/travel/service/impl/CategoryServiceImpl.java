package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao = new CategoryDaoImpl();

    @Override
    public List<Category> findAllCategory() {
        // 1. 从redis中查询
        // 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        // 1.1使用sortedset排序查询（cid）和（cname）
        Set<Tuple> category = jedis.zrangeWithScores("category", 0, -1);
        // 2. 判断
        List<Category> categories = null;
        if (category == null || category.size() == 0) {
            // 2.1 为空，从数据库查询
            categories = dao.findAllCategory();
            // 2.2 将数据保存在redis中
            for (int i = 0; i < categories.size(); i++)
                jedis.zadd("category", categories.get(i).getCid(), categories.get(i).getCname());
        } else {
            // 将set集合数据转变为list集合数据
            categories = new ArrayList<Category>();
            for (Tuple tuple : category) {
                Category c = new Category();
                c.setCname(tuple.getElement());
                c.setCid((int)tuple.getScore());
                categories.add(c);
            }
        }

        return categories;
    }
}
