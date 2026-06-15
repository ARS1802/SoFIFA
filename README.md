[README_SoFIFA.md](https://github.com/user-attachments/files/28969830/README_SoFIFA.md)
# SoFIFA

## Descrição

O **SoFIFA** é um projeto Java desenvolvido para processar dados de jogadores de futebol extraídos do site SoFIFA. O sistema lê informações em formato CSV, transforma cada linha em um objeto `Player`, organiza os jogadores com uma árvore AVL e permite gerar uma saída em `.csv` com os dados processados.

O projeto foi pensado como uma aplicação acadêmica de Estrutura de Dados, com foco em comparação de objetos, ordenação flexível, árvores balanceadas e manipulação de arquivos.

## Contexto Acadêmico

Este projeto foi desenvolvido como parte dos estudos da disciplina de **Estrutura de Dados**, com o objetivo de aplicar conceitos vistos em sala em um problema prático.

O enunciado propõe o uso de uma **árvore binária de busca balanceada AVL** para armazenar e ordenar jogadores de futebol conforme parâmetros de pesquisa, como atributos, posições, ordem de classificação e quantidade de resultados.

Entre os conceitos praticados estão:

- árvores binárias de busca;
- balanceamento AVL;
- comparação de objetos;
- uso de `Comparator`;
- leitura e escrita de arquivos CSV;
- organização de responsabilidades entre classes.

## Objetivo Principal

O objetivo principal do sistema é:

> Ler jogadores de um arquivo CSV, criar objetos `Player`, armazená-los em uma árvore AVL usando critérios de comparação flexíveis e exportar os resultados processados para um novo arquivo `.csv`.

## Tecnologias Utilizadas

- Java
- Estruturas de Dados
- Árvore AVL
- `Comparator`
- Generics
- Manipulação de arquivos CSV
- Programação orientada a objetos

## Estrutura do Projeto

```text
src/
├── Main.java
├── arvore/
│   ├── AVL.java
│   └── TreeNode.java
├── filters/
│   ├── Atributes.java
│   ├── Filters.java
│   └── Position.java
├── model/
│   └── Player.java
├── tools/
│   └── Document.java
└── ui/
    └── ...
```

O package `ui` existe para interação com o usuário, mas o núcleo do projeto está principalmente em:

- `model.Player`;
- `arvore.AVL`;
- `arvore.TreeNode`;
- `filters.Atributes`;
- `filters.Position`;
- `tools.Document`.

## Fluxo Geral do Sistema

O fluxo principal dos dados no projeto pode ser entendido assim:

```text
Arquivo CSV de entrada
        ↓
Leitura com Document
        ↓
Criação de objetos Player
        ↓
Criação de Comparator<Player>
        ↓
Inserção dos jogadores na AVL
        ↓
Balanceamento automático da árvore
        ↓
Ordenação dos dados processados
        ↓
Exportação para arquivo CSV
```

Na aplicação atual, a tela principal coordena esse fluxo: lê o CSV, cria os jogadores, monta a `AVL<Player>`, mantém uma lista paralela com os mesmos jogadores ordenados pelo mesmo `Comparator` e usa essa lista para exibir e exportar os resultados.

## Classe Player

A classe `Player`, localizada em `src/model/Player.java`, representa um jogador carregado do CSV.

Ela armazena atributos como:

- `playerId`;
- `shortName`;
- `longName`;
- `playerPositions`;
- `overall`;
- `potential`;
- `valueEur`;
- `wageEur`;
- `age`;
- `heightCm`;
- `weightKg`;
- `clubName`;
- `playerFaceUrl`.

A criação de um jogador acontece pelo construtor:

```java
public Player(String[] s)
```

Esse construtor recebe uma linha do CSV já separada em colunas e usa o enum `Atributes` para saber quais índices devem ser lidos.

Exemplo interno:

```java
playerId = Atributes.parse("player_id", s[Atributes.PLAYER_ID.index]);
overall = Atributes.parse("overall", s[Atributes.OVERALL.index]);
```

Assim, `Player` concentra a representação dos dados de um jogador e também oferece métodos para comparação e exportação.

## Uso de Comparator<Player>

O projeto usa `Comparator<Player>` em vez de fazer `Player extends Comparable<Player>`.

Essa decisão é importante porque `Comparable` define uma ordenação natural fixa dentro da própria classe. Se `Player` implementasse `Comparable<Player>`, ele teria uma única regra principal de comparação, por exemplo, ordenar sempre por `playerId`.

No SoFIFA, isso seria limitado, porque jogadores podem ser ordenados por diferentes critérios, como:

- ID;
- nome;
- idade;
- overall;
- potencial;
- posição;
- salário;
- valor de mercado.

Com `Comparator<Player>`, o projeto pode criar diferentes estratégias de ordenação sem prender a classe `Player` a uma única regra.

A classe `Player` possui o método:

```java
public static Comparator<Player> filters(Filters... filters)
```

Esse método cria um `Comparator<Player>` baseado nos filtros recebidos. Internamente, ele chama:

```java
compareTo(Player otherPlayer, Filters... filters)
```

Esse `compareTo` é um método próprio da classe, não é a implementação da interface `Comparable`.

## Filtros e Critérios de Comparação

Os filtros do projeto são representados pela interface `Filters`.

Dois enums implementam essa interface:

- `Atributes`;
- `Position`.

### Atributes

O enum `Atributes` representa colunas do CSV e informa:

- nome da coluna;
- tipo do dado;
- índice da coluna no arquivo.

Exemplo:

```java
OVERALL("overall", Integer.class, 8)
```

Ele também possui o método:

```java
public static <T> T parse(String csvColumn, String data)
```

Esse método converte o texto vindo do CSV para o tipo correto, como `Integer`, `Double` ou `String`.

### Position

O enum `Position` representa posições de jogadores, como:

- `GK`;
- `CB`;
- `CM`;
- `CAM`;
- `ST`.

A comparação por posição é tratada de forma especial. Primeiro, o jogador precisa passar pelo predicado de posição, ou seja, precisa jogar em pelo menos uma posição selecionada.

Depois, a ordenação prioriza jogadores que combinam melhor com as posições escolhidas. Por exemplo, se forem selecionadas `CM` e `CAM`, jogadores que possuem as duas posições aparecem antes de jogadores que possuem apenas uma delas.

## Árvore AVL

A classe `AVL<T>`, localizada em `src/arvore/AVL.java`, implementa uma árvore binária de busca balanceada.

Uma AVL é uma árvore binária de busca que mantém sua altura controlada. Isso evita que a árvore fique muito desbalanceada e garante operações mais eficientes de inserção, busca e ordenação.

No projeto, a árvore é genérica:

```java
public class AVL<T>
```

Isso significa que ela não depende diretamente da classe `Player`. Ela pode armazenar qualquer tipo `T`, desde que receba um `Comparator<? super T>` para comparar os elementos.

No caso do SoFIFA, o uso principal é:

```java
AVL<Player> currentPlayersTree = new AVL<>(comparator);
```

## Inserção na AVL

A inserção começa pelo método:

```java
public void add(T value)
```

Se a árvore estiver vazia, o novo valor vira a raiz:

```java
this.root = new TreeNode<>(value);
```

Se já existir raiz, a inserção continua recursivamente pelo método:

```java
private boolean addRecursivelly(T value, TreeNode<T> data, TreeNode<T> parent)
```

Dentro desse método, o projeto usa o `Comparator` para decidir o caminho:

```java
int comparison = comparator.compare(value, data.getElement());
```

A regra é:

- se `comparison < 0`, o valor vai para a esquerda;
- se `comparison > 0`, o valor vai para a direita;
- se `comparison == 0`, o elemento é tratado como equivalente e não é inserido novamente.

Isso mantém a propriedade de árvore binária de busca: menores à esquerda, maiores à direita, sempre de acordo com o critério definido pelo `Comparator`.

## Balanceamento

Depois de inserir um nó, a AVL atualiza a altura e o fator de balanceamento do nó atual.

A atualização acontece em:

```java
private void updateNodeState(TreeNode<T> node)
```

A altura é calculada usando a maior altura entre os filhos:

```java
node.setHeight(1 + Math.max(leftHeight, rightHeight));
```

O fator de balanceamento é calculado assim:

```java
node.setFB(leftHeight - rightHeight);
```

Ou seja:

- `FB > 1`: árvore mais pesada para a esquerda;
- `FB < -1`: árvore mais pesada para a direita;
- `FB` entre `-1` e `1`: nó balanceado.

## Rotações da AVL

Quando a árvore fica desbalanceada, são aplicadas rotações.

### Rotação à direita

Usada quando a subárvore esquerda está pesada.

Método:

```java
private void rotateRight(TreeNode<T> data, TreeNode<T> parent)
```

A rotação à direita promove o filho esquerdo e reposiciona o antigo nó raiz da subárvore à direita desse filho.

### Rotação à esquerda

Usada quando a subárvore direita está pesada.

Método:

```java
private void rotateLeft(TreeNode<T> data, TreeNode<T> parent)
```

A rotação à esquerda promove o filho direito e reposiciona o antigo nó raiz da subárvore à esquerda desse filho.

### Rotação dupla esquerda-direita

Acontece quando o nó está pesado para a esquerda, mas o filho esquerdo está pesado para a direita.

No código:

```java
if(data.getFB() > 1){
    if(data.getLeft().getFB() < 0){
        rotateLeft(data.getLeft(), data);
    }
    rotateRight(data, parent);
}
```

Primeiro ocorre uma rotação à esquerda no filho esquerdo, depois uma rotação à direita no nó atual.

### Rotação dupla direita-esquerda

Acontece quando o nó está pesado para a direita, mas o filho direito está pesado para a esquerda.

No código:

```java
if(data.getFB() < -1){
    if(data.getRight().getFB() > 0){
        rotateRight(data.getRight(), data);
    }
    rotateLeft(data, parent);
}
```

Primeiro ocorre uma rotação à direita no filho direito, depois uma rotação à esquerda no nó atual.

## Percurso da Árvore

A classe `AVL` possui métodos internos de percurso:

- `preOrder`;
- `inOrder`;
- `posOrder`.

O método usado no `toString()` atual é o percurso em ordem:

```java
return inOrder(this.root);
```

O percurso em ordem visita:

```text
subárvore esquerda → nó atual → subárvore direita
```

Em uma árvore binária de busca, isso gera os dados em ordem crescente segundo o `Comparator` usado na árvore.

## Exportação para CSV

A classe responsável pela escrita de arquivos é:

```java
tools.Document
```

Ela possui o método:

```java
public void setOutputFile(String path)
```

Esse método define o arquivo de saída. Se o caminho não terminar com `.csv`, ele é tratado como diretório e o projeto cria um arquivo com timestamp.

A escrita é feita por:

```java
public void writeLine(Object data)
```

Esse método chama `data.toString()` e grava o resultado no arquivo.

No caso de `Player`, o método `toString()` retorna os dados do jogador em formato CSV:

```java
@Override
public String toString()
```

Ele também trata valores com vírgulas, aspas ou quebras de linha usando o helper interno `csvValue`.

No fluxo atual da aplicação, a exportação é feita a partir da lista de jogadores processados, que é ordenada com o mesmo `Comparator<Player>` usado na AVL. A AVL também possui percurso em ordem por `toString()`, mas a rotina atual de exportação não percorre a árvore diretamente; ela usa a lista paralela mantida pela tela principal.

## Principais Classes e Métodos

### `model.Player`

Responsabilidade: representar um jogador do CSV e fornecer regras de comparação/exportação.

Principais métodos:

- `Player(String[] s)`: cria um jogador a partir das colunas do CSV.
- `filters(Filters... filters)`: cria um `Comparator<Player>`.
- `compareTo(Player otherPlayer, Filters... filters)`: compara jogadores usando filtros.
- `playsAs(Position position)`: verifica se o jogador atua em uma posição.
- `matchesAnyPosition(Position... positions)`: verifica se o jogador passa no filtro de posições.
- `comparePositions(Player otherPlayer, Position... positions)`: compara jogadores pelas posições selecionadas.
- `getValue(Atributes attribute)`: retorna o valor bruto de um atributo.
- `getDisplayValue(Atributes attribute)`: retorna valor como texto.
- `toString()`: gera uma linha CSV do jogador.

### `filters.Atributes`

Responsabilidade: mapear atributos usados pelo sistema para colunas do CSV.

Principais métodos:

- `parse(String csvColumn, String data)`: converte texto do CSV para o tipo correto.
- `csvColumn()`: retorna o nome da coluna.
- `type()`: retorna o tipo Java esperado.

### `filters.Position`

Responsabilidade: representar posições de futebol usadas nos filtros.

Exemplos:

```java
GK, CB, CM, CAM, ST
```

Cada posição possui nome em inglês e português.

### `filters.Filters`

Responsabilidade: atuar como interface comum para filtros.

Ela permite que `Atributes` e `Position` sejam usados no mesmo fluxo de comparação.

### `arvore.AVL<T>`

Responsabilidade: armazenar elementos em uma árvore binária de busca balanceada.

Principais métodos:

- `AVL(Comparator<? super T> comparator)`: cria árvore com regra de comparação.
- `add(T value)`: insere elemento na árvore.
- `addRecursivelly(...)`: realiza inserção recursiva.
- `updateNodeState(...)`: atualiza altura e fator de balanceamento.
- `rotateLeft(...)`: executa rotação à esquerda.
- `rotateRight(...)`: executa rotação à direita.
- `toString()`: retorna o percurso em ordem da árvore.

### `arvore.TreeNode<T>`

Responsabilidade: representar cada nó da árvore AVL.

Cada nó armazena:

- elemento;
- filho esquerdo;
- filho direito;
- altura;
- fator de balanceamento.

### `tools.Document`

Responsabilidade: ler e escrever arquivos CSV.

Principais métodos:

- `Document(String path)`: abre arquivo de entrada.
- `setInputFile(String path)`: define arquivo de leitura.
- `readerHasNextLine()`: verifica próxima linha não vazia.
- `readLine()`: lê e separa uma linha CSV.
- `setOutputFile(String path)`: define arquivo de saída.
- `writeLine(Object data)`: escreve uma linha no arquivo.

## Exemplo Conceitual de Uso

Um fluxo simplificado usando as classes principais seria:

```java
Document document = new Document("input/FC26_20250921.csv");

Comparator<Player> comparator = Player.filters(
        Atributes.POTENTIAL,
        Atributes.OVERALL
);

AVL<Player> playersTree = new AVL<>(comparator);
List<Player> players = new ArrayList<>();

document.readLine(); // ignora o cabeçalho

while(document.readerHasNextLine()){
    String[] row = document.readLine();

    if(row != null){
        Player player = new Player(row);

        playersTree.add(player);
        players.add(player);
    }
}

players.sort(comparator);

document.setOutputFile("output");
document.writeLine("player_id,short_name,long_name,player_positions,overall,potential,value_eur,wage_eur,age,height_cm,weight_kg,club_team_id,club_name,player_face_url");

for(Player player : players){
    document.writeLine(player);
}
```

Esse exemplo mostra a ideia central:

1. abrir o CSV;
2. criar jogadores;
3. criar um `Comparator<Player>`;
4. inserir jogadores na AVL;
5. ordenar os dados processados;
6. exportar o resultado.

## Aprendizados

Este projeto pratica vários conceitos importantes de Ciência da Computação:

- **Encapsulamento:** `Player`, `Document`, `AVL` e `TreeNode` possuem responsabilidades próprias.
- **Comparação de objetos:** jogadores são comparados por filtros flexíveis.
- **Comparator:** permite múltiplas estratégias de ordenação sem acoplar tudo em `Player`.
- **Árvore binária de busca:** a AVL organiza elementos pela relação menor/maior.
- **Balanceamento AVL:** rotações mantêm a árvore com altura controlada.
- **Recursão:** inserção, remoção, busca e percursos da árvore usam chamadas recursivas.
- **Generics:** `AVL<T>` e `TreeNode<T>` funcionam com diferentes tipos de dados.
- **Manipulação de arquivos:** `Document` lê e escreve arquivos CSV.
- **Separação de responsabilidades:** cada classe tem papel específico no fluxo.

## Observações

- A árvore AVL é criada e preenchida com os jogadores processados, mas a exportação atual usa uma lista paralela ordenada com o mesmo `Comparator<Player>`.
- A classe `AVL` possui percurso em ordem por `toString()`, que pode representar os dados ordenados da árvore.
- O parser CSV em `Document.readLine()` é simples e foi implementado manualmente.
- `Document.setOutputFile()` escreve em modo append, então exportações repetidas para o mesmo arquivo podem acumular conteúdo.
- O package `ui` coordena a interação com o usuário, mas a lógica principal do projeto está nas classes de modelo, filtros, arquivo e árvore.

## Resultado Esperado

Ao final da execução, o projeto deve produzir um arquivo `.csv` com jogadores selecionados e ordenados conforme os filtros definidos.

Esse arquivo representa a saída processada do sistema, partindo dos dados originais do SoFIFA e aplicando as regras de comparação e organização implementadas no projeto.
