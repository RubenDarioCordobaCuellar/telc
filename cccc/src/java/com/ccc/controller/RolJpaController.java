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
import com.ccc.entities.Permiso;
import com.ccc.entities.Rol;
import java.util.ArrayList;
import java.util.List;
import com.ccc.entities.Tipousuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class RolJpaController implements Serializable {

    public RolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (rol.getPermisoList() == null) {
            rol.setPermisoList(new ArrayList<Permiso>());
        }
        if (rol.getTipousuarioList() == null) {
            rol.setTipousuarioList(new ArrayList<Tipousuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Permiso> attachedPermisoList = new ArrayList<Permiso>();
            for (Permiso permisoListPermisoToAttach : rol.getPermisoList()) {
                permisoListPermisoToAttach = em.getReference(permisoListPermisoToAttach.getClass(), permisoListPermisoToAttach.getCodigoPermiso());
                attachedPermisoList.add(permisoListPermisoToAttach);
            }
            rol.setPermisoList(attachedPermisoList);
            List<Tipousuario> attachedTipousuarioList = new ArrayList<Tipousuario>();
            for (Tipousuario tipousuarioListTipousuarioToAttach : rol.getTipousuarioList()) {
                tipousuarioListTipousuarioToAttach = em.getReference(tipousuarioListTipousuarioToAttach.getClass(), tipousuarioListTipousuarioToAttach.getCodigoTipoUsuario());
                attachedTipousuarioList.add(tipousuarioListTipousuarioToAttach);
            }
            rol.setTipousuarioList(attachedTipousuarioList);
            em.persist(rol);
            for (Permiso permisoListPermiso : rol.getPermisoList()) {
                permisoListPermiso.getRolList().add(rol);
                permisoListPermiso = em.merge(permisoListPermiso);
            }
            for (Tipousuario tipousuarioListTipousuario : rol.getTipousuarioList()) {
                Rol oldRolcodigoRolOfTipousuarioListTipousuario = tipousuarioListTipousuario.getRolcodigoRol();
                tipousuarioListTipousuario.setRolcodigoRol(rol);
                tipousuarioListTipousuario = em.merge(tipousuarioListTipousuario);
                if (oldRolcodigoRolOfTipousuarioListTipousuario != null) {
                    oldRolcodigoRolOfTipousuarioListTipousuario.getTipousuarioList().remove(tipousuarioListTipousuario);
                    oldRolcodigoRolOfTipousuarioListTipousuario = em.merge(oldRolcodigoRolOfTipousuarioListTipousuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRol(rol.getCodigoRol()) != null) {
                throw new PreexistingEntityException("Rol " + rol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rol persistentRol = em.find(Rol.class, rol.getCodigoRol());
            List<Permiso> permisoListOld = persistentRol.getPermisoList();
            List<Permiso> permisoListNew = rol.getPermisoList();
            List<Tipousuario> tipousuarioListOld = persistentRol.getTipousuarioList();
            List<Tipousuario> tipousuarioListNew = rol.getTipousuarioList();
            List<String> illegalOrphanMessages = null;
            for (Tipousuario tipousuarioListOldTipousuario : tipousuarioListOld) {
                if (!tipousuarioListNew.contains(tipousuarioListOldTipousuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tipousuario " + tipousuarioListOldTipousuario + " since its rolcodigoRol field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Permiso> attachedPermisoListNew = new ArrayList<Permiso>();
            for (Permiso permisoListNewPermisoToAttach : permisoListNew) {
                permisoListNewPermisoToAttach = em.getReference(permisoListNewPermisoToAttach.getClass(), permisoListNewPermisoToAttach.getCodigoPermiso());
                attachedPermisoListNew.add(permisoListNewPermisoToAttach);
            }
            permisoListNew = attachedPermisoListNew;
            rol.setPermisoList(permisoListNew);
            List<Tipousuario> attachedTipousuarioListNew = new ArrayList<Tipousuario>();
            for (Tipousuario tipousuarioListNewTipousuarioToAttach : tipousuarioListNew) {
                tipousuarioListNewTipousuarioToAttach = em.getReference(tipousuarioListNewTipousuarioToAttach.getClass(), tipousuarioListNewTipousuarioToAttach.getCodigoTipoUsuario());
                attachedTipousuarioListNew.add(tipousuarioListNewTipousuarioToAttach);
            }
            tipousuarioListNew = attachedTipousuarioListNew;
            rol.setTipousuarioList(tipousuarioListNew);
            rol = em.merge(rol);
            for (Permiso permisoListOldPermiso : permisoListOld) {
                if (!permisoListNew.contains(permisoListOldPermiso)) {
                    permisoListOldPermiso.getRolList().remove(rol);
                    permisoListOldPermiso = em.merge(permisoListOldPermiso);
                }
            }
            for (Permiso permisoListNewPermiso : permisoListNew) {
                if (!permisoListOld.contains(permisoListNewPermiso)) {
                    permisoListNewPermiso.getRolList().add(rol);
                    permisoListNewPermiso = em.merge(permisoListNewPermiso);
                }
            }
            for (Tipousuario tipousuarioListNewTipousuario : tipousuarioListNew) {
                if (!tipousuarioListOld.contains(tipousuarioListNewTipousuario)) {
                    Rol oldRolcodigoRolOfTipousuarioListNewTipousuario = tipousuarioListNewTipousuario.getRolcodigoRol();
                    tipousuarioListNewTipousuario.setRolcodigoRol(rol);
                    tipousuarioListNewTipousuario = em.merge(tipousuarioListNewTipousuario);
                    if (oldRolcodigoRolOfTipousuarioListNewTipousuario != null && !oldRolcodigoRolOfTipousuarioListNewTipousuario.equals(rol)) {
                        oldRolcodigoRolOfTipousuarioListNewTipousuario.getTipousuarioList().remove(tipousuarioListNewTipousuario);
                        oldRolcodigoRolOfTipousuarioListNewTipousuario = em.merge(oldRolcodigoRolOfTipousuarioListNewTipousuario);
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
                Integer id = rol.getCodigoRol();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
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
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getCodigoRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tipousuario> tipousuarioListOrphanCheck = rol.getTipousuarioList();
            for (Tipousuario tipousuarioListOrphanCheckTipousuario : tipousuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rol (" + rol + ") cannot be destroyed since the Tipousuario " + tipousuarioListOrphanCheckTipousuario + " in its tipousuarioList field has a non-nullable rolcodigoRol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Permiso> permisoList = rol.getPermisoList();
            for (Permiso permisoListPermiso : permisoList) {
                permisoListPermiso.getRolList().remove(rol);
                permisoListPermiso = em.merge(permisoListPermiso);
            }
            em.remove(rol);
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

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
