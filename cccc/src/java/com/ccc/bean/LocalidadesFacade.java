/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccc.bean;

import com.ccc.entities.Localidades;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author cpe
 */
@Stateless
public class LocalidadesFacade extends AbstractFacade<Localidades> {
    @PersistenceContext(unitName = "ccccPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LocalidadesFacade() {
        super(Localidades.class);
    }
    
}
