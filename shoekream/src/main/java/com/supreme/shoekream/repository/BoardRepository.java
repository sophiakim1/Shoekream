package com.supreme.shoekream.repository;

import com.supreme.shoekream.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}