package tacos.persistence;

import org.springframework.data.repository.CrudRepository;

import tacos.dommain.Taco;

public interface TacoRepository extends CrudRepository<Taco, Long>{
}
