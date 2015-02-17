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
import com.ccc.entities.Corregimiento;
import java.util.ArrayList;
import java.util.List;
import com.ccc.entities.Localidades;
import com.ccc.entities.MunicipioCiudad;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class MunicipioCiudadJpaController implements Serializable {

    public MunicipioCiudadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MunicipioCiudad municipioCiudad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (municipioCiudad.getCorregimientoList() == null) {
            municipioCiudad.setCorregimientoList(new ArrayList<Corregimiento>());
        }
        if (municipioCiudad.getLocalidadesList() == null) {
            municipioCiudad.setLocalidadesList(new ArrayList<Localidades>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Corregimiento> attachedCorregimientoList = new ArrayList<Corregimiento>();
            for (Corregimiento corregimientoListCorregimientoToAttach : municipioCiudad.getCorregimientoList()) {
                corregimientoListCorregimientoToAttach = em.getReference(corregimientoListCorregimientoToAttach.getClass(), corregimientoListCorregimientoToAttach.getCodigoMunicipio());
                attachedCorregimientoList.add(corregimientoListCorregimientoToAttach);
            }
            municipioCiudad.setCorregimientoList(attachedCorregimientoList);
            List<Localidades> attachedLocalidadesList = new ArrayList<Localidades>();
            for (Localidades localidadesListLocalidadesToAttach : municipioCiudad.getLocalidadesList()) {
                localidadesListLocalidadesToAttach = em.getReference(localidadesListLocalidadesToAttach.getClass(), localidadesListLocalidadesToAttach.getCodigoLocalidad());
                attachedLocalidadesList.add(localidadesListLocalidadesToAttach);
            }
            municipioCiudad.setLocalidadesList(attachedLocalidadesList);
            em.persist(municipioCiudad);
            for (Corregimiento corregimientoListCorregimiento : municipioCiudad.getCorregimientoList()) {
                MunicipioCiudad oldMunicipioCiudadcodigoMunicipioOfCorregimientoListCorregimiento = corregimientoListCorregimiento.getMunicipioCiudadcodigoMunicipio();
                corregimientoListCorregimiento.setMunicipioCiudadcodigoMunicipio(municipioCiudad);
                corregimientoListCorregimiento = em.merge(corregimientoListCorregimiento);
                if (oldMunicipioCiudadcodigoMunicipioOfCorregimientoListCorregimiento != null) {
                    oldMunicipioCiudadcodigoMunicipioOfCorregimientoListCorregimiento.getCorregimientoList().remove(corregimientoListCorregimiento);
                    oldMunicipioCiudadcodigoMunicipioOfCorregimientoListCorregimiento = em.merge(oldMunicipioCiudadcodigoMunicipioOfCorregimientoListCorregimiento);
                }
            }
            for (Localidades localidadesListLocalidades : municipioCiudad.getLocalidadesList()) {
                MunicipioCiudad oldMunicipioCiudadcodigoMunicipioOfLocalidadesListLocalidades = localidadesListLocalidades.getMunicipioCiudadcodigoMunicipio();
                localidadesListLocalidades.setMunicipioCiudadcodigoMunicipio(municipioCiudad);
                localidadesListLocalidades = em.merge(localidadesListLocalidades);
                if (oldMunicipioCiudadcodigoMunicipioOfLocalidadesListLocalidades != null) {
                    oldMunicipioCiudadcodigoMunicipioOfLocalidadesListLocalidades.getLocalidadesList().remove(localidadesListLocalidades);
                    oldMunicipioCiudadcodigoMunicipioOfLocalidadesListLocalidades = em.merge(oldMunicipioCiudadcodigoMunicipioOfLocalidadesListLocalidades);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMunicipioCiudad(municipioCiudad.getCodigoMunicipio()) != null) {
                throw new PreexistingEntityException("MunicipioCiudad " + municipioCiudad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MunicipioCiudad municipioCiudad) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MunicipioCiudad persistentMunicipioCiudad = em.find(MunicipioCiudad.class, municipioCiudad.getCodigoMunicipio());
            List<Corregimiento> corregimientoListOld = persistentMunicipioCiudad.getCorregimientoList();
            List<Corregimiento> corregimientoListNew = municipioCiudad.getCorregimientoList();
            List<Localidades> localidadesListOld = persistentMunicipioCiudad.getLocalidadesList();
            List<Localidades> localidadesListNew = municipioCiudad.getLocalidadesList();
            List<String> illegalOrphanMessages = null;
            for (Corregimiento corregimientoListOldCorregimiento : corregimientoListOld) {
                if (!corregimientoListNew.contains(corregimientoListOldCorregimiento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Corregimiento " + corregimientoListOldCorregimiento + " since its municipioCiudadcodigoMunicipio field is not nullable.");
                }
            }
            for (Localidades localidadesListOldLocalidades : localidadesListOld) {
                if (!localidadesListNew.contains(localidadesListOldLocalidades)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Localidades " + localidadesListOldLocalidades + " since its municipioCiudadcodigoMunicipio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Corregimiento> attachedCorregimientoListNew = new ArrayList<Corregimiento>();
            for (Corregimiento corregimientoListNewCorregimientoToAttach : corregimientoListNew) {
                corregimientoListNewCorregimientoToAttach = em.getReference(corregimientoListNewCorregimientoToAttach.getClass(), corregimientoListNewCorregimientoToAttach.getCodigoMunicipio());
                attachedCorregimientoListNew.add(corregimientoListNewCorregimientoToAttach);
            }
            corregimientoListNew = attachedCorregimientoListNew;
            municipioCiudad.setCorregimientoList(corregimientoListNew);
            List<Localidades> attachedLocalidadesListNew = new ArrayList<Localidades>();
            for (Localidades localidadesListNewLocalidadesToAttach : localidadesListNew) {
                localidadesListNewLocalidadesToAttach = em.getReference(localidadesListNewLocalidadesToAttach.getClass(), localidadesListNewLocalidadesToAttach.getCodigoLocalidad());
                attachedLocalidadesListNew.add(localidadesListNewLocalidadesToAttach);
            }
            localidadesListNew = attachedLocalidadesListNew;
            municipioCiudad.setLocalidadesList(localidadesListNew);
            municipioCiudad = em.merge(municipioCiudad);
            for (Corregimiento corregimientoListNewCorregimiento : corregimientoListNew) {
                if (!corregimientoListOld.contains(corregimientoListNewCorregimiento)) {
                    MunicipioCiudad oldMunicipioCiudadcodigoMunicipioOfCorregimientoListNewCorregimiento = corregimientoListNewCorregimiento.getMunicipioCiudadcodigoMunicipio();
                    corregimientoListNewCorregimiento.setMunicipioCiudadcodigoMunicipio(municipioCiudad);
                    corregimientoListNewCorregimiento = em.merge(corregimientoListNewCorregimiento);
                    if (oldMunicipioCiudadcodigoMunicipioOfCorregimientoListNewCorregimiento != null && !oldMunicipioCiudadcodigoMunicipioOfCorregimientoListNewCorregimiento.equals(municipioCiudad)) {
                        oldMunicipioCiudadcodigoMunicipioOfCorregimientoListNewCorregimiento.getCorregimientoList().remove(corregimientoListNewCorregimiento);
                        oldMunicipioCiudadcodigoMunicipioOfCorregimientoListNewCorregimiento = em.merge(oldMunicipioCiudadcodigoMunicipioOfCorregimientoListNewCorregimiento);
                    }
                }
            }
            for (Localidades localidadesListNewLocalidades : localidadesListNew) {
                if (!localidadesListOld.contains(localidadesListNewLocalidades)) {
                    MunicipioCiudad oldMunicipioCiudadcodigoMunicipioOfLocalidadesListNewLocalidades = localidadesListNewLocalidades.getMunicipioCiudadcodigoMunicipio();
                    localidadesListNewLocalidades.setMunicipioCiudadcodigoMunicipio(municipioCiudad);
                    localidadesListNewLocalidades = em.merge(localidadesListNewLocalidades);
                    if (oldMunicipioCiudadcodigoMunicipioOfLocalidadesListNewLocalidades != null && !oldMunicipioCiudadcodigoMunicipioOfLocalidadesListNewLocalidades.equals(municipioCiudad)) {
                        oldMunicipioCiudadcodigoMunicipioOfLocalidadesListNewLocalidades.getLocalidadesList().remove(localidadesListNewLocalidades);
                        oldMunicipioCiudadcodigoMunicipioOfLocalidadesListNewLocalidades = em.merge(oldMunicipioCiudadcodigoMunicipioOfLocalidadesListNewLocalidades);
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
                Integer id = municipioCiudad.getCodigoMunicipio();
                if (findMunicipioCiudad(id) == null) {
                    throw new NonexistentEntityException("The municipioCiudad with id " + id + " no longer exists.");
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
            MunicipioCiudad municipioCiudad;
            try {
                municipioCiudad = em.getReference(MunicipioCiudad.class, id);
                municipioCiudad.getCodigoMunicipio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The municipioCiudad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Corregimiento> corregimientoListOrphanCheck = municipioCiudad.getCorregimientoList();
            for (Corregimiento corregimientoListOrphanCheckCorregimiento : corregimientoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MunicipioCiudad (" + municipioCiudad + ") cannot be destroyed since the Corregimiento " + corregimientoListOrphanCheckCorregimiento + " in its corregimientoList field has a non-nullable municipioCiudadcodigoMunicipio field.");
            }
            List<Localidades> localidadesListOrphanCheck = municipioCiudad.getLocalidadesList();
            for (Localidades localidadesListOrphanCheckLocalidades : localidadesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MunicipioCiudad (" + municipioCiudad + ") cannot be destroyed since the Localidades " + localidadesListOrphanCheckLocalidades + " in its localidadesList field has a non-nullable municipioCiudadcodigoMunicipio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(municipioCiudad);
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

    public List<MunicipioCiudad> findMunicipioCiudadEntities() {
        return findMunicipioCiudadEntities(true, -1, -1);
    }

    public List<MunicipioCiudad> findMunicipioCiudadEntities(int maxResults, int firstResult) {
        return findMunicipioCiudadEntities(false, maxResults, firstResult);
    }

    private List<MunicipioCiudad> findMunicipioCiudadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MunicipioCiudad.class));
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

    public MunicipioCiudad findMunicipioCiudad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MunicipioCiudad.class, id);
        } finally {
            em.close();
        }
    }

    public int getMunicipioCiudadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MunicipioCiudad> rt = cq.from(MunicipioCiudad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
