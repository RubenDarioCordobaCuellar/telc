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
import com.ccc.entities.Tipousuario;
import com.ccc.entities.Vehiculo;
import java.util.ArrayList;
import java.util.List;
import com.ccc.entities.Indicadortpo;
import com.ccc.entities.Ordenes;
import com.ccc.entities.Tecnico;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class TecnicoJpaController implements Serializable {

    public TecnicoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tecnico tecnico) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tecnico.getVehiculoList() == null) {
            tecnico.setVehiculoList(new ArrayList<Vehiculo>());
        }
        if (tecnico.getIndicadortpoList() == null) {
            tecnico.setIndicadortpoList(new ArrayList<Indicadortpo>());
        }
        if (tecnico.getOrdenesList() == null) {
            tecnico.setOrdenesList(new ArrayList<Ordenes>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipousuario tipoUsuariocodigoTipoUsuario = tecnico.getTipoUsuariocodigoTipoUsuario();
            if (tipoUsuariocodigoTipoUsuario != null) {
                tipoUsuariocodigoTipoUsuario = em.getReference(tipoUsuariocodigoTipoUsuario.getClass(), tipoUsuariocodigoTipoUsuario.getCodigoTipoUsuario());
                tecnico.setTipoUsuariocodigoTipoUsuario(tipoUsuariocodigoTipoUsuario);
            }
            List<Vehiculo> attachedVehiculoList = new ArrayList<Vehiculo>();
            for (Vehiculo vehiculoListVehiculoToAttach : tecnico.getVehiculoList()) {
                vehiculoListVehiculoToAttach = em.getReference(vehiculoListVehiculoToAttach.getClass(), vehiculoListVehiculoToAttach.getCodigoVehiculo());
                attachedVehiculoList.add(vehiculoListVehiculoToAttach);
            }
            tecnico.setVehiculoList(attachedVehiculoList);
            List<Indicadortpo> attachedIndicadortpoList = new ArrayList<Indicadortpo>();
            for (Indicadortpo indicadortpoListIndicadortpoToAttach : tecnico.getIndicadortpoList()) {
                indicadortpoListIndicadortpoToAttach = em.getReference(indicadortpoListIndicadortpoToAttach.getClass(), indicadortpoListIndicadortpoToAttach.getCodigoIndicador());
                attachedIndicadortpoList.add(indicadortpoListIndicadortpoToAttach);
            }
            tecnico.setIndicadortpoList(attachedIndicadortpoList);
            List<Ordenes> attachedOrdenesList = new ArrayList<Ordenes>();
            for (Ordenes ordenesListOrdenesToAttach : tecnico.getOrdenesList()) {
                ordenesListOrdenesToAttach = em.getReference(ordenesListOrdenesToAttach.getClass(), ordenesListOrdenesToAttach.getNumeroOrden());
                attachedOrdenesList.add(ordenesListOrdenesToAttach);
            }
            tecnico.setOrdenesList(attachedOrdenesList);
            em.persist(tecnico);
            if (tipoUsuariocodigoTipoUsuario != null) {
                tipoUsuariocodigoTipoUsuario.getTecnicoList().add(tecnico);
                tipoUsuariocodigoTipoUsuario = em.merge(tipoUsuariocodigoTipoUsuario);
            }
            for (Vehiculo vehiculoListVehiculo : tecnico.getVehiculoList()) {
                vehiculoListVehiculo.getTecnicoList().add(tecnico);
                vehiculoListVehiculo = em.merge(vehiculoListVehiculo);
            }
            for (Indicadortpo indicadortpoListIndicadortpo : tecnico.getIndicadortpoList()) {
                indicadortpoListIndicadortpo.getTecnicoList().add(tecnico);
                indicadortpoListIndicadortpo = em.merge(indicadortpoListIndicadortpo);
            }
            for (Ordenes ordenesListOrdenes : tecnico.getOrdenesList()) {
                Tecnico oldTecnicocodigoTecnicoOfOrdenesListOrdenes = ordenesListOrdenes.getTecnicocodigoTecnico();
                ordenesListOrdenes.setTecnicocodigoTecnico(tecnico);
                ordenesListOrdenes = em.merge(ordenesListOrdenes);
                if (oldTecnicocodigoTecnicoOfOrdenesListOrdenes != null) {
                    oldTecnicocodigoTecnicoOfOrdenesListOrdenes.getOrdenesList().remove(ordenesListOrdenes);
                    oldTecnicocodigoTecnicoOfOrdenesListOrdenes = em.merge(oldTecnicocodigoTecnicoOfOrdenesListOrdenes);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTecnico(tecnico.getCodigoTecnico()) != null) {
                throw new PreexistingEntityException("Tecnico " + tecnico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tecnico tecnico) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tecnico persistentTecnico = em.find(Tecnico.class, tecnico.getCodigoTecnico());
            Tipousuario tipoUsuariocodigoTipoUsuarioOld = persistentTecnico.getTipoUsuariocodigoTipoUsuario();
            Tipousuario tipoUsuariocodigoTipoUsuarioNew = tecnico.getTipoUsuariocodigoTipoUsuario();
            List<Vehiculo> vehiculoListOld = persistentTecnico.getVehiculoList();
            List<Vehiculo> vehiculoListNew = tecnico.getVehiculoList();
            List<Indicadortpo> indicadortpoListOld = persistentTecnico.getIndicadortpoList();
            List<Indicadortpo> indicadortpoListNew = tecnico.getIndicadortpoList();
            List<Ordenes> ordenesListOld = persistentTecnico.getOrdenesList();
            List<Ordenes> ordenesListNew = tecnico.getOrdenesList();
            List<String> illegalOrphanMessages = null;
            for (Ordenes ordenesListOldOrdenes : ordenesListOld) {
                if (!ordenesListNew.contains(ordenesListOldOrdenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ordenes " + ordenesListOldOrdenes + " since its tecnicocodigoTecnico field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoUsuariocodigoTipoUsuarioNew != null) {
                tipoUsuariocodigoTipoUsuarioNew = em.getReference(tipoUsuariocodigoTipoUsuarioNew.getClass(), tipoUsuariocodigoTipoUsuarioNew.getCodigoTipoUsuario());
                tecnico.setTipoUsuariocodigoTipoUsuario(tipoUsuariocodigoTipoUsuarioNew);
            }
            List<Vehiculo> attachedVehiculoListNew = new ArrayList<Vehiculo>();
            for (Vehiculo vehiculoListNewVehiculoToAttach : vehiculoListNew) {
                vehiculoListNewVehiculoToAttach = em.getReference(vehiculoListNewVehiculoToAttach.getClass(), vehiculoListNewVehiculoToAttach.getCodigoVehiculo());
                attachedVehiculoListNew.add(vehiculoListNewVehiculoToAttach);
            }
            vehiculoListNew = attachedVehiculoListNew;
            tecnico.setVehiculoList(vehiculoListNew);
            List<Indicadortpo> attachedIndicadortpoListNew = new ArrayList<Indicadortpo>();
            for (Indicadortpo indicadortpoListNewIndicadortpoToAttach : indicadortpoListNew) {
                indicadortpoListNewIndicadortpoToAttach = em.getReference(indicadortpoListNewIndicadortpoToAttach.getClass(), indicadortpoListNewIndicadortpoToAttach.getCodigoIndicador());
                attachedIndicadortpoListNew.add(indicadortpoListNewIndicadortpoToAttach);
            }
            indicadortpoListNew = attachedIndicadortpoListNew;
            tecnico.setIndicadortpoList(indicadortpoListNew);
            List<Ordenes> attachedOrdenesListNew = new ArrayList<Ordenes>();
            for (Ordenes ordenesListNewOrdenesToAttach : ordenesListNew) {
                ordenesListNewOrdenesToAttach = em.getReference(ordenesListNewOrdenesToAttach.getClass(), ordenesListNewOrdenesToAttach.getNumeroOrden());
                attachedOrdenesListNew.add(ordenesListNewOrdenesToAttach);
            }
            ordenesListNew = attachedOrdenesListNew;
            tecnico.setOrdenesList(ordenesListNew);
            tecnico = em.merge(tecnico);
            if (tipoUsuariocodigoTipoUsuarioOld != null && !tipoUsuariocodigoTipoUsuarioOld.equals(tipoUsuariocodigoTipoUsuarioNew)) {
                tipoUsuariocodigoTipoUsuarioOld.getTecnicoList().remove(tecnico);
                tipoUsuariocodigoTipoUsuarioOld = em.merge(tipoUsuariocodigoTipoUsuarioOld);
            }
            if (tipoUsuariocodigoTipoUsuarioNew != null && !tipoUsuariocodigoTipoUsuarioNew.equals(tipoUsuariocodigoTipoUsuarioOld)) {
                tipoUsuariocodigoTipoUsuarioNew.getTecnicoList().add(tecnico);
                tipoUsuariocodigoTipoUsuarioNew = em.merge(tipoUsuariocodigoTipoUsuarioNew);
            }
            for (Vehiculo vehiculoListOldVehiculo : vehiculoListOld) {
                if (!vehiculoListNew.contains(vehiculoListOldVehiculo)) {
                    vehiculoListOldVehiculo.getTecnicoList().remove(tecnico);
                    vehiculoListOldVehiculo = em.merge(vehiculoListOldVehiculo);
                }
            }
            for (Vehiculo vehiculoListNewVehiculo : vehiculoListNew) {
                if (!vehiculoListOld.contains(vehiculoListNewVehiculo)) {
                    vehiculoListNewVehiculo.getTecnicoList().add(tecnico);
                    vehiculoListNewVehiculo = em.merge(vehiculoListNewVehiculo);
                }
            }
            for (Indicadortpo indicadortpoListOldIndicadortpo : indicadortpoListOld) {
                if (!indicadortpoListNew.contains(indicadortpoListOldIndicadortpo)) {
                    indicadortpoListOldIndicadortpo.getTecnicoList().remove(tecnico);
                    indicadortpoListOldIndicadortpo = em.merge(indicadortpoListOldIndicadortpo);
                }
            }
            for (Indicadortpo indicadortpoListNewIndicadortpo : indicadortpoListNew) {
                if (!indicadortpoListOld.contains(indicadortpoListNewIndicadortpo)) {
                    indicadortpoListNewIndicadortpo.getTecnicoList().add(tecnico);
                    indicadortpoListNewIndicadortpo = em.merge(indicadortpoListNewIndicadortpo);
                }
            }
            for (Ordenes ordenesListNewOrdenes : ordenesListNew) {
                if (!ordenesListOld.contains(ordenesListNewOrdenes)) {
                    Tecnico oldTecnicocodigoTecnicoOfOrdenesListNewOrdenes = ordenesListNewOrdenes.getTecnicocodigoTecnico();
                    ordenesListNewOrdenes.setTecnicocodigoTecnico(tecnico);
                    ordenesListNewOrdenes = em.merge(ordenesListNewOrdenes);
                    if (oldTecnicocodigoTecnicoOfOrdenesListNewOrdenes != null && !oldTecnicocodigoTecnicoOfOrdenesListNewOrdenes.equals(tecnico)) {
                        oldTecnicocodigoTecnicoOfOrdenesListNewOrdenes.getOrdenesList().remove(ordenesListNewOrdenes);
                        oldTecnicocodigoTecnicoOfOrdenesListNewOrdenes = em.merge(oldTecnicocodigoTecnicoOfOrdenesListNewOrdenes);
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
                Integer id = tecnico.getCodigoTecnico();
                if (findTecnico(id) == null) {
                    throw new NonexistentEntityException("The tecnico with id " + id + " no longer exists.");
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
            Tecnico tecnico;
            try {
                tecnico = em.getReference(Tecnico.class, id);
                tecnico.getCodigoTecnico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tecnico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ordenes> ordenesListOrphanCheck = tecnico.getOrdenesList();
            for (Ordenes ordenesListOrphanCheckOrdenes : ordenesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tecnico (" + tecnico + ") cannot be destroyed since the Ordenes " + ordenesListOrphanCheckOrdenes + " in its ordenesList field has a non-nullable tecnicocodigoTecnico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tipousuario tipoUsuariocodigoTipoUsuario = tecnico.getTipoUsuariocodigoTipoUsuario();
            if (tipoUsuariocodigoTipoUsuario != null) {
                tipoUsuariocodigoTipoUsuario.getTecnicoList().remove(tecnico);
                tipoUsuariocodigoTipoUsuario = em.merge(tipoUsuariocodigoTipoUsuario);
            }
            List<Vehiculo> vehiculoList = tecnico.getVehiculoList();
            for (Vehiculo vehiculoListVehiculo : vehiculoList) {
                vehiculoListVehiculo.getTecnicoList().remove(tecnico);
                vehiculoListVehiculo = em.merge(vehiculoListVehiculo);
            }
            List<Indicadortpo> indicadortpoList = tecnico.getIndicadortpoList();
            for (Indicadortpo indicadortpoListIndicadortpo : indicadortpoList) {
                indicadortpoListIndicadortpo.getTecnicoList().remove(tecnico);
                indicadortpoListIndicadortpo = em.merge(indicadortpoListIndicadortpo);
            }
            em.remove(tecnico);
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

    public List<Tecnico> findTecnicoEntities() {
        return findTecnicoEntities(true, -1, -1);
    }

    public List<Tecnico> findTecnicoEntities(int maxResults, int firstResult) {
        return findTecnicoEntities(false, maxResults, firstResult);
    }

    private List<Tecnico> findTecnicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tecnico.class));
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

    public Tecnico findTecnico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tecnico.class, id);
        } finally {
            em.close();
        }
    }

    public int getTecnicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tecnico> rt = cq.from(Tecnico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
