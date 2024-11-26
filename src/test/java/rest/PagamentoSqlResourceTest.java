package rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Pagamentos;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PagamentoSqlResourceTest {

    String entrada = "{\n" +
            "      \"numero_cartao\": \"9876-5432-1234-8765\",\n" +
            "      \"tipo_cliente\": 1,\n" +
            "      \"cpf_cnpj\": \"987.654.321-00\",\n" +
            "      \"mes_vencimento\": 5,\n" +
            "      \"ano_vencimento\": 2024,\n" +
            "      \"cvv\": \"3789\",\n" +
            "      \"valor_pagamento\": 58.99\n" +
            "    }";

    @BeforeEach
    void setUp() {
    }

    @Test
    void criarPagamento() {
        Pagamentos pagamentos = new Pagamentos();
        assertEquals("9876-5432-1234-8765", pagamentos.getNumeroCartao());
    }

    @Test
    void obterPagamentos() {
    }
}