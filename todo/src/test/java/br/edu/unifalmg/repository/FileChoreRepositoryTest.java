package br.edu.unifalmg.repository;

import br.edu.unifalmg.domain.Chore;
import br.edu.unifalmg.repository.impl.FileChoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileChoreRepositoryTest {

    @InjectMocks
    private FileChoreRepository repository;

    @Mock
    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("#load > When the file is found > When the content is empty > Return empty lsit")
    void loadWhenTheFileIsFoundThenTheContentIsEmptyReturnEmptyList() throws IOException {
        Mockito.when(
                mapper.readValue(new File("chores.json"), Chore[].class)
        ).thenReturn(new Chore[0]);

        List<Chore> response = repository.load();
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#load > When the file is not found (or path is invalid) > Return an empty list")
    void loadWhenTheFileIsNotFoundPathIsInvalidReturnEmptyList() throws IOException {
        Mockito.when(
                mapper.readValue(new File("chores.json"), Chore[].class)
        ).thenReturn(new Chore[0]);

        List<Chore> response = repository.load();
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#load > OK")
    void loadWhenTheFileIsLoadReturnAChoreList() throws IOException{
        Mockito.when(
                mapper.readValue(new File("chores.json"), Chore[].class)
        ).thenReturn(new Chore[]{
                new Chore("First Chore", Boolean.FALSE, LocalDate.now()),
                new Chore("Second Chore", Boolean.TRUE, LocalDate.now().minusDays(2))
        });

        List<Chore> chores = repository.load();
        assertAll(
                () -> assertEquals(2, chores.size()),
                () -> assertEquals("First Chore", chores.get(0).getDescription()),
                () -> assertEquals("Second Chore", chores.get(1).getDescription())
        );
    }
}
