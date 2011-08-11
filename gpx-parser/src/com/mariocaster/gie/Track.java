package com.mariocaster.gie;
import java.util.ArrayList;
import java.util.Date;


public class Track extends ArrayList<TrackSegment>{
	private float distancia;
	private Date tiempo;
	private String name;
	private String desc;
	
	public String getDesc() {
		return desc;
	}
	public float getDistancia() {
		return distancia;
	}
	public String getName() {
		return name;
	}
	public Date getTiempo() {
		return tiempo;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setDistancia(float distancia) {
		this.distancia = distancia;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTiempo(Date tiempo) {
		this.tiempo = tiempo;
	}
}
