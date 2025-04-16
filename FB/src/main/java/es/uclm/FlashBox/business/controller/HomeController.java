package es.uclm.FlashBox.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.uclm.FlashBox.business.entity.Restaurante;
import es.uclm.FlashBox.business.entity.Usuario;
import es.uclm.FlashBox.business.enums.Rol;
import es.uclm.FlashBox.business.persistence.RestauranteDAO;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private RestauranteDAO restauranteDAO;

	@GetMapping({ "/", "/home" })
	public String mostrarInicio(Model model) {
		List<Restaurante> restaurantes = restauranteDAO.findAll();
		model.addAttribute("restaurantes", restaurantes);
		return "home";
	}

	@GetMapping("/inicio")
	public String inicioCliente(HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		if (usuario == null || usuario.getRol() != Rol.CLIENTE) {
			return "redirect:/login";
		}

		List<Restaurante> restaurantes = restauranteDAO.findAll();
		model.addAttribute("restaurantes", restaurantes);
		return "home";
	}

}