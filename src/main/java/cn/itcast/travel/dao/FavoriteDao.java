package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    public Favorite isFavorite(int rid, int uid);

    int getCountByRid(String rid);

    void add(int rid, int uid);
}
