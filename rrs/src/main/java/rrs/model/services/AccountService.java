package rrs.model.services; 

import java.util.List;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rrs.model.entities.Account;
import rrs.model.repositories.AccountRepository;
import rrs.model.utils.SendMail;
import rrs.util.HTMLUtil;

@Service
public class AccountService extends AbstractService<Account, String> {
	
	@Autowired SendMail mail;

	@Override
	protected String getId(Account entity) {
		return entity.getUsername();
	}
	
	public List<Account> getByRole(String role_id) {
		return ((AccountRepository) super.rep).findByRole(role_id);
	}

	public Account getByEmail(String email) {
		return ((AccountRepository) super.rep).getByEmail(email);
	}
	
	/**
	 * <h3>if exist => return account in database</h3>
	 * <h3>else return new account and save into database</h3>
	 * 
	 * @param account to get or save into database
	 * @param sendMail if create account then send email
	 * @return {@link Account} from database
	 * @throws MessagingException 
	 */
	public Account createNotExist(Account account, boolean sendMail) {
		Account o = ((AccountRepository) super.rep).getByUnique(account);
		if(o == null && sendMail) try {
			String unique = account.getFlatform()+"("+account.getUsername()+")";
			String title = "RRS liên kết tài khoản "+unique+" mới";
			String text = HTMLUtil.newAccount(title, unique, account.getPassword(), null);
			mail.sendMimeMessage(title, text, null, RecipientType.TO, account.getEmail());
			return super.rep.save(account);
		} catch (MessagingException e) {}
		return o;
	}
}
