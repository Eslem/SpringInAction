package tacos.persistence;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import lombok.Data;
import tacos.dommain.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
	List<Order> findByZip(String zip);
	
	List<Order> readOrdersByZipAndPlacedAtBetween(String zip, Data startDate, Date endDate);
	
	List<Order> findByStreetAndCityAllIgnoreCase(String street, String city);
	
	@Query("SELECT o FROM Order o WHERE o.city='Seattle'")
	List<Order> readOrdersDeliveredInSeattle();
}
