package es.uclm.FlashBox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import es.uclm.FlashBox.business.controller.CartaMenuController;
import es.uclm.FlashBox.business.persistence.CartaMenuDAO;
import es.uclm.FlashBox.business.persistence.ItemMenuDAO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CartaMenuController.class)
public class CartaMenuControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartaMenuDAO cartaMenuDAO;

	@MockBean
	private ItemMenuDAO itemMenuDAO;

	@Test
	public void testVistaCartaMenu() throws Exception {
		mockMvc.perform(get("/restaurante/carta")).andExpect(status().isOk());
	}
}
