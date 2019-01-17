package com.jacketus.RSOI_Lab2.songsservice.repository;

import com.jacketus.RSOI_Lab2.songsservice.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongsRepository extends JpaRepository<Song, Long> {
}
