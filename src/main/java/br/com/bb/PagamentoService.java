package br.com.bb;

import io.quarkus.arc.ArcContainer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import model.Pagamentos;
import model.Panache;
import java.time.YearMonth;
import java.util.List;
import org.jboss.logging.Logger;
import java.math.BigDecimal;

@ApplicationScoped
public class PagamentoService {

    private static final byte TIPO_CLIENTE_CPF = 1;
    private static final byte TIPO_CLIENTE_CNPJ = 2;
    private static final Logger log = Logger.getLogger(PagamentoService.class);

    @Inject
    Panache panache;

    @Transactional
    public void salvarPagamento(Pagamentos pagamentos) throws Exception {

        validaCvv(pagamentos);
        validaMesVencimento(pagamentos);
        validaAnoVencimento(pagamentos);
        validaVencimento(pagamentos);
        validaTipoCliente(pagamentos);
        validaNumeroCartao(pagamentos);

        formataNumeroCartao(pagamentos);

            // Validacao do valor do pagamento




            // Validacao do campo cpf_cnpj
            if (pagamentos.getCpfCnpj() == null || pagamentos.getCpfCnpj().isEmpty()) {
                String mensagemDeErro = "Forneça um CPF (tipo do cliente = 1) ou um CNPJ (tipo do cliente = 2).";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            // Se o tipo do cliente for 1 (CPF)
            if (pagamentos.getTipoCliente() == TIPO_CLIENTE_CPF) {
                if (!validarCPF(pagamentos.getCpfCnpj())) {
                    String mensagemDeErro = "CPF em formato inválido. Forneça um CPF no formato XXX.XXX.XXX-XX";
                    log.error(mensagemDeErro);
                    throw new Exception(mensagemDeErro);
                }

                pagamentos.setCpfCnpj(formatarCPF(pagamentos.getCpfCnpj()));
            }

            // Se o tipo do cliente for 2 (CNPJ)
            else if (pagamentos.getTipoCliente() == TIPO_CLIENTE_CNPJ) {
                if (!validarCNPJ(pagamentos.getCpfCnpj())) {
                    String mensagemDeErro = "CNPJ em formato inválido. Forneça um CNPJ no formato XX.XXX.XXX/XXXX-XX";
                    log.error(mensagemDeErro);
                    throw new Exception(mensagemDeErro);
                }

                pagamentos.setCpfCnpj(formatarCNPJ(pagamentos.getCpfCnpj()));
            }

            panache.persist(pagamentos);
    }

    // Metodos
    private void validaCvv(Pagamentos pagamentos) throws Exception{
        if (pagamentos.getCvv() == null || !pagamentos.getCvv().matches("\\d{4}")) {
            String mensagemDeErro = "O cvv informado está em um formato inválido.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }
    }

    private void validaMesVencimento(Pagamentos pagamentos) throws Exception{
        if (pagamentos.getMesVencimento() == null) {
            String mensagemDeErro = "O mês do vencimento não pode está vazio.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }
        if ((pagamentos.getMesVencimento() < 1 || pagamentos.getMesVencimento() > 12)) {
            String mensagemDeErro = "O mês do vencimento informado é inválido. Forneça um número de 1 a 12.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }
    }

    private void validaAnoVencimento(Pagamentos pagamentos) throws Exception{
        if (pagamentos.getAnoVencimento() == null) {
            String mensagemDeErro = "O ano do vencimento não pode está vazio.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }
        if (!String.valueOf(pagamentos.getAnoVencimento()).matches("\\d{4,}")) {
            String mensagemDeErro = "O ano do vencimento informado é inválido. Forneça um ano no formato XXXX.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }
    }

    private void validaVencimento(Pagamentos pagamentos) throws Exception{
        YearMonth mesAnoAtual = YearMonth.now();
        YearMonth mesAnoValidade = YearMonth.of(pagamentos.getAnoVencimento(),
                pagamentos.getMesVencimento());

        if (mesAnoValidade.isBefore(mesAnoAtual)) {
            String mensagemDeErro = "O cartão está fora da validade.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }
    }

    private void validaTipoCliente(Pagamentos pagamentos) throws Exception{
        if (pagamentos.getTipoCliente() == null) {
            String mensagemDeErro = "O campo tipo do cliente não pode ser vazio. Preencha 1 para CPF e 2 para CNPJ.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }

        if (pagamentos.getTipoCliente() != TIPO_CLIENTE_CPF && pagamentos.getTipoCliente() != TIPO_CLIENTE_CNPJ) {
            String mensagemDeErro = "Tipo de cliente inválido. Preencha 1 para CPF e 2 para CNPJ.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }
    }

    private boolean validarCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{11}")
                || cpf.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
    }

    private boolean validarCNPJ(String cnpj) {
        return cnpj != null && cnpj.matches("\\d{14}")
                || cnpj.matches("^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$");
    }

    private String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarCNPJ(String cnpj) {
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    private void validaNumeroCartao(Pagamentos pagamentos) throws Exception {
        if (pagamentos.getNumeroCartao() == null
                || !pagamentos.getNumeroCartao().matches("^\\d{4}(-|\\s)?\\d{4}(-|\\s)?\\d{4}(-|\\s)?\\d{4}$")
                || pagamentos.getNumeroCartao().replaceAll("[-\\s]", "").length() > 19) {
            String mensagemDeErro = "O número do cartão informado é inválido.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }
    }

    private void formataNumeroCartao(Pagamentos pagamentos){
            String numeroCartaoFormatado = pagamentos.getNumeroCartao()
                    .replaceAll("[-\\s]",
                            "");
            numeroCartaoFormatado = numeroCartaoFormatado.replaceAll("(\\d{4})(?=\\d)",
                    "$1-");
            pagamentos.setNumeroCartao(numeroCartaoFormatado);
        }

        private void validaValorPagamento (Pagamentos pagamentos) throws Exception{
        if (pagamentos.getValorPagamento() == null
                || pagamentos.getValorPagamento().compareTo(BigDecimal.ZERO) == 0) {
            String mensagemDeErro = "O valor do pagamento não pode ser vazio ou nulo.";
            log.error(mensagemDeErro);
            throw new Exception(mensagemDeErro);
        }

        String valorPagamentoString = pagamentos.getValorPagamento()
                .toPlainString();

            if (!valorPagamentoString.matches("\\d+\\.\\d{2}")) {
                String mensagemDeErro = "O valor do pagamento deve ser um número positivo com duas casas decimais separadas por um ponto";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            pagamentos.setValorPagamento(new BigDecimal(valorPagamentoString));
    }

    public List<Pagamentos> obterPagamentos () {
        return panache.listAll();
    }
}