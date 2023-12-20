package com.example.demo.Controller;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.VO.Client;
import com.example.demo.VO.PCMonitorChartVO;
import com.example.demo.VO.PCMonitorVO;
import com.example.demo.VO.UserVO;

import lombok.extern.slf4j.Slf4j;

/**
 * [ 템플릿 설명 ]
 * - 해당 파일은 Restful API 형태로 URL 관리하는 파일입니다
 * - 뷰 자체만을 리턴해주거나 API 호출에 대한 처리를 해줍니다.
 * [ Annotation 설명 ]
 * - @Slf4j : 로그를 위해 사용하는 Annotation
 * - @RestController: Restful API 구조에서 JSON 타입으로 데이터를 반환 받기 위해 사용하는 Annotation
 * - @Controller: Spring MVC 패턴을 사용하기 위한 Annotation
 * - @RequestMapper: Controller URL Default
 *
 * @author lee
 * @since 2022.09.30
 */


@Slf4j
@RequestMapping("/")
@Controller
public class MainController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping({"index",""})
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("index");
		HttpSession session = request.getSession(false);
		
		if (session == null || session.getAttribute("user") == null) {
			modelAndView.setViewName("redirect:login");
			return modelAndView;
		}
		
		
		Client client = new Client();
		String ip = client.getRemoteIP(request);
		UserVO user = (UserVO) session.getAttribute("user");
		session.setAttribute("user", user);
		modelAndView.addObject("clientIP",ip);
		modelAndView.addObject("userSessionListLangth",num);
		
		/* CPU 사용량 */
		PCMonitorVO pcMonitorVO = new PCMonitorVO();
//		modelAndView.addObject("pcMonitorVO", pcMonitorVO);
		
		return modelAndView;
	}
	
	@GetMapping("login")
	public ModelAndView getLogin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("login");
		System.out.println("Login Page");
		 try{
			modelAndView.addObject("hostname", InetAddress.getLocalHost().getHostName());
			modelAndView.addObject("ip", InetAddress.getLocalHost().getHostAddress());
			System.out.println( InetAddress.getLocalHost().getHostName() );
			System.out.println( InetAddress.getLocalHost().getHostAddress() );
			System.out.println("Access Client IP : "+new Client().getRemoteIP(request));
		}
		catch( UnknownHostException e ){
		  e.printStackTrace();
		  logger.info("Exception : "+e);
		}
		 
		Client client = new Client();
		String ip = client.getRemoteIP(request);
		modelAndView.addObject("clientIP",ip);
		
		/* CPU 사용량 */
		return modelAndView;
	}
	
	static int num;
	@PostMapping("login")
	public String postLogin(HttpServletRequest request, HttpServletResponse response) {
		
		UserVO user = new UserVO();
		num ++;
		user.setNum(num);
		user.setEmail(request.getParameter("inputEmail"));
		user.setPassword(request.getParameter("inputPassword"));
		user.setClientIP(new Client().getRemoteIP(request));
		ModelAndView modelAndView = new ModelAndView("index");
		
		/* session 등록 */
		// 세션을 생성하기 전에 기존의 세션 파기
		request.getSession().invalidate();
		HttpSession session = request.getSession(true);  // Session이 없으면 생성
		user.setSessionID(session.getId());
		// 세션에 userId를 넣어줌
		session.setAttribute("user", user);
		session.setAttribute("ip", user.getClientIP());
		session.setMaxInactiveInterval(1800); // Session이 30분동안 유지

		System.out.println(user.toString());
//		return modelAndView;
		return "redirect:/index";
	}
	
	@PostMapping("deleteSession")
	@ResponseBody
	public boolean deleteSession(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(false);
			session.invalidate();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@PostMapping("getSessionID")
	@ResponseBody
	public String getSessionID(HttpServletRequest request, HttpServletResponse response, @RequestBody String sessionID) {
		try {
			HttpSession session = request.getSession(false);
			System.out.println("SESSION CHECK : "+session.getId());
		} catch (Exception e) {
		}
		return sessionID;
	}
	
	@PostMapping("chart")
	@ResponseBody
	public PCMonitorChartVO chart() {
		PCMonitorChartVO chart=new PCMonitorChartVO();
//		System.out.println("TEST"+chart.toString());
		return new PCMonitorChartVO();
	}
	
	@PostMapping("proxytest")
	@ResponseBody
	public String proxytest(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "url") String url) {
		System.out.println("PROXY TEST URL :"+url);
		String result = "";
		
		logger.debug("============= PROXY LOG =============");
		try {
            // HTTP 테스트
            URL httpUrl = new URL("http://"+url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            System.out.println("HTTP Response Code: " + httpURLConnection.getResponseCode());
            result = "<h3 style=\"color : #3a6351\">HTTP Response Code: " + httpURLConnection.getResponseCode()+"</h3>";
            // HTTPS 테스트
            URL httpsUrl = new URL("https://"+url);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) httpsUrl.openConnection();
            System.out.println("HTTPS Response Code: " + httpsURLConnection.getResponseCode());
            result += "<h3 style=\"color : #3a6351\">HTTPS Response Code: " + httpsURLConnection.getResponseCode()+"</h3>";
        } catch (Exception e) {
        	StringWriter sw = new StringWriter();
        	PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            result = "<hr>";
            result += "<h1 style=\"color : #be0000\">Exception Log</h1>";
            result += sw.toString();
        }
		result += "<hr>";
		result +="<h2>OS 환경변수</h2>" 
				+"<br>"+System.getenv();
		
		result += "<hr>";
		result +="<h2>JVM 환경변수</h2>" ;
		Properties properties = System.getProperties();
		StringBuilder resultBuilder = new StringBuilder();
		Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
		for (Map.Entry<Object, Object> entry : entrySet) {
			resultBuilder.append("<br>").append(entry.getKey()).append(": ").append(entry.getValue());
		}
		return result+resultBuilder;
	}
	
	@GetMapping("proxy")
	public ModelAndView proxyPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("proxy");
		System.out.println("proxy.jsp");
		return modelAndView;
	}
}
