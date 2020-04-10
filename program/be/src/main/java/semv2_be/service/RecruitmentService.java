package jp.co.sogeninc.semv2_be.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.Setting;
import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.repository.RecruitmentRepository;

/**
 * Recruitment コントローラー用サービス
 *
 *
 */
@Service
@Transactional
public class RecruitmentService {

	@Autowired
	private RecruitmentRepository recruitmentRepository;


	/**
	 * 募集情報キーより取得
	 *
	 * @param id
	 * @return
	 */
	public Recruitment findOne(Long id) {
		return recruitmentRepository.findOne(id);
	}



	public List<Recruitment> getAllRecrs(User owner) {
		return recruitmentRepository.findAllRecrsByOwner(owner);
	}

//	/**
//	 * ゼミ情報より取得
//	 *
//	 * @param seminarId
//	 * @return
//	 */
//	public List<Recruitment> findBySeminar(Long seminarId) {
//		return recruitmentRepository.findBySeminar(seminarId);
//	}
//
//	/**
//	 * ゼミ情報と募集期間より取得
//	 *
//	 * @param seminarId
//	 * @param settingId
//	 * @return
//	 */
//	public List<Recruitment> findBySeminarAndSetting(Long seminarId, Long settingId) {
//		return recruitmentRepository.findBySeminarAndSetting(seminarId, settingId);
//	}
//
//	/**
//	 * 募集より募集設定情報取得
//	 *
//	 * @param recruitmentId
//	 * @return
//	 */
//	public List<RecruitmentSetting> findSettingByRecruitmentId(Long recruitmentId) {
//		List<RecruitmentSetting> list = recruitmentSettingRepository.findByRecruitmentId(recruitmentId);
//		setRecrSettingList(list, recruitmentId);
//
//		return list;
//	}
//
//	/**
//	 * 募集設定情報に設定
//	 * @param settingList
//	 * @param recrId
//	 */
//	public void setRecrSettingList(List<RecruitmentSetting> settingList, Long recrId) {
//		for (RecruitmentSetting setting: settingList) {
//			List<Application> appList = applicationRepository.findByRecGrade(recrId, setting.getGrade());
//			setting.setNumber(appList.size());
//		}
//	}
//
//	/**
//	 * 募集より募集詳細情報取得
//	 *
//	 * @param recruitmentId
//	 * @return
//	 */
//	public List<RecruitmentDetail> findDetailByRecruitmentId(Long recruitmentId) {
//		return recruitmentDetailRepository.findByRecruitmentId(recruitmentId);
//	}
//
//	/**
//	 * 募集より募集詳細データ(画面用)取得
//	 *
//	 * @param recruitmentId
//	 * @return
//	 */
//	public List<RecruitmentDetailData> findDetailDataByRecruitmentId(Long recruitmentId) {
//
//		List<RecruitmentDetailData> detailDataList = new ArrayList<RecruitmentDetailData>();
//
//		List<RecruitmentDetail> detailList = recruitmentDetailRepository.findByRecruitmentId(recruitmentId);
//
//		Long divide = 5L; // 選考手段
//		List<Code> divideCode = codeRepository.findAllCodeFromDiv(divide);
//		Integer j = 0;
//		for (Integer i = 1; i <= divideCode.size(); i++) {
//			RecruitmentDetail detail = new RecruitmentDetail();;
//			if (detailList.size() > 0){
//				detail = detailList.get(j);
//
//				if (!i.toString().equals(detail.getSelectionMeans())){
//					detail = new RecruitmentDetail(recruitmentId, i.toString());
//				}else{
//					j++;
//				}
//
//			}else{
//				detail = new RecruitmentDetail(recruitmentId, i.toString());
//			}
//
//			RecruitmentDetailData detailData = new RecruitmentDetailData(detail,
//					codeRepository.findByCode(divide, detail.getSelectionMeans()));
//
//			detailDataList.add(detailData);
//
//		}
//
//
//
//		return detailDataList;
//	}

