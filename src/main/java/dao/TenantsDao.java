package dao;

import model.Tenants;
import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface TenantsDao extends CrudRepository<Tenants, Integer> {
  
  public Tenants findById(int id);
  
}