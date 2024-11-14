package example.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тестовый класс для контейнера
 */
class ContainerTest {

    /**
     * Тест проверят, что объекты Item добавляются в контейнер
     */
    @Test
    void add() {
        Item item = new Item(1);
        Item item2 = new Item(2);
        Item item3 = new Item(3);

        Container container = new Container();

        container.add(item);
        container.add(item2);
        container.add(item3);

        Assertions.assertEquals(3, container.size());
        Assertions.assertEquals(1, container.get(0).getNum());
        Assertions.assertEquals(2, container.get(1).getNum());
        Assertions.assertEquals(3, container.get(2).getNum());
    }

    /**
     * Тест проверяет, что объекты удаляются из коллекции
     */
    @Test
    void remove() {
        Item item = new Item(1);
        Item item2 = new Item(2);

        Container container = new Container();

        container.add(item);
        container.add(item2);

        container.remove(item);

        Assertions.assertEquals(1, container.size());
        Assertions.assertEquals(2, container.get(0).getNum());
    }
}
