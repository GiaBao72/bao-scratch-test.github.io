package rrs.model.services;

import org.springframework.stereotype.Service;

import rrs.model.repositories.StatisticRepository;
import rrs.model.utils.AbstractSQL;

@Service
public class SQLService extends AbstractSQL implements StatisticRepository {

	@Override
	public Object execute(S_ORDER proc, Object... params) throws Exception {
		return super.execute(proc.toString(), params);
	}

	@Override
	public Object execute(CUSTOM query, Object... params) throws Exception {
		return super.execute(query.toString(), params);
	}

}
