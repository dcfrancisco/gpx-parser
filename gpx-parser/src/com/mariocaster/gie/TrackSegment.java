package com.mariocaster.gie;
import java.util.ArrayList;
import java.util.Date;


public class TrackSegment extends ArrayList<TrackPoint> {
	private float distancia;
	private Date tiempo;
	
	public float getDistancia() {
		return distancia;
	}
	public Date getTiempo() {
		return tiempo;
	}
	public void setDistancia(float distancia) {
		this.distancia = distancia;
	}
	public void setTiempo(Date tiempo) {
		this.tiempo = tiempo;
	}

}
