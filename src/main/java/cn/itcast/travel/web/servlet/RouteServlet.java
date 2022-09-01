package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        // 接受搜索输入的rname
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");

        // 2. 处理参数 // 类别id
        int cid = 0;
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr))
            cid = Integer.parseInt(cidStr);
        else
            cid = 0;

        int currentPage = 0; // 当前页码，默认第一页
        if (currentPageStr != null && currentPageStr.length() > 0 && "null".equals(currentPageStr))
            currentPage = Integer.parseInt(currentPageStr);
        else
            currentPage = 1;

        int pageSize = 0; // 每页显示条数，默认显示五条
        if (pageSizeStr != null && pageSizeStr.length() > 0)
            pageSize = Integer.parseInt(pageSizeStr);
        else
            pageSize = 5;

        // 3. 调用service对象查询
        PageBean<Route> routePageBean = service.pageQuery(cid, currentPage, pageSize, rname);
        // 4. 写回客户端
        writeValue(routePageBean, response);
    }

    /**
     * 根据id查询一个route的详情信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 接收id
        String rid = request.getParameter("rid");
        // 2. 调用service查询route对象
        Route route = service.findOne(rid);
        // 3. 转化为json写回
        writeValue(route, response);
    }

    /**
     * 判断是否收藏了该线路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取线路id
        String rid = request.getParameter("rid");
        // 2. 获取当前登录的用户uid
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) uid = 0;
        else uid = user.getUid();
        // 3. 调用FavoriteService的方法进行查询
        boolean favorite = favoriteService.isFavorite(rid, uid);
        // 4. 写回客户端
        writeValue(favorite, response);
    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) return;
        else uid = user.getUid();
        // 调用service
        favoriteService.add(rid, uid);
    }
}
