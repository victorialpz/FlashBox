package es.uclm.FlashBox;

import es.uclm.FlashBox.business.entity.Repartidor;
import es.uclm.FlashBox.business.persistence.RepartidorDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AsignacionRepartidorTest {

	@Autowired
	private RepartidorDAO repartidorDAO;

	@Test
	public void testAsignarRepartidorMasEficiente() {
		Repartidor r1 = new Repartidor("Ana", "Pérez", "ana@test.com", 75);
		Repartidor r2 = new Repartidor("Luis", "Gómez", "luis@test.com", 90);
		Repartidor r3 = new Repartidor("Marta", "Sánchez", "marta@test.com", 50);

		repartidorDAO.saveAll(List.of(r1, r2, r3));

		List<Repartidor> disponibles = repartidorDAO.findAll();

		Repartidor mejor = disponibles.stream().max((a, b) -> Integer.compare(a.getEficiencia(), b.getEficiencia()))
				.orElse(null);

		assertNotNull(mejor);
		assertEquals("Luis", mejor.getNombre());
		assertEquals(90, mejor.getEficiencia());
	}
}
