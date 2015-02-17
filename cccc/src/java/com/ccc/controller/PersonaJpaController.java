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
import com.ccc.entities.Cargo;
import com.ccc.entities.Genero;
import com.ccc.entities.Persona;
import com.ccc.entities.Tipousuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (persona.getTipousuarioList() == null) {
            persona.setTipousuarioList(new ArrayList<Tipousuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cargo cargocodigoCargo = persona.getCargocodigoCargo();
            if (cargocodigoCargo != null) {
                cargocodigoCargo = em.getReference(cargocodigoCargo.getClass(), cargocodigoCargo.getCodigoCargo());
                persona.setCargocodigoCargo(cargocodigoCargo);
            }
            Genero generocodigoGenero = persona.getGenerocodigoGenero();
            if (generocodigoGenero != null) {
                generocodigoGenero = em.getReference(generocodigoGenero.getClass(), generocodigoGenero.getCodigoGenero());
                persona.setGenerocodigoGenero(generocodigoGenero);
            }
            List<Tipousuario> attachedTipousuarioList = new ArrayList<Tipousuario>();
            for (Tipousuario tipousuarioListTipousuarioToAttach : persona.getTipousuarioList()) {
                tipousuarioListTipousuarioToAttach = em.getReference(tipousuarioListTipousuarioToAttach.getClass(), tipousuarioListTipousuarioToAttach.getCodigoTipoUsuario());
                attachedTipousuarioList.add(tipousuarioListTipousuarioToAttach);
            }
            persona.setTipousuarioList(attachedTipousuarioList);
            em.persist(persona);
            if (cargocodigoCargo != null) {
                cargocodigoCargo.getPersonaList().add(persona);
                cargocodigoCargo = em.merge(cargocodigoCargo);
            }
            if (generocodigoGenero != null) {
                generocodigoGenero.getPersonaList().add(persona);
                generocodigoGenero = em.merge(generocodigoGenero);
            }
            for (Tipousuario tipousuarioListTipousuario : persona.getTipousuarioList()) {
                Persona oldPersonaCedulaOfTipousuarioListTipousuario = tipousuarioListTipousuario.getPersonaCedula();
                tipousuarioListTipousuario.setPersonaCedula(persona);
                tipousuarioListTipousuario = em.merge(tipousuarioListTipousuario);
                if (oldPersonaCedulaOfTipousuarioListTipousuario != null) {
                    oldPersonaCedulaOfTipousuarioListTipousuario.getTipousuarioList().remove(tipousuarioListTipousuario);
                    oldPersonaCedulaOfTipousuarioListTipousuario = em.merge(oldPersonaCedulaOfTipousuarioListTipousuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPersona(persona.getCedula()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Persona persistentPersona = em.find(Persona.class, persona.getCedula());
            Cargo cargocodigoCargoOld = persistentPersona.getCargocodigoCargo();
            Cargo cargocodigoCargoNew = persona.getCargocodigoCargo();
            Genero generocodigoGeneroOld = persistentPersona.getGenerocodigoGenero();
            Genero generocodigoGeneroNew = persona.getGenerocodigoGenero();
            List<Tipousuario> tipousuarioListOld = persistentPersona.getTipousuarioList();
            List<Tipousuario> tipousuarioListNew = persona.getTipousuarioList();
            List<String> illegalOrphanMessages = null;
            for (Tipousuario tipousuarioListOldTipousuario : tipousuarioListOld) {
                if (!tipousuarioListNew.contains(tipousuarioListOldTipousuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tipousuario " + tipousuarioListOldTipousuario + " since its personaCedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cargocodigoCargoNew != null) {
                cargocodigoCargoNew = em.getReference(cargocodigoCargoNew.getClass(), cargocodigoCargoNew.getCodigoCargo());
                persona.setCargocodigoCargo(cargocodigoCargoNew);
            }
            if (generocodigoGeneroNew != null) {
                generocodigoGeneroNew = em.getReference(generocodigoGeneroNew.getClass(), generocodigoGeneroNew.getCodigoGenero());
                persona.setGenerocodigoGenero(generocodigoGeneroNew);
            }
            List<Tipousuario> attachedTipousuarioListNew = new ArrayList<Tipousuario>();
            for (Tipousuario tipousuarioListNewTipousuarioToAttach : tipousuarioListNew) {
                tipousuarioListNewTipousuarioToAttach = em.getReference(tipousuarioListNewTipousuarioToAttach.getClass(), tipousuarioListNewTipousuarioToAttach.getCodigoTipoUsuario());
                attachedTipousuarioListNew.add(tipousuarioListNewTipousuarioToAttach);
            }
            tipousuarioListNew = attachedTipousuarioListNew;
            persona.setTipousuarioList(tipousuarioListNew);
            persona = em.merge(persona);
            if (cargocodigoCargoOld != null && !cargocodigoCargoOld.equals(cargocodigoCargoNew)) {
                cargocodigoCargoOld.getPersonaList().remove(persona);
                cargocodigoCargoOld = em.merge(cargocodigoCargoOld);
            }
            if (cargocodigoCargoNew != null && !cargocodigoCargoNew.equals(cargocodigoCargoOld)) {
                cargocodigoCargoNew.getPersonaList().add(persona);
                cargocodigoCargoNew = em.merge(cargocodigoCargoNew);
            }
            if (generocodigoGeneroOld != null && !generocodigoGeneroOld.equals(generocodigoGeneroNew)) {
                generocodigoGeneroOld.getPersonaList().remove(persona);
                generocodigoGeneroOld = em.merge(generocodigoGeneroOld);
            }
            if (generocodigoGeneroNew != null && !generocodigoGeneroNew.equals(generocodigoGeneroOld)) {
                generocodigoGeneroNew.getPersonaList().add(persona);
                generocodigoGeneroNew = em.merge(generocodigoGeneroNew);
            }
            for (Tipousuario tipousuarioListNewTipousuario : tipousuarioListNew) {
                if (!tipousuarioListOld.contains(tipousuarioListNewTipousuario)) {
                    Persona oldPersonaCedulaOfTipousuarioListNewTipousuario = tipousuarioListNewTipousuario.getPersonaCedula();
                    tipousuarioListNewTipousuario.setPersonaCedula(persona);
                    tipousuarioListNewTipousuario = em.merge(tipousuarioListNewTipousuario);
                    if (oldPersonaCedulaOfTipousuarioListNewTipousuario != null && !oldPersonaCedulaOfTipousuarioListNewTipousuario.equals(persona)) {
                        oldPersonaCedulaOfTipousuarioListNewTipousuario.getTipousuarioList().remove(tipousuarioListNewTipousuario);
                        oldPersonaCedulaOfTipousuarioListNewTipousuario = em.merge(oldPersonaCedulaOfTipousuarioListNewTipousuario);
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
                Integer id = persona.getCedula();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
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
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tipousuario> tipousuarioListOrphanCheck = persona.getTipousuarioList();
            for (Tipousuario tipousuarioListOrphanCheckTipousuario : tipousuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Tipousuario " + tipousuarioListOrphanCheckTipousuario + " in its tipousuarioList field has a non-nullable personaCedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cargo cargocodigoCargo = persona.getCargocodigoCargo();
            if (cargocodigoCargo != null) {
                cargocodigoCargo.getPersonaList().remove(persona);
                cargocodigoCargo = em.merge(cargocodigoCargo);
            }
            Genero generocodigoGenero = persona.getGenerocodigoGenero();
            if (generocodigoGenero != null) {
                generocodigoGenero.getPersonaList().remove(persona);
                generocodigoGenero = em.merge(generocodigoGenero);
            }
            em.remove(persona);
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

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
