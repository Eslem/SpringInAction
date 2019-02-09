package tacos.web.api;

import java.util.Date;
import java.util.List;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import lombok.Getter;
import tacos.dommain.Ingredient;
import tacos.dommain.Taco;

@Relation(value="taco", collectionRelation="tacos")
public class TacoResource extends ResourceSupport {
	@Getter
	private final String name;
	@Getter
	private final Date createdAt;
	@Getter
	private final List<Ingredient> ingredients;

	public TacoResource(Taco taco) {
		this.name = taco.getName();
		this.createdAt = taco.getCreatedAt();
		this.ingredients = taco.getIngredients();
	}
}