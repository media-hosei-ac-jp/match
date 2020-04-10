package jp.co.sogeninc.semv2_be.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sogeninc.semv2_be.domain.Seminar;
import jp.co.sogeninc.semv2_be.domain.UploadFile;
import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.service.SeminarService;
import jp.co.sogeninc.semv2_be.service.UploadFileService;


/**
 * Seminar Restコントローラー
 *
 *
 */
@RestController
@RequestMapping("api/seminar")
public class SeminarController {

	@Autowired
	private SeminarService seminarService;

//	@Autowired
//	private UserService userService;

	@Autowired
	private UploadFileService uploadFileService;

//	@Autowired
//	protected MessageSource msg;

	/**
	 * 担当ゼミ情報取得
	 *
	 * @param userDetails
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Seminar getSeminar(@AuthenticationPrincipal User user) throws Exception {
		// ログインユーザのゼミ情報取得
		return seminarService.findByOwner(user.getId());
	}

	/**
	 * 担当ゼミ情報更新
	 *
	 * @param seminarData
	 *            画面上のデータ
	 * @param userDetails
	 *            ログイン情報
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@RequestMapping(method = RequestMethod.PUT)
	@Transactional
	public Seminar putSeminar(@RequestBody Seminar seminar,
			@AuthenticationPrincipal User user) {
		Seminar _sem = seminarService.findByOwner(user);
		seminar.setId(_sem.getId());
		seminar.setOwner(_sem.getOwner());
		
		if(seminar.getFile()!=null&&seminar.getFile().getContent()!=null) {
			UploadFile file = uploadFileService.save(seminar.getFile());
			seminar.setFile(file);
		}
		
		seminar.setUpdateDate(new Date());
		seminar.setOwner(user);
		
		Seminar updateSeminar = seminarService.save(seminar);

		return updateSeminar;
	}


}
