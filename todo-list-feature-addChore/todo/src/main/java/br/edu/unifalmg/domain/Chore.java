package br.edu.unifalmg.domain;

import lombok.*;

import java.beans.BeanProperty;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor

public class Chore {

    private String description;

    private Boolean isCompleted;

    private LocalDate deadline;

    public Chore(String description, Boolean bolean, LocalDate deadline){
        this.deadline = deadline;
        this.description = description;
        this.isCompleted = bolean;
    }

    public String getDescription(){
        return this.description;
    }

    public LocalDate getDeadline(){
        return this.deadline;
    }

}
