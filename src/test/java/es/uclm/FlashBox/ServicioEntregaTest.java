package es.uclm.FlashBox;

import es.uclm.FlashBox.business.entity.*;
import es.uclm.FlashBox.business.persistence.ServicioEntregaDAO;
import es.uclm.FlashBox.business.persistence.PedidoDAO;
import es.uclm.FlashBox.business.persistence.RepartidorDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ServicioEntregaTest {

	@Autowired
	private ServicioEntregaDAO servicioEntregaDAO;

	@Autowired
	private PedidoDAO pedidoDAO;

	@Autowired
	private RepartidorDAO repartidorDAO;

	private Pedido pedido;
	private Repartidor repartidor;

	@BeforeEach
	public void setup() {
		pedido = new Pedido();
		pedido.setCalle("Calle Real");
		pedido.setNumero("10");
		pedido.setPiso("1A");
		pedidoDAO.save(pedido);

		repartidor = new Repartidor("Carlos", "Ramírez", "Carlos@gmail.com",85);
		repartidorDAO.save(repartidor);
	}

	@Test
	public void testGuardarYRecuperarServicioEntrega() {
		ServicioEntrega entrega = new ServicioEntrega();
		entrega.setPedido(pedido);
		entrega.setRepartidor(repartidor);

		servicioEntregaDAO.save(entrega);

		ServicioEntrega encontrada = servicioEntregaDAO.findById(entrega.getId()).orElse(null);

		assertNotNull(encontrada);
		assertEquals("Carlos", encontrada.getRepartidor().getNombre());
		assertEquals("Calle Real", encontrada.getPedido().getCalle());
	}
}