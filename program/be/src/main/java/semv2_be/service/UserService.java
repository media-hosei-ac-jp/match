package jp.co.sogeninc.semv2_be.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.domain.User.Role;
import jp.co.sogeninc.semv2_be.repository.UserRepository;

/**
 * Userコントローラー用サービスクラス
 *
 *
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

//	@Autowired
//	private ExternalizedConfig config;
	
	public User findByUid(String uid) {
		// ログイン取得
		return userRepository.findByUid(uid);
	}	
	
	public User findByEmail(String email) {
		// ログイン取得
		return userRepository.findByEmail(email);
	}

	/**
	 * ユーザ取得
	 *
	 * @param id
	 * @return User
	 */
	public User findOne(Long id) {
		return userRepository.findOne(id);
	}

	/**
	 * ユーザ保存
	 *
	 * @param u
	 * @return User
	 */
	public User save(User u) {
		return userRepository.save(u);
	}

	/**
	 * 全ユーザ取得
	 *
	 * @return
	 */
	public List<User> findAll() {
		return userRepository.findAll();
	}

//	/**
//	 * 全ユーザ取得（応募情報より）
//	 *
//	 * @param applicationId
//	 * @return
//	 */
//	public List<User> findAllApplicationUser(Long applicationId) {
//		return userRepository.findAllApplicationUser(applicationId);
//	}
	
	// /**
	// * 全ユーザ取得（応募情報より）
	// *
	// * @param applicationId
	// * @return
	// */
	// public List<User> findAllApplicationUser(Application application) {
	// return userRepository.findAllApplicationUser(application);
	// }

	/**
	 * ユーザ削除
	 * @param id
	 */
	public void delete(Long id){
		userRepository.delete(id);
	}

	public List<User> findAllByRole(Role role) {
		return userRepository.findAllByRole(role);
	}

}
