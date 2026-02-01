package dev.ale.proyect.reservation.utils;

import dev.ale.proyect.reservation.exception.InvalidReservationException;

public class Validates {
    //Valdate that a number is not null (idex,capacity,price)
    public static <T extends Number> void validate(T value, String message) throws Exception {
        if (value == null) {
            throw new InvalidReservationException(message);
        }
    }
    //Validate that an object is not null (reservation,Events, etc...)
    public static <T> void  validateObject( T obj, String message ) throws Exception {
        if(obj==null){
            throw new InvalidReservationException(message);
        }
    }

    //Validate that a text is not null or empty (name,description,etc...)
    public static<T> void validateText(String text, String message) throws Exception {
        if(text==null || text.isEmpty()){
            throw new InvalidReservationException(message);
        }
    }

}
