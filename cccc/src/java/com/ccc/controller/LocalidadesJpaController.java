/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccc.controller;

import com.ccc.controller.exceptions.NonexistentEntityException;
import com.ccc.controller.exceptions.PreexistingEntityException;
import com.ccc.controller.exceptions.RollbackFailureException;
import com.ccc.entities.Localidades;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ccc.entities.MunicipioCiudad;
import com.ccc.entities.Sector;
import com.ccc.entities.Ordenes;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class LocalidadesJpaController implements Serializable {

    public LocalidadesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Localidades localidades) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (localidades.getOrdenesList() == null) {
            localidades.setOrdenesList(new ArrayList<Ordenes>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MunicipioCiudad municipioCiudadcodigoMunicipio = localidades.getMunicipioCiudadcodigoMunicipio();
            if (municipioCiudadcodigoMunicipio != null) {
                municipioCiudadcodigoMunicipio = em.getReference(municipioCiudadcodigoMunicipio.getClass(), municipioCiudadcodigoMunicipio.getCodigoMunicipio());
                localidades.setMunicipioCiudadcodigoMunicipio(municipioCiudadcodigoMunicipio);
            }
            Sector sectorcodigoSector = localidades.getSectorcodigoSector();
            if (sectorcodigoSector != null) {
                sectorcodigoSector = em.getReference(sectorcodigoSector.getClass(), sectorcodigoSector.getCodigoSector());
                localidades.setSectorcodigoSector(sectorcodigoSector);
            }
            List<Ordenes> attachedOrdenesList = new ArrayList<Ordenes>();
            for (Ordenes ordenesListOrdenesToAttach : localidades.getOrdenesList()) {
                ordenesListOrdenesToAttach = em.getReference(ordenesListOrdenesToAttach.getClass(), ordenesListOrdenesToAttach.getNumeroOrden());
                attachedOrdenesList.add(ordenesListOrdenesToAttach);
            }
            localidades.setOrdenesList(attachedOrdenesList);
            em.persist(localidades);
            if (municipioCiudadcodigoMunicipio != null) {
                municipioCiudadcodigoMunicipio.getLocalidadesList().add(localidades);
                municipioCiudadcodigoMunicipio = em.merge(municipioCiudadcodigoMunicipio);
            }
            if (sectorcodigoSector != null) {
                sectorcodigoSector.getLocalidadesList().add(localidades);
                sectorcodigoSector = em.merge(sectorcodigoSector);
            }
            for (Ordenes ordenesListOrdenes : localidades.getOrdenesList()) {
                ordenesListOrdenes.getLocalidadesList().add(localidades);
                ordenesListOrdenes = em.merge(ordenesListOrdenes);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findLocalidades(localidades.getCodigoLocalidad()) != null) {
                throw new PreexistingEntityException("Localidades " + localidades + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Localidades localidades) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Localidades persistentLocalidades = em.find(Localidades.class, localidades.getCodigoLocalidad());
            MunicipioCiudad municipioCiudadcodigoMunicipioOld = persistentLocalidades.getMunicipioCiudadcodigoMunicipio();
            MunicipioCiudad municipioCiudadcodigoMunicipioNew = localidades.getMunicipioCiudadcodigoMunicipio();
            Sector sectorcodigoSectorOld = persistentLocalidades.getSectorcodigoSector();
            Sector sectorcodigoSectorNew = localidades.getSectorcodigoSector();
            List<Ordenes> ordenesListOld = persistentLocalidades.getOrdenesList();
            List<Ordenes> ordenesListNew = localidades.getOrdenesList();
            if (municipioCiudadcodigoMunicipioNew != null) {
                municipioCiudadcodigoMunicipioNew = em.getReference(municipioCiudadcodigoMunicipioNew.getClass(), municipioCiudadcodigoMunicipioNew.getCodigoMunicipio());
                localidades.setMunicipioCiudadcodigoMunicipio(municipioCiudadcodigoMunicipioNew);
            }
            if (sectorcodigoSectorNew != null) {
                sectorcodigoSectorNew = em.getReference(sectorcodigoSectorNew.getClass(), sectorcodigoSectorNew.getCodigoSector());
                localidades.setSectorcodigoSector(sectorcodigoSectorNew);
            }
            List<Ordenes> attachedOrdenesListNew = new ArrayList<Ordenes>();
            for (Ordenes ordenesListNewOrdenesToAttach : ordenesListNew) {
                ordenesListNewOrdenesToAttach = em.getReference(ordenesListNewOrdenesToAttach.getClass(), ordenesListNewOrdenesToAttach.getNumeroOrden());
                attachedOrdenesListNew.add(ordenesListNewOrdenesToAttach);
            }
            ordenesListNew = attachedOrdenesListNew;
            localidades.setOrdenesList(ordenesListNew);
            localidades = em.merge(localidades);
            if (municipioCiudadcodigoMunicipioOld != null && !municipioCiudadcodigoMunicipioOld.equals(municipioCiudadcodigoMunicipioNew)) {
                municipioCiudadcodigoMunicipioOld.getLocalidadesList().remove(localidades);
                municipioCiudadcodigoMunicipioOld = em.merge(municipioCiudadcodigoMunicipioOld);
            }
            if (municipioCiudadcodigoMunicipioNew != null && !municipioCiudadcodigoMunicipioNew.equals(municipioCiudadcodigoMunicipioOld)) {
                municipioCiudadcodigoMunicipioNew.getLocalidadesList().add(localidades);
                municipioCiudadcodigoMunicipioNew = em.merge(municipioCiudadcodigoMunicipioNew);
            }
            if (sectorcodigoSectorOld != null && !sectorcodigoSectorOld.equals(sectorcodigoSectorNew)) {
                sectorcodigoSectorOld.getLocalidadesList().remove(localidades);
                sectorcodigoSectorOld = em.merge(sectorcodigoSectorOld);
            }
            if (sectorcodigoSectorNew != null && !sectorcodigoSectorNew.equals(sectorcodigoSectorOld)) {
                sectorcodigoSectorNew.getLocalidadesList().add(localidades);
                sectorcodigoSectorNew = em.merge(sectorcodigoSectorNew);
            }
            for (Ordenes ordenesListOldOrdenes : ordenesListOld) {
                if (!ordenesListNew.contains(ordenesListOldOrdenes)) {
                    ordenesListOldOrdenes.getLocalidadesList().remove(localidades);
                    ordenesListOldOrdenes = em.merge(ordenesListOldOrdenes);
                }
            }
            for (Ordenes ordenesListNewOrdenes : ordenesListNew) {
                if (!ordenesListOld.contains(ordenesListNewOrdenes)) {
                    ordenesListNewOrdenes.getLocalidadesList().add(localidades);
                    ordenesListNewOrdenes = em.merge(ordenesListNewOrdenes);
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
                Integer id = localidades.getCodigoLocalidad();
                if (findLocalidades(id) == null) {
                    throw new NonexistentEntityException("The localidades with id " + id + " no longer exists.");
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
            Localidades localidades;
            try {
                localidades = em.getReference(Localidades.class, id);
                localidades.getCodigoLocalidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localidades with id " + id + " no longer exists.", enfe);
            }
            MunicipioCiudad municipioCiudadcodigoMunicipio = localidades.getMunicipioCiudadcodigoMunicipio();
            if (municipioCiudadcodigoMunicipio != null) {
                municipioCiudadcodigoMunicipio.getLocalidadesList().remove(localidades);
                municipioCiudadcodigoMunicipio = em.merge(municipioCiudadcodigoMunicipio);
            }
            Sector sectorcodigoSector = localidades.getSectorcodigoSector();
            if (sectorcodigoSector != null) {
                sectorcodigoSector.getLocalidadesList().remove(localidades);
                sectorcodigoSector = em.merge(sectorcodigoSector);
            }
            List<Ordenes> ordenesList = localidades.getOrdenesList();
            for (Ordenes ordenesListOrdenes : ordenesList) {
                ordenesListOrdenes.getLocalidadesList().remove(localidades);
                ordenesListOrdenes = em.merge(ordenesListOrdenes);
            }
            em.remove(localidades);
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

    public List<Localidades> findLocalidadesEntities() {
        return findLocalidadesEntities(true, -1, -1);
    }

    public List<Localidades> findLocalidadesEntities(int maxResults, int firstResult) {
        return findLocalidadesEntities(false, maxResults, firstResult);
    }

    private List<Localidades> findLocalidadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localidades.class));
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

    public Localidades findLocalidades(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localidades.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalidadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localidades> rt = cq.from(Localidades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
