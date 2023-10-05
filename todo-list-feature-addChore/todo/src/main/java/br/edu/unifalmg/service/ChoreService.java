package br.edu.unifalmg.service;

import br.edu.unifalmg.domain.Chore;
import br.edu.unifalmg.domain.ChoreFilter;
import br.edu.unifalmg.exception.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChoreService {

    private List<Chore> chores;

    public List<Chore> getChores(){
        return this.chores;
    }

    public ChoreService() {
        chores = new ArrayList<>();
    }

    /**
     * Method to add a new chore
     *
     * @param description The description of the chore
     * @param deadline The deadline to fulfill the chore
     * @return Chore The new (and uncompleted) chore
     * @throws InvalidDescriptionException When the description is null or empty
     * @throws InvalidDeadlineException When the deadline is null or empty
     * @throws DuplicatedChoreException When the given chore already exists
     */
    public Chore addChore(String description, LocalDate deadline) {
        if (Objects.isNull(description) || description.isEmpty()) {
            throw new InvalidDescriptionException("The description cannot be null or empty");
        }
        if (Objects.isNull(deadline) || deadline.isBefore(LocalDate.now())) {
            throw new InvalidDeadlineException("The deadline cannot be null or before the current date");
        }
        for (Chore chore : chores) {
            if (chore.getDescription().equals(description)
                    && chore.getDeadline().isEqual(deadline)) {
                throw new DuplicatedChoreException("The given chore already exists.");
            }
        }

//         Using anyMatch solution
//
//        boolean doesTheChoreExist = chores.stream().anyMatch(chore -> chore.getDescription().equals(description) && chore.getDeadline().isEqual(deadline));
//        if (doesTheChoreExist) {
//            throw new DuplicatedChoreException("The given chore already exists.");
//        }

        // Using Constructor with all arguments
        Chore chore = new Chore(description, Boolean.FALSE, deadline);


//         Using Lombok's builder
//
//         Chore chore = Chore.builder()
//                .description(description)
//                .deadline(deadline)
//                .isCompleted(false)
//                .build();

//         Using Getter and Setters
//
//         Chore chore = new Chore();
//         chore.setDescription(description);
//         chore.setDeadline(deadline);
//         chore.setIsCompleted(Boolean.FALSE);


        chores.add(chore);
        return chore;
    }

    public void delereChore(String descripition, LocalDate deadLine) throws ChoreNotFoundException {
        if (this.chores.isEmpty()){
           throw new EmptyChoreListException("Unavle to remove a chore from an empty list");
        }
        boolean isChoreExist = this.chores.stream().anyMatch(chore -> chore.getDescription().equals(descripition) && chore.getDeadline().equals(deadLine));
        if(!isChoreExist){
            throw new ChoreNotFoundException("The given chore does not exist.");
        }else{
            this.chores = this.chores.stream().filter(chore -> !chore.getDescription().equals(descripition) && !chore.getDeadline().equals(deadLine)).collect(Collectors.toList());
        }

    }


    public void toggleChore(String description, LocalDate deadline) throws ChoreNotFoundException {
        boolean isChoreExist = this.chores.stream().anyMatch(chore -> chore.getDescription().equals(description) && chore.getDeadline().equals(deadline));
        if(!isChoreExist){
            throw new ChoreNotFoundException("Chore not found. Impossible to toggle!");
        }
        this.chores = this.chores.stream().map(chore -> {
            if (!chore.getDescription().equals(description) && !chore.getDeadline().isEqual(deadline)) {
                return chore;
            }
            if (chore.getDeadline().isBefore(LocalDate.now())
                    && chore.getIsCompleted()) {
                throw new ToggleChoreWithInvalidDeadlineException("Unable to toggle a completed chore with a past deadline");
            }
            chore.setIsCompleted(!chore.getIsCompleted());
            return chore;
        }).collect(Collectors.toList());
    }

    public List<Chore> filterChores(ChoreFilter filter){
        switch (filter){
            case COMPLETED:
                return this.chores.stream().filter(Chore::getIsCompleted).collect(Collectors.toList());

            case UNCOMPLETED:
                return this.chores.stream().filter(chore -> !chore.getIsCompleted()).collect(Collectors.toList());

            case All:
            default:
                return this.chores;
        }
    }
    //private final Predicate<List<Chore>> isChoreListEmpty = List::isEmpty;
}
