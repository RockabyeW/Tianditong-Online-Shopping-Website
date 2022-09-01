package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImageDao {

    /**
     * 根据rid查询route对应的图片集合
     * @param rid
     * @return
     */
    public List<RouteImg> findByRid(int rid);
}
