# Документування коду

## Стандарти документування

У цьому проекті використовується стандарт **JavaDoc**, який є загальноприйнятим способом документування Java-коду. Він дозволяє автоматично генерувати зручну документацію у вигляді HTML-сторінок.

### Рекомендації:

- Документуйте всі публічні класи, методи, поля.
- Коментарі повинні бути інформативними, зрозумілими і містити опис призначення методу/класу, параметрів та повертаного значення.
- Уникайте зайвих коментарів для очевидного коду.
- Використовуйте JavaDoc теги: `@param`, `@return`, `@throws`, `@author`.

## Інструменти для генерації документації

1. **IntelliJ IDEA** – підтримує автоматичне створення JavaDoc через меню `Tools > Generate JavaDoc`.
2. **Maven** – використовуйте плагін `maven-javadoc-plugin`.
3. **Gradle** – використовуйте `javadoc` task.
4. **Javadoc CLI** – запуск з командного рядка:
   ```bash
   javadoc -d docs/api -sourcepath src/main/java -subpackages org.example.weatherdisplay
   ```
## Приклад документування класів/методів

```java
/**
 * Represents a weather data controller that handles the UI logic.
 * It receives data from the weather API and updates the JavaFX controls.
 */
public class WeatherController {

    /**
     * Updates the weather display with the current temperature, wind speed and description.
     *
     * @param weather The weather data object.
     */
    public void updateWeather(Weather weather) {
        // ...
    }

    /**
     * Loads weather data from the OpenWeatherMap API.
     *
     * @param city The city to load weather for.
     * @throws IOException If there is a problem with the API request.
     */
    public void loadWeather(String city) throws IOException {
        // ...
    }
}
