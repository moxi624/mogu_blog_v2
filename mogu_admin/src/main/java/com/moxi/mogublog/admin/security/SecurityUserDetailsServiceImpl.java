package com.moxi.mogublog.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moxi.mogublog.xo.entity.Admin;

@Service
public class SecurityUserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
    private SecurityUserRepository securityUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 Admin admin = securityUserRepository.findByUsername(username);
		 
	        if (admin == null) {
	            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
	        } else {
	            return SecurityUserFactory.create(admin);
	        }
	    }

}
