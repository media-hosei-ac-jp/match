package jp.co.sogeninc.semv2_be.api;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.sogeninc.semv2_be.domain.UploadFile;
import jp.co.sogeninc.semv2_be.service.UploadFileService;

@RestController
@RequestMapping("api/file")
public class FileController {
	@Autowired
	private UploadFileService fileService;

	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable long id) throws IOException {
		UploadFile file = fileService.findOne(id);
		if(file==null) {
			throw new IllegalArgumentException("fileが見つかりません。");
		}
		HttpHeaders h = new HttpHeaders();
		h.add("Content-Type", "applicaiton/pdf; charset=UTF-8");
		h.setContentDispositionFormData("filename", file.getFileName());
		h.setContentDispositionFormData("filename*", URLEncoder.encode(file.getFileName(), "UTF-8"));
		byte[] content = new ObjectMapper().convertValue(file.getContent(), byte[].class);
		return new ResponseEntity<>(content, h, HttpStatus.OK);
	}
}
