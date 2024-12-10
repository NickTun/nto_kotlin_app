# Файл для управления дистрибутивом Gradle

Данный репозиторий необходим для поддержки актуальности дистрибутива Gradle в проектах. Данный проект необходимо подключать подмодулём и использовать во всех проектах.

## Как обвновлять дистрибутив

Перед обновлением необходимо удостоверится, что версия самого Gradle установлена не ниже, чем в дистрибутиве. Сделать это можно [здесь](https://sicampus.ru/gitea/core/dependecies/src/branch/main/src/main/java/Version.kt#L16).

Процесс обновления выглядит следуюющим образом:
1. В начале обновляем саму версию Gradle ([в этом репозитории](https://sicampus.ru/gitea/core/dependecies)).
2. Обновлем ссылку на дестрибутив и проверяем совместимость.