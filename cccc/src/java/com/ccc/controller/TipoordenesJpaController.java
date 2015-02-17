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
import com.ccc.entities.Ordenes;
import com.ccc.entities.Tipoordenes;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class TipoordenesJpaController implements Serializable {

    public TipoordenesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipoordenes tipoordenes) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoordenes.getOrdenesList() == null) {
            tipoordenes.setOrdenesList(new ArrayList<Ordenes>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Ordenes> attachedOrdenesList = new ArrayList<Ordenes>();
            for (Ordenes ordenesListOrdenesToAttach : tipoordenes.getOrdenesList()) {
                ordenesListOrdenesToAttach = em.getReference(ordenesListOrdenesToAttach.getClass(), ordenesListOrdenesToAttach.getNumeroOrden());
                attachedOrdenesList.add(ordenesListOrdenesToAttach);
            }
            tipoordenes.setOrdenesList(attachedOrdenesList);
            em.persist(tipoordenes);
            for (Ordenes ordenesListOrdenes : tipoordenes.getOrdenesList()) {
                Tipoordenes oldTipoOrdenescodigoTipoOfOrdenesListOrdenes = ordenesListOrdenes.getTipoOrdenescodigoTipo();
                ordenesListOrdenes.setTipoOrdenescodigoTipo(tipoordenes);
                ordenesListOrdenes = em.merge(ordenesListOrdenes);
                if (oldTipoOrdenescodigoTipoOfOrdenesListOrdenes != null) {
                    oldTipoOrdenescodigoTipoOfOrdenesListOrdenes.getOrdenesList().remove(ordenesListOrdenes);
                    oldTipoOrdenescodigoTipoOfOrdenesListOrdenes = em.merge(oldTipoOrdenescodigoTipoOfOrdenesListOrdenes);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoordenes(tipoordenes.getCodigoTipo()) != null) {
                throw new PreexistingEntityException("Tipoordenes " + tipoordenes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipoordenes tipoordenes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipoordenes persistentTipoordenes = em.find(Tipoordenes.class, tipoordenes.getCodigoTipo());
            List<Ordenes> ordenesListOld = persistentTipoordenes.getOrdenesList();
            List<Ordenes> ordenesListNew = tipoordenes.getOrdenesList();
            List<String> illegalOrphanMessages = null;
            for (Ordenes ordenesListOldOrdenes : ordenesListOld) {
                if (!ordenesListNew.contains(ordenesListOldOrdenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ordenes " + ordenesListOldOrdenes + " since its tipoOrdenescodigoTipo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Ordenes> attachedOrdenesListNew = new ArrayList<Ordenes>();
            for (Ordenes ordenesListNewOrdenesToAttach : ordenesListNew) {
                ordenesListNewOrdenesToAttach = em.getReference(ordenesListNewOrdenesToAttach.getClass(), ordenesListNewOrdenesToAttach.getNumeroOrden());
                attachedOrdenesListNew.add(ordenesListNewOrdenesToAttach);
            }
            ordenesListNew = attachedOrdenesListNew;
            tipoordenes.setOrdenesList(ordenesListNew);
            tipoordenes = em.merge(tipoordenes);
            for (Ordenes ordenesListNewOrdenes : ordenesListNew) {
                if (!ordenesListOld.contains(ordenesListNewOrdenes)) {
                    Tipoordenes oldTipoOrdenescodigoTipoOfOrdenesListNewOrdenes = ordenesListNewOrdenes.getTipoOrdenescodigoTipo();
                    ordenesListNewOrdenes.setTipoOrdenescodigoTipo(tipoordenes);
                    ordenesListNewOrdenes = em.merge(ordenesListNewOrdenes);
                    if (oldTipoOrdenescodigoTipoOfOrdenesListNewOrdenes != null && !oldTipoOrdenescodigoTipoOfOrdenesListNewOrdenes.equals(tipoordenes)) {
                        oldTipoOrdenescodigoTipoOfOrdenesListNewOrdenes.getOrdenesList().remove(ordenesListNewOrdenes);
                        oldTipoOrdenescodigoTipoOfOrdenesListNewOrdenes = em.merge(oldTipoOrdenescodigoTipoOfOrdenesListNewOrdenes);
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
                Integer id = tipoordenes.getCodigoTipo();
                if (findTipoordenes(id) == null) {
                    throw new NonexistentEntityException("The tipoordenes with id " + id + " no longer exists.");
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
            Tipoordenes tipoordenes;
            try {
                tipoordenes = em.getReference(Tipoordenes.class, id);
                tipoordenes.getCodigoTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoordenes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ordenes> ordenesListOrphanCheck = tipoordenes.getOrdenesList();
            for (Ordenes ordenesListOrphanCheckOrdenes : ordenesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipoordenes (" + tipoordenes + ") cannot be destroyed since the Ordenes " + ordenesListOrphanCheckOrdenes + " in its ordenesList field has a non-nullable tipoOrdenescodigoTipo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoordenes);
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

    public List<Tipoordenes> findTipoordenesEntities() {
        return findTipoordenesEntities(true, -1, -1);
    }

    public List<Tipoordenes> findTipoordenesEntities(int maxResults, int firstResult) {
        return findTipoordenesEntities(false, maxResults, firstResult);
    }

    private List<Tipoordenes> findTipoordenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoordenes.class));
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

    public Tipoordenes findTipoordenes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoordenes.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoordenesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoordenes> rt = cq.from(Tipoordenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
