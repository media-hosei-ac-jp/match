package jp.co.sogeninc.semv2_be.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.sogeninc.semv2_be.domain.Application;
import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.Setting;
import jp.co.sogeninc.semv2_be.domain.User;

/**
 * application DBアクセスインターフェイス
 *
 *
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {


	/**
	 * ユーザと募集より応募情報取得
	 *
	 * @param userId
	 * @return
	 */
	@Query("select a from Application a where a.owner.id = :userId and a.recruitment.id = :recruitmentId and a.deleted = false")
	List<Application> findByOwnerAndRec(@Param("userId") Long userId, @Param("recruitmentId") Long recruitmentId);


	/**
	 * ユーザより応募情報取得
	 *
	 * @param userId
	 * @return
	 */
	@Query("select a from Application a where a.owner.id = :id order by a.updateDate desc")
	List<Application> findByOwnerId(@Param("id") Long id);

	/**
	 * 募集より応募情報取得
	 *
	 * @param recruitmentId
	 * @return
	 */
	@Query("select a from Application a where a.recruitment.id = :recruitmentId")
	List<Application> findByRec(@Param("recruitmentId") Long recruitmentId);

//	/**
//	 * 期間より合否ごとの応募情報取得(合否)
//	 *
//	 * @param settingId
//	 * @param result
//	 * @return
//	 */
//	@Query("select a from Application a where a.recruitment.setting.id = :settingId and a.resultStatus = :resultStatus")
//	List<Application> findBySetResult(@Param("settingId") Long settingId, @Param("resultStatus") Application.ResultInfo resultStatus);
//
//	/**
//	 * 期間より合否ごとの応募情報取得(追加合否)
//	 *
//	 * @param settingId
//	 * @param result
//	 * @return
//	 */
//	@Query("select a from Application a where r.recruitment.setting.id = :settingId and a.resultStatus = :resultStatus and a.notificationStatus = :notificationStatus")
//	List<Application> findBySetResult(@Param("settingId") Long settingId, @Param("resultStatus") Application.ResultInfo resultStatus
//			,@Param("notificationStatus") Application.NotificationStatus notificationStatus);

	/**
	 * 応募者一覧取得（学年より）
	 *
	 * @param recruitmentId
	 * @param grade
	 * @param result
	 * @return
	 */
	@Query("select a from Application a where a.recruitment.id = :recruitmentId and a.owner.grade = :grade")
	List<Application> findByRecGrade(@Param("recruitmentId") Long recruitmentId, @Param("grade") Integer grade);

