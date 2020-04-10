package jp.co.sogeninc.semv2_be.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * user DB参照モデル
 *
 *
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3592313469614348059L;

	public static enum Role {
		STUDENT, TEACHER, ADMIN, STRANGER;

	    private static Map<Role, GrantedAuthority> authMap;
	    static {
	    	authMap = Arrays.stream(values()).collect(Collectors.toMap(r->r, r->new SimpleGrantedAuthority(r.toString())));
	    }

	    public GrantedAuthority toGrantedAuthority() {
	    	return authMap.get(this);
	    }

	}

	/**
	 * ユーザid
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 内部認証パスワード
	 */
	@JsonIgnore
	@Column
	private String password;


	/**
	 * 学生証番号
	 */
	@Column(unique=true)
	private String uid;

    @ElementCollection(targetClass=Role.class,fetch=FetchType.EAGER) //no proxy error 対策でEAGERを指定
	@JoinTable(name="user_roles",joinColumns=@JoinColumn(name="user_id"))
	@Enumerated(EnumType.STRING)
	private Set<Role> roles = new TreeSet<>();

	/**
	 * 更新日時
	 */
	@Column
	private Date updateDate;

	/**
	 * 作成日時
	 */
	@Column
	private Date createDate;


    @Transient
	private boolean accountNonExpired = true;
    @Transient
	private boolean accountNonLocked = true;
    @Transient
	private boolean credentialsNonExpired = true;
	@Transient
	private boolean enabled = true;


    @JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		System.out.println("authority"+roles.stream().map(r->r.toGrantedAuthority()).collect(Collectors.toList()));
		return roles.stream().map(r->r.toGrantedAuthority()).collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return uid;
	}

	/** userInfo **/
	
	/**
	 * 姓
	 */
	@Column
	private String familyName;

	/**
	 * 名
	 */
	@Column
	private String givenName;

	/**
	 * 姓カナ
	 */
	@Column
	private String kanaFamilyName;

	/**
	 * 名カナ
	 */
	@Column
	private String kanaGivenName;

	/**
	 * Eメール
	 */
	@Column(unique=true)
	private String email;

	/**
	 * 携帯メール
	 */
	@Column
	private String mobileEmail;

	/**
	 * 電話番号
	 */
	@Column
	private String phoneNumber;

	/**
	 * 携帯番号
	 */
	@Column
	private String mobileNumber;

	/**
	 * 性別
	 */
	@Column
	private String gender;

	 /**
	 * 学科
	 *
	 **/
	 @Column
	 private String faculty;

	 /**
	 * 学部/部局
	 *
	 **/
	 @Column
	 private String department;

	/**
	 * 学年
	 */
	@Column
	private Integer grade;

	/**
	 * 生年月日
	 */
	@Column(name = "birthday")
	private Date birthday;

	/**
	 * 郵便番号
	 */
	@Column(name = "postalcode")
	private String postalcode;

	/**
	 * 住所
	 */
	@Column(name = "address")
	private String address;

	/**
	 * サークル
	 */
	@Column(name = "club")
	private String club;

	/**
	 * 趣味
	 */
	@Column(name = "hobby")
	private String hobby;

	/**
	 * 特技
	 */
	@Column(name = "qualification")
	private String qualification;

	/**
	 * 学歴高校
	 */
	@Column(name = "highschool")
	private String highschool;

	/**
	 * ブログ等URL
	 */
	@Column(name = "url")
	private String url;
	
	//学生が一つでもゼミに確定済みか
	@Column
	private boolean confirmed = false;


//	public void update(User userData) {
//		
//	}

}
