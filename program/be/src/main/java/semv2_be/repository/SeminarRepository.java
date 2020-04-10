package jp.co.sogeninc.semv2_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sogeninc.semv2_be.domain.Seminar;
import jp.co.sogeninc.semv2_be.domain.User;

/**
 * seminar DBアクセスインターフェイス
 *
 *
 */
@Repository
public interface SeminarRepository extends JpaRepository<Seminar, Long>{

	/**
	 * ユーザよりゼミ情報1件取得
	 * @param userId
	 * @return
	 */
	@Query("select s from Seminar s where s.owner.id = :user_id")
	Seminar findByOwner(@Param("user_id") Long userId);

	@Query("select s from Seminar s where s.owner = :owner")
	Seminar findByOwner(@Param("owner") User owner);
}
