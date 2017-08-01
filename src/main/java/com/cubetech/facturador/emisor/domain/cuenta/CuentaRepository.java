package com.cubetech.facturador.emisor.domain.cuenta;

import org.springframework.data.repository.Repository;

public interface CuentaRepository extends Repository<Cuenta, Long>{
	public Cuenta findOneByCorrelacion (String correlacion);
	public Cuenta save(Cuenta cuenta);
	public void delete(Cuenta cuenta);
}
