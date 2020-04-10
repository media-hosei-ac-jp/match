package jp.co.sogeninc.semv2_be.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sogeninc.semv2_be.domain.Application;
import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.Seminar;
import jp.co.sogeninc.semv2_be.domain.Setting;
import jp.co.sogeninc.semv2_be.domain.Setting.DisplayType;
import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.domain.User.Role;
import jp.co.sogeninc.semv2_be.model.RecrLimitData;
import jp.co.sogeninc.semv2_be.model.SelectionData;
import jp.co.sogeninc.semv2_be.model.SettingStatsData;
import jp.co.sogeninc.semv2_be.model.RecrStatsData;
import jp.co.sogeninc.semv2_be.service.ApplicationService;
import jp.co.sogeninc.semv2_be.service.MailService;
import jp.co.sogeninc.semv2_be.service.RecruitmentService;
import jp.co.sogeninc.semv2_be.service.SeminarService;
import jp.co.sogeninc.semv2_be.service.SettingService;
import jp.co.sogeninc.semv2_be.service.UserService;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 募集情報関連コントローラー
 *
 *
 */
@RestController
@RequestMapping("api/recruit")
public class RecruitmentController {

	private static final Logger logger = LoggerFactory.getLogger(RecruitmentController.class);
	@Autowired
	private RecruitmentService recruitmentService;

	@Autowired
	private UserService userService;

	@Autowired
	private SeminarService semService;

	@Autowired
	private SettingService settingService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private MailService mailService;
	//
	//	@Autowired
	//	private MessageSource msg;

	/**
	 * ゼミ募集情報取得
	 *
	 * @param userDetails
	 * @return
	 */
	@RequestMapping(value="/getMyRecrs", method = RequestMethod.GET)
	public List<Recruitment> getRecruitments(@AuthenticationPrincipal User user)  {
		return recruitmentService.getAllRecrs(user);
	}

	@RequestMapping(value="/getActiveRecrs/{settingId}", method = RequestMethod.GET)
	public List<Recruitment> getActiveRecruitments(@AuthenticationPrincipal User user, @PathVariable("settingId") long settingId)  {
		return recruitmentService.getActiveRecrs(settingId, user);
	}

