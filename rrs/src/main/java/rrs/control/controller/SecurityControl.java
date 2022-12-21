package rrs.control.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import rrs.model.entities.Account;
import rrs.model.entities.AuthPattern;
import rrs.model.entities.Role;
import rrs.model.services.AccountService;
import rrs.model.services.RoleService;
import rrs.model.utils.SendMail;
import rrs.util.HTMLUtil;
import rrs.util.Random;
import rrs.util.HTMLUtil.BGR;

@Controller
@RequestMapping("/security")
public class SecurityControl {

	private final String BACK_HOME = "/pages/home";
	private String general = Random.UpperCase("RRS", 10);
	private String email = null;
	
	// @formatter:on
	private @Autowired HttpServletRequest req;
	private @Autowired AccountService dao; // CRUD account
	private @Autowired RoleService rDao; // CRUD account
	private @Autowired SendMail mail;
	
	@RequestMapping({"/deniedPage"})
	public String denied() {
		return "/pages/denied";
	}
	
	@GetMapping("/{page}")
	public String pageURI() {
		return new StringBuilder("/pages").append(req.getRequestURI()).toString();
	}

	// ___________________________________________________________ FOR AUTH2 CLIENT
	@GetMapping("/auth2Success")
	public String loginSuccess(OAuth2AuthenticationToken token, Model model) {
		Account account = dao.createNotExist(new AuthPattern(token).toAccount(), true);
		model.addAttribute("account", account);
		return this.BACK_HOME;
	}
	
	// ___________________________________________________________ LOGIN
	@GetMapping("/loginForm")
	public String loginForm() {
		return pageURI(); // append "/pages" before URI
	}
	
	@GetMapping("/loginSuccess")
	public String loginSuccess() {
		Principal principal = req.getUserPrincipal();
		String title = "Đăng nhập tài khoản thành công";
		String message = principal.getName()+" đã đăng nhập thành công";
		req.setAttribute("message", HTMLUtil.alert(BGR.SUCCESS, title, message, 3000));
		
		return this.BACK_HOME; // back home
	}

	@RequestMapping("/loginFailed")
	public String loginFailed() {
		String title = "Đăng nhập tài khoản thất bại";
		String message = "Kiểm tra thông tin đăng nhập!";
		req.setAttribute("message", HTMLUtil.alert(BGR.WARN, title, message, 3000));

		return "/pages/security/loginForm"; // return page
	}
	
	// ___________________________________________________________ LOGOUT
	@GetMapping("/logoutForm")
	public String logoutForm() {
		String title = "Chuyển trang đăng xuất";
		String body = "Xác nhận bước tiếp để đăng xuất tài khoản";
		req.setAttribute("message", HTMLUtil.alert(BGR.WARN, title, body, 1000));
		return pageURI();
	}
	
	@GetMapping("/logoutSuccess")
	public String logoutSuccess() {
		String title = "Đăng xuất tài khoản";
		String message = "Đã đăng xuất tài khoản";
		req.setAttribute("message", HTMLUtil.alert(BGR.LIGHT, title, message, 3000));
		return this.BACK_HOME;
	}
	
	// _GET -> FORM _____________POST -> SAVE ____________________ SIGN-UP
	@GetMapping("/register")
	public String register(Model model, @ModelAttribute Account account) {
		account.setRoles(new HashSet<>());
		model.addAttribute("account", account);
		model.addAttribute("roles", getRoles());
		return pageURI(); // get register form
	}
	
	@PostMapping("/register")
	public String register(Model model, Account account, Errors errors) {
		String message, title = "Đăng ký tài khoản";
		if(!errors.hasErrors())
			try {
				if(dao.getByEmail(account.getEmail()) != null) {
					throw new Exception(account.getEmail()+" Đã được sử dụng, vui lòng chọn email khác");
				}
				Account a = dao.save(account);
				if(a != null) {
					message = "Đăng ký tài khoản "+a.getUsername()+" thành công";
					model.addAttribute("message", HTMLUtil.alert(BGR.SUCCESS, title, message, 3000));
					return "/pages/security/loginForm"; // load login page
				} 
				message = "Đăng ký tài khoản thất bại!";
				model.addAttribute("message", HTMLUtil.alert(BGR.DANGER, title, message, 5000));
			} catch (Exception e) {
				model.addAttribute("message", HTMLUtil.alert(BGR.WARN, title, e.getMessage(), 3000));
			}
		model.addAttribute("roles", getRoles());
		return pageURI(); // callback to re-register input data
	}
	

