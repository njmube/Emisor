package com.cubetech.facturador.emisor.domain.shared;

/**
 * An entity, as explained in the DDD book.
 *  
 */
public interface Entidad<T> {

  /**
   * Entities compare by identity, not by attributes.
   *
   * @param other The other entity.
   * @return true if the identities are the same, regardles of other attributes.
   */
  public boolean sameIdentityAs(T other);

}
