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


        Assertions.assertEquals("Вычислите степень: 10^2", firstQuestion);
        Assertions.assertEquals("Правильный ответ!", response);
    }

    /**
     * Метод тестирует выполнение команды тест,
     * при неправильных ответах
     */
    @Test
    void processTestCommand_whenResponseWrong() {
        service.processCommand(user, "/test");
        String firstQuestion = bot.messages.getFirst();
        service.processCommand(user, "101");
        String secondResponse = bot.messages.get(1);
        String third = bot.messages.get(2);
        service.processCommand(user, "10");
        String secondQuestionResponse = bot.messages.get(3);
        String endTestResponse = bot.messages.get(4);

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
    public void testNotifyCommand() throws InterruptedException {
        service.processCommand(user, "/notify");
        String firstResponse = bot.messages.getFirst();
        service.processCommand(user, "description");
        String secondResponse = bot.messages.get(1);
        service.processCommand(user, "1");
        String thirdResponse = bot.messages.get(2);
        Assertions.assertEquals(0, bot.getMessages().size());
        Thread.sleep(1001);

        Assertions.assertEquals("Введите текст напоминания", firstResponse);
        Assertions.assertEquals("Через сколько секунд напомнить?", secondResponse);
        Assertions.assertEquals("Напоминание установлено", thirdResponse);
        Assertions.assertEquals("Сработало напоминание: 'description'", bot.messages.get(3));
    }

    /**
     * Тест, что команда /notify работает при неверном введении количества секунд задержки
     *
     * @throws InterruptedException
     */
    @Test
    void testNotifyCommandUncorrectedWaitParam() {
        FakeBot bot = new FakeBot();
        BotLogic service = new BotLogic(bot);
        User user = new User(1L);

        service.processCommand(user, "/notify");
        String firstResponse = bot.messages.getFirst();
        service.processCommand(user, "description");
        String secondResponse = bot.messages.get(1);
        service.processCommand(user, "word");
        String thirdResponse = bot.messages.get(2);

        Assertions.assertEquals("Введите текст напоминания", firstResponse);
        Assertions.assertEquals("Через сколько секунд напомнить?", secondResponse);
        Assertions.assertEquals("Пожалуйста, введите целое число", thirdResponse);
    }

    /**
     * Тест команды /repeat, если нет вопросов для повторения
     */
    @Test
    void testRepeatCommand() {
        FakeBot bot = new FakeBot();
        BotLogic service = new BotLogic(bot);
        User user = new User(1L);

        service.processCommand(user, "/repeat");
        String response = bot.messages.getFirst();

        Assertions.assertEquals("Нет вопросов для повторения", response);
        Assertions.assertTrue(user.getWrongAnswerQuestions().isEmpty());
    }

    /**
     * Тест команды /repeat, если есть вопросы для повторения
     */
    @Test
    void testRepeatCommandWithQuestion() {
        service.processCommand(user, "/test");
        service.processCommand(user, "50");
        service.processCommand(user, "0");

        service.processCommand(user, "/repeat");
        String response = bot.messages.get(5);
        Assertions.assertEquals("Вычислите степень: 10^2", response);

        service.processCommand(user, "100");
        String rightResponseFirst = bot.messages.get(6);
        Assertions.assertEquals("Правильный ответ!", rightResponseFirst);

        String secondResponse = bot.messages.get(7);
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", secondResponse);

        service.processCommand(user, "6");
        String rightResponseSecond = bot.messages.get(8);
        Assertions.assertEquals("Правильный ответ!", rightResponseSecond);

        String thirdResponse = bot.messages.get(9);
        Assertions.assertEquals("Тест завершен", thirdResponse);

        service.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", bot.getMessages().get(10));
    }

}
