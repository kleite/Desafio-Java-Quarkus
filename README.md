# Desafio de Programação em Java Quarkus - Microsserviço de Recebimento e Armazenamento de Pagamento com Cartão de Crédito
 
Descrição: Você foi designado para desenvolver um microsserviço em Java Quarkus que recebe dados de um pagamento fictício realizado com cartão de crédito e armazena essas informações. O microsserviço deve validar se os dados recebidos estão no formato correto e, em seguida, persistir essas informações em um banco de dados relacional. Além disso, o microsserviço deve fornecer um endpoint HTTP para receber os dados do pagamento e um mecanismo para consulta posterior desses dados armazenados.
Requisitos Técnicos:
 
Utilize o framework Quarkus para criar o microsserviço Java.
Crie uma função que receba os dados do pagamento (em formato JSON) e valide se eles estão no formato esperado antes de prosseguir.
Utilize um banco de dados relacional (por exemplo, PostgreSQL ou MySQL) para armazenar os dados do pagamento. Crie uma tabela adequada para armazenar essas informações.
Implemente um endpoint HTTP (por exemplo, utilizando JAX-RS) que permita o envio dos dados do pagamento para o microsserviço.
Forneça uma funcionalidade adicional para consultar os dados de pagamento armazenados no banco de dados.
Instruções:
O candidato deve criar um repositório Git para o projeto e fornecer o link quando concluir. (sugestão: GitHub)
Certifique-se de que os dados de pagamento sejam armazenados corretamente no banco de dados após a validação.
Opcional:
O candiado poderá entregar configurações para rodar o microsserviço em container (Dockerfile, docker-compose...)
O candidato poderá disponibilizar no código a geração de uma métrica Prometheus através de um endpoint com a assinatura `/metrics`. Pode ser qualquer tipo de métrica (contador, histogram, summary ou gauge)  para ser aplicado nos próprios endpoints implementados.

Sugestão de dados do pagamento:
    - Nº do Pagamento (sequencial, auto-incrementável)
    - Nº do cartão (String, máximo de 19 dígitos, aceitar com e sem "-")
    - Tipo de Pessoa (Numérico, 1 posição - domínio 1 para CPF, 2 para CNPJ)
    - CPF/CNPJ do Cliente (máscara padrão)
    - Mês vencimento cartão (numérico, 1 a 12)
    - Ano vencimento cartão (numérico, ano com 4 posições)
    - CVV (String, 4 posições)
    - Valor do Pagamento (decimal, 2 casas)
