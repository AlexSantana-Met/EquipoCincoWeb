/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.exceptions.NonexistentEntityException;
import ManagedBean.exceptions.PreexistingEntityException;
import ManagedBean.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Clientes;
import entity.Ordenes;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro
 */
public class OrdenesJpaController implements Serializable {

    public OrdenesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ordenes ordenes) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clientes clientesId = ordenes.getClientesId();
            if (clientesId != null) {
                clientesId = em.getReference(clientesId.getClass(), clientesId.getIdCliente());
                ordenes.setClientesId(clientesId);
            }
            em.persist(ordenes);
            if (clientesId != null) {
                clientesId.getOrdenesList().add(ordenes);
                clientesId = em.merge(clientesId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findOrdenes(ordenes.getIdOrden()) != null) {
                throw new PreexistingEntityException("Ordenes " + ordenes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ordenes ordenes) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ordenes persistentOrdenes = em.find(Ordenes.class, ordenes.getIdOrden());
            Clientes clientesIdOld = persistentOrdenes.getClientesId();
            Clientes clientesIdNew = ordenes.getClientesId();
            if (clientesIdNew != null) {
                clientesIdNew = em.getReference(clientesIdNew.getClass(), clientesIdNew.getIdCliente());
                ordenes.setClientesId(clientesIdNew);
            }
            ordenes = em.merge(ordenes);
            if (clientesIdOld != null && !clientesIdOld.equals(clientesIdNew)) {
                clientesIdOld.getOrdenesList().remove(ordenes);
                clientesIdOld = em.merge(clientesIdOld);
            }
            if (clientesIdNew != null && !clientesIdNew.equals(clientesIdOld)) {
                clientesIdNew.getOrdenesList().add(ordenes);
                clientesIdNew = em.merge(clientesIdNew);
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
                Integer id = ordenes.getIdOrden();
                if (findOrdenes(id) == null) {
                    throw new NonexistentEntityException("The ordenes with id " + id + " no longer exists.");
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
            Ordenes ordenes;
            try {
                ordenes = em.getReference(Ordenes.class, id);
                ordenes.getIdOrden();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordenes with id " + id + " no longer exists.", enfe);
            }
            Clientes clientesId = ordenes.getClientesId();
            if (clientesId != null) {
                clientesId.getOrdenesList().remove(ordenes);
                clientesId = em.merge(clientesId);
            }
            em.remove(ordenes);
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

    public List<Ordenes> findOrdenesEntities() {
        return findOrdenesEntities(true, -1, -1);
    }

    public List<Ordenes> findOrdenesEntities(int maxResults, int firstResult) {
        return findOrdenesEntities(false, maxResults, firstResult);
    }

    private List<Ordenes> findOrdenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ordenes.class));
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

    public Ordenes findOrdenes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ordenes.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ordenes> rt = cq.from(Ordenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Ordenes> findByIdCliente(int idCliente) {
        EntityManager em = getEntityManager();
        List<Ordenes> lista = new ArrayList<>();
        TypedQuery<Ordenes> query = em.createQuery("SELECT o FROM Ordenes o WHERE o.clientesId.idCliente = " + idCliente, Ordenes.class);
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return null;
        } else {
            return lista;
        }
    }

}
