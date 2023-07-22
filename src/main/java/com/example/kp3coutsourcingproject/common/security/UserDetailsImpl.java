package com.example.kp3coutsourcingproject.common.security;

import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import static com.example.kp3coutsourcingproject.common.util.AccessUtils.getSafe;

public class UserDetailsImpl implements UserDetails {

	private final User user;

	public UserDetailsImpl(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// Authenticatgion
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		UserRoleEnum role = user.getRole();
		String authority = role.getAuthority();

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(simpleGrantedAuthority);

		return authorities;
	}

	// 계정이 만료되지 않았는지
	// true == 만료되지 않음
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠겨있지 않은지
	// true == 잠겨있지 않음
	@Override
	public boolean isAccountNonLocked() {
		return getSafe(getUser().getEnabled(), true);
	}

	// 계정 패스워드가 만료되지 않았는지
	// true == 패스워드가 만료되지 않음
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 사용가능한 계정인지
	// true == 사용가능한 계정
	@Override
	public boolean isEnabled() {
		return getSafe(getUser().getEnabled(), true);
	}
}