	/**
	 * 募集情報更新
	 *
	 * @param recruitmentData
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@RequestMapping(method = RequestMethod.PUT)
	public Recruitment putRecruitment(@AuthenticationPrincipal User user, @RequestBody Recruitment recr) {
		Date now = new Date();
		if(!recr.registerationDateValid(now)) {
			throw new IllegalArgumentException("登録期間外です。");
		}
		if(!recr.countValid()) {
			throw new IllegalArgumentException("定員数が条件を満たしていません。");
		}

		if(!recr.getSeminar().getOwner().equals(user)) {
			throw new SecurityException();
		}
		//		recr.setOwner(user);
		if(recr.getCreateDate()==null) {
			recr.setCreateDate(now);
		}
		else {
			recr.setUpdateDate(now);
		}

		Recruitment ret = recruitmentService.save(recr);
		mailService.sendRecrCompleteMail(ret);
		return ret;
	}

	@RequestMapping(value="/getAllStats", method=RequestMethod.GET)
	public List<RecrStatsData> getAllStatus(@AuthenticationPrincipal User user) {
		//DateFormat format = new SimpleDateFormat("[yyyy/MM//dd hh:mm:ss.SSS]");
		//logger.info("start" + format.format(new Date()));//time
		Setting currentSetting = settingService.getCurrentSetting();
		//logger.info("settingService.getCurrentSetting()" + format.format(new Date()));//time
		List<Recruitment> recrs = recruitmentService.getAllRecrs(currentSetting.getId());

		//logger.info("recruitmentService.getAllRecrs(currentSetting)" + format.format(new Date()));//time
		Date now = new Date();
		boolean limited = !(user.getRoles().contains(Role.ADMIN)|| user.getRoles().contains(Role.TEACHER))
				&& currentSetting.getDisplayType().equals(DisplayType.STEP)
				&& now.before(currentSetting.getApplicationEndDate()); 
		Date limitDate = currentSetting.getDataRefreshDate();

		//logger.info("判定用条件式" + format.format(new Date()));//time
		//mailService.send("sg-zemi-support@sogeninc.co.jp", "ゼミ応募システム メールテスト", "送信テスト/nシステムからの送信です。");
		return recrs.stream().map(r->{
			Recruitment tr = r;
			int[] appCounts = new int[5];
			/* 20181030 パフォーマンス低下のため一時無効化
			 * 20181119 再度有効化 */
			for(int i=1; i<=4; i++) {
				if(!limited) {
					appCounts[i] = applicationService.countAppByGrade(r, i);
				}
				else {
					if(now.before(limitDate)) {
						appCounts[i] = 0;
					}
					else {
						appCounts[i] = applicationService.countAppByGradeBefore(r, i, limitDate);
					}
				}
			}
			/* */
			return new RecrStatsData(tr, appCounts);
		}).collect(Collectors.toList());
	}

	@PreAuthorize("hasAuthority('TEACHER')")
	@RequestMapping(value="/confirmSelection", method = RequestMethod.POST)
	public Recruitment confirmSelection(@AuthenticationPrincipal User user, @RequestBody SelectionData sel) {
		Recruitment recr = sel.getRecr();
		List<Application> apps = sel.getApps();
		//追加合格の場合Trueとなる
		boolean isAdditional = sel.isAdditional();

		Date now = new Date();
		if((!isAdditional && !recr.selectionDateValid(now))
				|| (isAdditional && !recr.addSelectionDateValid(now))) {
			throw new IllegalArgumentException("登録期間外です。");
		}
		
		User owner = recruitmentService.findOne(recr.getId()).getSeminar().getOwner();
		if(!recr.getSeminar().getOwner().equals(owner)) {
			throw new SecurityException();
		}
		
		Recruitment trecr = recruitmentService.findOne(recr.getId());
		if((!isAdditional && trecr.isConfirmed())
				|| (isAdditional && trecr.isAddConfirmed())) {
			throw new IllegalArgumentException("すでに確定済みです。");
		}
		
		if(!apps.stream().allMatch(app->app.getPassed()!=null)) {
			throw new IllegalArgumentException("合否が未選択のものがあります。");
		}
		

		if(isAdditional) {
			recr.setAddConfirmed(true);
		}
		else {
			recr.setConfirmed(true);
		}

		//save apps
		for(Application app : apps) {
			Application _tapp = applicationService.findOne(app.getId());
			if(!_tapp.getRecruitment().equals(recr)) {
				throw new SecurityException();
			}
			_tapp.setPassed(app.getPassed());
			if(isAdditional) {
				_tapp.setAddPassed(app.getPassed());
			}
			applicationService.save(_tapp);
		}

		Recruitment ret = recruitmentService.save(recr);
		//send notification mail
		mailService.sendSelectCompleteMail(ret, apps, isAdditional);

		if(!isAdditional) {
			List<Application> rapps = applicationService.findByRecr(ret.getId());
			for(Application app : rapps) {
				if(app.isDeleted()) {
					continue;
				}

				if(recr.getSetting().getMaximum() == 1 
					&& (app.getPassed()!=null && app.getPassed())){
					app.setConfirmed(true);
				
					Application retapp = applicationService.save(app);
					User _user = app.getOwner();
					_user.setConfirmed(true);
					userService.save(_user);
					
					mailService.sendAppConfirmMail(retapp);	
				}
				else{
					mailService.sendResultMail(app, isAdditional);
				}
			}
		}
		else { //追加合格
			List<Application> rapps = applicationService.findAddByRecr(ret.getId());
			for(Application app : rapps) {
				if(!app.isDeleted()) {
					mailService.sendResultMail(app, isAdditional);
				}
			}
		}

		return ret;
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/updateLimits", method = RequestMethod.POST)
	public boolean updateLimits(@RequestBody List<RecrLimitData> rds) {

		for(RecrLimitData rd : rds) {
			Setting setting = settingService.findOne(rd.settingId);
			User owner = userService.findByUid(rd.uid);
			if(owner==null) {
				System.err.println(rd.uid);
				throw new IllegalStateException("userが存在しません。");
			}

			Recruitment r = recruitmentService.findByOwner(setting, owner);
			if(r==null) {
				//System.err.println("uid: "+rd.uid);
				//throw new IllegalComponentStateException("recruitmentが存在しません。");
				r = new Recruitment();
				r.setSetting(setting);
				Seminar s = semService.findByOwner(owner);
				if(s==null) {
					System.err.println(owner.getUid());
					throw new IllegalStateException("seminarが存在しません。");
				}
				r.setSeminar(s);
				r.setCreateDate(new Date());
			}
			r.updateLimits(rd);
			recruitmentService.save(r);
		}
		return true;
	}
	
	/*
	 * 管理者用API
	 */

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/get4A/{id}", method = RequestMethod.GET)
	public Recruitment get4A(@PathVariable("id") long id) {
		return recruitmentService.findOne(id);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/save4A", method = RequestMethod.POST)
	public Recruitment save4A(@RequestBody Recruitment recr) {
		return recruitmentService.save(recr);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/getAllBySettingId4A/{sId}", method = RequestMethod.GET)
	public List<Recruitment> getAllBySettingId4A(@PathVariable("sId") long sId) {
		return recruitmentService.getAllRecrs(sId);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/getAllStats4A/{sId}", method = RequestMethod.GET)
	public SettingStatsData getAllStats4A(@PathVariable("sId") long sid) {
		Setting setting = settingService.findOne(sid);
		List<Recruitment> recrs = recruitmentService.getAllRecrs(sid);
		
		int[] applicantCount = new int[5];
		int[] passCount = new int[5];
		int[] confirmCount = new int[5];
		int[] addPassCount = new int[5];
		int[] addConfirmCount = new int[5];
		//実人数
		for(int i=1; i<=4; i++) {
			applicantCount[i] = applicationService.countApplicantByGrade(sid, i);
			passCount[i] = applicationService.countApplicantPassed1stByGrade(sid, i);
			confirmCount[i] = applicationService.countApplicantConfirmed1stByGrade(sid, i);
			addPassCount[i] = applicationService.countApplicantAddPassedByGrade(sid, i);
			addConfirmCount[i] = applicationService.countApplicantAddConfirmedByGrade(sid, i);
		}

		List<RecrStatsData> rsdl = recrs.stream().map(r->{
			Recruitment tr = r;
			int[] appCounts = new int[5];
			int[] passCounts = new int[5];
			int[] confirmCounts = new int[5];
			int[] addPassCounts = new int[5];
			int[] addConfirmCounts = new int[5];
			for(int i=1; i<=4; i++) {
				appCounts[i] = applicationService.countAppByGrade(r, i);
				passCounts[i] = applicationService.countPassed1stByGrade(r, i);
				confirmCounts[i] = applicationService.countConfirmed1stByGrade(r, i);
				addPassCounts[i] = applicationService.countAddPassedByGrade(r, i);
				addConfirmCounts[i] = applicationService.countAddConfirmedByGrade(r, i);
			}
								
			return new RecrStatsData(tr, appCounts, passCounts, confirmCounts, addPassCounts, addConfirmCounts);

		}).collect(Collectors.toList());
		
		return new SettingStatsData(setting, rsdl, applicantCount, passCount, confirmCount, addPassCount, addConfirmCount);
	}

}
