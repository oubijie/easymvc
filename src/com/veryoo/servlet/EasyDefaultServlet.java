package com.veryoo.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.veryoo.controller.HomeController;
import com.veryoo.controller.UserController;

@WebServlet(value={"/"})
public class EasyDefaultServlet extends HttpServlet{
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String result = "404.jsp";
		
		String uri = req.getRequestURI();
		String controllerName = getControllerName(uri);
		System.out.println("请求的URL：" + uri);
		System.out.println("处理的Controller：" + controllerName);
		
		try {
			Class conCls = Class.forName(controllerName);
			Object con = conCls.newInstance();
			result = (String)conCls.getMethod("execute").invoke(con);
		} catch (ClassNotFoundException e) {
			System.err.println("找不到处理的类：" + controllerName);
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println("实例化类出错：" + controllerName);
		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.err.println("执行Controller的execute方法出错：" + controllerName);
		} 
		
//		if(uri.equals("/home")){
//			HomeController controller = new HomeController();
//			result = controller.execute();
//		}else if(uri.equals("/user")){
//			UserController controller = new UserController();
//			result = controller.execute();
//		}
		
		req.getRequestDispatcher(result).forward(req, resp);
	}
	
	//根据请求的URL地址返回处理请求的Controller全称
	private String getControllerName(String uri){
		String name = uri.substring(1);
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		name = "com.veryoo.controller." + name + "Controller";  //TODO 包名应从配置获取，或者根据当前servlet的包名获取
		return name;
	}
}
