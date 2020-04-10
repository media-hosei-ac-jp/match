package jp.co.sogeninc.semv2_be.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sogeninc.semv2_be.domain.Application;
import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.Setting;
import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.repository.ApplicationRepository;

@Service
@Transactional
public class ApplicationService {

	@Autowired
	private ApplicationRepository applicationRepository;
	
	public Application findOne(long id) {
		return applicationRepository.findOne(id);
	}

	/**
	 * 応募情報更新（新規作成含む）
	 */
	public Application save(Application application) {
		return applicationRepository.save(application);
	}

	/**
	 * 応募情報取得（ユーザより１件）
	 *
	 * @param userId
	 * @return
	 */
	public Application findByUser(User user) {

		List<Application> applicationList = applicationRepository.findByOwnerId(user.getId());
		Application app = new Application();
		if (applicationList.size() > 0) {
			app = applicationList.get(0);
		}
		return app;
	}

	/**
	 * 応募情報取得（ユーザより１件）
	 *
	 * @param userId
	 * @return
	 */
	public List<Application> findByUserList(Long id) {
		return applicationRepository.findByOwnerId(id);
	}

	/**
	 * 応募情報取得（募集より学生リスト）
	 *
	 * @param rec
	 * @return
	 */
	public List<Application> findByRecr(Long id) {
		return applicationRepository.findByRec(id);
	}

	/**
	 * 期間より応募情報取得
	 *
	 * @param rec
	 * @return
	 */
	public List<Application> findBySetting(Long settingId) {
		return applicationRepository.findBySetting(settingId);
	}

//	/**
//	 * 募集より合否毎の応募情報取得
//	 *
//	 * @param settingId
//	 * @param result
//	 * @return
//	 */
//	public List<Application> findBySetResult(Long settingId, Integer result) {
//		return applicationRepository.findBySetResult(settingId, result);
//	}
//
//	/**
//	 * 募集より合否毎の応募情報取得
//	 *
//	 * @param settingId
//	 * @param result
//	 * @param notificationResult
//	 * @return
//	 */
//	public List<Application> findBySetResult(Long settingId, Integer result, Integer notificationResult) {
//		return applicationRepository.findBySetResult(settingId, result, notificationResult);
//	}
//	/**
//	 * 募集より合否毎の応募情報取得
//	 *
//	 * @param settingId
//	 * @param pass
//	 * @return
//	 */
//	public List<Application> findBySetResult(Long settingId, Short pass) {
//		return applicationRepository.findBySetResult(settingId, pass);
//	}

	/**
	 * 応募取得
	 *
	 * @param userId
	 * @param recruitmentId
	 * @return
	 */
	public List<Application> findByUserAndRec(Long userId, Long recruitmentId) {
		return applicationRepository.findByOwnerAndRec(userId, recruitmentId);
	}


	/**
	 * 応募者数取得（学年）
	 *
	 * @param recruitmentId
	 * @param grade
	 * @param result
	 * @return
	 */
	public List<Application> findByRecGrade(Long recruitmentId, Integer grade) {
		return applicationRepository.findByRecGrade(recruitmentId, grade);
	}

//	/**
//	 * 応募者一覧取得（学年と合否より）
//	 *
//	 * @param recruitmentId
//	 * @param grade
//	 * @param result
//	 * @return
//	 */
//	public List<Application> findByRecGradeResult(Long recruitmentId, Integer grade, List<Integer> result) {
//		return applicationRepository.findByRecGradeResult(recruitmentId, grade, result);
//	}

	/**
	 * 募集期間よりユーザ毎の応募情報取得
	 *
	 * @param settingId
	 * @return
	 */
	public List<Application> findBySetttingOrderUser(Long settingId) {
		return applicationRepository.findBySetttingOrderUser(settingId);
	}

	/**
	 * 募集期間よりゼミ毎の応募情報取得
	 *
	 * @param settingId
	 * @return
	 */
	public List<Application> findBySetttingOrderSeminar(Long settingId) {
		return applicationRepository.findBySetting(settingId);
	}

	public int countAppByGrade(Recruitment recr, int grade) {
		return applicationRepository.countAppByGrade(recr, grade);
	}
	
	public int countAppByGradeBefore(Recruitment recr, int grade, Date date) {
		return applicationRepository.countAppByGradeBefore(recr, grade, date);
	}

	public int countActiveApps(Setting setting, User owner) {
		return applicationRepository.countActiveApps(setting, owner);
	}

	public List<Application> findActiveApps(long settingId, User owner) {
		return applicationRepository.findActiveApps(settingId, owner);
	}

	/**
	 * 追加合格対象のAppを取得（一つもゼミに合格していないユーザのApp）
	 * @param recrId
	 * @return
	 */
	public List<Application> findAddByRecr(long recrId) {
		return applicationRepository.findAddByRec(recrId);
	}

	public List<Application> findByOwner(long id) {
		return applicationRepository.findByOwnerId(id);
	}
	
	/*
	 * 結果集計用（パフォーマンスに難ありか）
	 */
	
	public int countPassed1stByGrade(Recruitment recr, int grade) {
		return applicationRepository.countPassed1stByGrade(recr, grade);
	}

	public int countAddPassedByGrade(Recruitment recr, int grade) {
		return applicationRepository.countAddPassedByGrade(recr, grade);
	}

	public int countConfirmed1stByGrade(Recruitment recr, int grade) {
		return applicationRepository.countConfirmed1stByGrade(recr, grade);
	}

	public int countAddConfirmedByGrade(Recruitment recr, int grade) {
		return applicationRepository.countAddConfirmedByGrade(recr, grade);
	}

	
	public int countApplicantByGrade(long sid, int grade) {
		return applicationRepository.countApplicantByGrade(sid, grade);
	}

	public int countApplicantPassed1stByGrade(long sid, int grade) {
		return applicationRepository.countApplicantPassed1stByGrade(sid, grade);
	}

	public int countApplicantAddPassedByGrade(long sid, int grade) {
		return applicationRepository.countApplicantAddPassedByGrade(sid, grade);
	}

	public int countApplicantConfirmed1stByGrade(long sid, int grade) {
		return applicationRepository.countApplicantConfirmed1stByGrade(sid, grade);
	}

	public int countApplicantAddConfirmedByGrade(long sid, int grade) {
		return applicationRepository.countApplicantAddConfirmedByGrade(sid, grade);
	}

	public List<Application> findByConfirmed() {
		return applicationRepository.findByConfirmed(true);
	}
	
}
