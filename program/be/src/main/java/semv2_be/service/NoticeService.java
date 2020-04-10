package jp.co.sogeninc.semv2_be.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sogeninc.semv2_be.domain.Notice;
import jp.co.sogeninc.semv2_be.repository.NoticeRepository;

@Service
@Transactional
public class NoticeService {
	@Autowired
	private NoticeRepository noticeRepository;
	
	public String getContent() {
		List<Notice> l = noticeRepository.findAll();
		if(l.isEmpty()) {
			return "";
		}
		else {
			return l.get(0).getContent();
		}
	}
	
	public void saveContent(String content) {
		List<Notice> l = noticeRepository.findAll();
		Notice n;
		if(l.isEmpty()) {
			 n = new Notice();
		}
		else {
			n = l.get(0);
		}
		n.setContent(content);
		noticeRepository.save(n);	
	}

}
