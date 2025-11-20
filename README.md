# Instruções para Testar o Aplicativo Collection Manager

## Sobre o Projeto

O Collection Manager é um aplicativo para Android que permite gerenciar coleções de itens e peças/pedidos. Com ele, você pode:

- Criar, editar e excluir coleções
- Adicionar peças/pedidos a cada coleção
- Controlar o status de produção (Pendente, Em Produção, Concluído)
- Organizar as informações por datas
- Tudo isso de forma offline (sem necessidade de internet)

## Requisitos do Sistema

Para testar este aplicativo, você precisará de:

### Requisitos de Hardware:
- Computador com Windows, macOS ou Linux
- Pelo menos 4 GB de RAM (recomendado 8 GB)
- Pelo menos 2 GB de espaço livre em disco
- Processador de 64 bits (recomendado)

### Requisitos de Software:
- Android Studio (IDE de desenvolvimento para Android)
- JDK (Java Development Kit) 8 ou superior
- SDK do Android (será instalado junto com o Android Studio)
- Um dispositivo Android (físico) ou emulador Android

## Passo a Passo para Instalar o Ambiente

### Passo 1: Instalar o Android Studio

1. Acesse o site oficial do Android Studio: https://developer.android.com/studio
2. Clique em "Download Android Studio"
3. Baixe a versão compatível com o seu sistema operacional
4. Execute o instalador e siga os passos:
   - Aceite os termos de licença
   - Escolha o tipo de instalação (recomendamos "Standard")
   - Selecione o tema (pode deixar o padrão)
   - Aguarde o download e instalação (pode levar alguns minutos)
   - Quando terminar, clique em "Finish"

### Passo 2: Configurar o Android Studio

1. Abra o Android Studio
2. Na tela de boas-vindas, clique em "More Actions" > "SDK Manager"
3. Na aba "SDK Platforms", certifique-se de que "Android 13.0 (API 33)" está marcado
4. Na aba "SDK Tools", verifique se estas opções estão marcadas:
   - Android SDK Build-Tools
   - Android Emulator (opcional, se for usar emulador)
   - Android SDK Platform-Tools
5. Clique em "Apply" e depois em "OK"

### Passo 3: Baixar o Código do Projeto

1. Se o código está em um arquivo ZIP:
   - Extraia o conteúdo do ZIP para uma pasta no seu computador (ex: C:\Projetos\CollectionManager)
   
2. Se estiver usando Git:
   - Abra o terminal ou prompt de comando
   - Navegue até a pasta onde deseja salvar o projeto
   - Execute: `git clone [URL_DO_REPOSITÓRIO]`

### Passo 4: Abrir o Projeto no Android Studio

1. Abra o Android Studio
2. Clique em "Open an existing project" (ou "Open Project")
3. Navegue até a pasta onde você salvou o código do Collection Manager
4. Selecione a pasta raiz do projeto (a pasta que contém arquivos como build.gradle)
5. Clique em "OK"
6. Aguarde o Android Studio sincronizar o projeto (isso pode levar alguns minutos)

## Passo a Passo para Testar no Dispositivo Físico

### Passo 1: Preparar o Dispositivo Android

1. Conecte seu celular/tablet ao computador com um cabo USB
2. No seu dispositivo Android:
   - Vá até as "Configurações" ou "Definições"
   - Vá em "Sobre o telefone" ou "Sobre o dispositivo"
   - Toque 7 vezes seguidas em "Número da versão" ou "Build number" para ativar as "Opções do desenvolvedor"
   - Volte às configurações principais
   - Vá em "Opções do desenvolvedor" (pode estar dentro de "Sistema")
   - Ative a opção "Depuração USB" ou "USB Debugging"
   - Clique em "OK" se aparecer uma mensagem de confirmação

### Passo 2: Executar o Aplicativo

1. No Android Studio, certifique-se de que seu dispositivo aparece no canto superior direito
2. Clique no botão verde com ícone de "Play" (▶) ou pressione Shift + F10
3. O aplicativo será compilado e instalado automaticamente no seu dispositivo
4. Aguarde alguns segundos até aparecer no seu dispositivo

