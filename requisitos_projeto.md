1. Requisitos Funcionais (RF)

RF01 – Cadastro de Coleções

O sistema deve permitir cadastrar novas coleções, incluindo:

nome da coleção,

descrição (opcional),

data de início,

data prevista para lançamento.


RF02 – Visualização de Coleções

O aplicativo deve exibir uma lista de coleções cadastradas, organizadas por ordem de criação ou por data.

RF03 – Cadastro de Peças / Pedidos

Para cada coleção, o sistema deve permitir cadastrar peças ou pedidos, incluindo:

nome da peça/pedido,

descrição,

data de entrada,

prazo de entrega,

observações adicionais.


RF04 – Status da Produção

O sistema deve permitir atualizar o status de cada peça, com as opções:

“Pendente”,

“Em produção”,

“Concluído”.


RF05 – Edição de Dados

O usuário deve poder editar coleções e peças já cadastradas.

RF06 – Exclusão de Registros

O sistema deve permitir excluir coleções e peças, com aviso de confirmação.

RF07 – Salvamento Automático

As informações devem ser salvas automaticamente no dispositivo do usuário.

RF08 – Persistência de Dados

O sistema deve manter as informações mesmo que o aplicativo seja fechado, utilizando AsyncStorage ou SQLite.

RF09 – Tela de Detalhes da Coleção

Ao clicar em uma coleção, o sistema deve exibir todas as peças relacionadas.

RF10 – Filtro e Organização

O aplicativo deve permitir filtrar peças por status (pendente, em produção, concluído) para facilitar a visualização.

RF11 – Notas/Observações

Permitir adicionar observações extras em cada peça.

RF12 – Interface Simples e Intuitiva

O fluxo de navegação deve ser simples, sem telas complexas, adequado ao uso por pequenos empreendedores.


---

✅ 2. Requisitos Não Funcionais (RNF)

RNF01 – Plataforma Android

O aplicativo deve funcionar em smartphones com sistema Android.

RNF02 – Usabilidade

A interface deve ser clara, com botões grandes e textos de fácil leitura, considerando que a empreendedora não tem familiaridade com sistemas complexos.

RNF03 – Performance

O aplicativo deve carregar rapidamente e funcionar suavemente em aparelhos intermediários ou antigos.

RNF04 – Armazenamento Local

Todas as informações devem ser armazenadas localmente (sem depender de internet).

RNF05 – Navegação Simples

Deve ser utilizado React Navigation com poucas camadas entre as telas.

RNF06 – Baixo Consumo de Bateria

As operações do app devem ser leves, evitando processamento desnecessário.

RNF07 – Offline First

O aplicativo deve funcionar totalmente sem internet.

RNF08 – Segurança Básica

Os dados devem ser armazenados localmente sem exposição externa (ainda que não seja necessário criptografar).


---

✅ 3. Regras de Negócio (RN)

RN01 – Uma coleção pode ter várias peças

Cada coleção pode conter várias peças/pedidos, mas uma peça só pode pertencer a uma única coleção.

RN02 – Não é permitido criar uma peça sem definir uma coleção

Evita registros desorganizados.

RN03 – Status de produção deve ser atualizado manualmente

Somente a empreendedora decide quando a peça muda de fase.

RN04 – Datas obrigatórias

Toda peça deve registrar:

data de entrada,

prazo de entrega.


RN05 – Coleções não podem ser excluídas enquanto houver peças ligadas a elas

Evita perda de dados acidental.

RN06 – Alterações devem ser salvas imediatamente

O usuário não deve precisar clicar em “salvar”.

RN07 – As coleções devem ser organizadas por data de início

Ou seja, as mais recentes aparecem primeiro.