/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.exceptions.NonexistentEntityException;
import ManagedBean.exceptions.PreexistingEntityException;
import ManagedBean.exceptions.RollbackFailureException;
import entity.Citas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Clientes;
import entity.Empleados;
import java.util.ArrayList;
import java.util.Date;
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
public class CitasJpaController implements Serializable {

    public CitasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Citas citas) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        try {
//            utx.begin();
//            em = getEntityManager();
            em.getTransaction().begin();
            Clientes clientesId = citas.getClientesId();
            if (clientesId != null) {
                clientesId = em.getReference(clientesId.getClass(), clientesId.getIdCliente());
                citas.setClientesId(clientesId);
            }
            Empleados empleadosId = citas.getEmpleadosId();
            if (empleadosId != null) {
                empleadosId = em.getReference(empleadosId.getClass(), empleadosId.getIdEmpleado());
                citas.setEmpleadosId(empleadosId);
            }
            em.persist(citas);
            if (clientesId != null) {
                clientesId.getCitasList().add(citas);
                clientesId = em.merge(clientesId);
            }
            if (empleadosId != null) {
                empleadosId.getCitasList().add(citas);
                empleadosId = em.merge(empleadosId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
//                utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCitas(citas.getIdCita()) != null) {
                throw new PreexistingEntityException("Citas " + citas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Citas citas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Citas persistentCitas = em.find(Citas.class, citas.getIdCita());
            Clientes clientesIdOld = persistentCitas.getClientesId();
            Clientes clientesIdNew = citas.getClientesId();
            Empleados empleadosIdOld = persistentCitas.getEmpleadosId();
            Empleados empleadosIdNew = citas.getEmpleadosId();
            if (clientesIdNew != null) {
                clientesIdNew = em.getReference(clientesIdNew.getClass(), clientesIdNew.getIdCliente());
                citas.setClientesId(clientesIdNew);
            }
            if (empleadosIdNew != null) {
                empleadosIdNew = em.getReference(empleadosIdNew.getClass(), empleadosIdNew.getIdEmpleado());
                citas.setEmpleadosId(empleadosIdNew);
            }
            citas = em.merge(citas);
            if (clientesIdOld != null && !clientesIdOld.equals(clientesIdNew)) {
                clientesIdOld.getCitasList().remove(citas);
                clientesIdOld = em.merge(clientesIdOld);
            }
            if (clientesIdNew != null && !clientesIdNew.equals(clientesIdOld)) {
                clientesIdNew.getCitasList().add(citas);
                clientesIdNew = em.merge(clientesIdNew);
            }
            if (empleadosIdOld != null && !empleadosIdOld.equals(empleadosIdNew)) {
                empleadosIdOld.getCitasList().remove(citas);
                empleadosIdOld = em.merge(empleadosIdOld);
            }
            if (empleadosIdNew != null && !empleadosIdNew.equals(empleadosIdOld)) {
                empleadosIdNew.getCitasList().add(citas);
                empleadosIdNew = em.merge(empleadosIdNew);
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
                Integer id = citas.getIdCita();
                if (findCitas(id) == null) {
                    throw new NonexistentEntityException("The citas with id " + id + " no longer exists.");
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
            Citas citas;
            try {
                citas = em.getReference(Citas.class, id);
                citas.getIdCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The citas with id " + id + " no longer exists.", enfe);
            }
            Clientes clientesId = citas.getClientesId();
            if (clientesId != null) {
                clientesId.getCitasList().remove(citas);
                clientesId = em.merge(clientesId);
            }
            Empleados empleadosId = citas.getEmpleadosId();
            if (empleadosId != null) {
                empleadosId.getCitasList().remove(citas);
                empleadosId = em.merge(empleadosId);
            }
            em.remove(citas);
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

    public List<Citas> findCitasEntities() {
        return findCitasEntities(true, -1, -1);
    }

    public List<Citas> findCitasEntities(int maxResults, int firstResult) {
        return findCitasEntities(false, maxResults, firstResult);
    }

    private List<Citas> findCitasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Citas.class));
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

    public Citas findCitas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Citas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Citas> rt = cq.from(Citas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Citas> findByIdCliente(int idCliente) {
        EntityManager em = getEntityManager();
        List<Citas> lista = new ArrayList<>();
        TypedQuery<Citas> query = em.createQuery("SELECT c FROM Citas c WHERE c.clientesId.idCliente = " + idCliente, Citas.class);
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return null;
        } else {
            return lista;
        }
    }

    public List<String> getHorasCitas(int dia, int mes, int anio) {
        EntityManager em = getEntityManager();
        List<Date> lista2 = new ArrayList<>();
        List<Citas> lista = new ArrayList<>();
        List<String> result = new ArrayList<>();

//        TypedQuery<Citas> query = em.createQuery("SELECT c FROM Citas c WHERE c.dia=:dia AND c.mes=:mes AND c.anio=:anio", Citas.class);
        TypedQuery<Citas> query = em.createQuery("SELECT c FROM Citas c WHERE c.dia = " + dia + " AND c.mes = " + mes + " AND c.anio = " + anio, Citas.class);
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return null;
        } else {
            for (Citas citas : lista) {
                lista2.add(citas.getHora());
            }
            for (Date date : lista2) {
                String horas = date.getHours() < 10 ? "0" + String.valueOf(date.getHours()) : String.valueOf(date.getHours());
                result.add(horas + ":00");
            }
            return result;
        }
    }

    public int getMaxId() {
        int res = -1;
        EntityManager em = getEntityManager();
        List<Citas> lista = new ArrayList<>();
        TypedQuery<Citas> query = em.createQuery("SELECT c FROM Citas c", Citas.class);
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return -1;
        } else {
            res = lista.get(lista.size() - 1).getIdCita();
            return res;
        }
    }

}
