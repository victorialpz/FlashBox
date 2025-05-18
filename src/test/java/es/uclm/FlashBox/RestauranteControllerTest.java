package es.uclm.FlashBox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import es.uclm.FlashBox.business.controller.RestauranteController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RestauranteController.class)
public class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private es.uclm.FlashBox.business.persistence.RestauranteDAO restauranteDAO;

    @Test
    public void testVistaRestauranteHome() throws Exception {
        mockMvc.perform(get("/restaurante/home"))
                .andExpect(status().isOk());
    }
}
