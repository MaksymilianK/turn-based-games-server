package pl.konradmaksymilian.turnbasedgames.player.entity;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import pl.konradmaksymilian.turnbasedgames.core.entity.BaseEntity;
import pl.konradmaksymilian.turnbasedgames.player.Role;

@Entity
public class Player extends BaseEntity implements UserDetails {
	
	@Column(unique = true)
	private String nick;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private Role role;
	
	@Transient
	private Collection<GrantedAuthority> authorities;
	
	private boolean isAccountNonLocked = true;
			
	protected Player() {}

	public Player(String nick, String email, String password, Role role) {
		this.nick = nick;
		this.email = email;
		this.password = password;
		this.role = role;
		setAuthorities();
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
		setAuthorities();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
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
		if (obj instanceof Player) {
			Player other = (Player) obj;
			if (getUsername().equals(other.getUsername())) {
				return true;
			}
		}
		return false;
	}
	
	private void setAuthorities() {
		authorities = Collections.singleton(new SimpleGrantedAuthority(role.toString()));
	}
}
