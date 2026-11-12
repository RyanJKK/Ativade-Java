# Sistema Valiantus — Projeto Acadêmico

**Integrantes:** Diego Carlos Raimundo Silva e Bruno Ryan Pacas Veras.

Sistema de demonstração acadêmica inspirado no contexto de uma empresa de proteção veicular.
Não é um produto oficial da Valiantus e não deve ser utilizado com dados pessoais reais.

## Requisitos
- IntelliJ IDEA
- JDK 17 ou 21
- Internet na primeira execução para o Maven baixar o driver SQLite JDBC

## Como abrir
1. Extraia o ZIP para uma pasta do computador.
2. Abra o IntelliJ IDEA e escolha **Open**.
3. Selecione a pasta `ValiantusSistema` (a que contém `pom.xml`).
4. Confirme a importação do Maven e espere o download das dependências.
5. Em **Project Structure > Project SDK**, selecione JDK 17 ou 21.
6. Abra `src/main/java/br/com/valiantus/Main.java`.
7. Clique no triângulo verde ao lado de `main` e em **Run**.

## Primeiro acesso
Usuário: `admin`
Senha inicial: `Admin@123`

A conta de demonstração é criada somente quando a tabela de usuários está vazia.
A senha é armazenada como hash PBKDF2 com sal aleatório, e não como texto puro.
**Atenção:** esta conta inicial é apenas para apresentação local; não use em produção.

## Onde fica o banco
O SQLite cria o arquivo `valiantus.db` na pasta `.valiantus-academico` do diretório pessoal do usuário.
Ele é mantido mesmo após fechar o programa.

## Teste manual do CRUD
1. Entre com o usuário de demonstração.
2. Abra Clientes e cadastre um cliente fictício com CPF de teste matematicamente válido.
3. Pesquise o cliente pelo nome, selecione a linha e altere o telefone.
4. Abra Veículos, clique em "Atualizar clientes" e escolha o cliente cadastrado.
5. Cadastre um veículo com placa fictícia no formato `ABC1D23`.
6. Pesquise, selecione e altere o veículo.
7. Tente excluir o cliente com veículo associado: o banco deve impedir.
8. Exclua primeiro o veículo e depois o cliente.
9. Feche e reabra o programa para verificar a persistência.

## Organização
- `model`: objetos Cliente, Veiculo e Usuario.
- `dao`: comandos SQL; `CrudDAO<T>` define as operações comuns.
- `database`: conexão SQLite e criação de tabelas.
- `view`: janelas Swing.
- `service`: hash e verificação de senha.
- `exception`: exceção de validação.
- `Main`: ponto de entrada do programa.

## Conceitos da atividade
- **CRUD:** métodos `cadastrar`, `listar`, `atualizar`, `excluir` em ClienteDAO e VeiculoDAO.
- **Encapsulamento:** atributos privados nas classes de modelo.
- **Interface:** `CrudDAO<T>`.
- **Polimorfismo:** variáveis `CrudDAO<Cliente>` e `CrudDAO<Veiculo>` nas telas referenciam implementações diferentes.
- **Exceções:** `ValidacaoException` e tratamento de `SQLException`.
- **Banco relacional:** clientes e veículos vinculados por chave estrangeira.

## Observações
- Para simplificar o trabalho acadêmico, o projeto usa Swing padrão, sem bibliotecas visuais adicionais.
- O sistema é local e não possui servidor.
- Os dados não são criptografados no arquivo SQLite; não armazene dados reais de clientes.
- A validação de CPF é matemática e não verifica existência na Receita Federal.
