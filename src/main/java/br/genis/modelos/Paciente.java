package br.genis.modelos;

import javax.persistence.Lob;

public class Paciente extends Usuario {

	private static final long serialVersionUID = -6355558396116940811L;

	@Lob
	private String diagnostico;

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((diagnostico == null) ? 0 : diagnostico.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paciente other = (Paciente) obj;
		if (diagnostico == null) {
			if (other.diagnostico != null)
				return false;
		} else if (!diagnostico.equals(other.diagnostico))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Paciente [diagnostico=" + diagnostico + "]";
	}

}
