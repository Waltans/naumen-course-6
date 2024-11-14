package example.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тест на метод обработки бота
 */
class BotLogicTest {

    /**
     * Метод тестирует выполнение команды тест,
     * при правильных ответах
     */
    @Test
    void processTestCommand() {
        FakeBot bot = new FakeBot();
        BotLogic service = new BotLogic(bot);
        User user = new User(1L);

        service.processCommand(user,"/test");
        String firstQuestion =  bot.responses.getFirst();
        service.processCommand(user,"100");
        String secondQuestion = bot.responses.get(1);


        Assertions.assertEquals("Вычислите степень: 10^2", firstQuestion);
        Assertions.assertEquals("Правильный ответ!", secondQuestion);
    }

    /**
     * Метод тестирует выполнение команды тест,
     * при неправильных ответах
     */
    @Test
    void processTestCommand_whenResponseWrong() {
        FakeBot bot = new FakeBot();
        BotLogic service = new BotLogic(bot);
        User user = new User(1L);

        service.processCommand(user,"/test");
        String firstQuestion =  bot.responses.getFirst();
        service.processCommand(user,"101");
        String secondResponse = bot.responses.get(1);
        String third = bot.responses.get(2);
        service.processCommand(user,"10");
        String secondQuestionResponse = bot.responses.get(3);
        String endTestResponse = bot.responses.get(4);

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
        FakeBot bot = new FakeBot();
        BotLogic service = new BotLogic(bot);
        User user = new User(1L);

        service.processCommand(user,"/notify");
        String firstResponse =  bot.responses.getFirst();
        service.processCommand(user,"description");
        String secondResponse = bot.responses.get(1);
        service.processCommand(user,"1");
        String thirdResponse = bot.responses.get(2);
        Thread.sleep(1001);

        Assertions.assertEquals("Введите текст напоминания", firstResponse);
        Assertions.assertEquals("Через сколько секунд напомнить?", secondResponse);
        Assertions.assertEquals("Напоминание установлено", thirdResponse);
        Assertions.assertEquals("Сработало напоминание: 'description'", bot.responses.get(3));
    }

    /**
     * Тест, что команда /notify работает при неверном введении количества секунд задержки
     *
     * @throws InterruptedException
     */
    @Test
    void testNotifyCommandUncorrectedWaitParam() throws InterruptedException {
        FakeBot bot = new FakeBot();
        BotLogic service = new BotLogic(bot);
        User user = new User(1L);

        service.processCommand(user,"/notify");
        String firstResponse =  bot.responses.getFirst();
        service.processCommand(user,"description");
        String secondResponse = bot.responses.get(1);
        service.processCommand(user,"word");
        String thirdResponse = bot.responses.get(2);

        Assertions.assertEquals("Введите текст напоминания", firstResponse);
        Assertions.assertEquals("Через сколько секунд напомнить?", secondResponse);
        Assertions.assertEquals("Пожалуйста, введите целое число", thirdResponse);
    }

    /**
     * Тест команды /repeat, если нет вопросов для повторения
     */
    @Test
    void testRepeatCommand(){
        FakeBot bot = new FakeBot();
        BotLogic service = new BotLogic(bot);
        User user = new User(1L);

        service.processCommand(user, "/repeat");
        String response = bot.responses.get(0);

        Assertions.assertEquals("Нет вопросов для повторения", response);
        Assertions.assertTrue(user.getWrongAnswerQuestions().isEmpty());
    }

    /**
     * Тест команды /repeat, если есть вопросы для повторения
     */
    @Test
    void testRepeatCommandWithQuestion(){
        FakeBot bot = new FakeBot();
        BotLogic service = new BotLogic(bot);
        User user = new User(1L);

        service.processCommand(user, "/test");
        service.processCommand(user, "50");
        service.processCommand(user, "0");

        service.processCommand(user, "/repeat");
        String response = bot.responses.get(5);
        service.processCommand(user, "100");
        String rightResponseFirst = bot.responses.get(6);
        String secondResponse = bot.responses.get(7);
        service.processCommand(user, "6");
        String rightResponseSecond = bot.responses.get(8);
        String thirdResponse = bot.responses.get(9);

        Assertions.assertEquals("Вычислите степень: 10^2", response);
        Assertions.assertEquals("Правильный ответ!", rightResponseFirst);
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", secondResponse);
        Assertions.assertEquals("Правильный ответ!", rightResponseSecond);
        Assertions.assertEquals("Тест завершен", thirdResponse);
    }

}
