package rrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import rrs.model.services.AuthService;


@Configuration
@SuppressWarnings("deprecation")
public class AuthConfig extends WebSecurityConfigurerAdapter {
	
	private final String[] PERMIT = {
		"/css/**", "/js/**", "/data/**", "/rest/**", "/security/**", "/rrs",
		"/rrs/pages/rrs", "/rrs/pages/home", "/rrs/pages/product_detail"
	};	
	private final String[] FOR_ADMIN = {
			"/rrs/pages/setting", // quản lý khác
			"/rrs/pages/statistic_order", // Thống kê
			"/rrs/pages/account_management", // Tài khoản
			"/rrs/pages/progress_management", // Quy trình đặt hàng
			"/rest/accounts","/rest/accounts/**", // Người dùng
	};
	private final String[] FOR_SHIPPER = {
		"/rrs/pages/shipper_order" // quản lý đơn hàng được giao
	};
	private final String[] FOR_SELLER = {
		"/rrs/pages/product_seller" // quản lý sản phẩm bán được
	};
	private final String[] FOR_BUYER = {
		"/rrs/pages/product_buyer" // quản lý đơn hàng đã/đang mua
	};

	@Autowired private AuthService user;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(user);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().disable(); // _csrf -> code... so we need to disabled to make easier login

		// ____________________________________________________________ page accessibility
		http // allow request page authenticated
			.authorizeRequests()
        	.antMatchers(PERMIT).permitAll()
        	.antMatchers(FOR_ADMIN).hasAnyRole("OWNER","ADMIN")
        	.antMatchers(FOR_SELLER).hasAnyRole("OWNER","SELLER")
			.antMatchers(FOR_SHIPPER).hasAnyRole("OWNER","SHIPPER")
			.antMatchers(FOR_BUYER).hasAnyRole("OWNER","BUYER","USER")
        	.anyRequest().authenticated(); // have login
		http.exceptionHandling().accessDeniedPage("/security/deniedPage");
		
		// ____________________________________________________________ form configuration
		http // form login
	 		.formLogin()
			.loginProcessingUrl("/security/login") // default [/login] => process the submitted credential
			.loginPage("/security/loginForm") // form display to login - this post method
	 		.defaultSuccessUrl("/security/loginSuccess", false)
			.failureForwardUrl("/security/loginFailed"); // login failed

        http.oauth2Login() //Login with google account
//        .loginPage("/security/loginForm")
        .failureUrl("/security/loginFailed")
        .defaultSuccessUrl("/security/auth2Success", false)
        .authorizationEndpoint().baseUri("/security/loginAuth2");
        
        http// logout
			.logout()
			.logoutUrl("/security/logout") // default [/logout]
			.logoutSuccessUrl("/security/logoutSuccess");
        
        
	}
	
}
