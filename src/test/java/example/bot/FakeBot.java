package example.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Фейковый бот для тестов
 */
public class FakeBot implements Bot {

    List<String> responses = new ArrayList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        responses.add(message);
    }
}
