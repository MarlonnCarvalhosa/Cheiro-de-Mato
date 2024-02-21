# Cheiro de Mato App

## Overview
O Cheiro de Mato App é um aplicativo desenvolvido em Kotlin para gerenciar a empresa "Cheiro de Mato", especializada na venda de tempeiros e produtos naturais. Este aplicativo é projetado para facilitar o gerenciamento financeiro, proporcionando recursos para acompanhar o histórico de vendas, lucros e estoque da empresa.

## Tecnologias Utilizadas
- **Linguagem:** Kotlin
- **Injeção de Dependência:** Koin
- **Comunicação de Rede:** Retrofit
- **Arquitetura:** Clean Architecture
- **Padrão de Projeto:** MVVM com Flow
- **Autenticação e Banco de Dados:** Firebase Authentication e Firestore

## Funcionalidades Principais

### Gerenciamento Financeiro
O aplicativo permite o acompanhamento detalhado do aspecto financeiro da empresa, incluindo:

- **Histórico de Vendas:** Registra todas as transações de vendas, fornecendo dados essenciais como data, produtos vendidos, quantidade e valor total.
- **Lucros:** Calcula e exibe o lucro obtido a partir das vendas, considerando os custos associados.
- **Estoque:** Monitora o nível de estoque de tempeiros e produtos naturais para evitar problemas de falta de produtos.

### Autenticação e Banco de Dados Firebase
- **Login Seguro:** Utiliza o Firebase Authentication para garantir a segurança do login dos usuários.
- **Banco de Dados em Tempo Real:** O Firestore é utilizado para armazenar e recuperar dados em tempo real, garantindo sincronização instantânea entre os dispositivos.

### Interface Intuitiva
A interface do usuário é projetada para ser amigável e intuitiva, facilitando a navegação e utilização do aplicativo.

## Estrutura do Projeto
O código-fonte do aplicativo está organizado seguindo os princípios da Clean Architecture, proporcionando modularidade e escalabilidade. As camadas incluem:

- **Data:** Manipulação de dados e interação com o Firebase (Firestore).
- **Domain:** Lógica de negócios e regras de negócios.
- **Presentation:** Camada responsável pela interface do usuário, implementando o padrão MVVM com Flow.

## Configuração do Projeto
1. Clone o repositório: `git clone https://github.com/MarlonnCarvalhosa/Cheiro-de-Mato`
2. Configure as credenciais do Firebase no arquivo de configuração.
3. Abra o projeto no Android Studio ou sua IDE preferida.
4. Compile e execute o aplicativo em um emulador ou dispositivo Android.

## Prints do Aplicativo

![Tela de Login](screenshots/login_screen.png)
*Legenda: Tela de login do Cheiro de Mato Manager App*

![Histórico de Vendas](screenshots/sales_history_screen.png)
*Legenda: Histórico de vendas mostrando detalhes das transações*

## Contribuição
Contribuições são bem-vindas! Se você encontrar bugs, problemas ou deseja adicionar novos recursos, fique à vontade para abrir issues e pull requests.