//	/**
//	 * 応募者一覧取得（合否と学年より）
//	 *
//	 * @param recruitmentId
//	 * @param grade
//	 * @param result
//	 * @return
//	 */
//	@Query("select a from Application a where a.recruitment.id = :recruitmentId and a.owner.grade = :grade and a.resultStatus IN :resultStatus")
//	List<Application> findByRecGradeResult(@Param("recruitmentId") Long recruitmentId, @Param("grade") Integer grade,
//			@Param("resultStatus") List<Application.ResultInfo> resultStatus);

	/**
	 * 募集期間よりユーザ毎の応募情報取得
	 *
	 * @param settingId
	 * @return
	 */
	@Query("select a from Application a where a.recruitment.setting.id = :settingId order by a.owner.uid")
	List<Application> findBySetttingOrderUser(@Param("settingId") Long settingId);

	/**
	 * 募集期間よりゼミ毎の応募情報取得
	 *
	 * @param settingId
	 * @return
	 */
	@Query("select a from Application a where a.recruitment.setting.id = :settingId order by a.owner.kanaFamilyName")
	List<Application> findBySetting(@Param("settingId") Long settingId);

	@Query("select a from Application a where a.recruitment.setting.id = :settingId and a.owner = :owner and "
//			+ "(a.deleted = false or (a.passed is not null and a.passed = true))")
			+ "(a.deleted = false)")
	List<Application> findActiveApps(@Param("settingId") long settingId, @Param("owner")User owner);

	@Query("select count(a) from Application a where a.recruitment = :recr and a.owner.grade = :grade and a.deleted = false and a.createDate < :date ")
	int countAppByGradeBefore(@Param("recr") Recruitment recr, @Param("grade") int grade, @Param("date") Date date);

	@Query("select a from Application a where a.recruitment.id = :recrId and "
			+ "not exists (select 1 from Application a2 "
			+ "where a2.owner = a.owner and a2.recruitment.setting = a.recruitment.setting and a2.passed = true and a2.addPassed is null)")
	List<Application> findAddByRec(@Param("recrId") long recrId);
	
	/*
	 * 結果集計用（パフォーマンスに難ありか）
	 */
	
	@Query("select count(a) from Application a where a.recruitment.setting = :setting and a.owner = :owner and a.deleted = false")
	int countActiveApps(@Param("setting")Setting setting, @Param("owner")User owner);
	
	
	@Query("select count(a) from Application a where a.recruitment = :recr and a.owner.grade = :grade and a.deleted = false")
	int countAppByGrade(@Param("recr") Recruitment recr, @Param("grade") int grade);

	@Query("select count(a) from Application a where a.recruitment = :recr and a.owner.grade = :grade and a.deleted = false and a.passed = true and a.addPassed is null")
	int countPassed1stByGrade(@Param("recr") Recruitment recr, @Param("grade") int grade);

	@Query("select count(a) from Application a where a.recruitment = :recr and a.owner.grade = :grade and a.deleted = false and a.addPassed = true")
	int countAddPassedByGrade(@Param("recr") Recruitment recr, @Param("grade") int grade);

	@Query("select count(a) from Application a where a.recruitment = :recr and a.owner.grade = :grade and a.deleted = false and a.addPassed is null and a.confirmed = true")
	int countConfirmed1stByGrade(@Param("recr") Recruitment recr, @Param("grade") int grade);

	@Query("select count(a) from Application a where a.recruitment = :recr and a.owner.grade = :grade and a.deleted = false and a.addPassed = true and a.confirmed = true")
	int countAddConfirmedByGrade(@Param("recr") Recruitment recr, @Param("grade") int grade);

	@Query("select count(u) from User u where u.grade = :grade and exists (select 1 from Application a where a.recruitment.setting.id = :sid and u = a.owner and a.deleted = false)")
	int countApplicantByGrade(@Param("sid") long sid, @Param("grade") int grade);

	@Query("select count(u) from User u where u.grade = :grade and exists (select 1 from Application a where a.recruitment.setting.id = :sid and u = a.owner and a.deleted = false and a.passed = true and a.addPassed is null)")
	int countApplicantPassed1stByGrade(@Param("sid") long sid, @Param("grade") int grade);

	@Query("select count(u) from User u where u.grade = :grade and exists (select 1 from Application a where a.recruitment.setting.id = :sid and u = a.owner and a.deleted = false and a.addPassed = true)")
	int countApplicantAddPassedByGrade(@Param("sid") long sid, @Param("grade") int grade);

	@Query("select count(u) from User u where u.grade = :grade and exists (select 1 from Application a where a.recruitment.setting.id = :sid and u = a.owner and a.deleted = false and a.confirmed = true and a.addPassed is null)")
	int countApplicantConfirmed1stByGrade(@Param("sid") long sid, @Param("grade") int grade);

	@Query("select count(u) from User u where u.grade = :grade and exists (select 1 from Application a where a.recruitment.setting.id = :sid and u = a.owner and a.deleted = false and a.confirmed = true and a.addPassed = true)")
	int countApplicantAddConfirmedByGrade(@Param("sid") long sid, @Param("grade") int grade);

	@Query("select a from Application a where a.confirmed = :confirmed")
	List<Application> findByConfirmed(@Param("confirmed") boolean confirmed);

}
