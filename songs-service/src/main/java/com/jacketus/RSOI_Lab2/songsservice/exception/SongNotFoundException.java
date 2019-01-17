package com.jacketus.RSOI_Lab2.songsservice.exception;

public class SongNotFoundException extends Exception {
    public SongNotFoundException(Long id){
        super("Song with id= " + id + " was not found!");
    }
}
