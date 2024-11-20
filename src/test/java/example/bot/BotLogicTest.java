package example.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тест на метод обработки бота
 */
class BotLogicTest {
    private BotLogic service;
    private FakeBot bot;
    private User user;

    /**
     * Инициализация необходимых переменных для тестов
     */
    @BeforeEach
    void setUp() {
        bot = new FakeBot();
        service = new BotLogic(bot);
        user = new User(1L);
    }

    /**
     * Метод тестирует выполнение команды тест,
     * при правильных ответах
     */
    @Test
    void processTestCommand() {
        service.processCommand(user, "/test");
        String firstQuestion = bot.getMessages().getFirst();
        service.processCommand(user, "100");
        String response = bot.getMessages().get(1);
        String secondQuestion = bot.getMessages().get(2);
        service.processCommand(user, "6");
        String secondResponse = bot.getMessages().get(3);
        String thirdResponse = bot.getMessages().get(4);


        Assertions.assertEquals("Вычислите степень: 10^2", firstQuestion);
        Assertions.assertEquals("Правильный ответ!", response);
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", secondQuestion);
        Assertions.assertEquals("Правильный ответ!", secondResponse);
        Assertions.assertEquals("Тест завершен", thirdResponse);
    }

    /**
     * Метод тестирует выполнение команды тест,
     * при неправильных ответах
     */
    @Test
    void processTestCommand_whenResponseWrong() {
        service.processCommand(user, "/test");
        String firstQuestion = bot.getMessages().getFirst();
        service.processCommand(user, "101");
        String secondResponse = bot.getMessages().get(1);
        String third = bot.getMessages().get(2);
        service.processCommand(user, "10");
        String secondQuestionResponse = bot.getMessages().get(3);
        String endTestResponse = bot.getMessages().get(4);

        Assertions.assertEquals("Вычислите степень: 10^2", firstQuestion);
        Assertions.assertEquals("Вы ошиблись, верный ответ: 100", secondResponse);
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", third);
        Assertions.assertEquals("Вы ошиблись, верный ответ: 6", secondQuestionResponse);
        Assertions.assertEquals("Тест завершен", endTestResponse);
    }


    /**
     * Тест, что команда /notify работает корректно
     *
     * @throws InterruptedException
     */
    @Test
    void testNotifyCommand() throws InterruptedException {
        service.processCommand(user, "/notify");
        String firstResponse = bot.getMessages().getFirst();
        service.processCommand(user, "description");
        String secondResponse = bot.getMessages().get(1);
        service.processCommand(user, "1");
        String thirdResponse = bot.getMessages().get(2);
        Assertions.assertEquals(3, bot.getMessages().size());
        Thread.sleep(1020);

        Assertions.assertEquals("Введите текст напоминания", firstResponse);
        Assertions.assertEquals("Через сколько секунд напомнить?", secondResponse);
        Assertions.assertEquals("Напоминание установлено", thirdResponse);
        Assertions.assertEquals("Сработало напоминание: 'description'", bot.getMessages().get(3));
    }

    /**
     * Тест, что команда /notify работает при неверном введении количества секунд задержки
     */
    @Test
    void testNotifyCommandUncorrectedWaitParam() {
        service.processCommand(user, "/notify");
        String firstResponse = bot.getMessages().getFirst();
        service.processCommand(user, "description");
        String secondResponse = bot.getMessages().get(1);
        service.processCommand(user, "word");
        String thirdResponse = bot.getMessages().get(2);

        Assertions.assertEquals("Введите текст напоминания", firstResponse);
        Assertions.assertEquals("Через сколько секунд напомнить?", secondResponse);
        Assertions.assertEquals("Пожалуйста, введите целое число", thirdResponse);
    }

    /**
     * Тест команды /repeat, если нет вопросов для повторения
     */
    @Test
    void testRepeatCommand() {

        service.processCommand(user, "/test");
        service.processCommand(user, "100");
        service.processCommand(user, "6");
        service.processCommand(user, "/repeat");
        String response = bot.getMessages().getLast();

        Assertions.assertEquals("Нет вопросов для повторения", response);
    }

    /**
     * Тест команды /repeat, если есть вопросы для повторения
     */
    @Test
    void testRepeatCommandWithQuestions() {
        service.processCommand(user, "/test");
        service.processCommand(user, "50");
        Assertions.assertEquals("Вы ошиблись, верный ответ: 100", bot.getMessages().get(1));
        service.processCommand(user, "0");
        Assertions.assertEquals("Вы ошиблись, верный ответ: 6", bot.getMessages().get(3));

        service.processCommand(user, "/repeat");
        String response = bot.getMessages().get(5);
        Assertions.assertEquals("Вычислите степень: 10^2", response);

        service.processCommand(user, "100");
        String rightResponseFirst = bot.getMessages().get(6);
        Assertions.assertEquals("Правильный ответ!", rightResponseFirst);

        String secondResponse = bot.getMessages().get(7);
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", secondResponse);

        service.processCommand(user, "6");
        String rightResponseSecond = bot.getMessages().get(8);
        Assertions.assertEquals("Правильный ответ!", rightResponseSecond);

        String thirdResponse = bot.getMessages().get(9);
        Assertions.assertEquals("Тест завершен", thirdResponse);

        service.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", bot.getMessages().get(10));
    }

    /**
     * Тест команды /repeat, если есть 1 вопрос для повторения
     */
    @Test
    void testRepeatCommandWithQuestion() {
        service.processCommand(user, "/test");
        service.processCommand(user, "100");
        Assertions.assertEquals("Правильный ответ!", bot.getMessages().get(1));
        service.processCommand(user, "0");
        Assertions.assertEquals("Вы ошиблись, верный ответ: 6", bot.getMessages().get(3));

        service.processCommand(user, "/repeat");

        String secondResponse = bot.getMessages().get(5);
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", secondResponse);

        service.processCommand(user, "6");
        String rightResponseSecond = bot.getMessages().get(6);
        Assertions.assertEquals("Правильный ответ!", rightResponseSecond);

        String thirdResponse = bot.getMessages().get(7);
        Assertions.assertEquals("Тест завершен", thirdResponse);

        service.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", bot.getMessages().get(8));
    }

}
