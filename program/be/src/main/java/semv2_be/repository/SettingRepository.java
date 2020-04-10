package jp.co.sogeninc.semv2_be.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sogeninc.semv2_be.domain.Setting;

/**
 * setting DBアクセスインターフェイス
 *
 *
 */
@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
	//学生向けに現在の選考を返す。(info表示用）
	//教員の応募情報入力期間が終わった後のものを返す
/*  修正前　学生の応募が開始しら返す
	@Query("select s2 from Setting s2 where s2.applicationStartDate = "
			+ "(select max(s.applicationStartDate) from Setting s where s.applicationStartDate<:now and "
			+ "((s.addRegistrationEndDate is not null and :now<s.addRegistrationEndDate) or "
			+ ":now<s.registrationEndDate)))")
			*/
	//修正後　教員の募集情報入力期間が終わったら返す
	@Query("select s2 from Setting s2 where s2.recruitmentEndDate = "
			+ "(select max(s.recruitmentEndDate) from Setting s where s.recruitmentEndDate<:now and "
			+ "((s.addRegistrationEndDate is not null and :now<s.addRegistrationEndDate) or "
			+ ":now<s.registrationEndDate)))")
	Setting getCurrentSetting(@Param("now") Date now);

//	/**
//	 * 募集期間情報取得（募集情報表示用）
//	 *
//	 * @param userId
//	 * @return
//	 */
//	@Query(value = "select s.* from setting s where s.recruitment_end_date > cast(concat(cast(DATE_FORMAT(now() - INTERVAL 3 MONTH,'%Y/%m/%d') as char), ' 23:59:00') as datetime) ORDER BY recruitment_end_date DESC", nativeQuery = true)
//	List<Setting> findForRecDisp();
//
//	/**
//	 * 募集期間情報取得（応募情報表示用）
//	 *
//	 * @param userId
//	 * @return
//	 */
//	@Query(value = "select s.* from setting s where s.recruitment_end_date > cast(concat(cast(DATE_FORMAT(now() - INTERVAL 4 MONTH,'%Y/%m/%d') as char), ' 23:59:00') as datetime) ORDER BY recruitment_start_date DESC", nativeQuery = true)
//	List<Setting> findForAppDisp();

}
