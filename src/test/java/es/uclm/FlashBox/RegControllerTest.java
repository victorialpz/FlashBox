package es.uclm.FlashBox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import es.uclm.FlashBox.business.controller.RegController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegController.class)
public class RegControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testLoginPageLoads() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk());
	}

	@Test
	public void testRegistroPageLoads() throws Exception {
		mockMvc.perform(get("/registro")).andExpect(status().isOk());
	}
}
