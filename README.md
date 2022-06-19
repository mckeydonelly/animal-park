Animal Park
=========
### Краткое описание

Симулятор острова животных на java.

Программа заселяет остров животными и симулирует их жизнь с элементами случайности.
Настройки программы запрашиваются при старте, либо используются дефолтные.

Дефолтные настройки хранятся в папке ```resources```:
- ```app.properties``` - настройки приложения
- ```animal-settings.yaml``` - параметры существ

Используемые фреймворки и библиотеки:
- jline для считывания с консоли
- JColor для вывода на консоль текста цветом

Сборка проекта:

```
$ cd animal-park
$ mvn package
```

Запуск проекта:
```
$ java -jar ./target/animal-park.jar
```

### Ограничения

- Поддерживаемые виды животных и их параметры определены в пакете ```entities```  
- Максимальный размер карты или любых других настраиваемых параметров ограничен ```Integer.MAX_VALUE```  
- Ход происходит по раундам. Пока все в текущем раунде не сходят, следующий раунд не начнется
- Функционал очистки консоли и считывания ввода для вызова меню поддерживается только в нативной консоли  

### Краткое описание классов

В корневом пакете проекта `com.mckeydonelly.animalpark` находится класс ```App```, содержащий в себе точку входа в приложение.

Пакеты:  
- ```console``` - содержит классы для чтения с консоли  
- ```park``` - содержит основной класс запуска подготовки данных и запуска логики, а также вывода статистики  
- ```menu``` - содержит классы для работы с меню  
- ```settings``` - содержит классы для работы с настройками программы  
- ```entities``` - содержит классы животных и их параметры  
- ```map``` - содержит классы для работы с картой  
- ```activity``` - содержит классы для запуска жизни животных в многопоточном режиме  

Пример ответа:
```
Welcome to animal park!
s - start with default parameters
c - manual configuration
q - quit

Start simulation with parameters:
Parameter: count map rows (number) - 100
Parameter: count map columns (number) - 20
Parameter: turns count (number) - 1000
Parameter: turns for die (unit loses every turn of "maximum food / turns for die" (number) - 3
Parameter: statistics update frequency (ms) - 100
Parameter: the frequency of the task of growing new plants (ms) - 500

For continue press SPACE...

------------------------------------------------------
p - pause | g - get statistics for a specific cell | q - quit

Turn - 10
Total turns - 1000

Population by type for all map:

🐺=26687
🐍=9786
🦊=23859
🐻=6204
🦅=25064
🐴=12423
🦌=13015
🐇=8700
🐁=2604
🐐=11589
🐑=11094
🐗=14550
🦆=45159
🐛=1425
🦬=6523
🌱=19703
```