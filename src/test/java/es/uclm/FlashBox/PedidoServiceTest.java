package es.uclm.FlashBox;

import es.uclm.FlashBox.business.entity.*;
import es.uclm.FlashBox.business.enums.EstadoPedido;
import es.uclm.FlashBox.business.persistence.PedidoDAO;
import es.uclm.FlashBox.business.persistence.ServicioEntregaDAO;
import es.uclm.FlashBox.business.persistence.RepartidorDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PedidoServiceTest {

	@Autowired
	private PedidoDAO pedidoDAO;

	@Autowired
	private RepartidorDAO repartidorDAO;

	@Autowired
	private ServicioEntregaDAO servicioEntregaDAO;

	private Cliente cliente;
	private Restaurante restaurante;

	@BeforeEach
	public void setup() {
		cliente = new Cliente("Rodrigo", "Test", "rodrigo@test.com");
		restaurante = new Restaurante();
		restaurante.setNombre("La Cuchara Rebelde");
	}

	@Test
	public void testCrearPedidoConDatosBasicos() {
		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setCalle("Avenida Europa");
		pedido.setNumero("45");
		pedido.setPiso("3A");
		pedido.setEstado(EstadoPedido.PENDIENTE_PAGO);
		pedido.setPagado(false);

		Set<ItemMenu> items = new HashSet<>();
		pedido.setItemsSeleccionados(items);

		pedidoDAO.save(pedido);

		Optional<Pedido> guardado = pedidoDAO.findById(pedido.getId());

		assertTrue(guardado.isPresent());
		assertEquals(EstadoPedido.PENDIENTE_PAGO, guardado.get().getEstado());
		assertEquals("45", guardado.get().getNumero());
	}

	@Test
	public void testTransicionDeEstadoDelPedido() {
		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setEstado(EstadoPedido.PENDIENTE_PAGO);
		pedidoDAO.save(pedido);

		pedido.setEstado(EstadoPedido.PREPARANDO);
		pedidoDAO.save(pedido);

		Pedido actualizado = pedidoDAO.findById(pedido.getId()).orElseThrow();
		assertEquals(EstadoPedido.PREPARANDO, actualizado.getEstado());
	}

	@Test
	public void testAsignarServicioEntregaAlPedido() {
		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setEstado(EstadoPedido.PREPARANDO);
		pedidoDAO.save(pedido);

		Repartidor r = new Repartidor("Laura", "Martín","laura@gmail.com", 80);
		repartidorDAO.save(r);

		ServicioEntrega entrega = new ServicioEntrega();
		entrega.setRepartidor(r);
		entrega.setPedido(pedido);
		servicioEntregaDAO.save(entrega);

		pedido.setServicioEntrega(entrega);
		pedidoDAO.save(pedido);

		Pedido resultado = pedidoDAO.findById(pedido.getId()).orElse(null);
		assertNotNull(resultado);
		assertNotNull(resultado.getServicioEntrega());
		assertEquals("Laura", resultado.getServicioEntrega().getRepartidor().getNombre());
	}
}