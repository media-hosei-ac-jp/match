package jp.co.sogeninc.semv2_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.domain.User.Role;

/**
 * user DBアクセスインターフェイス
 *
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{


//	/**
//	 * 応募idより全てのユーザ取得
//	 * @param application
//	 * @return
//	 */
//	@Query("select u from User u where u.application = :application")
//	List<User> findAllApplicationUser(@Param("application") Application application);


	/**
	 * ユーザ画面用
	 * 自ユーザ取得
	 * @param faculty
	 * @return
	 */
	@Query("select u from User u where u.uid = :uid")
	User findByUid(@Param("uid") String uid);
	
	@Query("select u from User u where u.email = :email")
	User findByEmail(@Param("email") String email);
	
	@Query("select u from User u where :role member of u.roles")
	List<User> findAllByRole(@Param("role") Role role);

//	/**
//	 * 学部ユーザ画面用
//	 * 学科idより全てのユーザ取得(ユーザ情報リスト)
//	 * @param faculty
//	 * @return
//	 */
//	@Query("select u from User u, Login l where l.userId = u.id and u.facultyId = :facultyId order by u.kanaLastName, u.kanaFirstName")
//	List<User> findUserByFacultyId(@Param("facultyId") Long facultyId);
//
//	/**
//	 * 学部ユーザ画面用（学科200越えは経済学部こみ用）
//	 * 学科idより全てのユーザ取得(ユーザ情報リスト)
//	 * @param faculty
//	 * @return
//	 */
//	@Query("select u from User u, Login l where l.userId = u.id and u.facultyId = :facultyId or u.facultyId = :adminFid order by u.kanaLastName, u.kanaFirstName")
//	List<User> findUserByFacultyIdOrAdmin(@Param("facultyId") Long facultyId, @Param("adminFid") Long adminFid);

//	/**
//	 * 応募idより全てのユーザ取得
//	 * @param id
//	 * @return
//	 */
//	@Query("select u from User u, Application a where u.id = a.userId and a.id = :applicationId")
//	List<User> findAllApplicationUser(@Param("applicationId") Long id);


}
