package com.packt.webstore.domain.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.packt.webstore.domain.Customer;
import com.packt.webstore.domain.repository.CustomerRepository;

@Repository
public class InMemoryCustomerRepositoryImpl implements CustomerRepository {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Override
	public List<Customer> getAllCustomers() {
		Map<String, Object> params = new HashMap<String,Object>();
				List<Customer> result = jdbcTemplate.query
						("SELECT * FROM customers", params, new ProductMapper());
				return result;
	}
	

	private static final class ProductMapper implements RowMapper<Customer> {
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			Customer customer = new Customer();
			customer.setCustomerId(rs.getInt("id"));
			customer.setName(rs.getString("name"));
			customer.setAddress(rs.getString("address"));
			customer.setNoOfOrdersMade(rs.getInt("noOfOrdersMade"));
			
			return customer;
		}
	}


}
