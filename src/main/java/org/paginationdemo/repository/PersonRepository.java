package org.paginationdemo.repository;

import org.paginationdemo.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
	
	public Page<Person> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

}
