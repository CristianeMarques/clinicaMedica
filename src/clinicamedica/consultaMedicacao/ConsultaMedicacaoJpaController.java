/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clinicamedica.consultaMedicacao;

import clinicamedica.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import clinicamedica.medicacao.Medicacao;
import clinicamedica.consulta.Consulta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author CristianeMarques
 */
public class ConsultaMedicacaoJpaController implements Serializable {

    public ConsultaMedicacaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConsultaMedicacao consultaMedicacao) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicacao idMedicacao = consultaMedicacao.getIdMedicacao();
            if (idMedicacao != null) {
                idMedicacao = em.getReference(idMedicacao.getClass(), idMedicacao.getIdMedicacao());
                consultaMedicacao.setIdMedicacao(idMedicacao);
            }
            Consulta idConsulta = consultaMedicacao.getIdConsulta();
            if (idConsulta != null) {
                idConsulta = em.getReference(idConsulta.getClass(), idConsulta.getIdConsulta());
                consultaMedicacao.setIdConsulta(idConsulta);
            }
            em.persist(consultaMedicacao);
            if (idMedicacao != null) {
                idMedicacao.getConsultaMedicacaoCollection().add(consultaMedicacao);
                idMedicacao = em.merge(idMedicacao);
            }
            if (idConsulta != null) {
                idConsulta.getConsultaMedicacaoCollection().add(consultaMedicacao);
                idConsulta = em.merge(idConsulta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConsultaMedicacao consultaMedicacao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConsultaMedicacao persistentConsultaMedicacao = em.find(ConsultaMedicacao.class, consultaMedicacao.getIdConsultaMedicacao());
            Medicacao idMedicacaoOld = persistentConsultaMedicacao.getIdMedicacao();
            Medicacao idMedicacaoNew = consultaMedicacao.getIdMedicacao();
            Consulta idConsultaOld = persistentConsultaMedicacao.getIdConsulta();
            Consulta idConsultaNew = consultaMedicacao.getIdConsulta();
            if (idMedicacaoNew != null) {
                idMedicacaoNew = em.getReference(idMedicacaoNew.getClass(), idMedicacaoNew.getIdMedicacao());
                consultaMedicacao.setIdMedicacao(idMedicacaoNew);
            }
            if (idConsultaNew != null) {
                idConsultaNew = em.getReference(idConsultaNew.getClass(), idConsultaNew.getIdConsulta());
                consultaMedicacao.setIdConsulta(idConsultaNew);
            }
            consultaMedicacao = em.merge(consultaMedicacao);
            if (idMedicacaoOld != null && !idMedicacaoOld.equals(idMedicacaoNew)) {
                idMedicacaoOld.getConsultaMedicacaoCollection().remove(consultaMedicacao);
                idMedicacaoOld = em.merge(idMedicacaoOld);
            }
            if (idMedicacaoNew != null && !idMedicacaoNew.equals(idMedicacaoOld)) {
                idMedicacaoNew.getConsultaMedicacaoCollection().add(consultaMedicacao);
                idMedicacaoNew = em.merge(idMedicacaoNew);
            }
            if (idConsultaOld != null && !idConsultaOld.equals(idConsultaNew)) {
                idConsultaOld.getConsultaMedicacaoCollection().remove(consultaMedicacao);
                idConsultaOld = em.merge(idConsultaOld);
            }
            if (idConsultaNew != null && !idConsultaNew.equals(idConsultaOld)) {
                idConsultaNew.getConsultaMedicacaoCollection().add(consultaMedicacao);
                idConsultaNew = em.merge(idConsultaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consultaMedicacao.getIdConsultaMedicacao();
                if (findConsultaMedicacao(id) == null) {
                    throw new NonexistentEntityException("The consultaMedicacao with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConsultaMedicacao consultaMedicacao;
            try {
                consultaMedicacao = em.getReference(ConsultaMedicacao.class, id);
                consultaMedicacao.getIdConsultaMedicacao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consultaMedicacao with id " + id + " no longer exists.", enfe);
            }
            Medicacao idMedicacao = consultaMedicacao.getIdMedicacao();
            if (idMedicacao != null) {
                idMedicacao.getConsultaMedicacaoCollection().remove(consultaMedicacao);
                idMedicacao = em.merge(idMedicacao);
            }
            Consulta idConsulta = consultaMedicacao.getIdConsulta();
            if (idConsulta != null) {
                idConsulta.getConsultaMedicacaoCollection().remove(consultaMedicacao);
                idConsulta = em.merge(idConsulta);
            }
            em.remove(consultaMedicacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConsultaMedicacao> findConsultaMedicacaoEntities() {
        return findConsultaMedicacaoEntities(true, -1, -1);
    }

    public List<ConsultaMedicacao> findConsultaMedicacaoEntities(int maxResults, int firstResult) {
        return findConsultaMedicacaoEntities(false, maxResults, firstResult);
    }

    private List<ConsultaMedicacao> findConsultaMedicacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConsultaMedicacao.class));
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

    public ConsultaMedicacao findConsultaMedicacao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConsultaMedicacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultaMedicacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConsultaMedicacao> rt = cq.from(ConsultaMedicacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
