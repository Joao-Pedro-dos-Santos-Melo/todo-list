package br.edu.unifalmg.service;

import br.edu.unifalmg.domain.Chore;
import br.edu.unifalmg.domain.ChoreFilter;
import br.edu.unifalmg.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChoreServiceTest {

    @Test
    void addChoreWhenTheDescriptionIsInvalidThrowAnException() {
        ChoreService service = new ChoreService();
        assertAll(
                () -> assertThrows(InvalidDescriptionException.class,
                        () -> service.addChore(null, null)),
                () -> assertThrows(InvalidDescriptionException.class,
                        () -> service.addChore("", null)),
                () -> assertThrows(InvalidDescriptionException.class,
                        () -> service.addChore(null, LocalDate.now().plusDays(1))),
                () -> assertThrows(InvalidDescriptionException.class,
                        () -> service.addChore("", LocalDate.now().plusDays(1))),
                () -> assertThrows(InvalidDescriptionException.class,
                        () -> service.addChore(null, LocalDate.now().minusDays(1))),
                () -> assertThrows(InvalidDescriptionException.class,
                        () -> service.addChore("", LocalDate.now().minusDays(1)))
        );
    }

    @Test
    @DisplayName("#addChore > When the deadline is invalid > Throw an exception")
    void addChoreWhenTheDeadlineIsInvalidThrowAnException() {
        ChoreService service = new ChoreService();
        assertAll(
                () -> assertThrows(InvalidDeadlineException.class,
                        () -> service.addChore("Description", null)),
                () -> assertThrows(InvalidDeadlineException.class,
                        () -> service.addChore("Description", LocalDate.now().minusDays(1)))
        );
    }

    @Test
    @DisplayName("#addChore > When adding a chore > When the chore already exists > Throw an exception")
    void addChoreWhenAddingAChoreWhenTheChoreAlreadyExistsThrowAnException() {
        ChoreService service = new ChoreService();
        service.addChore("Description", LocalDate.now());
        assertThrows(DuplicatedChoreException.class,
                () -> service.addChore("Description", LocalDate.now()));
    }

    /*
     * TODO: Create The following test cases:
     * 1. When adding a single chore. Compare the results (description, deadline, and isCompleted)
     * 2. When adding more than one chore. Also compare the results.
     *
     */

    @Test
    void addSingleChore(){
        ChoreService service = new ChoreService();
        assertAll(
                () ->service.addChore("Reunião", LocalDate.now())
        );
    }

    @Test
    void addMultipleChore(){
        ChoreService service = new ChoreService();
        assertAll(
                () -> service.addChore("Reunião", LocalDate.now()),
                () -> service.addChore("Academia", LocalDate.now()),
                () -> service.addChore("Almoçar", LocalDate.now())
        );
    }

    @Test
    void deleteChoreWhenListIsEmptyThrowAnException(){
        ChoreService service = new ChoreService();
        assertThrows(EmptyChoreListException.class, () ->{
           service.delereChore("Qualquer coisa", LocalDate.now());
        });
    }

    @Test
    void deleteChoreWhenTheListIsNotEmptyWheTheChoreDoesNotExistThrowAnException(){
        ChoreService service =new ChoreService();
        service.addChore("qualqer coisa", LocalDate.now());
        assertThrows(ChoreNotFoundException.class, () ->{
            service.delereChore("chore", LocalDate.now());
        });
    }

    @Test
    void deleteChoreWhenTheListNotEmptyWheTheChoreExistsDeleteTheChore() throws ChoreNotFoundException {
        ChoreService service = new ChoreService();
        service.addChore("Chore 01", LocalDate.now().plusDays(1));
        assertEquals(1, service.getChores().size());
        service.delereChore("Chore 01", LocalDate.now().plusDays(1));
        assertEquals(0, service.getChores().size());
    }

    @Test
    void toggleChoreWhenTheDeadLineIsValidToggleTheChore(){
        ChoreService service = new ChoreService();
        service.addChore("Chore #01", LocalDate.now());

        Assertions.assertFalse(service.getChores().get(0).getIsCompleted());

        assertDoesNotThrow(() -> service.toggleChore("Chore #01", LocalDate.now()));

        assertTrue(service.getChores().get(0).getIsCompleted());

    }

    @Test
    void togglerChoreWhenTheDeadlineIsValidWhenToggleTheChoreTwiceToggleTheChore(){
        ChoreService service = new ChoreService();
        service.addChore("Chore #01", LocalDate.now());

        Assertions.assertFalse(service.getChores().get(0).getIsCompleted());

        assertDoesNotThrow(() -> service.toggleChore("Chore #01", LocalDate.now()));

        assertTrue(service.getChores().get(0).getIsCompleted());

        assertDoesNotThrow(() -> service.toggleChore("Chore #01", LocalDate.now()));

        assertFalse(service.getChores().get(0).getIsCompleted());
    }

    @Test
    void toggleChoreWhenTheChoreDoesNotExistThrowAnException(){
        ChoreService service = new ChoreService();
        assertThrows(ChoreNotFoundException.class, () -> service.toggleChore("chore", LocalDate.now()));
    }

    @Test
    void toggleChoreTheDeadLineIsInvalidWhenTheStatusInUncompletedToggleTheChore(){
        ChoreService service = new ChoreService();
        service.addChore("Chore #01", LocalDate.now());
        assertFalse(service.getChores().get(0).getIsCompleted());
        service.getChores().get(0).setDeadline(LocalDate.now().minusDays(1));
        assertDoesNotThrow(() -> service.toggleChore("Chore #01", LocalDate.now().minusDays(1)));
        assertTrue(service.getChores().get(0).getIsCompleted());
    }

    @Test
    void toggleChoreWhenTheDeadLineIsInvalidWhenStatusIsCompletedThrowAnException() throws ChoreNotFoundException {
        ChoreService service = new ChoreService();
        service.addChore("Chore #01", LocalDate.now());
        service.toggleChore("Chore #01", LocalDate.now());
        service.getChores().get(0).setDeadline(LocalDate.now().minusDays(1));
        assertThrows(ToggleChoreWithInvalidDeadlineException.class, () -> service.toggleChore("Chore #01", LocalDate.now().minusDays(1)));
    }

    @Test
    @DisplayName("#filterChores > When the filter is ALL > When the list is empty > Return all chores")
    void filterChoresWhenFilterIsAllWhenTheListIsEmptyReturnAllChores(){
        ChoreService service = new ChoreService();
        List<Chore> response = service.filterChores(ChoreFilter.All);
        assertEquals(0, response.size());
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#filterChores > When the filtter is ALL > When the list is not empty > Return All chores")
    void filterChoresWhenTheFilterIsAllWhenListIsNotEmptyReturnAllChores(){
        ChoreService service = new ChoreService();
        service.getChores().add(new Chore("Chore #01", Boolean.FALSE, LocalDate.now()));
        service.getChores().add(new Chore("Chore #02", Boolean.TRUE, LocalDate.now()));
        List<Chore> response = service.filterChores(ChoreFilter.All);
        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals("Chore #01", response.get(0).getDescription()),
                () -> assertEquals(Boolean.FALSE, response.get(0).getIsCompleted()),
                () -> assertEquals("Chore #02", response.get(1).getDescription()),
                () -> assertEquals(Boolean.TRUE, response.get(1).getIsCompleted())
        );
    }

    @Test
    void filterChoresWhenTheFilterIsCompleteWhenTheListIsEmptyReturnAnEmptyList(){
        ChoreService service = new ChoreService();
        List<Chore> response = service.filterChores(ChoreFilter.COMPLETED);
        assertEquals(0, response.size());
        assertTrue(response.isEmpty());
    }

    @Test
    void filterChoresWhenTheFilterIsCompletedWhenListIsNotEmptyReturnTheFilteredChores(){
        ChoreService service = new ChoreService();
        service.getChores().add(new Chore("Chore #01", Boolean.FALSE, LocalDate.now()));
        service.getChores().add(new Chore("Chore #02", Boolean.TRUE, LocalDate.now()));
        List<Chore> response = service.filterChores(ChoreFilter.COMPLETED);
        assertAll(
                () -> assertEquals(1, response.size()),
                () -> assertEquals("Chore #02", response.get(0).getDescription()),
                () -> assertEquals(Boolean.TRUE, response.get(0).getIsCompleted())
        );
    }

    @Test
    void filterChoresWhenTheFilterIsUncompletedWhenListIsEmptyReturnAnEmptyList(){
        ChoreService service = new ChoreService();
        List<Chore> response = service.filterChores(ChoreFilter.UNCOMPLETED);
        assertEquals(0, response.size());
        assertTrue(response.isEmpty());
    }

    @Test
    void filterChoresWhenTheFilterIsUncompletedWhenListIsNotEmptyReturnTheFilteredChores(){
        ChoreService service = new ChoreService();
        service.getChores().add(new Chore("Chore #01", Boolean.FALSE, LocalDate.now()));
        service.getChores().add(new Chore("Chore #02", Boolean.TRUE, LocalDate.now()));
        List<Chore> response = service.filterChores(ChoreFilter.UNCOMPLETED);
        assertAll(
                () -> assertEquals(1, response.size()),
                () -> assertEquals("Chore #01", response.get(0).getDescription()),
                () -> assertEquals(Boolean.FALSE, response.get(0).getIsCompleted())
        );
    }

}
