package chess.controller.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(controllers = MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 메인_페이지를_리턴한다() throws Exception {
        String viewName = this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andReturn()
                .getModelAndView()
                .getViewName();
        assertThat(viewName).isEqualTo("index.html");
    }
}