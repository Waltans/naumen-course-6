package example.note;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NoteLogicTest {

    /**
     * Тест проверяет работоспособность команды /add и команды /notes,
     * при валидном ответе бот должен отвечать сообщением с добавленными notes
     */
    @Test
    void handleAddMessageWrongAnswer() {
        NoteLogic noteLogic = new NoteLogic();

        String addResponse = noteLogic.handleMessage("/add newTitle");
        String notesResponse = noteLogic.handleMessage("/notes");

        Assertions.assertEquals("Note added!", addResponse);
        Assertions.assertEquals("Your notes:\n"
                + "newTitle", notesResponse);
    }

    /**
     * Тест проверяет, что бот меняет название заметки,
     * при вводе команды /edit и /notes
     */
    @Test
    void handleEditMessageSuccess() {
        NoteLogic noteLogic = new NoteLogic();

        noteLogic.handleMessage("/add newTitle");
        String editResponse = noteLogic.handleMessage("/edit newTitle1");

        String notesResponse = noteLogic.handleMessage("/notes");

        Assertions.assertEquals("Note edited!", editResponse);
        Assertions.assertEquals("Your notes:\n"
                + "newTitle1", notesResponse);
    }

    /**
     * Тест, что бот отправляет ответ,
     * при вводе команды /delete, при добавлении
     */
    @Test
    void handleDeleteMessageSuccess() {
        NoteLogic noteLogic = new NoteLogic();

        noteLogic.handleMessage("/add newTitle");
        noteLogic.handleMessage("/add newTitle2");
        String editResponse = noteLogic.handleMessage("/delete newTitle");

        String notesResponse = noteLogic.handleMessage("/notes");

        Assertions.assertEquals("Note deleted!", editResponse);
        Assertions.assertEquals("Your notes:\n"
                + "newTitle2", notesResponse);
    }
}
