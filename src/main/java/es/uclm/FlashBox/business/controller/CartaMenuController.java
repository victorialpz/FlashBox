package es.uclm.FlashBox.business.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.FlashBox.business.entity.CartaMenu;
import es.uclm.FlashBox.business.entity.ItemMenu;
import es.uclm.FlashBox.business.entity.Restaurante;
import es.uclm.FlashBox.business.entity.Usuario;
import es.uclm.FlashBox.business.enums.Rol;
import es.uclm.FlashBox.business.persistence.CartaMenuDAO;
import es.uclm.FlashBox.business.persistence.ItemMenuDAO;
import es.uclm.FlashBox.business.persistence.RestauranteDAO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/restaurante/menu")
public class CartaMenuController {

	private final RestauranteDAO restauranteDAO;
	private final CartaMenuDAO cartaMenuDAO;
	private final ItemMenuDAO itemMenuDAO;

	public CartaMenuController(RestauranteDAO restauranteDAO, CartaMenuDAO cartaMenuDAO, ItemMenuDAO itemMenuDAO) {
		this.restauranteDAO = restauranteDAO;
		this.cartaMenuDAO = cartaMenuDAO;
		this.itemMenuDAO = itemMenuDAO;
	}

	private boolean esUsuarioRestauranteValido(Usuario usuario, Restaurante restaurante) {
		return usuario != null && usuario.getRol() == Rol.RESTAURANTE
				&& restaurante.getUsuario().getId().equals(usuario.getId());
	}

	@GetMapping("/{id}")
	public String verMenu(@PathVariable Long id, Model model) {
		Restaurante restaurante = restauranteDAO.findById(id).orElse(null);
		if (restaurante == null) {
			return "redirect:/error";
		}

		CartaMenu carta = restaurante.getCartaMenu();
		if (carta == null) {
			carta = new CartaMenu();
			carta.setNombre("Carta de " + restaurante.getNombre());
			carta = cartaMenuDAO.save(carta);
			restaurante.setCartaMenu(carta);
			restauranteDAO.save(restaurante);
		}

		List<ItemMenu> items = carta.getItems();
		model.addAttribute("restaurante", restaurante);
		model.addAttribute("items", items);
		model.addAttribute("itemMenu", new ItemMenu());

		if (items == null || items.isEmpty()) {
			model.addAttribute("mensaje", "Ups, el restaurante todavía no ha introducido ningún menú.");
		}

		return "carta_menu";
	}

	@PostMapping("/{id}/agregar")
	public String agregarItem(@PathVariable Long id, @ModelAttribute ItemMenu itemMenu, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Restaurante restaurante = restauranteDAO.findById(id).orElse(null);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		if (restaurante == null || usuario == null || !esUsuarioRestauranteValido(usuario, restaurante)) {
			return "redirect:/error";
		}

		CartaMenu carta = restaurante.getCartaMenu();
		if (carta == null) {
			redirectAttributes.addFlashAttribute("mensaje", "Carta no encontrada.");
			return "redirect:/restaurante/menu/" + id;
		}

		itemMenu.setCartaMenu(carta);

		if (itemMenu.getNombre() == null || itemMenu.getNombre().isBlank() || itemMenu.getPrecio() == null
				|| itemMenu.getTipo() == null) {
			redirectAttributes.addFlashAttribute("mensaje", "Completa todos los campos.");
			return "redirect:/restaurante/menu/" + id;
		}

		itemMenuDAO.save(itemMenu);
		return "redirect:/restaurante/menu/" + id;
	}

	@GetMapping("/{id}/editar/{itemId}")
	public String editarItemForm(@PathVariable Long id, @PathVariable Long itemId, Model model, HttpSession session) {
		ItemMenu item = itemMenuDAO.findById(itemId).orElse(null);
		Restaurante restaurante = restauranteDAO.findById(id).orElse(null);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		if (item == null || restaurante == null || !esUsuarioRestauranteValido(usuario, restaurante))
			return "redirect:/error";

		model.addAttribute("itemMenu", item);
		model.addAttribute("restaurante", restaurante);
		return "editar_item";
	}

	@PostMapping("/{id}/editar/{itemId}")
	public String guardarEdicion(@PathVariable Long id, @PathVariable Long itemId,
			@ModelAttribute ItemMenu itemActualizado, HttpSession session) {
		Restaurante restaurante = restauranteDAO.findById(id).orElse(null);
		ItemMenu original = itemMenuDAO.findById(itemId).orElse(null);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		if (restaurante == null || original == null || !esUsuarioRestauranteValido(usuario, restaurante)) {
			return "redirect:/error";
		}

		original.setNombre(itemActualizado.getNombre());
		original.setPrecio(itemActualizado.getPrecio());
		original.setTipo(itemActualizado.getTipo());
		itemMenuDAO.save(original);

		return "redirect:/restaurante/menu/" + id;
	}

	@GetMapping("/{id}/eliminar/{itemId}")
	public String eliminarItem(@PathVariable Long id, @PathVariable Long itemId, HttpSession session) {
		Restaurante restaurante = restauranteDAO.findById(id).orElse(null);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		if (restaurante == null || !esUsuarioRestauranteValido(usuario, restaurante)) {
			return "redirect:/error";
		}

		itemMenuDAO.deleteById(itemId);
		return "redirect:/restaurante/menu/" + id;
	}
}
