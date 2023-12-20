package com.example.demo.VO;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public String getRemoteIP(HttpServletRequest request){
		
		String ip = request.getHeader("X-Forwarded-For"); 
		String chekc = null;
		//proxy 환경일 경우
		if (ip == null || ip.length() == 0) {
		    ip = request.getHeader("Proxy-Client-IP");
		    chekc = " x-forwarded-for 정보 없음";
		    logger.info("Proxy-Client-IP : "+ip);
		}
		
		//웹로직 서버일 경우
		if (ip == null || ip.length() == 0) {
		    ip = request.getHeader("WL-Proxy-Client-IP");
		    chekc = " x-forwarded-for 정보 없음";
		    logger.info("WL-Proxy-Client-IP : "+ip);
		}
		
		if (ip == null || ip.length() == 0) {
		    ip = request.getRemoteAddr() ;
		    chekc = " x-forwarded-for 정보 없음";
		    logger.info("request.getRemoteAddr : "+ip);
		}
		
//		System.out.println("CLIENT IP :"+ip);
		
		String userAgent = request.getHeader("User-Agent");

        if (userAgent != null && userAgent.contains("Mobile")) {
            // 모바일 디바이스에 대한 처리
            return "[mobile] : "+ip+" | "+chekc;
        } else {
            // 데스크톱 디바이스에 대한 처리
            return "[pc] : "+ ip+" | "+chekc;
        }
	}
}
