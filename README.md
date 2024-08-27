stella-type-checker
===================

Интерпретатор: [Stella](https://fizruk.github.io/stella/)

__Парсер__

```shell
antlr4 src/main/antlr/stellaLexer.g4 src/main/antlr/stellaParser.g4 \
    -visitor \
    -long-messages
```

__Сборка__

```shell
./gradlew clean shadowJar
```

__Запуск__


```shell
java -jar build/libs/stella-type-checker.jar
```

__Тесты__

-   Генерация

    ```shell
    ./gradlew clean generateTests
    ```

-   Запуск

    ```shell
    ./gradlew clean test
    ```
