package com.faguy.genius.model.DTO;

public class FichierDTO {
		private int id;
		private String vignette64;
		private String tailleVignette;
		private String formatVignette;
		private String nomFichier;
		private String chemin;
		private String contentType;
		private String sampleName;
		public int getId() {
			return id;
		}
		public FichierDTO(int id) {
			super();
			this.id = id;
		}
		public FichierDTO(int id, String vignette64) {
			super();
			this.id = id;
			this.vignette64 = vignette64;
		}
		public FichierDTO(int id, String vignette64, String nomFichier, String sampleName) {
			super();
			this.id = id;
			this.vignette64 = vignette64;
			this.nomFichier = nomFichier;
			this.sampleName=sampleName;
		}
		public FichierDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getVignette64() {
			return vignette64;
		}
		public void setVignette64(String vignette64) {
			this.vignette64 = vignette64;
		}
		public String getTailleVignette() {
			return tailleVignette;
		}
		public void setTailleVignette(String tailleVignette) {
			this.tailleVignette = tailleVignette;
		}
		public String getFormatVignette() {
			return formatVignette;
		}
		public void setFormatVignette(String formatVignette) {
			this.formatVignette = formatVignette;
		}
		public String getNomFichier() {
			return nomFichier;
		}
		public void setNomFichier(String nomFichier) {
			this.nomFichier = nomFichier;
		}
		public String getChemin() {
			return chemin;
		}
		public void setChemin(String chemin) {
			this.chemin = chemin;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public String getSampleName() {
			return sampleName;
		}
		public void setSampleName(String sampleName) {
			this.sampleName = sampleName;
		}
}
