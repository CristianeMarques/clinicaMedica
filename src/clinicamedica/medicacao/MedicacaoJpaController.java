/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clinicamedica.medicacao;

import clinicamedica.controller.exceptions.IllegalOrphanException;
import clinicamedica.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import clinicamedica.consultaMedicacao.ConsultaMedicacao;
import clinicamedica.medicacao.Medicacao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author CristianeMarques
 */
public class MedicacaoJpaController implements Serializable {

    public MedicacaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicacao medicacao) {
        if (medicacao.getConsultaMedicacaoCollection() == null) {
            medicacao.setConsultaMedicacaoCollection(new ArrayList<ConsultaMedicacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ConsultaMedicacao> attachedConsultaMedicacaoCollection = new ArrayList<ConsultaMedicacao>();
            for (ConsultaMedicacao consultaMedicacaoCollectionConsultaMedicacaoToAttach : medicacao.getConsultaMedicacaoCollection()) {
                consultaMedicacaoCollectionConsultaMedicacaoToAttach = em.getReference(consultaMedicacaoCollectionConsultaMedicacaoToAttach.getClass(), consultaMedicacaoCollectionConsultaMedicacaoToAttach.getIdConsultaMedicacao());
                attachedConsultaMedicacaoCollection.add(consultaMedicacaoCollectionConsultaMedicacaoToAttach);
            }
            medicacao.setConsultaMedicacaoCollection(attachedConsultaMedicacaoCollection);
            em.persist(medicacao);
            for (ConsultaMedicacao consultaMedicacaoCollectionConsultaMedicacao : medicacao.getConsultaMedicacaoCollection()) {
                Medicacao oldIdMedicacaoOfConsultaMedicacaoCollectionConsultaMedicacao = consultaMedicacaoCollectionConsultaMedicacao.getIdMedicacao();
                consultaMedicacaoCollectionConsultaMedicacao.setIdMedicacao(medicacao);
                consultaMedicacaoCollectionConsultaMedicacao = em.merge(consultaMedicacaoCollectionConsultaMedicacao);
                if (oldIdMedicacaoOfConsultaMedicacaoCollectionConsultaMedicacao != null) {
                    oldIdMedicacaoOfConsultaMedicacaoCollectionConsultaMedicacao.getConsultaMedicacaoCollection().remove(consultaMedicacaoCollectionConsultaMedicacao);
                    oldIdMedicacaoOfConsultaMedicacaoCollectionConsultaMedicacao = em.merge(oldIdMedicacaoOfConsultaMedicacaoCollectionConsultaMedicacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medicacao medicacao) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicacao persistentMedicacao = em.find(Medicacao.class, medicacao.getIdMedicacao());
            Collection<ConsultaMedicacao> consultaMedicacaoCollectionOld = persistentMedicacao.getConsultaMedicacaoCollection();
            Collection<ConsultaMedicacao> consultaMedicacaoCollectionNew = medicacao.getConsultaMedicacaoCollection();
            List<String> illegalOrphanMessages = null;
            for (ConsultaMedicacao consultaMedicacaoCollectionOldConsultaMedicacao : consultaMedicacaoCollectionOld) {
                if (!consultaMedicacaoCollectionNew.contains(consultaMedicacaoCollectionOldConsultaMedicacao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConsultaMedicacao " + consultaMedicacaoCollectionOldConsultaMedicacao + " since its idMedicacao field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ConsultaMedicacao> attachedConsultaMedicacaoCollectionNew = new ArrayList<ConsultaMedicacao>();
            for (ConsultaMedicacao consultaMedicacaoCollectionNewConsultaMedicacaoToAttach : consultaMedicacaoCollectionNew) {
                consultaMedicacaoCollectionNewConsultaMedicacaoToAttach = em.getReference(consultaMedicacaoCollectionNewConsultaMedicacaoToAttach.getClass(), consultaMedicacaoCollectionNewConsultaMedicacaoToAttach.getIdConsultaMedicacao());
                attachedConsultaMedicacaoCollectionNew.add(consultaMedicacaoCollectionNewConsultaMedicacaoToAttach);
            }
            consultaMedicacaoCollectionNew = attachedConsultaMedicacaoCollectionNew;
            medicacao.setConsultaMedicacaoCollection(consultaMedicacaoCollectionNew);
            medicacao = em.merge(medicacao);
            for (ConsultaMedicacao consultaMedicacaoCollectionNewConsultaMedicacao : consultaMedicacaoCollectionNew) {
                if (!consultaMedicacaoCollectionOld.contains(consultaMedicacaoCollectionNewConsultaMedicacao)) {
                    Medicacao oldIdMedicacaoOfConsultaMedicacaoCollectionNewConsultaMedicacao = consultaMedicacaoCollectionNewConsultaMedicacao.getIdMedicacao();
                    consultaMedicacaoCollectionNewConsultaMedicacao.setIdMedicacao(medicacao);
                    consultaMedicacaoCollectionNewConsultaMedicacao = em.merge(consultaMedicacaoCollectionNewConsultaMedicacao);
                    if (oldIdMedicacaoOfConsultaMedicacaoCollectionNewConsultaMedicacao != null && !oldIdMedicacaoOfConsultaMedicacaoCollectionNewConsultaMedicacao.equals(medicacao)) {
                        oldIdMedicacaoOfConsultaMedicacaoCollectionNewConsultaMedicacao.getConsultaMedicacaoCollection().remove(consultaMedicacaoCollectionNewConsultaMedicacao);
                        oldIdMedicacaoOfConsultaMedicacaoCollectionNewConsultaMedicacao = em.merge(oldIdMedicacaoOfConsultaMedicacaoCollectionNewConsultaMedicacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = medicacao.getIdMedicacao();
                if (findMedicacao(id) == null) {
                    throw new NonexistentEntityException("The medicacao with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicacao medicacao;
            try {
                medicacao = em.getReference(Medicacao.class, id);
                medicacao.getIdMedicacao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicacao with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ConsultaMedicacao> consultaMedicacaoCollectionOrphanCheck = medicacao.getConsultaMedicacaoCollection();
            for (ConsultaMedicacao consultaMedicacaoCollectionOrphanCheckConsultaMedicacao : consultaMedicacaoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicacao (" + medicacao + ") cannot be destroyed since the ConsultaMedicacao " + consultaMedicacaoCollectionOrphanCheckConsultaMedicacao + " in its consultaMedicacaoCollection field has a non-nullable idMedicacao field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(medicacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medicacao> findMedicacaoEntities() {
        return findMedicacaoEntities(true, -1, -1);
    }

    public List<Medicacao> findMedicacaoEntities(int maxResults, int firstResult) {
        return findMedicacaoEntities(false, maxResults, firstResult);
    }

    private List<Medicacao> findMedicacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicacao.class));
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

    public Medicacao findMedicacao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicacao> rt = cq.from(Medicacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
