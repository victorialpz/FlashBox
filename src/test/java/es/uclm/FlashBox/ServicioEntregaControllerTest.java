package es.uclm.FlashBox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import es.uclm.FlashBox.business.controller.ServicioEntregaController;
import es.uclm.FlashBox.business.persistence.ServicioEntregaDAO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServicioEntregaController.class)
public class ServicioEntregaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ServicioEntregaDAO servicioEntregaDAO;

	@Test
	public void testVistaEntregasPendientes() throws Exception {
		mockMvc.perform(get("/repartidor/entregas")).andExpect(status().isOk());
	}
}
