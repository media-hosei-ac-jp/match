package jp.co.sogeninc.semv2_be.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.service.NoticeService;

/**
 * お知らせ Restコントローラー
 *
 *
 */
@RestController
@RequestMapping("api/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
    @RequestMapping(value="/getContent", method=RequestMethod.GET)
    public String getContent() {
        return noticeService.getContent();
    }
    
	@PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/putContent", method=RequestMethod.POST)
    public String putContent(@RequestBody String content) {
		noticeService.saveContent(content);
        return content;
    }

}
