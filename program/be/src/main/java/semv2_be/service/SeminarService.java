package jp.co.sogeninc.semv2_be.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sogeninc.semv2_be.domain.Seminar;
import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.repository.SeminarRepository;

/**
 * Seminar コントローラー用サービスクラス
 *
 *
 */
@Service
@Transactional
public class SeminarService {


    @Autowired
    private SeminarRepository seminarRepository;

    /**
     * ゼミ1件取得（ユーザidより）
     * @param userId
     * @return Seminar
     */
    public Seminar findByOwner(Long userId) {
        return seminarRepository.findByOwner(userId);
    }
    
	public Seminar findByOwner(User owner) {
		return seminarRepository.findByOwner(owner);
	}

    /**
     * ゼミ1件取得
     * @param id
     * @return Seminar
     */
    public Seminar findOne(Long id) {
        return seminarRepository.findOne(id);
    }

    /**
     * ゼミ全件取得
     *
     * @return List<Seminar>
     */
    public List<Seminar> findAll() {
        return seminarRepository.findAll();
    }

    /**
     * ゼミ情報保存
     * @param s
     * @return Seminar
     */
    public Seminar save(Seminar s) {
        return seminarRepository.save(s);
    }

}
