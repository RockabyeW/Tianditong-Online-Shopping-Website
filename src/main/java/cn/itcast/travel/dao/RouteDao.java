package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 根据cid查询总记录数
     *
     * @param cid
     * @param rname
     * @return
     */
    public int findTotalCount(int cid, String rname);

    /**
     * 查询当前页面的数据
     *
     * @param cid
     * @param start
     * @param pageSize
     * @param rname
     * @return
     */
    public List<Route> findByCurrentPage(int cid, int start, int pageSize, String rname);

    /**
     * 根据rid查询route详情信息
     *
     * @param rid
     * @return
     */
    public Route findById(int rid);
}
