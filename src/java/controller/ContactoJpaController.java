/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.exceptions.NonexistentEntityException;
import ManagedBean.exceptions.PreexistingEntityException;
import ManagedBean.exceptions.RollbackFailureException;
import entity.Clientes;
import entity.Contacto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro
 */
public class ContactoJpaController implements Serializable {

    public ContactoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contacto contacto) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        try {
//            utx.begin();
            em.getTransaction().begin();
//            em = getEntityManager();
            em.persist(contacto);
//            utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
//                utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findContacto(contacto.getIdContacto()) != null) {
                throw new PreexistingEntityException("Contacto " + contacto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contacto contacto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        try {
//            utx.begin();
//            em = getEntityManager();
            em.getTransaction().begin();
            contacto = em.merge(contacto);
//            utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
//                utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contacto.getIdContacto();
                if (findContacto(id) == null) {
                    throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.");
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
        EntityManager em = getEntityManager();
        try {
//            utx.begin();
//            em = getEntityManager();
            em.getTransaction().begin();
            Contacto contacto;
            try {
                contacto = em.getReference(Contacto.class, id);
                contacto.getIdContacto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.", enfe);
            }
            em.remove(contacto);
//            utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
//                utx.rollback();
                em.getTransaction().rollback();
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

    public List<Contacto> findContactoEntities() {
        return findContactoEntities(true, -1, -1);
    }

    public List<Contacto> findContactoEntities(int maxResults, int firstResult) {
        return findContactoEntities(false, maxResults, firstResult);
    }

    private List<Contacto> findContactoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contacto.class));
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

    public Contacto findContacto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contacto.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contacto> rt = cq.from(Contacto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getMaxId() {
        int res = -1;
        EntityManager em = getEntityManager();
        List<Contacto> lista = new ArrayList<>();
        TypedQuery<Contacto> query = em.createQuery("SELECT c FROM Contacto c", Contacto.class);
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return -1;
        } else {
            res = lista.get(lista.size() - 1).getIdContacto();
            return res;
        }
    }
    
}
