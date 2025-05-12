package es.uclm.FlashBox.business.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.uclm.FlashBox.business.entity.*;
import es.uclm.FlashBox.business.persistence.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cliente/pedido")
public class PedidoController {

	@Autowired
	private RestauranteDAO restauranteDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private RepartidorDAO repartidorDAO;
	@Autowired
	private PedidoDAO pedidoDAO;
	@Autowired
	private ServicioEntregaDAO servicioEntregaDAO;

	@GetMapping("/realizar/{restauranteId}")
	public String mostrarFormularioPedido(@PathVariable Long restauranteId, Model model, HttpSession session) {
		Restaurante restaurante = restauranteDAO.findById(restauranteId).orElse(null);
		if (restaurante == null || restaurante.getCartaMenu() == null) {
			return "redirect:/error";
		}

		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || usuario.getCliente() == null) {
			return "redirect:/login";
		}

		model.addAttribute("restaurante", restaurante);
		model.addAttribute("items", restaurante.getCartaMenu().getItems());
		model.addAttribute("pedido", new Pedido());
		return "realizar_pedido";
	}

	@PostMapping("/realizar")
	public String procesarPedido(@ModelAttribute Pedido pedido, @RequestParam List<Long> itemIds,
			@RequestParam Long restauranteId, @RequestParam String calle, @RequestParam String numero,
			@RequestParam String piso, HttpSession session, Model model) {

		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || usuario.getCliente() == null) {
			return "redirect:/login";
		}

		Cliente cliente = usuario.getCliente();
		Restaurante restaurante = restauranteDAO.findById(restauranteId).orElse(null);

		if (itemIds == null || itemIds.isEmpty() || restaurante == null) {
			model.addAttribute("mensaje", "❌ Debes seleccionar al menos un producto.");
			return "redirect:/cliente/pedido/realizar/" + restauranteId;
		}

		Set<Long> idsUnicos = new HashSet<>(itemIds);
		List<ItemMenu> itemsSeleccionados = restaurante.getCartaMenu().getItems().stream()
				.filter(item -> idsUnicos.contains(item.getId())).toList();

		Pedido nuevoPedido = new Pedido();
		nuevoPedido.setCliente(cliente);
		nuevoPedido.setRestaurante(restaurante);
		nuevoPedido.setCalle(calle);
		nuevoPedido.setNumero(numero);
		nuevoPedido.setPiso(piso);
		nuevoPedido.setPagado(false);
		nuevoPedido.setItemsSeleccionados(new LinkedHashSet<>(itemsSeleccionados));

		pedidoDAO.save(nuevoPedido);
		pedidoDAO.flush();

		return "redirect:/cliente/pedido/pago/" + nuevoPedido.getId();
	}

	@GetMapping("/pago/{pedidoId}")
	public String mostrarPaginaPago(@PathVariable Long pedidoId, Model model) {
		Pedido pedido = pedidoDAO.findById(pedidoId).orElse(null);
		if (pedido == null || pedido.isPagado()) {
			return "redirect:/error";
		}

		model.addAttribute("pedido", pedido);
		return "pago";
	}

	@PostMapping("/pagar/{pedidoId}")
	public String confirmarPago(@PathVariable Long pedidoId, Model model) {
		Pedido pedido = pedidoDAO.findById(pedidoId).orElse(null);
		if (pedido == null || pedido.isPagado()) {
			return "redirect:/error";
		}

		pedido.setPagado(true);

		Repartidor repartidor = seleccionarRepartidorMasEficiente();
		if (repartidor == null) {
			model.addAttribute("mensaje", "❌ No hay repartidores disponibles.");
			return "error";
		}

		ServicioEntrega servicio = new ServicioEntrega();
		servicio.setPuntoA(pedido.getRestaurante().getDireccion());
		servicio.setPuntoB(pedido.getCalle());
		servicio.setRepartidor(repartidor);
		servicio.setEntregado(false);
		servicioEntregaDAO.save(servicio);

		pedido.setServicioEntrega(servicio);
		pedidoDAO.save(pedido);

		model.addAttribute("repartidor", repartidor);
		return "pedido_confirmado";
	}

	@PostMapping("/guardarMetodoPago/{pedidoId}")
	public String guardarMetodoPago(@PathVariable Long pedidoId, @RequestParam String titularTarjeta,
	        @RequestParam String numeroTarjeta, HttpSession session, Model model) {

	    Pedido pedido = pedidoDAO.findById(pedidoId).orElse(null);
	    if (pedido == null) {
	        return "redirect:/error";
	    }

	    Usuario usuario = (Usuario) session.getAttribute("usuario");
	    if (usuario == null || usuario.getId() == null) {
	        return "redirect:/login";
	    }

	    Cliente cliente = pedido.getCliente();
	    if (cliente == null) {
	        model.addAttribute("mensaje", "Cliente no encontrado.");
	        return "redirect:/cliente/pedido/pago/" + pedidoId;
	    }

	    // 🔐 ADVERTENCIA: número de tarjeta almacenado sin cifrado. En producción, cifrar.
	    cliente.setTitularTarjeta(titularTarjeta);
	    try {
	        cliente.setNumeroTarjeta(Long.parseLong(numeroTarjeta));
	    } catch (NumberFormatException e) {
	        model.addAttribute("mensaje", "Número de tarjeta inválido.");
	        return "redirect:/cliente/pedido/pago/" + pedidoId;
	    }

	    // Guarda el cliente con los datos actualizados
	    clienteDAO.save(cliente);

	    model.addAttribute("mensaje", "✅ Método de pago guardado correctamente.");
	    return "redirect:/cliente/pedido/pago/" + pedidoId;
	}
	

	private Repartidor seleccionarRepartidorMasEficiente() {
		return repartidorDAO.findAll().stream().filter(Objects::nonNull)
				.sorted(Comparator.comparingInt(Repartidor::getEficiencia).reversed()).findFirst().orElse(null);
	}
}