### Passo 3: Testar as Funcionalidades

1. **Tela Principal**:
   - Ao abrir o app, você verá uma tela com as coleções criadas (estará vazia no primeiro acesso)
   - Clique no botão "Adicionar Coleção" para criar uma nova

2. **Adicionar Coleção**:
   - Preencha as informações:
     - Nome da coleção (obrigatório)
     - Descrição (opcional)
     - Data de início (obrigatório)
     - Data estimada de lançamento (obrigatório)
   - Clique em "Salvar" para adicionar

3. **Gerenciar Coleções**:
   - Na tela principal, você pode editar ou excluir coleções
   - Clique em uma coleção para ver as peças associadas a ela

4. **Adicionar Peças**:
   - Dentro de uma coleção, clique em "Adicionar Peça"
   - Preencha as informações:
     - Nome da peça (obrigatório)
     - Descrição (opcional)
     - Data de entrada (obrigatório)
     - Data de entrega (obrigatório)
     - Observações (opcional)
     - Status (Pendente, Em Produção, Concluído)
   - Clique em "Salvar"

5. **Filtrar e Visualizar**:
   - Na tela de coleção, você pode filtrar as peças por status
   - Isso ajuda a acompanhar qual parte da produção está em cada estágio

## Passo a Passo para Testar no Emulador (Alternativa)

1. No Android Studio, clique em "AVD Manager" (ícone de dispositivo Android no canto superior direito)
2. Clique em "Create Virtual Device"
3. Escolha um modelo de dispositivo (recomendamos um modelo comum como Pixel 4)
4. Escolha uma imagem do sistema (recomendamos Android 10 ou superior)
5. Clique em "Download" se necessário, depois em "Next"
6. Dê um nome para seu emulador e clique em "Finish"
7. Clique no ícone de "Play" ao lado do emulador criado
8. Aguarde o emulador iniciar (pode levar 2-5 minutos na primeira vez)
9. Quando o emulador estiver pronto, pressione o botão verde "Play" no Android Studio

## Possíveis Problemas e Soluções

### Problema: "Failed to install app"
- Verifique se o seu dispositivo não está com a opção "Fontes desconhecidas" desativada
- Verifique se a depuração USB está ativada corretamente

### Problema: "Device not found"
- Verifique se o cabo USB está funcionando corretamente
- Verifique se o driver do seu dispositivo está instalado corretamente
- Tente reconectar o dispositivo
- Verifique se o dispositivo está desbloqueado

### Problema: "App crashes on startup"
- Certifique-se de que você seguiu todos os passos de configuração
- Verifique se o Android Studio instalou todas as dependências corretamente
- Tente limpar e reconstruir o projeto: Build > Clean Project e depois Build > Rebuild Project

## Dicas para Teste Completo

1. **Teste de Usabilidade**:
   - Tente criar várias coleções diferentes
   - Adicione diversas peças em cada coleção
   - Teste a funcionalidade de edição
   - Teste a funcionalidade de exclusão
   - Verifique se os filtros funcionam corretamente

2. **Teste de Persistência**:
   - Feche e abra o aplicativo para verificar se os dados permanecem salvos
   - Desconecte da internet e verifique se o aplicativo continua funcionando (offline-first)

3. **Teste de Regras de Negócio**:
   - Tente excluir uma coleção que tenha peças associadas (deve impedir com mensagem)
   - Verifique se as datas são obrigatórias
   - Teste todos os status de produção

## Informações Técnicas (Opcional para o Testador)

- **Linguagem**: Java
- **Banco de dados**: SQLite (armazenamento local)
- **Arquitetura**: MVC (Model-View-Controller)
- **Compatibilidade**: Android 5.0 (API 21) ou superior
- **Tipo de Aplicativo**: Offline-first (não precisa de internet)

## Observações Finais

- O aplicativo não requer conexão com internet
- Todos os dados são armazenados localmente no dispositivo
- O aplicativo segue boas práticas de desenvolvimento Android
- A interface é intuitiva e fácil de usar

Se encontrar algum problema ou tiver dúvidas, você pode consultar a documentação do projeto ou entrar em contato com o desenvolvedor.