// PedidoController.java
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

    @Autowired private RestauranteDAO restauranteDAO;
    @Autowired private ClienteDAO clienteDAO;
    @Autowired private RepartidorDAO repartidorDAO;
    @Autowired private PedidoDAO pedidoDAO;
    @Autowired private ServicioEntregaDAO servicioEntregaDAO;

    @GetMapping("/realizar/{restauranteId}")
    public String mostrarFormularioPedido(@PathVariable Long restauranteId, Model model) {
        Restaurante restaurante = restauranteDAO.findById(restauranteId).orElse(null);
        if (restaurante == null || restaurante.getCartaMenu() == null) {
            return "redirect:/error";
        }

        model.addAttribute("restaurante", restaurante);
        model.addAttribute("items", restaurante.getCartaMenu().getItems());
        model.addAttribute("pedido", new Pedido());
        return "realizar_pedido";
    }

    @PostMapping("/realizar")
    public String procesarPedido(@ModelAttribute Pedido pedido, @RequestParam List<Long> itemIds,
                                  @RequestParam Long clienteId, @RequestParam Long restauranteId,
                                  @RequestParam String direccionEntrega, Model model) {

    	System.out.println(">>> PROCESANDO PEDIDO <<<");
        System.out.println("clienteId: " + clienteId);
        System.out.println("restauranteId: " + restauranteId);
        System.out.println("direccionEntrega: " + direccionEntrega);
        System.out.println("itemIds: " + itemIds);

        if (itemIds == null || itemIds.isEmpty()) {
            model.addAttribute("mensaje", "❌ Debes seleccionar al menos un producto.");
            return "redirect:/cliente/pedido/realizar/" + restauranteId;
        }

        Cliente cliente = clienteDAO.findById(clienteId).orElse(null);
        Restaurante restaurante = restauranteDAO.findById(restauranteId).orElse(null);
        if (cliente == null || restaurante == null) {
            return "redirect:/error";
        }

        // 🔥 Filtrar ítems duplicados y limpiar relación antes de guardar
        Set<Long> idsUnicos = new HashSet<>(itemIds);
        List<ItemMenu> itemsSeleccionados = restaurante.getCartaMenu().getItems().stream()
            .filter(item -> idsUnicos.contains(item.getId()))
            .toList();

        Set<ItemMenu> itemsUnicos = new LinkedHashSet<>(itemsSeleccionados);
       
        pedido.setItemsSeleccionados(itemsUnicos);


        pedido.setItemsSeleccionados(itemsUnicos);

        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setDireccionEntrega(direccionEntrega);
        pedido.setPagado(false); // Pedido aún no pagado

        pedidoDAO.save(pedido);

        // Redirigir a la página de pago
        return "redirect:/cliente/pedido/pago/" + pedido.getId();
    }
    
    @GetMapping("/pago/{pedidoId}")
    public String mostrarPaginaPago(@PathVariable Long pedidoId, Model model) {
        Pedido pedido = pedidoDAO.findById(pedidoId).orElse(null);
        if (pedido == null || pedido.isPagado()) {
            return "redirect:/error";
        }

        model.addAttribute("pedido", pedido);
        return "pago"; // Vista para la página de pago
    }
    @PostMapping("/pagar/{pedidoId}")
    public String confirmarPago(@PathVariable Long pedidoId, Model model) {
        Pedido pedido = pedidoDAO.findById(pedidoId).orElse(null);
        if (pedido == null || pedido.isPagado()) {
            return "redirect:/error";
        }

        // Confirmar el pago
        pedido.setPagado(true);

        // Selección automática de repartidor
        Repartidor repartidor = seleccionarRepartidorMasEficiente();
        ServicioEntrega servicio = new ServicioEntrega();
        servicio.setPuntoA(pedido.getRestaurante().getDireccion());
        servicio.setPuntoB(pedido.getDireccionEntrega());
        servicio.setRepartidor(repartidor);
        servicio.setEntregado(false);

        servicioEntregaDAO.save(servicio);
        pedido.setServicioEntrega(servicio);

        pedidoDAO.save(pedido);

        model.addAttribute("repartidor", repartidor);
        return "pedido_confirmado"; // Vista para confirmar el pedido
    }
    private Repartidor seleccionarRepartidorMasEficiente() {
        return repartidorDAO.findAll().stream()
                .sorted(Comparator.comparingInt(Repartidor::getEficiencia).reversed())
                .findFirst()
                .orElse(null);
    }
}
