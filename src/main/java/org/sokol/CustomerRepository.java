package org.sokol;

import org.sokol.model.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
@RepositoryRestResource
public interface CustomerRepository extends PagingAndSortingRepository<Customer,Long> {

    List<Customer> findByName(@Param("name") String name);

}
