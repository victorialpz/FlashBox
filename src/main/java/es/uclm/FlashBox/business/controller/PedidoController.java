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
                                  @RequestParam String calle,
                                  @RequestParam String numero,
                                  @RequestParam String piso, Model model) {

    	System.out.println(">>> PROCESANDO PEDIDO <<<");
        System.out.println("clienteId: " + clienteId);
        System.out.println("restauranteId: " + restauranteId);
        System.out.println("calle: " + calle + ", numero: " + numero + ", piso: " + piso);
        System.out.println("itemIds: " + itemIds);
        
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

       
        Set<Long> idsUnicos = new HashSet<>(itemIds);
        List<ItemMenu> itemsSeleccionados = restaurante.getCartaMenu().getItems().stream()
            .filter(item -> idsUnicos.contains(item.getId()))
            .toList();

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setCliente(cliente);
        nuevoPedido.setRestaurante(restaurante);
        nuevoPedido.setCalle(calle);
        nuevoPedido.setNumero(numero);
        nuevoPedido.setPiso(piso);
        nuevoPedido.setPagado(false);
        nuevoPedido.setItemsSeleccionados(new LinkedHashSet<>(itemsSeleccionados));

        pedidoDAO.save(nuevoPedido); // aquí sí se genera ID
        pedidoDAO.flush();
        System.out.println("Pedido guardado con ID: " + nuevoPedido.getId());
        return "redirect:/cliente/pedido/pago/" + nuevoPedido.getId();
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
        servicio.setPuntoB(pedido.getCalle());
        servicio.setRepartidor(repartidor);
        servicio.setEntregado(false);

        servicioEntregaDAO.save(servicio);
        pedido.setServicioEntrega(servicio);

        pedidoDAO.save(pedido);

        model.addAttribute("repartidor", repartidor);
        return "pedido_confirmado"; // Vista para confirmar el pedido
    }
    @PostMapping("/guardarMetodoPago/{pedidoId}")
    public String guardarMetodoPago(@PathVariable Long pedidoId, @RequestParam String titularTarjeta, 
                                     @RequestParam String numeroTarjeta, Model model) {
        Pedido pedido = pedidoDAO.findById(pedidoId).orElse(null);
        if (pedido == null) {
            return "redirect:/error";
        }

        Usuario usuario = pedido.getCliente().getUsuario(); // Obtener el usuario asociado al cliente
        if (usuario == null) {
            return "redirect:/error";
        }

        // Asignar los valores de la tarjeta al usuario
        usuario.setTitularTarjeta(titularTarjeta);
        usuario.setNumeroTarjeta(Long.parseLong(numeroTarjeta));

        // Guardar el usuario actualizado en la base de datos
        clienteDAO.save(pedido.getCliente()); // Esto debería guardar el usuario también debido a la relación

        System.out.println("Método de pago guardado:");
        System.out.println("Titular: " + titularTarjeta);
        System.out.println("Número de Tarjeta: " + numeroTarjeta);

        model.addAttribute("mensaje", "✅ Método de pago guardado correctamente.");
        return "redirect:/cliente/pedido/pago/" + pedidoId;
    }
    private Repartidor seleccionarRepartidorMasEficiente() {
        return repartidorDAO.findAll().stream()
                .sorted(Comparator.comparingInt(Repartidor::getEficiencia).reversed())
                .findFirst()
                .orElse(null);
    }
}
