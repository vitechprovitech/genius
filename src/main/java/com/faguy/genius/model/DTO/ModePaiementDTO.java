package com.faguy.genius.model.DTO;

import com.faguy.genius.entity.ModePaiement;

public class ModePaiementDTO {
		private int id;
		private String code;
		private String mode;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public static ModePaiementDTO toModeDTO(ModePaiement mode) {
			ModePaiementDTO dto=new ModePaiementDTO();
			dto.setCode(mode.getCode());
			dto.setId((int) mode.getId());
			dto.setMode(mode.getMode());
			
			return dto;
		}
		public ModePaiementDTO(int id, String code) {
			super();
			this.id = id;
			this.code = code;
		}
		public ModePaiementDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMode() {
			return mode;
		}
		public void setMode(String mode) {
			this.mode = mode;
		}
}
