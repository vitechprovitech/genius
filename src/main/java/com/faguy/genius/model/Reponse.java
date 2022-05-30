package com.faguy.genius.model;
public class Reponse<T> {
		private int code;
		private String message;
		private T object;
		private String caller;
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public Reponse() {
			super();
		}
		public Reponse(int code, String message, T object, String caller) {
			super();
			this.code = code;
			this.message = message;
			this.object = object;
			this.caller = caller;
		}
		public Reponse(int code, String message, T object) {
			super();
			this.code = code;
			this.message = message;
			this.object = object;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Object getObject() {
			return object;
		}
		public void setObject(T object) {
			this.object = object;
		}
		public String getCaller() {
			return caller;
		}
		public void setCaller(String caller) {
			this.caller = caller;
		}
}