package pl.konradmaksymilian.turnbasedgames.user.entity;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import pl.konradmaksymilian.turnbasedgames.core.entity.BaseEntity;
import pl.konradmaksymilian.turnbasedgames.user.Role;

@Entity
public class User extends BaseEntity implements UserDetails {

	@Column(unique = true)
	private String nick;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private Role role;
	
	@Transient
	private Collection<GrantedAuthority> authorities;
	
	private boolean isAccountNonLocked = true;
			
	protected User() {}

	public User(String nick, String email, String password, Role role) {
		this.nick = nick;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (authorities == null) {
			setAuthorities();
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		return nick;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}
	
	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public int hashCode() {
		return 31 * nick.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User other = (User) obj;
			if (getUsername().equals(other.getUsername())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return nick;
	}
	
	private void setAuthorities() {
		authorities = Collections.singleton(new SimpleGrantedAuthority(role.toString()));
	}
}
