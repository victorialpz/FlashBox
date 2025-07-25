package es.uclm.FlashBox.business.controller;

import es.uclm.FlashBox.business.entity.Cliente;
import es.uclm.FlashBox.business.entity.Restaurante;
import es.uclm.FlashBox.business.entity.Usuario;
import es.uclm.FlashBox.business.persistence.ClienteDAO;
import es.uclm.FlashBox.business.persistence.RestauranteDAO;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente/favoritos")
public class FavoritosController {

	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private RestauranteDAO restauranteDAO;

	@PostMapping("/agregar")
	public String agregarFavorito(@RequestParam Long restauranteId, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		Cliente cliente = new Cliente();
		if (usuario == null || usuario.getCliente() == null) {
			return "redirect:/login";
		}

		if (!clienteDAO.findByUsuarioId(usuario.getId()).isEmpty()) {
			cliente = clienteDAO.findByUsuarioId(usuario.getId()).get();
			System.out.println("Cliente :" + cliente.getId());

		} else {
			System.out.println("Cliente no se encuentra");
		}
		Restaurante restaurante = restauranteDAO.findById(restauranteId).orElse(null);

		if (cliente != null && restaurante != null) {
			if (cliente.getFavoritos() != null && !cliente.getFavoritos().contains(restaurante)) {
				cliente.getFavoritos().add(restaurante);
				clienteDAO.save(cliente);
			}
		}

		return "redirect:/restaurantes/listar";
	}

	@GetMapping
	public String verFavoritos(HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		Cliente cliente = new Cliente();
		if (usuario == null || usuario.getCliente() == null) {
			return "redirect:/login";
		}

		if (!clienteDAO.findByUsuarioId(usuario.getId()).isEmpty()) {
			cliente = clienteDAO.findByUsuarioId(usuario.getId()).get();
		}

		if (cliente == null || cliente.getFavoritos() == null) {
			model.addAttribute("mensaje", "No tienes restaurantes favoritos.");

		} else {
			model.addAttribute("favoritos", cliente.getFavoritos());
		}

		return "favoritos";
	}

	@PostMapping("/eliminar")
	public String eliminarFavorito(@RequestParam Long restauranteId, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		Cliente cliente = new Cliente();

		if (usuario == null || usuario.getCliente() == null) {
			return "redirect:/login";
		}

		if (!clienteDAO.findByUsuarioId(usuario.getId()).isEmpty()) {
			cliente = clienteDAO.findByUsuarioId(usuario.getId()).get();
		}

		if (cliente.getFavoritos() != null) {
			cliente.getFavoritos().removeIf(r -> r.getId().equals(restauranteId));
			clienteDAO.save(cliente);
		}

		return "redirect:/cliente/favoritos";
	}
}