	/**
	 * 募集情報保存
	 *
	 * @return Recruitment
	 */
	public Recruitment save(Recruitment r) {
		return recruitmentRepository.save(r);
	}
//
//	/**
//	 * データを更新する
//	 *
//	 * @return Recruitment
//	 */
//	public int update(Recruitment r) {
//		return recruitmentRepository.update(r.getId(), r.getSeminarId(), r.getSettingId(), r.getUpdateUserId());
//	}
//
//	/**
//	 * 上限下限を0にする（削除対象）
//	 *
//	 * @return Recruitment
//	 */
//	public int updateDelLimit(Long id, Long userId) {
//		return recruitmentRepository.updateDelLimit(id, userId);
//	}
//
//	/**
//	 * 募集詳細情報保存
//	 *
//	 * @return List<RecruitmentDetail>
//	 */
//	public List<RecruitmentDetail> saveDetail(List<RecruitmentDetailData> list_rd) {
//
//		List<RecruitmentDetail> new_List = new ArrayList<>();
//
//
//		for (RecruitmentDetailData rdD : list_rd) {
//
//			RecruitmentDetail rd = rdD.getDetail();
//
//			RecruitmentDetail new_rd = new RecruitmentDetail();
//			// キー取得
//			Long id = rd.getRecruitmentId();
//			String selectionMeans = rd.getSelectionMeans();
//
//			int cnt = recruitmentDetailRepository.putRecDetail(id, selectionMeans, rd.getInstruction(),
//					rd.getDateTime(), rd.getPlace(), rd.getComment(), rd.getUpdateUserId());
//
//			if (cnt == 0) {
//				recruitmentDetailRepository.postRecDetail(id, selectionMeans, rd.getInstruction(), rd.getDateTime(),
//						rd.getPlace(), rd.getComment(), rd.getUpdateUserId());
//			}
//
//			new_rd.setRecruitmentId(id);
//			new_rd.setSelectionMeans(selectionMeans);
//			new_rd.setInstruction(rd.getInstruction());
//			new_rd.setDateTime(rd.getDateTime());
//			new_rd.setPlace(rd.getPlace());
//			new_rd.setComment(rd.getComment());
//			new_rd.setUpdateUserId(rd.getUpdateUserId());
//
//			new_List.add(new_rd);
//		}
//		return new_List;
//	}
//
//	/**
//	 * 募集設定情報保存
//	 *
//	 * @return List<RecruitmentDetail>
//	 */
//	public List<RecruitmentSetting> saveSetting(List<RecruitmentSetting> list_rs) {
//
//		List<RecruitmentSetting> new_List = new ArrayList<>();
//
//		for (RecruitmentSetting rs : list_rs) {
//
//			RecruitmentSetting new_rs = new RecruitmentSetting();
//			// キー取得
//			Long id = rs.getRecruitmentId();
//			Integer grade = rs.getGrade();
//
//			int cnt = recruitmentSettingRepository.putRecSetting(id, grade, rs.getCapacityLowerLimit(),
//					rs.getCapacityUpperLimit(), rs.getUpdateUserId());
//
//			if (cnt == 0) {
//				recruitmentSettingRepository.postRecSetting(id, grade, rs.getCapacityLowerLimit(),
//						rs.getCapacityUpperLimit(), rs.getUpdateUserId());
//			}
//
//			new_rs.setRecruitmentId(id);
//			new_rs.setGrade(grade);
//			new_rs.setCapacityLowerLimit(rs.getCapacityLowerLimit());
//			new_rs.setCapacityUpperLimit(rs.getCapacityUpperLimit());
//			new_rs.setUpdateUserId(rs.getUpdateUserId());
//
//			new_List.add(new_rs);
//		}
//		return new_List;
//	}
//
//	// /**
//	// * 募集情報取得（１件）
//	// *
//	// * @param id
//	// * @return
//	// */
//	// public Recruitment findRecruitment(Long id, Integer grade) {
//	// return recruitmentRepository.findById(id, grade);
//	// }
//
//	/**
//	 * 募集情報取得（１件）
//	 *
//	 * @param id
//	 * @return
//	 */
//	public Recruitment findRecruitment(Long id) {
//		return recruitmentRepository.findOne(id);
//	}
//
//	/**
//	 * 期間より全募集情報取得
//	 *
//	 * @param id
//	 * @return
//	 */
//	public List<Recruitment> findBySetting(Long settingId) {
//		return recruitmentRepository.findBySetting(settingId);
//	}
//
//	/**
//	 * 期間と学年より全募集情報取得
//	 *
//	 * @param id
//	 * @return
//	 */
//	public List<Recruitment> findBySettingAndGrade(Long settingId, Integer grade) {
//		return recruitmentRepository.findBySettingAndGrade(settingId, grade);
//	}
//
//	/**
//	 * 募集設定情報取得
//	 *
//	 * @param id
//	 * @return
//	 */
//	public RecruitmentSetting findByGrade(Long recrId, Integer grade) {
//		return recruitmentSettingRepository.findByGrade(recrId, grade);
//	}
//
//	/**
//	 * 応募取得
//	 *
//	 * @param recruitmentId
//	 * @param grade
//	 * @param result
//	 * @return
//	 */
//	public List<Recruitment> findSeasonResult(Long userId, Long recruitmentId) {
//		return recruitmentRepository.findSeasonResult(userId, recruitmentId);
//	}



	public List<Recruitment> getAllRecrs(Setting setting) {
		return recruitmentRepository.findAllRecrsBySetting(setting);
	}



	/**
	 * 学生が応募可能なRecruitmentを」のリストを返す
	 * @param settingId
	 * @param user
	 * @return
	 */
	public List<Recruitment> getActiveRecrs(long settingId, User user) {
		return recruitmentRepository.findActiveRecrs(settingId, user, user.getGrade());
	}



	public Recruitment findByOwner(Setting s, User owner) {
		return recruitmentRepository.findByOwner(s, owner);
	}



	public Recruitment findByOwner(long settingId, String uid) {
		return recruitmentRepository.findByOwner(settingId, uid);
	}



	public List<Recruitment> getAllRecrs(long sId) {
		return recruitmentRepository.findAllRecrsBySettingId(sId);
	}

}
