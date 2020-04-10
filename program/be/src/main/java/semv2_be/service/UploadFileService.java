package jp.co.sogeninc.semv2_be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sogeninc.semv2_be.domain.UploadFile;
import jp.co.sogeninc.semv2_be.repository.UploadFileRepository;


@Service
@Transactional
public class UploadFileService {
	
	@Autowired
	private UploadFileRepository fileRepository;
	
	public UploadFile findOne(long id) {
		return fileRepository.findOne(id);
	}

	public UploadFile save(UploadFile file) {
		return fileRepository.save(file);
	}

}
