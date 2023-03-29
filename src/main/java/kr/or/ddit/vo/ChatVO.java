package kr.or.ddit.vo;

import java.util.Date;

import lombok.Data;
@Data
public class ChatVO {

	private int chatNum;
	private String memId;  
	private Date chatDate;
	private String isRead;
	private String chatCont;
	private int gubun;
	
}
