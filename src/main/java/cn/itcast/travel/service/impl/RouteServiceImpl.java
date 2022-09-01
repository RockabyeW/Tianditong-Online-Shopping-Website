package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImageDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImageDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImageDao routeImageDao = new RouteImageDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        // 1. 封装pageBean
        PageBean<Route> pageBean = new PageBean<Route>();
        pageBean.setCurrentPage(currentPage); // 设置当前页码
        pageBean.setPageSize(pageSize); // 设置显示数

        int totalCount = routeDao.findTotalCount(cid, rname);
        pageBean.setTotalCount(totalCount); // 总记录数
        int start = (currentPage - 1) * pageSize; // 查询的开始页码
        List<Route> list = routeDao.findByCurrentPage(cid, start, pageSize, rname);
        pageBean.setList(list); // 数据集合
        int totalPage = totalCount % pageSize == 0 ? (totalCount / pageSize) : ((totalCount / pageSize) + 1);
        pageBean.setTotalPage(totalPage); // 总页数

        return pageBean;
    }

    @Override
    public Route findOne(String rid) {
        // 1. 根据id查询route表获取route对象
        Route route = routeDao.findById(Integer.parseInt(rid));
        // 2. 根据route的id查询图片list信息
        List<RouteImg> routeImgList = routeImageDao.findByRid(route.getRid());
        // 3. 集合中设置到route中
        route.setRouteImgList(routeImgList);
        // 4. 根据route信息，查询卖家的信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);
        // 5. 查询收藏次数
        int count = favoriteDao.getCountByRid(rid);
        route.setCount(count);

        return route;
    }
}
