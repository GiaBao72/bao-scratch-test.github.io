package rrs.control.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rrs.model.repositories.StatisticRepository;
import rrs.model.repositories.StatisticRepository.CUSTOM;
import rrs.model.repositories.StatisticRepository.S_ORDER;
@RestController
@CrossOrigin("*")
@RequestMapping({"/rest/statistic"})
public class StatisticRestControl {
	
	@Autowired private StatisticRepository dao;
	
	// THỐNG KÊ SỐ NỘI DUNG THEO TÀI KHOẢN
	/**
	 * @param s MIN_MAX | QTY
	 * @param p any parameters if exists
	 * EX :=> /so?s=QTY&p&p=2022-1-1&p&p=2&p=true
	 * @return {@link Object}
	 * @throws Exception
	 */
	@GetMapping({"/so"}) public ResponseEntity<Object> proc_CS (
			@RequestParam(required = false) S_ORDER s,
			@RequestParam(required = false) Object...p
	) throws Exception {
		return ResponseEntity.ok(dao.execute(s, p));
	}
	
	@GetMapping({"/less-buyers"}) public ResponseEntity<Object> getAccounts () throws Exception {
		return ResponseEntity.ok(dao.execute(CUSTOM.LESS_ACCOUNT, "SELLER"));
	}
}
