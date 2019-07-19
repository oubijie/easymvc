package com.veryoo.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.IllegalFormatException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.veryoo.controller.HomeController;
import com.veryoo.controller.UserController;

@WebServlet(value={"/"})
public class EasyDefaultServlet extends HttpServlet{
	
	public static final String CONTROLLER_PACKAGE = "com.veryoo.controller";
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String result = "/404.jsp";
		
		String controllerName = "";
		String methodName = "";
		
		try {
			
			String uri = req.getRequestURI();
			controllerName = getControllerName(uri);
			System.out.println("请求的URL：" + uri);
			System.out.println("处理的Controller：" + controllerName);
			methodName = getMethodName(uri);
			System.out.println("处理的Method：" + methodName);
		
			Class cls = Class.forName(controllerName);
			Object controller = cls.newInstance();
			//TODO 这里写死了拿父类的setRequest和setResponse方法，所以Controller只能一层继承
			cls.getSuperclass().getMethod("setRequest", HttpServletRequest.class).invoke(controller, req);
			cls.getSuperclass().getMethod("setResponse", HttpServletResponse.class).invoke(controller, resp);
			
			//调用Controller的处理方法
			result = (String)cls.getMethod(methodName).invoke(controller);
		} catch (ClassNotFoundException e) {
			System.err.println("找不到处理的类：" + controllerName);
			e.printStackTrace();
			result = "/500.jsp";
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println("实例化类出错：" + controllerName);
			e.printStackTrace();
			result = "/500.jsp";
		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.err.println("执行Controller的execute方法出错：" + controllerName);
			e.printStackTrace();
			result = "/500.jsp";
		} 
		
		//增加json支持，如果方法返回字符串“json”，则在request中把json内容取出并返回
		if(result!=null && result.equals("json")){
			resp.setContentType("application/Json");
			String json = (String)req.getAttribute("json");
			resp.getWriter().write(json);
			resp.getWriter().flush();
		}else{
			req.getRequestDispatcher(result).forward(req, resp);
		}
	}
	
	

	//根据请求的URL地址返回处理请求的Controller全称
	private String getControllerName(String uri){
		String name = uri.substring(1);
		//GET请求的参数带在url上，需要先过滤掉
		if(uri.indexOf("?") != -1){
			name = uri.substring(0, uri.indexOf("?"));
		}
		int splitIndex = name.indexOf("/")==-1 ? name.length() : name.indexOf("/");  //找到第二个/的下标
		name = name.substring(0, 1).toUpperCase() + name.substring(1, splitIndex);
		name = CONTROLLER_PACKAGE + "." + name + "Controller"; //拼出类的全限定名
		return name;
	}
	
	//根据请求的URL地址返回处理请求的方法
	private String getMethodName(String uri) {
		String methodName = "execute";
		//GET请求的参数带在url上，需要先过滤掉
		if(uri.indexOf("?") != -1){
			uri = uri.substring(0, uri.indexOf("?"));
		}
		StringTokenizer st = new StringTokenizer(uri, "/");
		if(st.countTokens() <= 0 || st.countTokens() > 2){
			throw new IllegalArgumentException("URL不正确");
		}
		String name = uri.substring(1);
		if(name.indexOf("/") != -1){
			methodName = name.substring(name.indexOf("/")+1).replace("/", "");
		}
		if(methodName.equals("")){
			methodName = "execute";
		}
		return methodName;
	}
}
