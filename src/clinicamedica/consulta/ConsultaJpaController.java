/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clinicamedica.consulta;

import clinicamedica.controller.exceptions.IllegalOrphanException;
import clinicamedica.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import clinicamedica.medico.Medico;
import clinicamedica.paciente.Paciente;
import clinicamedica.consultaMedicacao.ConsultaMedicacao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author CristianeMarques
 */
public class ConsultaJpaController implements Serializable {

    public ConsultaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consulta consulta) {
        if (consulta.getConsultaMedicacaoCollection() == null) {
            consulta.setConsultaMedicacaoCollection(new ArrayList<ConsultaMedicacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medico idMedico = consulta.getIdMedico();
            if (idMedico != null) {
                idMedico = em.getReference(idMedico.getClass(), idMedico.getIdMedico());
                consulta.setIdMedico(idMedico);
            }
            Paciente idPaciente = consulta.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                consulta.setIdPaciente(idPaciente);
            }
            Collection<ConsultaMedicacao> attachedConsultaMedicacaoCollection = new ArrayList<ConsultaMedicacao>();
            for (ConsultaMedicacao consultaMedicacaoCollectionConsultaMedicacaoToAttach : consulta.getConsultaMedicacaoCollection()) {
                consultaMedicacaoCollectionConsultaMedicacaoToAttach = em.getReference(consultaMedicacaoCollectionConsultaMedicacaoToAttach.getClass(), consultaMedicacaoCollectionConsultaMedicacaoToAttach.getIdConsultaMedicacao());
                attachedConsultaMedicacaoCollection.add(consultaMedicacaoCollectionConsultaMedicacaoToAttach);
            }
            consulta.setConsultaMedicacaoCollection(attachedConsultaMedicacaoCollection);
            em.persist(consulta);
            if (idMedico != null) {
                idMedico.getConsultaCollection().add(consulta);
                idMedico = em.merge(idMedico);
            }
            if (idPaciente != null) {
                idPaciente.getConsultaCollection().add(consulta);
                idPaciente = em.merge(idPaciente);
            }
            for (ConsultaMedicacao consultaMedicacaoCollectionConsultaMedicacao : consulta.getConsultaMedicacaoCollection()) {
                Consulta oldIdConsultaOfConsultaMedicacaoCollectionConsultaMedicacao = consultaMedicacaoCollectionConsultaMedicacao.getIdConsulta();
                consultaMedicacaoCollectionConsultaMedicacao.setIdConsulta(consulta);
                consultaMedicacaoCollectionConsultaMedicacao = em.merge(consultaMedicacaoCollectionConsultaMedicacao);
                if (oldIdConsultaOfConsultaMedicacaoCollectionConsultaMedicacao != null) {
                    oldIdConsultaOfConsultaMedicacaoCollectionConsultaMedicacao.getConsultaMedicacaoCollection().remove(consultaMedicacaoCollectionConsultaMedicacao);
                    oldIdConsultaOfConsultaMedicacaoCollectionConsultaMedicacao = em.merge(oldIdConsultaOfConsultaMedicacaoCollectionConsultaMedicacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Consulta consulta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consulta persistentConsulta = em.find(Consulta.class, consulta.getIdConsulta());
            Medico idMedicoOld = persistentConsulta.getIdMedico();
            Medico idMedicoNew = consulta.getIdMedico();
            Paciente idPacienteOld = persistentConsulta.getIdPaciente();
            Paciente idPacienteNew = consulta.getIdPaciente();
            Collection<ConsultaMedicacao> consultaMedicacaoCollectionOld = persistentConsulta.getConsultaMedicacaoCollection();
            Collection<ConsultaMedicacao> consultaMedicacaoCollectionNew = consulta.getConsultaMedicacaoCollection();
            List<String> illegalOrphanMessages = null;
            for (ConsultaMedicacao consultaMedicacaoCollectionOldConsultaMedicacao : consultaMedicacaoCollectionOld) {
                if (!consultaMedicacaoCollectionNew.contains(consultaMedicacaoCollectionOldConsultaMedicacao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConsultaMedicacao " + consultaMedicacaoCollectionOldConsultaMedicacao + " since its idConsulta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idMedicoNew != null) {
                idMedicoNew = em.getReference(idMedicoNew.getClass(), idMedicoNew.getIdMedico());
                consulta.setIdMedico(idMedicoNew);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                consulta.setIdPaciente(idPacienteNew);
            }
            Collection<ConsultaMedicacao> attachedConsultaMedicacaoCollectionNew = new ArrayList<ConsultaMedicacao>();
            for (ConsultaMedicacao consultaMedicacaoCollectionNewConsultaMedicacaoToAttach : consultaMedicacaoCollectionNew) {
                consultaMedicacaoCollectionNewConsultaMedicacaoToAttach = em.getReference(consultaMedicacaoCollectionNewConsultaMedicacaoToAttach.getClass(), consultaMedicacaoCollectionNewConsultaMedicacaoToAttach.getIdConsultaMedicacao());
                attachedConsultaMedicacaoCollectionNew.add(consultaMedicacaoCollectionNewConsultaMedicacaoToAttach);
            }
            consultaMedicacaoCollectionNew = attachedConsultaMedicacaoCollectionNew;
            consulta.setConsultaMedicacaoCollection(consultaMedicacaoCollectionNew);
            consulta = em.merge(consulta);
            if (idMedicoOld != null && !idMedicoOld.equals(idMedicoNew)) {
                idMedicoOld.getConsultaCollection().remove(consulta);
                idMedicoOld = em.merge(idMedicoOld);
            }
            if (idMedicoNew != null && !idMedicoNew.equals(idMedicoOld)) {
                idMedicoNew.getConsultaCollection().add(consulta);
                idMedicoNew = em.merge(idMedicoNew);
            }
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getConsultaCollection().remove(consulta);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getConsultaCollection().add(consulta);
                idPacienteNew = em.merge(idPacienteNew);
            }
            for (ConsultaMedicacao consultaMedicacaoCollectionNewConsultaMedicacao : consultaMedicacaoCollectionNew) {
                if (!consultaMedicacaoCollectionOld.contains(consultaMedicacaoCollectionNewConsultaMedicacao)) {
                    Consulta oldIdConsultaOfConsultaMedicacaoCollectionNewConsultaMedicacao = consultaMedicacaoCollectionNewConsultaMedicacao.getIdConsulta();
                    consultaMedicacaoCollectionNewConsultaMedicacao.setIdConsulta(consulta);
                    consultaMedicacaoCollectionNewConsultaMedicacao = em.merge(consultaMedicacaoCollectionNewConsultaMedicacao);
                    if (oldIdConsultaOfConsultaMedicacaoCollectionNewConsultaMedicacao != null && !oldIdConsultaOfConsultaMedicacaoCollectionNewConsultaMedicacao.equals(consulta)) {
                        oldIdConsultaOfConsultaMedicacaoCollectionNewConsultaMedicacao.getConsultaMedicacaoCollection().remove(consultaMedicacaoCollectionNewConsultaMedicacao);
                        oldIdConsultaOfConsultaMedicacaoCollectionNewConsultaMedicacao = em.merge(oldIdConsultaOfConsultaMedicacaoCollectionNewConsultaMedicacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consulta.getIdConsulta();
                if (findConsulta(id) == null) {
                    throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.");
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
            Consulta consulta;
            try {
                consulta = em.getReference(Consulta.class, id);
                consulta.getIdConsulta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ConsultaMedicacao> consultaMedicacaoCollectionOrphanCheck = consulta.getConsultaMedicacaoCollection();
            for (ConsultaMedicacao consultaMedicacaoCollectionOrphanCheckConsultaMedicacao : consultaMedicacaoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Consulta (" + consulta + ") cannot be destroyed since the ConsultaMedicacao " + consultaMedicacaoCollectionOrphanCheckConsultaMedicacao + " in its consultaMedicacaoCollection field has a non-nullable idConsulta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Medico idMedico = consulta.getIdMedico();
            if (idMedico != null) {
                idMedico.getConsultaCollection().remove(consulta);
                idMedico = em.merge(idMedico);
            }
            Paciente idPaciente = consulta.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getConsultaCollection().remove(consulta);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(consulta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Consulta> findConsultaEntities() {
        return findConsultaEntities(true, -1, -1);
    }

    public List<Consulta> findConsultaEntities(int maxResults, int firstResult) {
        return findConsultaEntities(false, maxResults, firstResult);
    }

    private List<Consulta> findConsultaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consulta.class));
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

    public Consulta findConsulta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consulta.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consulta> rt = cq.from(Consulta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
