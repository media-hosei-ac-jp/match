package jp.co.sogeninc.semv2_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sogeninc.semv2_be.domain.UploadFile;

/**
 * upload_file DBアクセスインターフェイス
 *
 *
 */
//TODO fix
@Repository
@Transactional
public interface UploadFileRepository extends JpaRepository<UploadFile, Long>{

//	/**
//	 * 機能ID毎のアップロード情報取得
//	 * @param functionId
//	 * @return
//	 */
//	@Query("select u from UploadFile u where u.functionId = :functionId order by u.updateDate desc")
//	List<UploadFile> findByFunctionId(@Param("functionId") Long functionId);
//
//
//	/**
//	 * アップロード情報取得（1件）
//	 * @param functionClass
//	 * @param functionId
//	 * @return
//	 */
//	@Query("select u from UploadFile u where u.functionClass = :functionClass and u.functionId = :functionId")
//	UploadFile findOne(@Param("functionClass") String functionClass, @Param("functionId") Long functionId);
//
//	/**
//	 * アップロードファイル情報新規作成
//	 * @param function_class 機能区分
//	 * @param function_id 機能ID
//	 * @param file_name ファイル名
//	 * @param file ファイル（バイナリ文字列）
//	 * @param user_id 更新ユーザ
//	 * @return
//	 */
//	@Modifying
//	@Query(value = "insert into upload_file "
//			+ "(function_class, function_id, file_name, file, update_user_id, create_user_id, create_date) "
//			+ "values "
//			+ "(:function_class, :function_id, :file_name, :file, :user_id, :user_id, now())", nativeQuery = true)
//	int postUploadFile(@Param("function_class") String function_class
//						, @Param("function_id") Long function_id
//						, @Param("file_name") String file_name
//						, @Param("file") String file
//						, @Param("user_id") Long user_id);
//
//	/**
//	 * アップロードファイル情報更新
//	 * @param function_class 機能区分
//	 * @param function_id 機能ID
//	 * @param file_name ファイル名
//	 * @param file ファイル（バイナリ文字列）
//	 * @param user_id 更新ユーザ
//	 * @return
//	 */
//	@Modifying
//	@Query(value = "update upload_file set "
//			+ "file_name = :file_name, file = :file, update_user_id = :user_id "
//			+ "where "
//			+ "function_class = :function_class and  function_id = :function_id", nativeQuery = true)
//	int putUploadFile(@Param("function_class") String function_class
//						, @Param("function_id") Long function_id
//						, @Param("file_name") String file_name
//						, @Param("file") String file
//						, @Param("user_id") Long user_id);

}
