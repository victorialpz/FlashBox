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
@WebMvcTest(RepartidorTest.class)
public class RepartidorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private es.uclm.FlashBox.business.persistence.RepartidorDAO repartidorDAO;

    @Test
    public void testVistaRepartidorHome() throws Exception {
        mockMvc.perform(get("/repartidor/home"))
                .andExpect(status().isOk());
    }
}
