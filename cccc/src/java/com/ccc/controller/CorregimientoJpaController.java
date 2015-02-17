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
import com.ccc.entities.Corregimiento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ccc.entities.MunicipioCiudad;
import com.ccc.entities.VeredaBarrio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class CorregimientoJpaController implements Serializable {

    public CorregimientoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Corregimiento corregimiento) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (corregimiento.getVeredaBarrioList() == null) {
            corregimiento.setVeredaBarrioList(new ArrayList<VeredaBarrio>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MunicipioCiudad municipioCiudadcodigoMunicipio = corregimiento.getMunicipioCiudadcodigoMunicipio();
            if (municipioCiudadcodigoMunicipio != null) {
                municipioCiudadcodigoMunicipio = em.getReference(municipioCiudadcodigoMunicipio.getClass(), municipioCiudadcodigoMunicipio.getCodigoMunicipio());
                corregimiento.setMunicipioCiudadcodigoMunicipio(municipioCiudadcodigoMunicipio);
            }
            List<VeredaBarrio> attachedVeredaBarrioList = new ArrayList<VeredaBarrio>();
            for (VeredaBarrio veredaBarrioListVeredaBarrioToAttach : corregimiento.getVeredaBarrioList()) {
                veredaBarrioListVeredaBarrioToAttach = em.getReference(veredaBarrioListVeredaBarrioToAttach.getClass(), veredaBarrioListVeredaBarrioToAttach.getCodigoVereda());
                attachedVeredaBarrioList.add(veredaBarrioListVeredaBarrioToAttach);
            }
            corregimiento.setVeredaBarrioList(attachedVeredaBarrioList);
            em.persist(corregimiento);
            if (municipioCiudadcodigoMunicipio != null) {
                municipioCiudadcodigoMunicipio.getCorregimientoList().add(corregimiento);
                municipioCiudadcodigoMunicipio = em.merge(municipioCiudadcodigoMunicipio);
            }
            for (VeredaBarrio veredaBarrioListVeredaBarrio : corregimiento.getVeredaBarrioList()) {
                Corregimiento oldCorregimientocodigoMunicipioOfVeredaBarrioListVeredaBarrio = veredaBarrioListVeredaBarrio.getCorregimientocodigoMunicipio();
                veredaBarrioListVeredaBarrio.setCorregimientocodigoMunicipio(corregimiento);
                veredaBarrioListVeredaBarrio = em.merge(veredaBarrioListVeredaBarrio);
                if (oldCorregimientocodigoMunicipioOfVeredaBarrioListVeredaBarrio != null) {
                    oldCorregimientocodigoMunicipioOfVeredaBarrioListVeredaBarrio.getVeredaBarrioList().remove(veredaBarrioListVeredaBarrio);
                    oldCorregimientocodigoMunicipioOfVeredaBarrioListVeredaBarrio = em.merge(oldCorregimientocodigoMunicipioOfVeredaBarrioListVeredaBarrio);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCorregimiento(corregimiento.getCodigoMunicipio()) != null) {
                throw new PreexistingEntityException("Corregimiento " + corregimiento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Corregimiento corregimiento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Corregimiento persistentCorregimiento = em.find(Corregimiento.class, corregimiento.getCodigoMunicipio());
            MunicipioCiudad municipioCiudadcodigoMunicipioOld = persistentCorregimiento.getMunicipioCiudadcodigoMunicipio();
            MunicipioCiudad municipioCiudadcodigoMunicipioNew = corregimiento.getMunicipioCiudadcodigoMunicipio();
            List<VeredaBarrio> veredaBarrioListOld = persistentCorregimiento.getVeredaBarrioList();
            List<VeredaBarrio> veredaBarrioListNew = corregimiento.getVeredaBarrioList();
            List<String> illegalOrphanMessages = null;
            for (VeredaBarrio veredaBarrioListOldVeredaBarrio : veredaBarrioListOld) {
                if (!veredaBarrioListNew.contains(veredaBarrioListOldVeredaBarrio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VeredaBarrio " + veredaBarrioListOldVeredaBarrio + " since its corregimientocodigoMunicipio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (municipioCiudadcodigoMunicipioNew != null) {
                municipioCiudadcodigoMunicipioNew = em.getReference(municipioCiudadcodigoMunicipioNew.getClass(), municipioCiudadcodigoMunicipioNew.getCodigoMunicipio());
                corregimiento.setMunicipioCiudadcodigoMunicipio(municipioCiudadcodigoMunicipioNew);
            }
            List<VeredaBarrio> attachedVeredaBarrioListNew = new ArrayList<VeredaBarrio>();
            for (VeredaBarrio veredaBarrioListNewVeredaBarrioToAttach : veredaBarrioListNew) {
                veredaBarrioListNewVeredaBarrioToAttach = em.getReference(veredaBarrioListNewVeredaBarrioToAttach.getClass(), veredaBarrioListNewVeredaBarrioToAttach.getCodigoVereda());
                attachedVeredaBarrioListNew.add(veredaBarrioListNewVeredaBarrioToAttach);
            }
            veredaBarrioListNew = attachedVeredaBarrioListNew;
            corregimiento.setVeredaBarrioList(veredaBarrioListNew);
            corregimiento = em.merge(corregimiento);
            if (municipioCiudadcodigoMunicipioOld != null && !municipioCiudadcodigoMunicipioOld.equals(municipioCiudadcodigoMunicipioNew)) {
                municipioCiudadcodigoMunicipioOld.getCorregimientoList().remove(corregimiento);
                municipioCiudadcodigoMunicipioOld = em.merge(municipioCiudadcodigoMunicipioOld);
            }
            if (municipioCiudadcodigoMunicipioNew != null && !municipioCiudadcodigoMunicipioNew.equals(municipioCiudadcodigoMunicipioOld)) {
                municipioCiudadcodigoMunicipioNew.getCorregimientoList().add(corregimiento);
                municipioCiudadcodigoMunicipioNew = em.merge(municipioCiudadcodigoMunicipioNew);
            }
            for (VeredaBarrio veredaBarrioListNewVeredaBarrio : veredaBarrioListNew) {
                if (!veredaBarrioListOld.contains(veredaBarrioListNewVeredaBarrio)) {
                    Corregimiento oldCorregimientocodigoMunicipioOfVeredaBarrioListNewVeredaBarrio = veredaBarrioListNewVeredaBarrio.getCorregimientocodigoMunicipio();
                    veredaBarrioListNewVeredaBarrio.setCorregimientocodigoMunicipio(corregimiento);
                    veredaBarrioListNewVeredaBarrio = em.merge(veredaBarrioListNewVeredaBarrio);
                    if (oldCorregimientocodigoMunicipioOfVeredaBarrioListNewVeredaBarrio != null && !oldCorregimientocodigoMunicipioOfVeredaBarrioListNewVeredaBarrio.equals(corregimiento)) {
                        oldCorregimientocodigoMunicipioOfVeredaBarrioListNewVeredaBarrio.getVeredaBarrioList().remove(veredaBarrioListNewVeredaBarrio);
                        oldCorregimientocodigoMunicipioOfVeredaBarrioListNewVeredaBarrio = em.merge(oldCorregimientocodigoMunicipioOfVeredaBarrioListNewVeredaBarrio);
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
                Integer id = corregimiento.getCodigoMunicipio();
                if (findCorregimiento(id) == null) {
                    throw new NonexistentEntityException("The corregimiento with id " + id + " no longer exists.");
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
            Corregimiento corregimiento;
            try {
                corregimiento = em.getReference(Corregimiento.class, id);
                corregimiento.getCodigoMunicipio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The corregimiento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VeredaBarrio> veredaBarrioListOrphanCheck = corregimiento.getVeredaBarrioList();
            for (VeredaBarrio veredaBarrioListOrphanCheckVeredaBarrio : veredaBarrioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Corregimiento (" + corregimiento + ") cannot be destroyed since the VeredaBarrio " + veredaBarrioListOrphanCheckVeredaBarrio + " in its veredaBarrioList field has a non-nullable corregimientocodigoMunicipio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            MunicipioCiudad municipioCiudadcodigoMunicipio = corregimiento.getMunicipioCiudadcodigoMunicipio();
            if (municipioCiudadcodigoMunicipio != null) {
                municipioCiudadcodigoMunicipio.getCorregimientoList().remove(corregimiento);
                municipioCiudadcodigoMunicipio = em.merge(municipioCiudadcodigoMunicipio);
            }
            em.remove(corregimiento);
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

    public List<Corregimiento> findCorregimientoEntities() {
        return findCorregimientoEntities(true, -1, -1);
    }

    public List<Corregimiento> findCorregimientoEntities(int maxResults, int firstResult) {
        return findCorregimientoEntities(false, maxResults, firstResult);
    }

    private List<Corregimiento> findCorregimientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Corregimiento.class));
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

    public Corregimiento findCorregimiento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Corregimiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorregimientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Corregimiento> rt = cq.from(Corregimiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
