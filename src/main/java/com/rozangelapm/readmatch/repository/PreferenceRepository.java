package com.rozangelapm.readmatch.repository;

import com.rozangelapm.readmatch.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Integer> {
}
