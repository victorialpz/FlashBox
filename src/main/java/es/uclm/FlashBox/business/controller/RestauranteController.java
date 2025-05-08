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
		return "lista_restaurantes"; // Se corresponde con el archivo lista_restaurantes.html
	}

	@GetMapping("/buscar")
	public String buscarRestaurantes(@RequestParam("nombre") String nombre, String tipo, Model model) {
		System.out.println("Estamos buscando restaurantes con nombre: " + nombre + " tipo: " + tipo);
		List<Restaurante> resultados = restauranteRepository.findByNombreContainingIgnoreCaseAndTipoContainingIgnoreCase(nombre, tipo);
	    model.addAttribute("restaurantes", resultados);

	    if (resultados.isEmpty()) {
	        model.addAttribute("mensaje", "No se encontraron restaurantes con ese nombre.");
	    }

	    return "inicio";
	}




}