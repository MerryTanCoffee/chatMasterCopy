package kr.or.ddit.controller;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.service.ChatService;
import kr.or.ddit.utill.MediaUtils;
import kr.or.ddit.utill.UploadFileUtils;
import kr.or.ddit.vo.ChatVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ajax")
public class AjaxMemberFileController {

	@Resource(name = "uploadPath")
	private String uploadPath;

//	@RequestMapping(value = "/register", method = RequestMethod.GET)
//	public String ajaxRegisterFileForm() {
//		log.info("ajaxRegisterFileForm() : ");
//
//		return "module/chat";
//	}

	// uploadAjax 메소드는 브라우저에서 넘겨받은 파일을 업로드하고 s_가 붙은 썸네일파일을 생성하는 기능을 담당한다.
		@ResponseBody
		@RequestMapping(value="/uploadAjax",method=RequestMethod.POST, produces = "text/plain;charset=utf-8")
		public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception {

			String savedName = UploadFileUtils.uploadFile(uploadPath, file.getOriginalFilename(),file.getBytes());
			
			log.info("savedName>>>" + savedName);
			return new ResponseEntity<String>(savedName, HttpStatus.CREATED);
		}

		@RequestMapping(value="/displayFile", method = RequestMethod.GET)
		public ResponseEntity<byte[]> display(String fileName) throws Exception {
			InputStream in = null;
			ResponseEntity<byte[]> entity = null;

			log.info("fileName : " + fileName);

			try {
				String formatName = fileName.substring(fileName.lastIndexOf(".")+1); // 확장자 추출
				MediaType mType = MediaUtils.getMediaType(formatName);
				HttpHeaders headers = new HttpHeaders();
				in = new FileInputStream(uploadPath + fileName);

				if(mType != null) {
					headers.setContentType(mType);
				} else {
					fileName = fileName.substring(fileName.indexOf("_") + 1);
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					headers.add("Content-Disposition", "attachment; filename=\""
							+new String(fileName.getBytes("UTF-8"),"ISO-8859-1") + "\"");
				}

				entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in),headers,HttpStatus.CREATED);
			} catch(Exception e) {
				e.printStackTrace();
				entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
			} finally {
				in.close();
			}
			return entity;
		}
}