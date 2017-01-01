package org.lacitysan.server.security;

import org.lacitysan.server.persistence.dao.UsersDao;
import org.lacitysan.server.persistence.entities.User;
import org.lacitysan.server.security.filters.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Custom implementation of <code>AuthenticationManager</code>.
 * @author Alvin Quach
 */
@Component
public class AuthenticationManagerImpl implements AuthenticationManager {

	@Autowired
	UsersDao usersDao;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String username = (String)authentication.getPrincipal();
		User user = usersDao.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid Username");
		}
		
		String password = (String)authentication.getCredentials();
		if (password == null || !passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Invalid Password");
		}
		
		AuthenticatedUser result = new AuthenticatedUser(new MyUserDetails(user));
		return result;
	}

}
