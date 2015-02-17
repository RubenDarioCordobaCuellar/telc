/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccc.controller;

import com.ccc.controller.exceptions.NonexistentEntityException;
import com.ccc.controller.exceptions.PreexistingEntityException;
import com.ccc.controller.exceptions.RollbackFailureException;
import com.ccc.entities.Cuadrilla;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ccc.entities.Vehiculo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class CuadrillaJpaController implements Serializable {

    public CuadrillaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuadrilla cuadrilla) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Vehiculo vehiculocodigoVehiculo = cuadrilla.getVehiculocodigoVehiculo();
            if (vehiculocodigoVehiculo != null) {
                vehiculocodigoVehiculo = em.getReference(vehiculocodigoVehiculo.getClass(), vehiculocodigoVehiculo.getCodigoVehiculo());
                cuadrilla.setVehiculocodigoVehiculo(vehiculocodigoVehiculo);
            }
            em.persist(cuadrilla);
            if (vehiculocodigoVehiculo != null) {
                vehiculocodigoVehiculo.getCuadrillaList().add(cuadrilla);
                vehiculocodigoVehiculo = em.merge(vehiculocodigoVehiculo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCuadrilla(cuadrilla.getCodigoCuadrilla()) != null) {
                throw new PreexistingEntityException("Cuadrilla " + cuadrilla + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cuadrilla cuadrilla) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cuadrilla persistentCuadrilla = em.find(Cuadrilla.class, cuadrilla.getCodigoCuadrilla());
            Vehiculo vehiculocodigoVehiculoOld = persistentCuadrilla.getVehiculocodigoVehiculo();
            Vehiculo vehiculocodigoVehiculoNew = cuadrilla.getVehiculocodigoVehiculo();
            if (vehiculocodigoVehiculoNew != null) {
                vehiculocodigoVehiculoNew = em.getReference(vehiculocodigoVehiculoNew.getClass(), vehiculocodigoVehiculoNew.getCodigoVehiculo());
                cuadrilla.setVehiculocodigoVehiculo(vehiculocodigoVehiculoNew);
            }
            cuadrilla = em.merge(cuadrilla);
            if (vehiculocodigoVehiculoOld != null && !vehiculocodigoVehiculoOld.equals(vehiculocodigoVehiculoNew)) {
                vehiculocodigoVehiculoOld.getCuadrillaList().remove(cuadrilla);
                vehiculocodigoVehiculoOld = em.merge(vehiculocodigoVehiculoOld);
            }
            if (vehiculocodigoVehiculoNew != null && !vehiculocodigoVehiculoNew.equals(vehiculocodigoVehiculoOld)) {
                vehiculocodigoVehiculoNew.getCuadrillaList().add(cuadrilla);
                vehiculocodigoVehiculoNew = em.merge(vehiculocodigoVehiculoNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cuadrilla.getCodigoCuadrilla();
                if (findCuadrilla(id) == null) {
                    throw new NonexistentEntityException("The cuadrilla with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cuadrilla cuadrilla;
            try {
                cuadrilla = em.getReference(Cuadrilla.class, id);
                cuadrilla.getCodigoCuadrilla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuadrilla with id " + id + " no longer exists.", enfe);
            }
            Vehiculo vehiculocodigoVehiculo = cuadrilla.getVehiculocodigoVehiculo();
            if (vehiculocodigoVehiculo != null) {
                vehiculocodigoVehiculo.getCuadrillaList().remove(cuadrilla);
                vehiculocodigoVehiculo = em.merge(vehiculocodigoVehiculo);
            }
            em.remove(cuadrilla);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cuadrilla> findCuadrillaEntities() {
        return findCuadrillaEntities(true, -1, -1);
    }

    public List<Cuadrilla> findCuadrillaEntities(int maxResults, int firstResult) {
        return findCuadrillaEntities(false, maxResults, firstResult);
    }

    private List<Cuadrilla> findCuadrillaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuadrilla.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Cuadrilla findCuadrilla(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuadrilla.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuadrillaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuadrilla> rt = cq.from(Cuadrilla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
