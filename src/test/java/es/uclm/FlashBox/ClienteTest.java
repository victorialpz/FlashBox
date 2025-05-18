package es.uclm.FlashBox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClienteTest.class)
public class ClienteTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private es.uclm.FlashBox.business.persistence.ClienteDAO clienteDAO;

	@Test
	public void testVistaClienteHome() throws Exception {
		mockMvc.perform(get("/cliente/home")).andExpect(status().isOk());
	}
}
