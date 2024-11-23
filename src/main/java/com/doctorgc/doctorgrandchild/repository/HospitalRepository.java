package com.doctorgc.doctorgrandchild.repository;

import com.doctorgc.doctorgrandchild.entity.hospital.Hospital;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital,Long> {
    Optional<Hospital> findById(Long id);

}
