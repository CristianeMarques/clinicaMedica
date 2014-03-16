/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clinicamedica.medicacao;

import clinicamedica.consultaMedicacao.ConsultaMedicacao;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CristianeMarques
 */
@Entity
@Table(name = "medicacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medicacao.findAll", query = "SELECT m FROM Medicacao m"),
    @NamedQuery(name = "Medicacao.findByIdMedicacao", query = "SELECT m FROM Medicacao m WHERE m.idMedicacao = :idMedicacao"),
    @NamedQuery(name = "Medicacao.findByNome", query = "SELECT m FROM Medicacao m WHERE m.nome = :nome")})
public class Medicacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_medicacao")
    private Integer idMedicacao;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMedicacao")
    private Collection<ConsultaMedicacao> consultaMedicacaoCollection;

    public Medicacao() {
    }

    public Medicacao(Integer idMedicacao) {
        this.idMedicacao = idMedicacao;
    }

    public Medicacao(Integer idMedicacao, String nome) {
        this.idMedicacao = idMedicacao;
        this.nome = nome;
    }

    public Integer getIdMedicacao() {
        return idMedicacao;
    }

    public void setIdMedicacao(Integer idMedicacao) {
        this.idMedicacao = idMedicacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        hash += (idMedicacao != null ? idMedicacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicacao)) {
            return false;
        }
        Medicacao other = (Medicacao) object;
        if ((this.idMedicacao == null && other.idMedicacao != null) || (this.idMedicacao != null && !this.idMedicacao.equals(other.idMedicacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clinicamedica.entity.Medicacao[ idMedicacao=" + idMedicacao + " ]";
    }
    
}
