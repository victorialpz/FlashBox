package es.uclm.FlashBox.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uclm.FlashBox.business.persistence.RestauranteDAO;
import es.uclm.FlashBox.business.entity.Restaurante;

@Controller
public class RestauranteController {

	@Autowired
	private RestauranteDAO restauranteRepository;

	@GetMapping("/restaurantes/listar")
	public String listarRestaurantes(Model model) {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		model.addAttribute("restaurantes", restaurantes);
		return "lista_restaurantes";
	}

	@GetMapping("/buscar")
	public String buscarRestaurantes(@RequestParam("nombre") String nombre, @RequestParam("tipo") String tipo,
			Model model) {
		if ((nombre == null || nombre.isBlank()) && (tipo == null || tipo.isBlank())) {
			model.addAttribute("mensaje", "Introduce al menos un criterio de búsqueda.");
			return "inicio";
		}

		System.out.println("Estamos buscando restaurantes con nombre: " + nombre + " tipo: " + tipo);
		List<Restaurante> resultados = restauranteRepository
				.findByNombreContainingIgnoreCaseAndTipoContainingIgnoreCase(nombre, tipo);

		model.addAttribute("restaurantes", resultados);

		if (resultados.isEmpty()) {
			model.addAttribute("mensaje", "No se encontraron restaurantes con ese nombre o tipo.");
		}

		return "inicio";
	}
}
