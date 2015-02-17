/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccc.controller;

import com.ccc.controller.exceptions.IllegalOrphanException;
import com.ccc.controller.exceptions.NonexistentEntityException;
import com.ccc.controller.exceptions.PreexistingEntityException;
import com.ccc.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ccc.entities.Tecnico;
import java.util.ArrayList;
import java.util.List;
import com.ccc.entities.Cuadrilla;
import com.ccc.entities.Vehiculo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class VehiculoJpaController implements Serializable {

    public VehiculoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vehiculo vehiculo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (vehiculo.getTecnicoList() == null) {
            vehiculo.setTecnicoList(new ArrayList<Tecnico>());
        }
        if (vehiculo.getCuadrillaList() == null) {
            vehiculo.setCuadrillaList(new ArrayList<Cuadrilla>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Tecnico> attachedTecnicoList = new ArrayList<Tecnico>();
            for (Tecnico tecnicoListTecnicoToAttach : vehiculo.getTecnicoList()) {
                tecnicoListTecnicoToAttach = em.getReference(tecnicoListTecnicoToAttach.getClass(), tecnicoListTecnicoToAttach.getCodigoTecnico());
                attachedTecnicoList.add(tecnicoListTecnicoToAttach);
            }
            vehiculo.setTecnicoList(attachedTecnicoList);
            List<Cuadrilla> attachedCuadrillaList = new ArrayList<Cuadrilla>();
            for (Cuadrilla cuadrillaListCuadrillaToAttach : vehiculo.getCuadrillaList()) {
                cuadrillaListCuadrillaToAttach = em.getReference(cuadrillaListCuadrillaToAttach.getClass(), cuadrillaListCuadrillaToAttach.getCodigoCuadrilla());
                attachedCuadrillaList.add(cuadrillaListCuadrillaToAttach);
            }
            vehiculo.setCuadrillaList(attachedCuadrillaList);
            em.persist(vehiculo);
            for (Tecnico tecnicoListTecnico : vehiculo.getTecnicoList()) {
                tecnicoListTecnico.getVehiculoList().add(vehiculo);
                tecnicoListTecnico = em.merge(tecnicoListTecnico);
            }
            for (Cuadrilla cuadrillaListCuadrilla : vehiculo.getCuadrillaList()) {
                Vehiculo oldVehiculocodigoVehiculoOfCuadrillaListCuadrilla = cuadrillaListCuadrilla.getVehiculocodigoVehiculo();
                cuadrillaListCuadrilla.setVehiculocodigoVehiculo(vehiculo);
                cuadrillaListCuadrilla = em.merge(cuadrillaListCuadrilla);
                if (oldVehiculocodigoVehiculoOfCuadrillaListCuadrilla != null) {
                    oldVehiculocodigoVehiculoOfCuadrillaListCuadrilla.getCuadrillaList().remove(cuadrillaListCuadrilla);
                    oldVehiculocodigoVehiculoOfCuadrillaListCuadrilla = em.merge(oldVehiculocodigoVehiculoOfCuadrillaListCuadrilla);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVehiculo(vehiculo.getCodigoVehiculo()) != null) {
                throw new PreexistingEntityException("Vehiculo " + vehiculo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vehiculo vehiculo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Vehiculo persistentVehiculo = em.find(Vehiculo.class, vehiculo.getCodigoVehiculo());
            List<Tecnico> tecnicoListOld = persistentVehiculo.getTecnicoList();
            List<Tecnico> tecnicoListNew = vehiculo.getTecnicoList();
            List<Cuadrilla> cuadrillaListOld = persistentVehiculo.getCuadrillaList();
            List<Cuadrilla> cuadrillaListNew = vehiculo.getCuadrillaList();
            List<String> illegalOrphanMessages = null;
            for (Cuadrilla cuadrillaListOldCuadrilla : cuadrillaListOld) {
                if (!cuadrillaListNew.contains(cuadrillaListOldCuadrilla)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cuadrilla " + cuadrillaListOldCuadrilla + " since its vehiculocodigoVehiculo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Tecnico> attachedTecnicoListNew = new ArrayList<Tecnico>();
            for (Tecnico tecnicoListNewTecnicoToAttach : tecnicoListNew) {
                tecnicoListNewTecnicoToAttach = em.getReference(tecnicoListNewTecnicoToAttach.getClass(), tecnicoListNewTecnicoToAttach.getCodigoTecnico());
                attachedTecnicoListNew.add(tecnicoListNewTecnicoToAttach);
            }
            tecnicoListNew = attachedTecnicoListNew;
            vehiculo.setTecnicoList(tecnicoListNew);
            List<Cuadrilla> attachedCuadrillaListNew = new ArrayList<Cuadrilla>();
            for (Cuadrilla cuadrillaListNewCuadrillaToAttach : cuadrillaListNew) {
                cuadrillaListNewCuadrillaToAttach = em.getReference(cuadrillaListNewCuadrillaToAttach.getClass(), cuadrillaListNewCuadrillaToAttach.getCodigoCuadrilla());
                attachedCuadrillaListNew.add(cuadrillaListNewCuadrillaToAttach);
            }
            cuadrillaListNew = attachedCuadrillaListNew;
            vehiculo.setCuadrillaList(cuadrillaListNew);
            vehiculo = em.merge(vehiculo);
            for (Tecnico tecnicoListOldTecnico : tecnicoListOld) {
                if (!tecnicoListNew.contains(tecnicoListOldTecnico)) {
                    tecnicoListOldTecnico.getVehiculoList().remove(vehiculo);
                    tecnicoListOldTecnico = em.merge(tecnicoListOldTecnico);
                }
            }
            for (Tecnico tecnicoListNewTecnico : tecnicoListNew) {
                if (!tecnicoListOld.contains(tecnicoListNewTecnico)) {
                    tecnicoListNewTecnico.getVehiculoList().add(vehiculo);
                    tecnicoListNewTecnico = em.merge(tecnicoListNewTecnico);
                }
            }
            for (Cuadrilla cuadrillaListNewCuadrilla : cuadrillaListNew) {
                if (!cuadrillaListOld.contains(cuadrillaListNewCuadrilla)) {
                    Vehiculo oldVehiculocodigoVehiculoOfCuadrillaListNewCuadrilla = cuadrillaListNewCuadrilla.getVehiculocodigoVehiculo();
                    cuadrillaListNewCuadrilla.setVehiculocodigoVehiculo(vehiculo);
                    cuadrillaListNewCuadrilla = em.merge(cuadrillaListNewCuadrilla);
                    if (oldVehiculocodigoVehiculoOfCuadrillaListNewCuadrilla != null && !oldVehiculocodigoVehiculoOfCuadrillaListNewCuadrilla.equals(vehiculo)) {
                        oldVehiculocodigoVehiculoOfCuadrillaListNewCuadrilla.getCuadrillaList().remove(cuadrillaListNewCuadrilla);
                        oldVehiculocodigoVehiculoOfCuadrillaListNewCuadrilla = em.merge(oldVehiculocodigoVehiculoOfCuadrillaListNewCuadrilla);
                    }
                }
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
                Integer id = vehiculo.getCodigoVehiculo();
                if (findVehiculo(id) == null) {
                    throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Vehiculo vehiculo;
            try {
                vehiculo = em.getReference(Vehiculo.class, id);
                vehiculo.getCodigoVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cuadrilla> cuadrillaListOrphanCheck = vehiculo.getCuadrillaList();
            for (Cuadrilla cuadrillaListOrphanCheckCuadrilla : cuadrillaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the Cuadrilla " + cuadrillaListOrphanCheckCuadrilla + " in its cuadrillaList field has a non-nullable vehiculocodigoVehiculo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Tecnico> tecnicoList = vehiculo.getTecnicoList();
            for (Tecnico tecnicoListTecnico : tecnicoList) {
                tecnicoListTecnico.getVehiculoList().remove(vehiculo);
                tecnicoListTecnico = em.merge(tecnicoListTecnico);
            }
            em.remove(vehiculo);
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

    public List<Vehiculo> findVehiculoEntities() {
        return findVehiculoEntities(true, -1, -1);
    }

    public List<Vehiculo> findVehiculoEntities(int maxResults, int firstResult) {
        return findVehiculoEntities(false, maxResults, firstResult);
    }

    private List<Vehiculo> findVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vehiculo.class));
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

    public Vehiculo findVehiculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vehiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVehiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vehiculo> rt = cq.from(Vehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
