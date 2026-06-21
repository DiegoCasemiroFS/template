package api.com.template.controller;

import api.com.template.TestFixtures;
import api.com.template.security.TokenProvider;
import api.com.template.security.UsersDetailsService;
import api.com.template.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Teste da camada web do UsuarioController. Sobe so o controller, com o service
 * mockado e os filtros de seguranca desligados para focar no endpoint.
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private UsersDetailsService usersDetailsService;

    @Test
    void shouldReturnUserLoggedData() throws Exception {
        when(service.getUserLogged()).thenReturn(TestFixtures.user());

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("maria@teste.com"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }
}
