/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clinicamedica.consulta;

import clinicamedica.consultaMedicacao.ConsultaMedicacao;
import clinicamedica.paciente.Paciente;
import clinicamedica.medico.Medico;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CristianeMarques
 */
@Entity
@Table(name = "consulta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consulta.findAll", query = "SELECT c FROM Consulta c"),
    @NamedQuery(name = "Consulta.findByIdConsulta", query = "SELECT c FROM Consulta c WHERE c.idConsulta = :idConsulta"),
    @NamedQuery(name = "Consulta.findByDataHoraInicio", query = "SELECT c FROM Consulta c WHERE c.dataHoraInicio = :dataHoraInicio"),
    @NamedQuery(name = "Consulta.findByDataHoraFim", query = "SELECT c FROM Consulta c WHERE c.dataHoraFim = :dataHoraFim")})
public class Consulta implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consulta")
    private Integer idConsulta;
    @Basic(optional = false)
    @Column(name = "data_hora_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraInicio;
    @Basic(optional = false)
    @Column(name = "data_hora_fim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraFim;
    @Basic(optional = false)
    @Lob
    @Column(name = "descricao")
    private String descricao;
    @JoinColumn(name = "id_medico", referencedColumnName = "id_medico")
    @ManyToOne(optional = false)
    private Medico idMedico;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne(optional = false)
    private Paciente idPaciente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idConsulta")
    private Collection<ConsultaMedicacao> consultaMedicacaoCollection;

    public Consulta() {
    }

    public Consulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Consulta(Integer idConsulta, Date dataHoraInicio, Date dataHoraFim, String descricao) {
        this.idConsulta = idConsulta;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.descricao = descricao;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        Integer oldIdConsulta = this.idConsulta;
        this.idConsulta = idConsulta;
        changeSupport.firePropertyChange("idConsulta", oldIdConsulta, idConsulta);
    }

    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Date dataHoraInicio) {
        Date oldDataHoraInicio = this.dataHoraInicio;
        this.dataHoraInicio = dataHoraInicio;
        changeSupport.firePropertyChange("dataHoraInicio", oldDataHoraInicio, dataHoraInicio);
    }

    public Date getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Date dataHoraFim) {
        Date oldDataHoraFim = this.dataHoraFim;
        this.dataHoraFim = dataHoraFim;
        changeSupport.firePropertyChange("dataHoraFim", oldDataHoraFim, dataHoraFim);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        String oldDescricao = this.descricao;
        this.descricao = descricao;
        changeSupport.firePropertyChange("descricao", oldDescricao, descricao);
    }

    public Medico getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Medico idMedico) {
        Medico oldIdMedico = this.idMedico;
        this.idMedico = idMedico;
        changeSupport.firePropertyChange("idMedico", oldIdMedico, idMedico);
    }

    public Paciente getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Paciente idPaciente) {
        Paciente oldIdPaciente = this.idPaciente;
        this.idPaciente = idPaciente;
        changeSupport.firePropertyChange("idPaciente", oldIdPaciente, idPaciente);
    }

    @XmlTransient
    public Collection<ConsultaMedicacao> getConsultaMedicacaoCollection() {
        return consultaMedicacaoCollection;
    }

    public void setConsultaMedicacaoCollection(Collection<ConsultaMedicacao> consultaMedicacaoCollection) {
        this.consultaMedicacaoCollection = consultaMedicacaoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsulta != null ? idConsulta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consulta)) {
            return false;
        }
        Consulta other = (Consulta) object;
        if ((this.idConsulta == null && other.idConsulta != null) || (this.idConsulta != null && !this.idConsulta.equals(other.idConsulta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clinicamedica.entity.Consulta[ idConsulta=" + idConsulta + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
