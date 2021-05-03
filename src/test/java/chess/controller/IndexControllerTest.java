package chess.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = IndexController.class)
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("페이지 출력 - 메인 홈페이지에서 index.html 페이지를 출력한다.")
    @Test
    void returnIndexHtml_success() throws Exception {
        // given
        String viewName = this.mockMvc.perform(get("/"))
                .andReturn()
                .getModelAndView()
                .getViewName();

        // then
        assertThat(viewName).isEqualTo("index.html");
    }
}