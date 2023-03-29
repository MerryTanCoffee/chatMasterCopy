package kr.or.ddit.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;

import kr.or.ddit.service.ChatService;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/module")
public class EchoHandler extends TextWebSocketHandler {
	
	@Inject
	private ChatService chatService;
	
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
	
	private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();
	
	
	// 연결이 되면 Map에  session.getId를 넣는다.
	// 최초 연결시 session이 고정된다.
	// 새로운 연결을 할 때마다 session을 새로 발급받는다.
	@RequestMapping(value = "/chat.do")
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info(session.getId() + "연결됨");
		String userName = searchUserName(session);
		System.out.println("연결된갯수 : " + sessionList.size()+1);
		users.put(session.getId(),session);
		// 접속한 전체 유저 아이디
		sessionList.add(session);
		 session.sendMessage(new TextMessage("recMs :"+chatService.countMessageView(userName)));
		// 로그인한 개별 유저 아이디를 가져옴.
		String memId = getId(session);
		// map에 개별 유저 아이디를 넣는다.
		users.put(memId, session);
		
		
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		String memId = getId(session);
		String userName = searchUserName(session);
		
		String strMessage = message.getPayload();
		System.out.println("메시지 : " + strMessage);
		
		for(WebSocketSession sess: sessionList) {
            sess.sendMessage(new TextMessage(userName +": "+message.getPayload()));
        }
		
		Map<String,Object> httpSession = session.getAttributes();
		String loginId = (String) httpSession.get("id");
		if(loginId != null) {
			  
	        //접속한 해당 유저 읽지않은 알림 데이터 전체 카운트 만 가져올 경우  
	        //int countInform=informService.countInform(memId);
	         
	        //1.해당 유저 알림데이터 전체 가져오기
	        //List<InformDTO> getInform= informService.selectInformDTO(memId);
	        //for(InformDTO informDTO :getInform) {
	            //System.out.println("getInform : "+informDTO.toString());  
	        //}
	         
	        //2.해당 유저 알림데이터 마지막 데이터만 가져올 경우
			 MemberVO selectLastInform= chatService.selectLastInform(memId);
			 WebSocketSession webSocketSession = users.get(memId);
			 Gson gson = new Gson();
		     //1.해당 유저 알림데이터 전체 가져오기일 경우 JSON 으로 전환 후 TextMessage 변환
		     //TextMessage textMessage = new TextMessage(gson.toJson(getInform));        
		         
		     //2.해당 유저 알림데이터 전체 가져오기일 경우  JSON 으로 전환 후 TextMessage 변환
		     TextMessage textMessage = new TextMessage(gson.toJson(selectLastInform));
		     webSocketSession.sendMessage(textMessage);			 
		}
		
		
		log.info(session.getId() + "로부터 메시지 수신: " + message.getPayload());
		// 모든 유저에게 발송
		
		for(WebSocketSession sess: sessionList) {
			sess.sendMessage(new TextMessage(message.getPayload()));
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		sessionList.remove(session);
		
		log.info(session.getId() + " 연결 종료됨");
		users.remove(session.getId());
		sessionList.remove(session);
		
	}
	
    	// 웹소켓 id 가져오기
	   private String getId(WebSocketSession session) {
	
	   /*      
	        (String)request.getSession().getAttribute("loginId");
	        또는 ,
	        session.getAttribute("loginId"); 
	        이렇게 세션값을 가져오나 여기 웹소켓에서는 세션값을 WebSocketSession session  형태로 가져옵니다.
	        따라서 , 다음과 코드 형태로 세션값을 가져옵니다. 
	*/
		     
	    Map<String, Object> httpSession = session.getAttributes();
	    String loginId = (String) httpSession.get("loginId");       
	    if (loginId == null) {
	        //System.out.println("로그인 loginID 가 널일경우  :" + session.getId());
	        //랜덤 아이디 생성, 사이트 접속한 사람 전체
	        //ex ) vawpuj5h, 5qw40sff
	        return session.getId();
	    } else {
	        //로그인한 유저 반환
	        log.info("String loginId : " + loginId);
	    	return loginId;
	    }
    }
     
    
	   // 유저 아이디 가져오기
	   public String searchUserName(WebSocketSession session)throws Exception {
		   String userName;
		   Map<String, Object> map;
		   map = session.getAttributes();
		   userName = (String)map.get("userName");
		   return userName;
	   }
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info(session.getId() + " 예외 발생: " + exception.getMessage());
	}

}