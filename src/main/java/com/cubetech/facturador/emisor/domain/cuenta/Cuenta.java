package com.cubetech.facturador.emisor.domain.cuenta;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.ValidationException;

import com.cubetech.facturador.emisor.domain.shared.Entidad;

import lombok.Data;
import lombok.ToString;

@Data 
@Entity
@ToString(exclude={"id", "emisores"})
@Table(indexes = @Index(columnList = "correlacion", unique= true))
public class Cuenta  implements Entidad<Cuenta>{
	
	@Id
	@Column(name="cta_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(nullable= false, unique = true)
	private String correlacion;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="cta_id", referencedColumnName="cta_id")
	private List<Emisor> emisores = new ArrayList<Emisor>();

	private boolean activa;
	
	@Override
	public boolean sameIdentityAs(Cuenta other) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Emisor buscaEmisor(String correlation){
		Emisor ret = null;
	
		for(Emisor e : emisores){
			if(e.getCorrelacion().equals(correlation))
				ret = e;
		}
		if(ret == null)
			throw new ValidationException("No existe el emisor");
		
		return ret;
	}
	
	public void upsert(Emisor e){
		Emisor tmp;
		
		try{
			tmp = buscaEmisor(e.getCorrelacion());
			tmp.copia(e);
		}catch(ValidationException ve){
			this.emisores.add(e);
		}
		
	}

}
