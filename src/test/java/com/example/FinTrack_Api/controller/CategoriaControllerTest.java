package com.example.FinTrack_Api.controller;

import com.example.FinTrack_Api.dto.request.categoria.DadosAtualizarCategoria;
import com.example.FinTrack_Api.dto.request.categoria.DadosCadastroCategoria;
import com.example.FinTrack_Api.dto.request.categoria.DadosRemoverCategoria;
import com.example.FinTrack_Api.dto.request.categoria.DadosRestaurarCategoria;
import com.example.FinTrack_Api.model.Categoria;
import com.example.FinTrack_Api.model.Usuario;
import com.example.FinTrack_Api.repository.CategoriaRepository;
import com.example.FinTrack_Api.seguranca.FiltroDeSeguranca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoriaController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = FiltroDeSeguranca.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
@WithMockUser
class CategoriaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroCategoria> jsonCadastroCategoria;

    @Autowired
    private JacksonTester<DadosAtualizarCategoria> jsonAtualizarCategoria;

    @Autowired
    private JacksonTester<DadosRemoverCategoria> jsonRemoverCategoria;

    @Autowired
    private JacksonTester<DadosRestaurarCategoria> jsonRestaurarCategoria;

    @MockitoBean
    private CategoriaRepository categoriaRepository;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void cadastrar() throws Exception {

        var dados = new DadosCadastroCategoria("Teste");

        mvc.perform(post("/categoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastroCategoria.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(categoriaRepository, times(1)).save(any());
    }


    @Test
    void listar() throws Exception {

        var usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("admin");
        usuario.setSenha("admin");

        var authentication = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Teste");
        categoria.setUsuario(usuario);

        when(categoriaRepository.findByExcluidoFalseAndUsuarioIs(usuario))
                .thenReturn(List.of(categoria));

        mvc.perform(get("/categoria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Teste"));

        verify(categoriaRepository).findByExcluidoFalseAndUsuarioIs(usuario);
    }

    @Test
    void atualizar() throws Exception {
        var dados = new DadosAtualizarCategoria(1L, "Teste");

        var categoriaMock = mock(Categoria.class);

        when(categoriaRepository.getReferenceById(1L)).thenReturn(categoriaMock);

        mvc.perform(put("/categoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizarCategoria.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(categoriaMock, times(1)).atualizarInformacoes(dados);
    }

    @Test
    void remover() throws Exception {
        var dados = new DadosRemoverCategoria(1L);

        var categoriaMock = mock(Categoria.class);

        when(categoriaRepository.getReferenceById(1L)).thenReturn(categoriaMock);

        mvc.perform(put("/categoria/remover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRemoverCategoria.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(categoriaMock, times(1)).remover(dados);
    }

    @Test
    void restaurar() throws Exception {
        var dados = new DadosRestaurarCategoria(1L);

        var categoriaMock = mock(Categoria.class);

        when(categoriaRepository.getReferenceById(1L)).thenReturn(categoriaMock);

        mvc.perform(put("/categoria/restaurar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRestaurarCategoria.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(categoriaMock, times(1)).restaurar(dados);
    }
}