package br.com.bb;

import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import model.Pagamentos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PagamentoServiceTest {

    @InjectMock
    private PagamentoService pagamentoService;

    @Mock
    private Pagamentos pagamentos;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); }

    @Test
    public void testValidaCvv_CvvInvalido() {
        when(pagamentos.getCvv()).thenReturn("12345");
        Exception exception = assertThrows(Exception.class, () -> pagamentoService.validaCvv(pagamentos));
        assertEquals("O cvv informado está em um formato inválido.", exception.getMessage());
}