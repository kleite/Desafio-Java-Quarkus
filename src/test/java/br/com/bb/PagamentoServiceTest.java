package br.com.bb;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import model.Pagamentos;
import model.Panache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PagamentoServiceTest {

    @InjectMock
    private Panache panache;
    private PagamentoService pagamentoService;

    @BeforeEach
    public void setUp() {
        pagamentoService = new PagamentoService();
        pagamentoService.panache = panache;
    }

    private Pagamentos criaPagamentoPadrao() {
        Pagamentos pagamento = new Pagamentos();
        pagamento.setNumeroCartao("1234-5678-1234-5678");
        pagamento.setCvv("1234");
        pagamento.setValorPagamento(new BigDecimal("100.00"));
        pagamento.setMesVencimento(12);
        pagamento.setAnoVencimento(YearMonth.now().getYear() + 1);
        pagamento.setTipoCliente((byte) 1);
        pagamento.setCpfCnpj("123.456.789-00");
        return pagamento;
    }

    @Test
    public void testSalvarPagamento() throws Exception{
        Pagamentos pagamento = criaPagamentoPadrao();
        pagamentoService.salvarPagamento(pagamento);
        verify(panache).persist(pagamento);
    }

    @Test
    public void testSalvarPagamentoNumeroCartaoMenor() {
        Pagamentos pagamento = criaPagamentoPadrao();
        pagamento.setNumeroCartao("1234");
        Exception exception = assertThrows(Exception.class, () -> pagamentoService.salvarPagamento(pagamento));
        assertEquals("O número do cartão informado é inválido.", exception.getMessage());
    }

    @Test
    public void testaSalvarPagamentoCvvInvalido() {
        Pagamentos pagamento = criaPagamentoPadrao();
        pagamento.setCvv("12345");
        Exception exception = assertThrows(Exception.class, () -> pagamentoService.salvarPagamento(pagamento));
        assertEquals("O cvv informado está em um formato inválido.", exception.getMessage());
    }

    @Test
    public void testaMesVencimento() {
        Pagamentos pagamento = criaPagamentoPadrao();
        pagamento.setMesVencimento(Integer.parseInt("13"));
        Exception exception = assertThrows(Exception.class, () -> pagamentoService.salvarPagamento(pagamento));
        assertEquals("O mês do vencimento informado é inválido. Forneça um número de 1 a 12.", exception.getMessage());
    }

    @Test
    public void testaAnoVencimento() {
        Pagamentos pagamento = criaPagamentoPadrao();
        pagamento.setAnoVencimento(Integer.parseInt("202"));
        Exception exception = assertThrows(Exception.class, () -> pagamentoService.salvarPagamento(pagamento));
        assertEquals("O ano do vencimento informado é inválido. Forneça um ano no formato XXXX.", exception.getMessage());
    }

    @Test
    public void testaVencimentoCartao() {
        Pagamentos pagamento = criaPagamentoPadrao();
        pagamento.setMesVencimento(Integer.parseInt("10"));
        pagamento.setAnoVencimento(Integer.parseInt("2024"));
        Exception exception = assertThrows(Exception.class, () -> pagamentoService.salvarPagamento(pagamento));
        assertEquals("O cartão está fora da validade.", exception.getMessage());
    }

    @Test
    public void testaVencimentoCartao2() {
        Pagamentos pagamento = criaPagamentoPadrao();
        LocalDate MesAnteriorAnoAtual = LocalDate.now().minusMonths(1);
        pagamento.setMesVencimento(MesAnteriorAnoAtual.getMonthValue());
        pagamento.setAnoVencimento(MesAnteriorAnoAtual.getYear());
        Exception exception = assertThrows(Exception.class, () -> pagamentoService.salvarPagamento(pagamento));
        assertEquals("O cartão está fora da validade.", exception.getMessage());
    }
}