	// ___________________________________________________________ FORGOT - PASSWORD
	@ResponseBody @RequestMapping("/getCode")
	public ResponseEntity<String> getCodeToMail(@RequestParam String address, HttpServletRequest req) throws Exception {
		String subject = "Yêu cầu mã xác thực tài khoản!";
		String text = HTMLUtil.getCode("RRS gửi mã xác thực tài khoản", this.general = Random.UpperCase("RRS", 10), "nhomTTXBS@gmail.com");

		if(dao.getByEmail(address)==null) throw new Exception(address + " chưa được đăng ký thông tin, vui lòng kiểm tra lại!");
		try {
			System.out.println("Send mail code: "+this.general);
			mail.sendMimeMessage(subject, text, null, null, this.email=address);
			return ResponseEntity.ok("[\"RRS đã gửi mã xác thực gồm "+this.general.length()+" ký tự tới email: "+address+" vui lòng kiểm tra hộp thư.\"]");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping("/getPassCode") 
	public String getPassCode(Model model, @RequestParam(required = false) String code) throws Exception {
		if(code.equals(this.general)) {
			Account a = dao.getByEmail(this.email);
			if(a==null) throw new Exception(this.email+" tài khoản không tồn tại!");
			model.addAttribute("account", new Account(a.getUsername(), a.getPassword()));
			return "/pages/security/change_password";
		} else req.setAttribute("message", HTMLUtil.alert(BGR.LIGHT,
				"Xác thực tài khoản","Xác thực thất bại, "+code+" không đúng!", 3000)
				);
		return "/pages/security/forgot_password";
	}

	// ___________________________________________________________ CHANGE PASSWORD
	@GetMapping("/change_password") public String changePassword(Model model, @ModelAttribute Account account) {
		Principal principal = req.getUserPrincipal();
		Optional<Account> optional = dao.getOptional(principal.getName()); 
		if(optional.isPresent()) {
			Account a = optional.get();
			account.setUsername(a.getUsername());
			account.setPassword(a.getPassword());			
		} model.addAttribute("account", account);
		return this.pageURI();
	}
	
	@PostMapping("/change_password") public String changePassword(@ModelAttribute Account account,
			@RequestParam String newPass, @RequestParam String rePass) {
		String title = "Thay đổi mật khẩu";
		String message = "Nhập lại mật khẩu không đúng!";
		
		if(!rePass.equals(newPass)) req.setAttribute("message", HTMLUtil.alert(BGR.WARN, title, message, 3000));
		else {
			try {
				Optional<Account> optional = dao.getOptional(account.getUsername());
				if(optional.isPresent()) {
					if(account.getPassword().equals(optional.get().getPassword())) {
						message = "Thay đổi mật khẩu thành công";
						req.setAttribute("message", HTMLUtil.alert(BGR.SUCCESS, title, message, 3000));
						account = optional.get();
						account.setPassword(newPass);
						dao.update(account);
						return BACK_HOME;
					}
				} req.setAttribute("message", HTMLUtil.alert(BGR.WHITE, title, "Tài khoản hoặc mật khẩu không đúng", 3000));
			} catch (Exception e) {
				e.printStackTrace();
				req.setAttribute("message", HTMLUtil.alert(BGR.WHITE, title, e.getMessage(), 3000));
			}
		} return this.pageURI();
	}
	
	// ___________________________________________________________ UPDATE INFO
	@GetMapping("/about_me") public String getAboutMe(Model model) {
		Principal p = req.getUserPrincipal();
		Optional<Account> optional = dao.getOptional(p.getName());
		model.addAttribute("account", optional.isEmpty() ? new Account() : optional.get());
		return this.pageURI();
	}
	
	@PostMapping("/about_me") public String saveAboutMe(Model model, Account account, Errors errors) {
		String message, title = "Cập nhật thông tin";
		
		if(!errors.hasErrors())
			try {
				
				// update account
				Account a = dao.update(account);
				if(a != null) {
					message = "Cập nhật tài khoản "+a.getUsername()+" thành công";
					model.addAttribute("message", HTMLUtil.alert(BGR.SUCCESS, title, message, 3000));
				} else {
					message = "Cập nhật thông tin cá nhân thất bại!";
					model.addAttribute("message", HTMLUtil.alert(BGR.WARN, title, message, 3000));
				}
			} catch (Exception e) {
				model.addAttribute("message", HTMLUtil.alert(BGR.DANGER, title, e.getMessage(), 5000));
			}
		
		return this.pageURI(); // callback to about_me page
	}
	
	private List<Role> getRoles() {
		List<Role> list = rDao.getList();
		for(int i = 0; i < list.size(); i++) {
			String id = list.get(i).getId();
			if(id.equalsIgnoreCase("ADMIN") || id.equalsIgnoreCase("OWNER")) list.remove(i--);
		} return list;
	}
	
	// @formatter:off
}
