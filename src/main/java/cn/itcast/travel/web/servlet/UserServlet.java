package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取数据
        // 获取请求中填写的验证码内容
        String check = request.getParameter("check");
        // 获取session中生成的验证码
        HttpSession session = request.getSession();
        String checkCode = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER"); // 保证验证码只能使用一次
        // 判断验证码
        ResultInfo info = new ResultInfo();
        if (checkCode == null || !checkCode.equalsIgnoreCase(check)) {
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
        } else {
            Map<String, String[]> map = request.getParameterMap();
            // 2. 封装对象
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            // 3. 调用 service 完成注册
            Boolean flag = service.register(user);

            // 4. 响应结果
            if (flag) {
                // 注册成功
                info.setFlag(true);
            } else {
                // 注册失败
                info.setFlag(false);
                info.setErrorMsg("注册失败!");
            }
        }

        // 将info对象序列化为json
        String json = writeValueAsString(info);

        // 将json数据写回客户端
        // 设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 查找一个功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从session中获取用户信息
        Object user = request.getSession().getAttribute("user");
        // 写会客户端
        writeValue(user, response);
    }

    /**
     * 退出功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 删除session数据
        request.getSession().invalidate();
        // 跳转页面
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * 登录功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 验证码校验
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        ResultInfo info = new ResultInfo(); // 保存提示信息
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
        } else {
            // 1. 获取用户名和密码数据
            Map<String, String[]> map = request.getParameterMap();
            // 2. 封装
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            // 3. 调用service根据用户名密码进行查询
            User loginUser = service.login(user);
            // 4. 判断
            if (loginUser == null) {
                info.setFlag(false);
                info.setErrorMsg("用户名/密码错误");
            } else {
                if ("N".equals(loginUser.getStatus())) {
                    // 用户未激活
                    info.setFlag(false);
                    info.setErrorMsg("您未激活邮箱，请激活");
                } else {
                    session.setAttribute("user", loginUser);
                    info.setFlag(true);
                }
            }
        }
        // 将info对象序列化为json
        String json = writeValueAsString(info);

        // 将json数据写回客户端
        // 设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 激活用户功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            // 2. 调用service激活
            boolean flag = service.active(code);
            // 3. 判断flag
            String msg = null;
            if (flag) {
                // 激活成功
                msg = "激活成功，请<a href='http://localhost/travel/login.html'>登录</a>";
            } else {
                // 激活失败
                msg = "激活失败，请联系客服!";
            }
            // 4. 回写
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

}
