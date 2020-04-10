package jp.co.sogeninc.semv2_be.api;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.Seminar;
import jp.co.sogeninc.semv2_be.domain.Setting;
import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.domain.User.Role;
import jp.co.sogeninc.semv2_be.service.RecruitmentService;
import jp.co.sogeninc.semv2_be.service.SeminarService;
import jp.co.sogeninc.semv2_be.service.SettingService;
import jp.co.sogeninc.semv2_be.service.UserService;

/**
 * User Restコントローラー
 *
 *
 */
@RestController
@RequestMapping("api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SeminarService seminarService;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private RecruitmentService recrService;

	@Autowired
	protected MessageSource msg;

    @RequestMapping(value="/me", method=RequestMethod.GET)
    public User getCurrentUser(@AuthenticationPrincipal User user) {
        return user;
    }
    
    @RequestMapping(value="/reloadMe", method=RequestMethod.GET)
    public User reloadCurrentUser(@AuthenticationPrincipal User user) {
        return userService.findOne(user.getId());
    }


//	/**
//	 * 全ユーザ取得
//	 *
//	 * @param userDetails
//	 * @return
//	 */
//	@RequestMapping(value = "/all", method = RequestMethod.GET)
//	public List<User> getAllUsers(@AuthenticationPrincipal Login login) throws Exception {
//
//
//		Integer intadminTargetRange = Integer.parseInt(config.getAdminTargetRange());
//
//
//		User user = userService.findOne(login.getUserId());
//
//		Long facaltyID = user.getFacultyId();
//
//		List<User> users;
//		if (facaltyID > intadminTargetRange) {
//			users = userService.findUserByFaculty(user.getFacultyId());
//
//		} else {
//			users = userService.findUserByFacultyIdOrAdmin(user.getFacultyId());
//		}
//
//		return users;
//	}


	/**
	 * 自ユーザ更新
	 *
	 * @param id
	 * @param user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public User putUser(@RequestBody User userData, @AuthenticationPrincipal User user) {
		userData.setId(user.getId());
		userData.setUid(user.getUid());
		userData.setEmail(user.getEmail());
		userData.setUpdateDate(new Date());

		return userService.save(userData);
	}

//	/**
//	 * 自ユーザ新規作成
//	 *
//	 * @param user
//	 * @param uriBuilder
//	 * @return
//	 */
//	@RequestMapping(method = RequestMethod.POST)
//	@ResponseStatus(HttpStatus.CREATED)
//	public UserData postUser(@RequestBody UserData userData, @AuthenticationPrincipal Login login) {
//		User created = userService.save(userData.getUser());
//		// コード取得
//		Code gend = userService.findByCode(created);
//		// 学科取得
//		Faculty fac = userService.findFacultyByUser(created);
//		// 所属取得
//		Department dep = userService.findDepartmentByUser(created);
//
//		return new UserData(created, userData.getLogin(), gend, fac, dep);
//	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/addStudents", method = RequestMethod.POST)
	public boolean addStudents(@RequestBody List<User> users) {
		for(User user : users) {
			//すでに登録済みの場合はアップデートする
			User tu = userService.findByUid(user.getUid()); //TODO findAllでmap作った方が早い
			if(tu!=null) {
				user.setId(tu.getId()); //idを上書きして保存	
				user.getRoles().addAll(tu.getRoles());
				//入力済みの情報を移行する
				user.setMobileEmail(tu.getMobileEmail());
				user.setPhoneNumber(tu.getPhoneNumber());
				user.setMobileNumber(tu.getMobileNumber());
				user.setBirthday(tu.getBirthday());
				user.setAddress(tu.getAddress());
				user.setPostalcode(tu.getPostalcode());
				user.setClub(tu.getClub());
				user.setHobby(tu.getHobby());
				user.setQualification(tu.getQualification());
				user.setHighschool(tu.getHighschool());
				user.setUrl(tu.getUrl());
			}
			user.getRoles().add(Role.STUDENT);
			userService.save(user);
		}
		return true;
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/addTeachers", method = RequestMethod.POST)
	public boolean addTeachers(@RequestBody List<User> users) {
		List<Setting> settings = settingService.findAll();
		
		for(User user : users) {
			User _tu = userService.findByEmail(user.getEmail());
			if(_tu != null) {
				//user = _tu;
				user.setId(_tu.getId()); //idを上書きして保存	
				user.getRoles().addAll(_tu.getRoles());
			}
			user.getRoles().add(Role.TEACHER);
			userService.save(user);
			
			Seminar _s = seminarService.findByOwner(user);
			if(_s == null) {
				//create sem
				Seminar sem = new Seminar();
				sem.setOwner(user);
				sem.setCreateDate(new Date());
				seminarService.save(sem);				
			}
			
			//add recr to existing settings
			for(Setting setting : settings) {
				Recruitment r = recrService.findByOwner(setting, user);
				if(r==null) {
					r = new Recruitment();
					r.setSetting(setting);
					Seminar s = seminarService.findByOwner(user);
					r.setSeminar(s);
					r.setCreateDate(new Date());
					recrService.save(r);

				}
			}
		}
		return true;
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/addAdmin", method = RequestMethod.POST)
	public boolean addAdmin(@RequestBody List<User> users) {
		for(User user : users) {
			User _tu = userService.findByEmail(user.getEmail());
			if(_tu != null) {
				//user = _tu;
				user.setId(_tu.getId()); //idを上書きして保存	
				user.getRoles().addAll(_tu.getRoles());
			}
			user.getRoles().add(Role.ADMIN);
			userService.save(user);
		}
		return true;
	}
	
	/* for admin */
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/get4A/{id}", method = RequestMethod.GET)
	public User get4A(@PathVariable("id") long id) {
		return userService.findOne(id);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/getByUid4A/{id}", method = RequestMethod.GET)
	public User getByUid4A(@PathVariable("uid") String uid) {
		return userService.findByUid(uid);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/save4A", method = RequestMethod.POST)
	public User save4A(@RequestBody User user) {
		return userService.save(user);
	}

}
