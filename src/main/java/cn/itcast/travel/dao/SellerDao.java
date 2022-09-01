package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    /**
     * 根据id查询卖家信息
     * @param sid
     * @return
     */
    public Seller findById(int sid);
}
