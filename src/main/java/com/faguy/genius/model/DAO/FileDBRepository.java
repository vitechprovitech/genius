package com.faguy.genius.model.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.faguy.genius.entity.FileDB;


@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

}