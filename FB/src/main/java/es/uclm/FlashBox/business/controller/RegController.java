package es.uclm.FlashBox.business.controller;

import es.uclm.FlashBox.business.entity.*;
import es.uclm.FlashBox.business.enums.Rol;
import es.uclm.FlashBox.business.persistence.*;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegController {

	@Autowired
	private UsuarioDAO usuarioDAO;

	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private RepartidorDAO repartidorDAO;

	@Autowired
	private RestauranteDAO restauranteDAO;

	@GetMapping("/registro")
	public String mostrarRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registro";
	}

	@PostMapping("/registro")
	public String registrarUsuario(@ModelAttribute Usuario usuario) {
		usuarioDAO.save(usuario);

		switch (usuario.getRol()) {
		case RESTAURANTE -> {
			CartaMenu carta = new CartaMenu();
			carta.setNombre("Carta de " + usuario.getNombre());

			Restaurante restaurante = new Restaurante();
			restaurante.setNombre(usuario.getNombre());
			restaurante.setTelefono(usuario.getTelefono());
			restaurante.setDireccion("Dirección pendiente");
			restaurante.setCartaMenu(carta);
			restaurante.setUsuario(usuario); // 🔗 relación
			usuario.setRestaurante(restaurante); // 🔁

			restauranteDAO.save(restaurante);
		}

		case CLIENTE -> {
			Cliente cliente = new Cliente();
			cliente.setNombre(usuario.getNombre());
			cliente.setApellidos(usuario.getApellidos());
			cliente.setCorreo(usuario.getCorreo());
			cliente.setUsuario(usuario); // 🔗 relación
			usuario.setCliente(cliente); // 🔁

			clienteDAO.save(cliente);
		}

		case REPARTIDOR -> {
			Repartidor repartidor = new Repartidor();
			repartidor.setNombre(usuario.getNombre());
			repartidor.setApellidos(usuario.getApellidos());
			repartidor.setCorreo(usuario.getCorreo());
			repartidor.setEficiencia(100); // valor por defecto
			repartidor.setUsuario(usuario); // 🔗 relación
			usuario.setRepartidor(repartidor); // 🔁

			repartidorDAO.save(repartidor);
		}
		}

		return "redirect:/login";
	}

	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}

	@PostMapping("/login")
	public String procesarLogin(@RequestParam String username, @RequestParam String password, Model model) {
		Usuario usuario = usuarioDAO.findByUsernameAndPassword(username, password);
		
		if (usuario != null) {
			if (usuario.getRol() == Rol.RESTAURANTE) {
				return "redirect:/restaurante/carta"; // Redirige a la página de añadir carta
			} else {
				return "redirect:/"; // Redirige a la página principal para otros roles
			}
		} else {
			model.addAttribute("error", "Credenciales incorrectas");
			return "login"; // Vuelve a la página de login con un mensaje de error
		}
	}
	
}
