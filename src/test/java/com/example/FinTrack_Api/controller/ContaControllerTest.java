package com.example.FinTrack_Api.controller;

import com.example.FinTrack_Api.dto.request.conta.DadosAtualizarConta;
import com.example.FinTrack_Api.dto.request.conta.DadosCadastroConta;
import com.example.FinTrack_Api.dto.request.conta.DadosRemoverConta;
import com.example.FinTrack_Api.dto.request.conta.DadosRestaurarConta;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.Usuario;
import com.example.FinTrack_Api.repository.ContaRepository;
import com.example.FinTrack_Api.seguranca.FiltroDeSeguranca;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ContaController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = FiltroDeSeguranca.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
@WithMockUser
class ContaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroConta> jsonCadastroConta;

    @Autowired
    private JacksonTester<DadosAtualizarConta> jsonAtualizarConta;

    @Autowired
    private JacksonTester<DadosRemoverConta> jsonRemoverConta;

    @Autowired
    private JacksonTester<DadosRestaurarConta> jsonRestaurarConta;

    @MockitoBean
    private ContaRepository contaRepository;

    @Test
    void cadastrar() throws Exception {

        var dados = new DadosCadastroConta("Teste");

        mvc.perform(post("/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastroConta.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(contaRepository, times(1)).save(any());
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

        var conta = new Conta();
        conta.setId(1L);
        conta.setNome("Teste");
        conta.setUsuario(usuario);

        when(contaRepository.findByExcluidoFalseAndUsuarioIs(usuario)).thenReturn(List.of(conta));

        mvc.perform(get("/conta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Teste"));

        verify(contaRepository, times(1)).findByExcluidoFalseAndUsuarioIs(usuario);

    }

    @Test
    void atualizar() throws Exception {
        var dados = new DadosAtualizarConta(1L, "Teste");

        var contaMock = mock(Conta.class);

        when(contaRepository.getReferenceById(1L)).thenReturn(contaMock);

        mvc.perform(put("/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizarConta.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(contaMock, times(1)).atualizarInformacoes(dados);

    }

    @Test
    void remover() throws Exception {
        var dados = new DadosRemoverConta(1L);

        var contaMock = mock(Conta.class);

        when(contaRepository.getReferenceById(1L)).thenReturn(contaMock);

        mvc.perform(put("/conta/remover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRemoverConta.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(contaMock, times(1)).remover(dados);
    }

    @Test
    void restaurar() throws Exception {
        var dados = new DadosRestaurarConta(1L);

        var contaMock = mock(Conta.class);

        when(contaRepository.getReferenceById(dados.id())).thenReturn(contaMock);

        mvc.perform(put("/conta/restaurar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRestaurarConta.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(contaMock, times(1)).restaurar(dados);
    }
}