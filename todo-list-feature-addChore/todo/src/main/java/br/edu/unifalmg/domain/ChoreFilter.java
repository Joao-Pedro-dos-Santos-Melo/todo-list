package br.edu.unifalmg.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ChoreFilter {
    All(1L, "All Chores"),
    COMPLETED(2L, "Only completed chores"),
    UNCOMPLETED(3L, "Only umcompleted");

    private Long identifier;
    private String description;


    ChoreFilter(long l, String s) {
    }
}
