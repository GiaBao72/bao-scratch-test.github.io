package rrs.control.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({ "/rrs" })
public class PageControl {

	// @formatter:off 
	@GetMapping({"" ,"/{pages}", "/{pages}/**"})
	public String getPage(
			@PathVariable(required = false) String pages,
			HttpServletRequest req
		) {
		String last = getURI(req, pages+'/');
		return (pages==null || last.isEmpty()) ? "/pages/home" : '/'+pages+'/'+last;
	}
	
	@GetMapping("/security") public String security(HttpServletRequest req) {
		return "ridirect:/security";
	}
	
	// @formatter:on
	private String getURI(HttpServletRequest req, String cutAt) {
		String uri = req.getRequestURI();
		return uri.substring(uri.indexOf(cutAt) + cutAt.length());
	}
}
