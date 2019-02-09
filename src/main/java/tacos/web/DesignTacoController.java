package tacos.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.dommain.Ingredient;
import tacos.dommain.Taco;
import tacos.persistence.IngredientRepository;
import tacos.persistence.TacoRepository;
import tacos.dommain.Ingredient.Type;
import tacos.dommain.Order;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

	private final IngredientRepository ingredientRepo;
	private TacoRepository designRepo;

	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
		this.ingredientRepo = ingredientRepo;
		this.designRepo = designRepo;
	}

	@ModelAttribute
	public void addIngredientsToModel(Model model) {
		List<Ingredient> ingredients = Arrays.asList(
				  new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
				  new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
				  new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
				  new Ingredient("CARN", "Carnitas", Type.PROTEIN),
				  new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
				  new Ingredient("LETC", "Lettuce", Type.VEGGIES),
				  new Ingredient("CHED", "Cheddar", Type.CHEESE),
				  new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
				  new Ingredient("SLSA", "Salsa", Type.SAUCE),
				  new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
				);
				
				Type[] types = Ingredient.Type.values();
				for (Type type : types) {
				  model.addAttribute(type.toString().toLowerCase(),
				      filterByType(ingredients, type));
				}
	}

	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}

	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}

	@GetMapping
	public String showDesignForm(Model model) {
		return "design";
	}

	@PostMapping
	public String processDesign(@Valid @ModelAttribute("taco") Taco design, Errors errors, @ModelAttribute Order order) {
		if (errors.hasErrors()) {
			log.info("Error: " + errors);
			return "design";
		}

		log.info("Processing design: " + design);
		
		Taco saved = designRepo.save(design);
		order.addDesign(saved);

		return "redirect:/orders/current";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}
}