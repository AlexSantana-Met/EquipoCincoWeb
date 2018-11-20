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
import entity.Login;
import entity.Citas;
import entity.Clientes;
import java.util.ArrayList;
import java.util.List;
import entity.Ordenes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro
 */
public class ClientesJpaController implements Serializable {

    public ClientesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
//        this.emf = Persistence.createEntityManagerFactory("OpticaAndes-PrograWebPU");
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (clientes.getCitasList() == null) {
            clientes.setCitasList(new ArrayList<Citas>());
        }
        if (clientes.getOrdenesList() == null) {
            clientes.setOrdenesList(new ArrayList<Ordenes>());
        }
        EntityManager em = getEntityManager();
        try {
//            utx.begin();
            em.getTransaction().begin();
//            em = getEntityManager();
            Login loginCorreo = clientes.getLoginCorreo();
            if (loginCorreo != null) {
                loginCorreo = em.getReference(loginCorreo.getClass(), loginCorreo.getCorreo());
                clientes.setLoginCorreo(loginCorreo);
            }
            List<Citas> attachedCitasList = new ArrayList<Citas>();
            for (Citas citasListCitasToAttach : clientes.getCitasList()) {
                citasListCitasToAttach = em.getReference(citasListCitasToAttach.getClass(), citasListCitasToAttach.getIdCita());
                attachedCitasList.add(citasListCitasToAttach);
            }
            clientes.setCitasList(attachedCitasList);
            List<Ordenes> attachedOrdenesList = new ArrayList<Ordenes>();
            for (Ordenes ordenesListOrdenesToAttach : clientes.getOrdenesList()) {
                ordenesListOrdenesToAttach = em.getReference(ordenesListOrdenesToAttach.getClass(), ordenesListOrdenesToAttach.getIdOrden());
                attachedOrdenesList.add(ordenesListOrdenesToAttach);
            }
            clientes.setOrdenesList(attachedOrdenesList);
            em.persist(clientes);
            if (loginCorreo != null) {
                loginCorreo.getClientesList().add(clientes);
                loginCorreo = em.merge(loginCorreo);
            }
            for (Citas citasListCitas : clientes.getCitasList()) {
                Clientes oldClientesIdOfCitasListCitas = citasListCitas.getClientesId();
                citasListCitas.setClientesId(clientes);
                citasListCitas = em.merge(citasListCitas);
                if (oldClientesIdOfCitasListCitas != null) {
                    oldClientesIdOfCitasListCitas.getCitasList().remove(citasListCitas);
                    oldClientesIdOfCitasListCitas = em.merge(oldClientesIdOfCitasListCitas);
                }
            }
            for (Ordenes ordenesListOrdenes : clientes.getOrdenesList()) {
                Clientes oldClientesIdOfOrdenesListOrdenes = ordenesListOrdenes.getClientesId();
                ordenesListOrdenes.setClientesId(clientes);
                ordenesListOrdenes = em.merge(ordenesListOrdenes);
                if (oldClientesIdOfOrdenesListOrdenes != null) {
                    oldClientesIdOfOrdenesListOrdenes.getOrdenesList().remove(ordenesListOrdenes);
                    oldClientesIdOfOrdenesListOrdenes = em.merge(oldClientesIdOfOrdenesListOrdenes);
                }
            }
//            utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
//                utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findClientes(clientes.getIdCliente()) != null) {
                throw new PreexistingEntityException("Clientes " + clientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clientes clientes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getIdCliente());
            Login loginCorreoOld = persistentClientes.getLoginCorreo();
            Login loginCorreoNew = clientes.getLoginCorreo();
            List<Citas> citasListOld = persistentClientes.getCitasList();
            List<Citas> citasListNew = clientes.getCitasList();
            List<Ordenes> ordenesListOld = persistentClientes.getOrdenesList();
            List<Ordenes> ordenesListNew = clientes.getOrdenesList();
            List<String> illegalOrphanMessages = null;
            for (Citas citasListOldCitas : citasListOld) {
                if (!citasListNew.contains(citasListOldCitas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citas " + citasListOldCitas + " since its clientesId field is not nullable.");
                }
            }
            for (Ordenes ordenesListOldOrdenes : ordenesListOld) {
                if (!ordenesListNew.contains(ordenesListOldOrdenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ordenes " + ordenesListOldOrdenes + " since its clientesId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (loginCorreoNew != null) {
                loginCorreoNew = em.getReference(loginCorreoNew.getClass(), loginCorreoNew.getCorreo());
                clientes.setLoginCorreo(loginCorreoNew);
            }
            List<Citas> attachedCitasListNew = new ArrayList<Citas>();
            for (Citas citasListNewCitasToAttach : citasListNew) {
                citasListNewCitasToAttach = em.getReference(citasListNewCitasToAttach.getClass(), citasListNewCitasToAttach.getIdCita());
                attachedCitasListNew.add(citasListNewCitasToAttach);
            }
            citasListNew = attachedCitasListNew;
            clientes.setCitasList(citasListNew);
            List<Ordenes> attachedOrdenesListNew = new ArrayList<Ordenes>();
            for (Ordenes ordenesListNewOrdenesToAttach : ordenesListNew) {
                ordenesListNewOrdenesToAttach = em.getReference(ordenesListNewOrdenesToAttach.getClass(), ordenesListNewOrdenesToAttach.getIdOrden());
                attachedOrdenesListNew.add(ordenesListNewOrdenesToAttach);
            }
            ordenesListNew = attachedOrdenesListNew;
            clientes.setOrdenesList(ordenesListNew);
            clientes = em.merge(clientes);
            if (loginCorreoOld != null && !loginCorreoOld.equals(loginCorreoNew)) {
                loginCorreoOld.getClientesList().remove(clientes);
                loginCorreoOld = em.merge(loginCorreoOld);
            }
            if (loginCorreoNew != null && !loginCorreoNew.equals(loginCorreoOld)) {
                loginCorreoNew.getClientesList().add(clientes);
                loginCorreoNew = em.merge(loginCorreoNew);
            }
            for (Citas citasListNewCitas : citasListNew) {
                if (!citasListOld.contains(citasListNewCitas)) {
                    Clientes oldClientesIdOfCitasListNewCitas = citasListNewCitas.getClientesId();
                    citasListNewCitas.setClientesId(clientes);
                    citasListNewCitas = em.merge(citasListNewCitas);
                    if (oldClientesIdOfCitasListNewCitas != null && !oldClientesIdOfCitasListNewCitas.equals(clientes)) {
                        oldClientesIdOfCitasListNewCitas.getCitasList().remove(citasListNewCitas);
                        oldClientesIdOfCitasListNewCitas = em.merge(oldClientesIdOfCitasListNewCitas);
                    }
                }
            }
            for (Ordenes ordenesListNewOrdenes : ordenesListNew) {
                if (!ordenesListOld.contains(ordenesListNewOrdenes)) {
                    Clientes oldClientesIdOfOrdenesListNewOrdenes = ordenesListNewOrdenes.getClientesId();
                    ordenesListNewOrdenes.setClientesId(clientes);
                    ordenesListNewOrdenes = em.merge(ordenesListNewOrdenes);
                    if (oldClientesIdOfOrdenesListNewOrdenes != null && !oldClientesIdOfOrdenesListNewOrdenes.equals(clientes)) {
                        oldClientesIdOfOrdenesListNewOrdenes.getOrdenesList().remove(ordenesListNewOrdenes);
                        oldClientesIdOfOrdenesListNewOrdenes = em.merge(oldClientesIdOfOrdenesListNewOrdenes);
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
                Integer id = clientes.getIdCliente();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
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
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Citas> citasListOrphanCheck = clientes.getCitasList();
            for (Citas citasListOrphanCheckCitas : citasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clientes (" + clientes + ") cannot be destroyed since the Citas " + citasListOrphanCheckCitas + " in its citasList field has a non-nullable clientesId field.");
            }
            List<Ordenes> ordenesListOrphanCheck = clientes.getOrdenesList();
            for (Ordenes ordenesListOrphanCheckOrdenes : ordenesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clientes (" + clientes + ") cannot be destroyed since the Ordenes " + ordenesListOrphanCheckOrdenes + " in its ordenesList field has a non-nullable clientesId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Login loginCorreo = clientes.getLoginCorreo();
            if (loginCorreo != null) {
                loginCorreo.getClientesList().remove(clientes);
                loginCorreo = em.merge(loginCorreo);
            }
            em.remove(clientes);
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

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
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

    public Clientes findClientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
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
        List<Clientes> lista = new ArrayList<>();
        TypedQuery<Clientes> query = em.createQuery("SELECT c FROM Clientes c", Clientes.class);
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return -1;
        } else {
            res = lista.get(lista.size() - 1).getIdCliente();
            return res;
//            res = (res < clientes.getIdCliente()) ? clientes.getIdCliente() : res;
        }
    }

}
