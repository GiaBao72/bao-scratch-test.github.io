package rrs.model.services; 

import java.util.Optional;

import org.springframework.stereotype.Service;
import rrs.model.entities.Category;
import rrs.util.Random;

@Service
public class CategoryService extends AbstractService<Category, String>{

	@Override
	protected String getId(Category entity) {
		return entity.getId();
	}

	// create and update data of the entity
	public <S extends Category> S save(S entity, Boolean isRandom) throws IllegalArgumentException {
		if(isRandom) { // random until id isEmpty
			Optional<Category> optional; do {
				entity.setId(Random.NumUppLow(entity.getId(), 8));
				optional = rep.findById(entity.getId());
			} while(optional.isPresent());
		} // not random and check category_id
		else if(rep.findById(entity.getId()).isPresent())
			throw new IllegalArgumentException(
				String.format("cannot saved, because %s already exsits", entity.getId())
			);
		return rep.save(entity);
	}

}
