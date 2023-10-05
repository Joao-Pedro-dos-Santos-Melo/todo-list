package br.edu.unifalmg.exception;

public class ChoreNotFoundException extends ClassNotFoundException{

    public ChoreNotFoundException(String message){
        super(message);
    }
}
