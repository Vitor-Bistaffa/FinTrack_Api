package com.example.FinTrack_Api.controller;

import com.example.FinTrack_Api.dto.request.transacao.DadosAtualizarTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosCadastroTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosListagemTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosTotalMes;
import com.example.FinTrack_Api.model.Categoria;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.Transacao;
import com.example.FinTrack_Api.model.enums.TipoTransacao;
import com.example.FinTrack_Api.repository.CategoriaRepository;
import com.example.FinTrack_Api.repository.ContaRepository;
import com.example.FinTrack_Api.repository.TransacaoRepository;
import com.example.FinTrack_Api.seguranca.FiltroDeSeguranca;
import com.example.FinTrack_Api.seguranca.TokenService;
import com.example.FinTrack_Api.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransacaoController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = FiltroDeSeguranca.class)
        }
)
@AutoConfigureJsonTesters
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
class TransacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroTransacao> jsonCadastroTransacao;

    @Autowired
    private JacksonTester<DadosAtualizarTransacao> jsonAtualizarTransacao;

    @MockitoBean
    private TransacaoRepository transacaoRepository;

    @MockitoBean
    private ContaRepository contaRepository;

    @MockitoBean
    private CategoriaRepository categoriaRepository;

    @MockitoBean
    private TransacaoService transacaoService;

    @MockitoBean
    private TokenService tokenService;


    @Test
    void cadastrar() throws Exception {

        var conta = new Conta();
        conta.setId(1L);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        var categoria = new Categoria();
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        var dados = new DadosCadastroTransacao(1L, 1L, new BigDecimal("100.00"), "Teste", "Teste", TipoTransacao.Despesa, LocalDate.now(), 1);

        mvc.perform(post("/transacao").contentType(MediaType.APPLICATION_JSON).content(jsonCadastroTransacao.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(transacaoRepository, times(1)).save(any());
    }

    @Test
    void listar() throws Exception {

        Conta conta = new Conta();
        conta.setId(1L);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        var lista = List.of(new DadosListagemTransacao(1L, conta, categoria, new BigDecimal("100.00"), "Teste", "Salário", "Despesa", LocalDate.now(), "1/1"));

        when(transacaoService.listarTransacoes(null)).thenReturn(lista);


        mvc.perform(get("/transacao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Salário"));

        verify(transacaoService, times(1)).listarTransacoes(null);
    }

    @Test
    void obterTotaisMensais() throws Exception {

        List<DadosTotalMes> lista = new ArrayList<>();

        var valor = 100;
        for (int i = 1; i <= 12; i++) {

            DadosTotalMes mes = new DadosTotalMes(i, new BigDecimal(valor));
            lista.add(mes);
            valor += 100;
        }

        when(transacaoService.calcularTotaisMensais(TipoTransacao.Despesa, 2025)).thenReturn(lista);

        mvc.perform(get("/transacao/total")
                        .param("tipo", "Despesa")
                        .param("ano", "2025")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].total").value(100))
                .andExpect(jsonPath("$[11].total").value(1200));

        verify(transacaoService, times(1)).calcularTotaisMensais(TipoTransacao.Despesa, 2025);
    }

    @Test
    void atualizar() throws Exception {
        Conta conta = new Conta();
        conta.setId(1L);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        var dados = new DadosAtualizarTransacao(1L, conta, categoria, new BigDecimal("100.00"), "Teste", "Teste", TipoTransacao.Despesa, LocalDate.now(), 1);

        var transacaoMock = mock(Transacao.class);
        when(transacaoRepository.getReferenceById(1L)).thenReturn(transacaoMock);


        mvc.perform(put("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizarTransacao.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(transacaoMock, times(1)).atualizarInformacoes(dados, conta, categoria);
    }

    @Test
    void deletar() throws Exception {

        Conta conta = new Conta();
        conta.setId(1L);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        var dados = new DadosAtualizarTransacao(1L, conta, categoria, new BigDecimal("100.00"), "Teste", "Teste", TipoTransacao.Despesa, LocalDate.now(), 1);

        mvc.perform(delete("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizarTransacao.write(dados).getJson()))
                .andExpect(status().isOk());

        verify(transacaoRepository, times(1)).deleteById(dados.id());


    }
}
