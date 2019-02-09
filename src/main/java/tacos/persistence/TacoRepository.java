package tacos.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import tacos.dommain.Taco;

public interface TacoRepository extends CrudRepository<Taco, Long>{
	
	List<Taco> findAll(Pageable page);
}
