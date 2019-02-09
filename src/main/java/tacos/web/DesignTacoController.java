package tacos.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;
import tacos.dommain.Ingredient;
import tacos.dommain.Taco;
import tacos.persistence.IngredientRepository;
import tacos.persistence.TacoRepository;
import tacos.dommain.Ingredient.Type;
import tacos.dommain.Order;

@Slf4j
@RestController
@RequestMapping(path = "/design", produces = "application/json")
@SessionAttributes("order")
@CrossOrigin(origins = "*")
public class DesignTacoController {

	private final IngredientRepository ingredientRepo;
	private TacoRepository tacoRepo;

	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
		this.ingredientRepo = ingredientRepo;
		this.tacoRepo = designRepo;
	}

	@GetMapping("/recent")
	public Iterable<Taco> recentTacos() {
		PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
		return tacoRepo.findAll(page);
	}

	/*
	 * 
	 * @GetMapping("/recent") public Resources<Resource<Taco>> recentTacos() {
	 * PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
	 * List<Taco> tacos = tacoRepo.findAll(page); Resources<Resource<Taco>>
	 * recentResources = Resources.wrap(tacos);
	 * recentResources.add(linkTo(methodOn(DesignTacoController.class).recentTacos()
	 * ).withRel("recents")); return recentResources; }
	 * 
	 * 
	 * @GetMapping("/recent") public Resources<TacoResource> recentTacos() {
	 * PageRequest page = PageRequest.of( 0, 12, Sort.by("createdAt").descending());
	 * List<Taco> tacos = tacoRepo.findAll(page).getContent(); List<TacoResource>
	 * tacoResources = new TacoResourceAssembler().toResources(tacos);
	 * Resources<TacoResource> recentResources = new
	 * Resources<TacoResource>(tacoResources); recentResources.add(
	 * linkTo(methodOn(DesignTacoController.class).recentTacos())
	 * .withRel("recents")); return recentResources; }
	 * 
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
		Optional<Taco> optTaco = tacoRepo.findById(id);
		if (optTaco.isPresent()) {
			return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Taco postTaco(@RequestBody Taco taco) {
		return tacoRepo.save(taco);
	}

	@ModelAttribute
	public void addIngredientsToModel(Model model) {
		List<Ingredient> ingredients = Arrays.asList(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
				new Ingredient("COTO", "Corn Tortilla", Type.WRAP), new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
				new Ingredient("CARN", "Carnitas", Type.PROTEIN),
				new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES), new Ingredient("LETC", "Lettuce", Type.VEGGIES),
				new Ingredient("CHED", "Cheddar", Type.CHEESE), new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
				new Ingredient("SLSA", "Salsa", Type.SAUCE), new Ingredient("SRCR", "Sour Cream", Type.SAUCE));

		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
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
	public String processDesign(@Valid @ModelAttribute("taco") Taco design, Errors errors,
			@ModelAttribute Order order) {
		if (errors.hasErrors()) {
			log.info("Error: " + errors);
			return "design";
		}

		log.info("Processing design: " + design);

		Taco saved = tacoRepo.save(design);
		order.addDesign(saved);

		return "redirect:/orders/current";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}
}
