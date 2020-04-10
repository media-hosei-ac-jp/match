package jp.co.sogeninc.semv2_be.api;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sogeninc.semv2_be.domain.Application;
import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.Setting;
import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.domain.User.Role;
import jp.co.sogeninc.semv2_be.service.ApplicationService;
import jp.co.sogeninc.semv2_be.service.MailService;
import jp.co.sogeninc.semv2_be.service.RecruitmentService;
import jp.co.sogeninc.semv2_be.service.SeminarService;
import jp.co.sogeninc.semv2_be.service.SettingService;
import jp.co.sogeninc.semv2_be.service.UserService;
import jp.co.sogeninc.semv2_be.utility.SpringBootMailSender;

/**
 * 応募RestWebコントローラー
 *
 */
@RestController
@RequestMapping("api/app")
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private UserService userService;
	@Autowired
	private RecruitmentService recruitmentService;
	@Autowired
	private SeminarService seminarService;
	@Autowired
	private SettingService settingService;
	//	@Autowired
	//	private UploadFileService uploadService;
	//	@Autowired
	//	private JavaMailSender javaMailSender;
	@Autowired
	private MailService mailService;

	@Autowired
	protected MessageSource msg;


	/**
	 * 応募情報新規作成（学生）
	 *
	 * @param applicationData
	 * @return
	 */
	@PreAuthorize("hasAuthority('STUDENT')") 
	@RequestMapping(method = RequestMethod.POST)
	public Application postApplication(@RequestBody Application app,
			@AuthenticationPrincipal User user) {
//		if(app.isDeleted()) { //削除機能の無効化
//			throw new IllegalArgumentException("不正な応募情報です。");
//		}
		
		if(app.getOwner()==null) {
			app.setOwner(user);	
		}
		if(!applicationCheck(app, user)) {
			throw new IllegalArgumentException("不正な応募情報です。");
		}
		
		Date now = new Date();
		if(app.getCreateDate()==null) {
			app.setCreateDate(now);
		}
		app.setUpdateDate(now);

		Application ret = applicationService.save(app);	
		if(ret.isDeleted()) {
			mailService.sendAppDeleteMail(ret);
		}
		else {
			mailService.sendAppCompleteMail(ret);
		}
		return ret;
	}

	@PreAuthorize("hasAuthority('STUDENT')") 
	@RequestMapping(value="confirm", method = RequestMethod.POST)
	public Application confirmApplication(@AuthenticationPrincipal User user, @RequestBody Application app) {
		Application _app = applicationService.findOne(app.getId());
		User _user = userService.findOne(user.getId());
		if(!confirmCheck(_app, _user)) {
			throw new IllegalArgumentException("不正な応募情報です。");
		}
		_app.setConfirmed(true);
		
		Application ret = applicationService.save(_app);
		_user.setConfirmed(true);
		userService.save(_user);
		mailService.sendAppConfirmMail(ret);
		
		return ret;
	}


	private boolean applicationCheck(Application app, User user)  {
		//所有者チェック
		if(!app.getOwner().equals(user)) {
			return false;
		}
		//すでに確定済み
		if(user==null || Boolean.TRUE.equals(app.getConfirmed()) ||user.isConfirmed()) {
			return false;
		}
		
		// 期間内データチェック
		Setting s = app.getRecruitment().getSetting();
		Date now = new Date();
		if(!(now.after(s.getApplicationStartDate())&&now.before(s.getApplicationEndDate()))) {
			return false;
		}

		//削除の場合
		if(app.isDeleted()) {
			return true;
		}
		
		// 併願者数チェック
		//新規応募かつ併願数がオーバー
		if (app.getId() == null && s.getMaximum() <= applicationService.countActiveApps(s, user)) {
			return false;
		}


		if(app.getId()!=null) {
			Application tApp = applicationService.findOne(app.getId());
			

			if(tApp!=null) {
				//ゼミの変更は無効とする（削除チェックのため）
				if(!app.getRecruitment().equals(tApp.getRecruitment())) {
					return false;
				}
				//選考終了後のゼミへの応募を無効化
				if(Boolean.TRUE.equals(tApp.getRecruitment().isConfirmed())) {
					return false;
				}
			}
		}

		return true;
	}
	
	private boolean confirmCheck(Application app, User user)  {
		//すでに確定済み
		User tu = userService.findOne(user.getId());
		if(tu==null || tu.isConfirmed()) {
			return false;
		}
		
		//不合格
		if(app.getPassed()==null||!app.getPassed()) {
			return false;
		}
		
		// 期間内データチェック
		Setting s = app.getRecruitment().getSetting();
		Date now = new Date();
		boolean isAdditional = app.getAddPassed() != null;
		if((!isAdditional&&!(now.after(s.getRegistrationStartDate())&&now.before(s.getRegistrationEndDate())))
			||(isAdditional&&!(now.after(s.getAddRegistrationStartDate())&&now.before(s.getAddRegistrationEndDate())))) {
			return false;
		}

		return true;
	}

	@RequestMapping(value="/getActiveApps/{settingId}", method = RequestMethod.GET)
	public List<Application> getActiveApps(@AuthenticationPrincipal User user, @PathVariable("settingId") long settingId) {
		return applicationService.findActiveApps(settingId, user);
	}
	
	@RequestMapping(value="/getAppsByRecrId/{recrId}", method = RequestMethod.GET)
	public List<Application> getAppsByRecrId(@AuthenticationPrincipal User user, @PathVariable("recrId") long recrId)  {
		Recruitment r = recruitmentService.findOne(recrId);
		if(r==null || 
			(!user.getRoles().contains(Role.ADMIN) && !r.getSeminar().getOwner().equals(user))) {
			throw new SecurityException("取得権限がありません。");
		}
		
		return applicationService.findByRecr(recrId);
//		return recruitmentService.getActiveRecrs(settingId, user);
	}
	
	
	/**
	 * 追加合格対象者を取得
	 * @param user
	 * @param recrId
	 * @return
	 */
	@RequestMapping(value="/getAddAppsByRecrId/{recrId}", method = RequestMethod.GET)
	public List<Application> getAddAppsByRecrId(@AuthenticationPrincipal User user, @PathVariable("recrId") long recrId)  {
		Recruitment r = recruitmentService.findOne(recrId);
		if(r==null || 
			(!user.getRoles().contains(Role.ADMIN) && !r.getSeminar().getOwner().equals(user))) {
			throw new SecurityException("取得権限がありません。");
		}
		
		return applicationService.findAddByRecr(recrId);
//		return recruitmentService.getActiveRecrs(settingId, user);
	}
	
	/*
	 * 管理者用API
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/get4A/{id}", method = RequestMethod.GET)
	public Application get4A(@PathVariable("id") long id) {
		return applicationService.findOne(id);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/save4A", method = RequestMethod.POST)
	public Application save4A(@RequestBody Application app) {
		return applicationService.save(app);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/getAllByRecrId4A/{id}", method = RequestMethod.GET)
	public List<Application> getAllByRecruitmentId4A(@PathVariable("id") long id) {
		return applicationService.findByRecr(id);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/getAllByOwnerId4A/{id}", method = RequestMethod.GET)
	public List<Application> getAllByOwnerId4A(@PathVariable("id") long id) {
		return applicationService.findByOwner(id);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/sendResultMail/{id}", method = RequestMethod.GET)
	public void sendResultMail(@PathVariable("id") long id) {
		Application app = applicationService.findOne(id);
		if(!app.isDeleted()) {
			mailService.sendResultMail(app, app.getAddPassed()!=null);
		}
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/getAllConfirmed4A", method = RequestMethod.GET)
	public List<Application> getAllConfirmed4A() {
		return applicationService.findByConfirmed() ;
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/addNewAppByUid4A/{rid}/{uid}", method = RequestMethod.GET)
	public Application addNewAppByUid4A(@PathVariable("uid") String uid, @PathVariable("rid") long rid ) {
		Application app = new Application();
		User owner = userService.findByUid(uid);
		Recruitment recr = recruitmentService.findOne(rid);
		if(owner==null || recr==null) {
			throw new IllegalArgumentException();
		}
		app.setOwner(owner);
		app.setRecruitment(recr);
		app.setPr("");
		Date now = new Date();
		app.setCreateDate(now);
		app.setUpdateDate(now);
		
		return applicationService.save(app);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/setConfirmedAllApps/{settingId}", method = RequestMethod.GET)
	public void setConfirmedAllApps(@AuthenticationPrincipal User user, @PathVariable("settingId") long settingId) {
		Setting s = settingService.findOne(settingId);
		if(s.getMaximum()!=1) {
			throw new IllegalStateException();
		}
		List<Application> apps = applicationService.findBySetting(settingId);
		apps.stream().filter(a->
		        (a.getPassed()!=null&&a.getPassed())
				&& !a.isDeleted()
				&& (a.getConfirmed()==null||!a.getConfirmed()))
		          .collect(Collectors.toList()).forEach(_app->{
			_app.setConfirmed(true);
			
			Application ret = applicationService.save(_app);
			User _user = _app.getOwner();
			_user.setConfirmed(true);
			userService.save(_user);
			mailService.sendAppConfirmMail(ret);			
		});
	}
}
