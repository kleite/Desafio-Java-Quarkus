package br.com.bb;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import model.Pagamentos;
import model.Panache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PagamentoServiceTest {

    @InjectMock
    private Panache panache; // Mock da dependência Panache

    private PagamentoService pagamentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pagamentoService = new PagamentoService();
        pagamentoService.panache = panache; // Injeção do mock na classe de serviço
    }

    @Test
    public void testSalvarPagamento() throws Exception {
        Pagamentos pagamento = new Pagamentos();
        pagamento.setNumeroCartao("1234-5678-1234-5678");
        pagamento.setCvv("1234");
        pagamento.setValorPagamento(new BigDecimal("100.00"));
        pagamento.setMesVencimento(12);
        pagamento.setAnoVencimento(YearMonth.now().getYear() + 1);
        pagamento.setTipoCliente((byte) 1);
        pagamento.setCpfCnpj("123.456.789-00");

        pagamentoService.salvarPagamento(pagamento);
    }

    @Test
    public void testSalvarPagamentoNumeroCartaoInvalido() {
        Pagamentos pagamento = new Pagamentos();
        pagamento.setNumeroCartao("1234");

        Exception exception = assertThrows(Exception.class, () -> pagamentoService.salvarPagamento(pagamento));
        assertEquals("O número do cartão informado é inválido.", exception.getMessage());
    }

    @Test
    public void testSalvarPagamentoCvvInvalido() {
        Pagamentos pagamento = new Pagamentos();
        pagamento.setNumeroCartao("1234-5678-1234-5678");
        pagamento.setCvv("12345");

        Exception exception = assertThrows(Exception.class, () -> pagamentoService.salvarPagamento(pagamento));
        assertEquals("O cvv informado está em um formato inválido.", exception.getMessage());
    }

}
