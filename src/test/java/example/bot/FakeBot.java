package example.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Фейковый бот для тестов.
 * Хранит в себе все отправленные сообщения
 */
public class FakeBot implements Bot {

    List<String> messages = new ArrayList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
