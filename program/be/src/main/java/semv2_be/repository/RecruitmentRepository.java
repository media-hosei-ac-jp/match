package jp.co.sogeninc.semv2_be.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.Setting;
import jp.co.sogeninc.semv2_be.domain.User;

/**
 * recruitment DBアクセスインターフェイス
 *
 *
 */
@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

	@Query("select r from Recruitment r where r.seminar.owner = :owner order by r.setting.id desc")
	List<Recruitment> findAllRecrsByOwner(@Param("owner") User owner);
//	/**
//	 * ゼミより募集情報取得
//	 *
//	 * @param seminarId
//	 * @return
//	 */
//	@Query("select r from Recruitment r, Seminar s where s.id = :seminarId and r.owner = s.owner order by r.updateDate")
//	List<Recruitment> findBySeminar(@Param("seminarId") Long seminarId);
//
//	/**
//	 * ゼミと期間より募集情報取得
//	 *
//	 * @param seminarId
//	 * @return
//	 */
//	@Query("select r from Recruitment r, User u, Seminar s where s.id = r.seminarId and u.id = s.userId and r.seminarId = :seminarId and r.settingId = :settingId order by u.kanaLastName, u.kanaFirstName")
//	List<Recruitment> findBySeminarAndSetting(@Param("seminarId") Long seminarId, @Param("settingId") Long settingId);
//
//
//	/**
//	 * 期間より募集情報取得
//	 *
//	 * @param seminarId
//	 * @return
//	 */
//	@Query("select r from Recruitment r where r.settingId = :settingId")
//	List<Recruitment> findBySetting(@Param("settingId") Long settingId);
//
//	/**
//	 * 期間と学年より募集情報取得
//	 *
//	 * @param seminarId
//	 * @return
//	 */
//	@Query("select r from Recruitment r, RecruitmentSetting rs where r.id = rs.recruitmentId and r.settingId = :settingId and rs.grade = :grade and rs.capacityLowerLimit > 0 and rs.capacityUpperLimit > 1 ")
//	List<Recruitment> findBySettingAndGrade(@Param("settingId") Long settingId, @Param("grade") Integer grade);
//
//
//
//	/**
//	 * ユーザと募集より応募情報取得
//	 *
//	 * @param userId
//	 * @return
//	 */
//	@Query("select r from Recruitment r, User u, Setting s "
//			+ "where "
//			+ "r.id = :recruitmentId and "
//			+ "u.id = :userId and "
//			+ "s.id = r.settingId and "
//			+ "("
//			+ "(s.chngeClosingDate = '0000-00-00' and now() between s.applicationStartDate and s.applicationEndDate) or "
//			+ "(s.chngeClosingDate != '0000-00-00' and now() between s.applicationStartDate and s.chngeClosingDate)"
//			+ ")")
//	List<Recruitment> findSeasonResult(@Param("userId") Long userId, @Param("recruitmentId") Long recruitmentId);

	@Query("select r from Recruitment r where r.setting = :setting order by r.seminar.owner.kanaFamilyName, r.seminar.owner.kanaGivenName")
	List<Recruitment> findAllRecrsBySetting(@Param("setting") Setting setting);
	
	@Query("select r from Recruitment r where r.setting.id = :sId order by r.seminar.owner.kanaFamilyName, r.seminar.owner.kanaGivenName")
	List<Recruitment> findAllRecrsBySettingId(@Param("sId") long sId);
	
	@Query("select r from Recruitment r "
			+ " where r.setting.id = :settingId "
			+ "and ("
			+ "(:grade = 1 and (r.grade1 is not null and r.grade1 >0)) "
			+ "or (:grade = 2 and (r.grade2 is not null and r.grade2 >0)) "
			+ "or (:grade = 3 and (r.grade3 is not null and r.grade3 >0)) "
			+ "or (:grade = 4 and (r.grade4 is not null and r.grade4 >0)) "
			+ ") "
			+ "and not exists (select 1 from Application a where a.owner = :user and a.recruitment = r and a.deleted = false) "
			+ "order by r.seminar.owner.kanaFamilyName, r.seminar.owner.kanaGivenName")	
	List<Recruitment> findActiveRecrs(@Param("settingId")long settingId, @Param("user") User user, @Param("grade") int grade);

	@Query("select r from Recruitment r where r.setting = :setting and r.seminar.owner = :owner")
	Recruitment findByOwner(@Param("setting") Setting s, @Param("owner")User owner);

	@Query("select r from Recruitment r where r.setting.id = :settingId and r.seminar.owner.uid = :uid")
	Recruitment findByOwner(@Param("settingId") long settingId, @Param("uid")String uid);



}
