package chess.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = IndexController.class)
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

//    @Test
//    void returnRoomHtml_success() throws Exception {
//        // given
//        String viewName = this.mockMvc.perform(get("/room"))
//                .andReturn()
//                .getModelAndView()
//                .getViewName();
//
//        // then
//        assertThat(viewName).isEqualTo("room.html");
//    }
}