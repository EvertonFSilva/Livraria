# Restrições de Negócio

---
## Restrições e regras de negócio

- Atributos em geral:
  - Aceita valor nulo ou não? 
      - _Apenas telefone (Pessoa) pode ser nulo_
  - Será permitido alteração futura?
    - _Apenas CPF (Pessoa) não pode alterar_
  - Deverá ser único?
    - _Apenas CPF (Pessoa), cargo (Funcionario), titulo (Livro) e id (Usuario / Livro / Pedido)_
  - Padrão (regex)-> um formato específico com CPF, CEP, CNPJ.
    - _Apenas CPF (Pessoa)_ 
- Atributos do tipo texto:
  - Quantidade mínima e máxima de caracteres
    - _login (Usuario)= Mínima: 3; Máxima: 30_
    - _senha (Usuario)= Mínima: 6; Máxima: 30_
    - _cpf (Pessoa)= Mínima: 11; Máxima: 14_
	- _nome (Pessoa)= Mínima: 3; Máxima: 60_
    - _email (Pessoa)= Mínima: 10; Máxima: 60_
    - _telefone (Pessoa)= Mínima: 9; Máxima: 16_
    - _endereco (Pessoa)= Mínima: 10; Máxima: 100_
	- _cargo (Funcionario)= Mínima: 5; Máxima: 30_
    - _titulo (Livro)= Mínima: 1; Máxima: 60_
    - _genero (Livro)= Mínima: 1; Máxima: 20_
    - _autor (Livro)= Mínima: 15; Máxima: 60_
	- _formaPagamento (Item)= Mínima: 3; Máxima: 20_
	- _tipo (Item)= Mínima: 3; Máxima: 20_	
  - Aceita valores espaço em branco ou não?
    - _Todos aceitam, exceto os mencionados como únicos_ 
- Atributos numéricos:
  - Aceita valores negativos, positivos ou indiferente?
    - _Todos os atributos numéricos são positivos_
  - Faixa de valores permitidos
    - _id (Usuario / Livro / Pedido / Aluguel, FormaPagamento)= 0 até 2147483647_
    - _salario (Funcionario)= 1320 até 6000_
    - _permissao (Usuario)= 1 até 10_
    - _valorTotal (Pedido)= 1 até 9999_
    - _quantidade (Item)= 1 até 20_
    - _precoUnitario (Item)= 1 até 200_
    - _qtdPaginas (Livro)= 10 até 3000_
    - _precoVenda (Livro)= 1 até 500_
    - _precoAluguel (Livro)= 1 até 200_
  - Valores multiplos de X. Ex: 10,20,30...
    - _não_
- Atributos de coleção:
  - Mínimo ou máximo de elementos
    - _telefones (Pessoa)= Mínimo: 1; Máximo: 2 (exemplo: cada pessoa pode ter no mínimo 1 e no máximo 2 números de telefone)_
    - _itensPedido (Pedido)= Mínimo: 0; Máximo: 99 (exemplo: um pedido pode conter de 0 a 99 itens)_
  - Atributos de relacionamento entre classes
    - _ _
  - Objeto A deve estar relacionamento com o objeto B
    - _ _
  - Número mínimo ou máximo de relacionamentos
    - _ _
  - Objeto A deve estar relacionado com objeto B que possua em seu atributo X o valor Y
    - _ _
  - Restrições de Classe (que envolvem mais de 1 atributo)
    - _ _
  - A soma do valor de atributo A com o valor do atributo B deve ser igual a X
    - _ _
  - Se atributo A possui o valor X, o atributo B deve estar preenchido com valor Y
    - _ _

---
## Requisitos

- A aplicação deve gerenciar pelo menos 2 entidades distintas e que se relacionam.
- Futuramente será implementando segurança com sistema de login, adicione no diagrama a classe que representará o usuário do sistema.
