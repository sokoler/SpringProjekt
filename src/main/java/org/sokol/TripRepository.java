package org.sokol;

import org.sokol.model.Trip;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface TripRepository extends PagingAndSortingRepository<Trip,Long> {

        List<Trip> findByDestination(@Param("destination") String name);

}
