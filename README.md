# Додаток для відображення погоди

JavaFX-додаток, який надає актуальну інформацію про погоду для будь-якого міста, включаючи температуру, швидкість вітру, вологість і погодні умови. Додаток отримує дані з OpenWeatherMap API та відображає прогноз у зручному інтерфейсі.

## Функціонал

- **Поточна погода:** Відображає температуру, швидкість вітру, вологість і погодні умови.
- **Погодинний прогноз:** Надає оновлення погоди на кілька наступних годин.
- **Денний прогноз:** Відображає прогноз погоди на найближчі дні.
- **Перемикання одиниць температури:** Користувачі можуть змінювати відображення температури між Цельсієм і Фаренгейтом.

## Використані технології

- **Java 17+**
- **JavaFX** для UI
- **OpenWeatherMap API** для отримання даних про погоду
- **Gson** для парсингу JSON
- **HTTP Client** для запитів до API

## Вимоги

- Java Development Kit (JDK) 17 або новіший
- Підключення до Інтернету
- API-ключ від [OpenWeatherMap](https://openweathermap.org/)

## Встановлення

1. Клонуйте репозиторій:
   ```sh
   git clone https://github.com/Asphode1l/WeatherDisplay
   ```
2. Перейдіть у директорію проєкту:
   ```sh
   cd weather-display
   ```
3. Переконайтеся, що JavaFX правильно налаштовано у вашому середовищі розробки.
4. Замініть `apiKey` у файлі `WeatherController.java` на ваш власний API-ключ OpenWeatherMap.

## Використання

1. Запустіть додаток через IDE або за допомогою команди:
   ```sh
   java -jar weather-display.jar
   ```
2. Введіть назву міста та натисніть Enter для отримання погоди.
3. Використовуйте кнопку "Перемкнути на Фаренгейт/Цельсій" для зміни одиниць температури.
4. Натисніть "Погодинний прогноз" або "Денний прогноз" для перегляду прогнозів.

## API-запити

Додаток використовує такі API OpenWeatherMap:

- **Geocoding API**: Отримує координати (широту і довготу) міста.
- **Current Weather API**: Отримує поточні погодні дані.
- **Hourly Forecast API**: Надає погодинний прогноз.
- **Daily Forecast API**: Надає прогноз на кілька днів.

## Можливі проблеми

- Переконайтеся, що у вас є дійсний API-ключ.
- Деякі міста можуть бути не розпізнані через обмеження API.
- Проблеми з мережею можуть впливати на отримання даних.

## Документування коду

У проекті використовується JavaDoc як стандарт для створення документації.

**Правила:**

- Документуйте всі публічні класи, методи та поля.
- Пояснюйте, що саме робить кожен метод, які параметри він приймає, що повертає.
- Використовуйте стандартні теги: `@param`, `@return`, `@throws`.

**Генерація документації:**

- Через IntelliJ IDEA: `Tools > Generate JavaDoc`.
- Через Maven: `mvn javadoc:javadoc`.
- Через Gradle: `./gradlew javadoc`.

Згенерована документація зберігається в `docs/api`.

Якщо ви додаєте нові класи або методи — будь ласка, дотримуйтесь цього стилю документування, щоб підтримувати єдиний стандарт у проекті.

