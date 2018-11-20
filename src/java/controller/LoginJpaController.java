/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.exceptions.IllegalOrphanException;
import ManagedBean.exceptions.NonexistentEntityException;
import ManagedBean.exceptions.PreexistingEntityException;
import ManagedBean.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Clientes;
import entity.Login;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro
 */
public class LoginJpaController implements Serializable {

    public LoginJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Login login) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (login.getClientesList() == null) {
            login.setClientesList(new ArrayList<>());
        }
        EntityManager em = getEntityManager();
        try {
//            utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            List<Clientes> attachedClientesList = new ArrayList<Clientes>();
            for (Clientes clientesListClientesToAttach : login.getClientesList()) {
                clientesListClientesToAttach = em.getReference(clientesListClientesToAttach.getClass(), clientesListClientesToAttach.getIdCliente());
                attachedClientesList.add(clientesListClientesToAttach);
            }
            login.setClientesList(attachedClientesList);
            em.persist(login);
            for (Clientes clientesListClientes : login.getClientesList()) {
                Login oldLoginCorreoOfClientesListClientes = clientesListClientes.getLoginCorreo();
                clientesListClientes.setLoginCorreo(login);
                clientesListClientes = em.merge(clientesListClientes);
                if (oldLoginCorreoOfClientesListClientes != null) {
                    oldLoginCorreoOfClientesListClientes.getClientesList().remove(clientesListClientes);
                    oldLoginCorreoOfClientesListClientes = em.merge(oldLoginCorreoOfClientesListClientes);
                }
            }
            em.getTransaction().commit();
//            utx.commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
//                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findLogin(login.getCorreo()) != null) {
                throw new PreexistingEntityException("Login " + login + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Login login) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Login persistentLogin = em.find(Login.class, login.getCorreo());
            List<Clientes> clientesListOld = persistentLogin.getClientesList();
            List<Clientes> clientesListNew = login.getClientesList();
            List<String> illegalOrphanMessages = null;
            for (Clientes clientesListOldClientes : clientesListOld) {
                if (!clientesListNew.contains(clientesListOldClientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clientes " + clientesListOldClientes + " since its loginCorreo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Clientes> attachedClientesListNew = new ArrayList<Clientes>();
            for (Clientes clientesListNewClientesToAttach : clientesListNew) {
                clientesListNewClientesToAttach = em.getReference(clientesListNewClientesToAttach.getClass(), clientesListNewClientesToAttach.getIdCliente());
                attachedClientesListNew.add(clientesListNewClientesToAttach);
            }
            clientesListNew = attachedClientesListNew;
            login.setClientesList(clientesListNew);
            login = em.merge(login);
            for (Clientes clientesListNewClientes : clientesListNew) {
                if (!clientesListOld.contains(clientesListNewClientes)) {
                    Login oldLoginCorreoOfClientesListNewClientes = clientesListNewClientes.getLoginCorreo();
                    clientesListNewClientes.setLoginCorreo(login);
                    clientesListNewClientes = em.merge(clientesListNewClientes);
                    if (oldLoginCorreoOfClientesListNewClientes != null && !oldLoginCorreoOfClientesListNewClientes.equals(login)) {
                        oldLoginCorreoOfClientesListNewClientes.getClientesList().remove(clientesListNewClientes);
                        oldLoginCorreoOfClientesListNewClientes = em.merge(oldLoginCorreoOfClientesListNewClientes);
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
                String id = login.getCorreo();
                if (findLogin(id) == null) {
                    throw new NonexistentEntityException("The login with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Login login;
            try {
                login = em.getReference(Login.class, id);
                login.getCorreo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The login with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Clientes> clientesListOrphanCheck = login.getClientesList();
            for (Clientes clientesListOrphanCheckClientes : clientesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Login (" + login + ") cannot be destroyed since the Clientes " + clientesListOrphanCheckClientes + " in its clientesList field has a non-nullable loginCorreo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(login);
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

    public List<Login> findLoginEntities() {
        return findLoginEntities(true, -1, -1);
    }

    public List<Login> findLoginEntities(int maxResults, int firstResult) {
        return findLoginEntities(false, maxResults, firstResult);
    }

    private List<Login> findLoginEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Login.class));
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

    public Login findLogin(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Login.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoginCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Login> rt = cq.from(Login.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
