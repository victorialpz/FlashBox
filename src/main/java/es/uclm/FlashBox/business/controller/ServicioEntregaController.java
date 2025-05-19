package es.uclm.FlashBox.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import es.uclm.FlashBox.business.entity.Pedido;
import es.uclm.FlashBox.business.entity.ServicioEntrega;
import es.uclm.FlashBox.business.entity.Usuario;
import es.uclm.FlashBox.business.persistence.ServicioEntregaDAO;
import jakarta.servlet.http.HttpSession;
import es.uclm.FlashBox.business.entity.Cliente;
import es.uclm.FlashBox.business.entity.Repartidor;

@Controller
@RequestMapping("/repartidor")
public class ServicioEntregaController {

	@Autowired
	private ServicioEntregaDAO servicioEntregaDAO;

	@GetMapping("/entregas")
	public String verEntregas(HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		if (usuario == null || usuario.getRepartidor() == null) {
			System.out.println("Usuario no válido o no tiene un repartidor asociado");
			return "redirect:/login";
		}

		Repartidor repartidor = usuario.getRepartidor();
		System.out.println("Repartidor ID: " + repartidor.getId());

		List<ServicioEntrega> entregas = servicioEntregaDAO.findByRepartidor(repartidor);
		System.out.println("Entregas encontradas: " + entregas.size());

		for (ServicioEntrega e : entregas) {
			System.out.println(" Entrega ID: " + e.getId());

			if (e.getPedido() != null && e.getPedido().getCliente() != null) {
				Pedido pedido = e.getPedido();
				Cliente cliente = (Cliente) pedido.getCliente();
				String nombreCliente = cliente.getUsuario() != null ? cliente.getUsuario().getNombre() : "Desconocido";
				System.out.println(" → Pedido: " + pedido.getId());
				System.out.println(" → Cliente: " + nombreCliente);
			}
		}

		model.addAttribute("entregas", entregas);
		return "entregas_repartidor";
	}

	@PostMapping("/entregas/{id}/recogido")
	public String marcarComoRecogido(@PathVariable Long id) {
		servicioEntregaDAO.findById(id).ifPresent(servicio -> {
			servicio.setRecogido(true);
			servicioEntregaDAO.save(servicio);
		});
		return "redirect:/repartidor/entregas";
	}

	@PostMapping("/entregas/{id}/entregado")
	public String marcarComoEntregado(@PathVariable Long id) {
		servicioEntregaDAO.findById(id).ifPresent(servicio -> {
			servicio.setEntregado(true);
			servicioEntregaDAO.save(servicio);
		});
		return "redirect:/repartidor/entregas";
	}
}
