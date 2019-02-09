package tacos.persistence;

import org.springframework.data.repository.CrudRepository;

import tacos.dommain.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String>{
}
