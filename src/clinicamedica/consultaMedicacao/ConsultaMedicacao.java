/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clinicamedica.consultaMedicacao;

import clinicamedica.consulta.Consulta;
import clinicamedica.medicacao.Medicacao;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author CristianeMarques
 */
@Entity
@Table(name = "consulta_medicacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsultaMedicacao.findAll", query = "SELECT c FROM ConsultaMedicacao c"),
    @NamedQuery(name = "ConsultaMedicacao.findByIdConsultaMedicacao", query = "SELECT c FROM ConsultaMedicacao c WHERE c.idConsultaMedicacao = :idConsultaMedicacao")})
public class ConsultaMedicacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consulta_medicacao")
    private Integer idConsultaMedicacao;
    @JoinColumn(name = "id_medicacao", referencedColumnName = "id_medicacao")
    @ManyToOne(optional = false)
    private Medicacao idMedicacao;
    @JoinColumn(name = "id_consulta", referencedColumnName = "id_consulta")
    @ManyToOne(optional = false)
    private Consulta idConsulta;

    public ConsultaMedicacao() {
    }

    public ConsultaMedicacao(Integer idConsultaMedicacao) {
        this.idConsultaMedicacao = idConsultaMedicacao;
    }

    public Integer getIdConsultaMedicacao() {
        return idConsultaMedicacao;
    }

    public void setIdConsultaMedicacao(Integer idConsultaMedicacao) {
        this.idConsultaMedicacao = idConsultaMedicacao;
    }

    public Medicacao getIdMedicacao() {
        return idMedicacao;
    }

    public void setIdMedicacao(Medicacao idMedicacao) {
        this.idMedicacao = idMedicacao;
    }

    public Consulta getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Consulta idConsulta) {
        this.idConsulta = idConsulta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsultaMedicacao != null ? idConsultaMedicacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsultaMedicacao)) {
            return false;
        }
        ConsultaMedicacao other = (ConsultaMedicacao) object;
        if ((this.idConsultaMedicacao == null && other.idConsultaMedicacao != null) || (this.idConsultaMedicacao != null && !this.idConsultaMedicacao.equals(other.idConsultaMedicacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clinicamedica.entity.ConsultaMedicacao[ idConsultaMedicacao=" + idConsultaMedicacao + " ]";
    }
    
}
