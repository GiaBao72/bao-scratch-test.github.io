package rrs.model.utils;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface InterDAO<E, K> {
	
	static final String D_USER = "owner";
	
	/**
	 * <h1 style="text-align: center; color: yellow; font-size: 2em; text-transform: uppercase;">
	 * 		find all entities
	 * </h1>
	 * @return all entities
	 */
	public List<E> getList();

	/**
	 * <h1 style="text-align: center; color: yellow; font-size: 2em; text-transform: uppercase;">
	 * 		find all entities by sort conditional
	 * </h1>
	 * @return all entities
	 */
	public List<E> getList(Sort sort);
	
	/**
	 * <h1 style="text-align: center; color: yellow; font-size: 2em; text-transform: uppercase;">
	 * 		find all entities by {@link Pageable} conditional
	 * </h1>
	 * @param pageable be the condition to find.
	 * @return {@link Page}
	 */
	public Page<E> getPage(Pageable pageable);

	/**
	 * <h1 style="text-align: center; color: yellow; font-size: 2em; text-transform: uppercase;">
	 * 		find {@link Optional<E>} by id
	 * </h1>
	 * @param id is key
	 * @return {@link Optional} entity -> {@link Optional#isPresent()} or {@link Optional#isEmpty()}
	 * @throws IllegalArgumentException
	 */
	public Optional<E> getOptional(K id) throws IllegalArgumentException;
	
	/**
	 * <h1 style="text-align: center; color: yellow; font-size: 2em; text-transform: uppercase;">
	 * 		save entity to database
	 * </h1>
	 * @param entity to save
	 * @return entity saved successfully
	 */
	public <S extends E> S save(S entity) throws IllegalArgumentException;
	
	/**
	 * <h1 style="text-align: center; color: yellow; font-size: 2em; text-transform: uppercase;">
	 * 		update entity into database
	 * </h1>
	 * @param entity to update
	 * @return entity updated successfully
	 * @throws IllegalArgumentException
	 */
	public <S extends E> S update(S entity) throws IllegalArgumentException;

	/**
	 * <h1 style="text-align: center; color: yellow; font-size: 2em; text-transform: uppercase;">
	 * 		delete data by {@link Id}
	 * </h1>
	 * @param id is key
	 * @throws IllegalArgumentException 
	 */
	public void remove(K id) throws IllegalArgumentException;
}
