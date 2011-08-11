package com.mariocaster.gie;


public class TrackPoint {
	private float pot;
	private long idTrack;
	private long idTrackSegment;
	private double lon;
	private double lat;
	private double ele;
	private long tiempo;
	private int frec;
	private int cad;
	private double pos;
	private float vel;
	
	public TrackPoint(){
		
	}
	
	public TrackPoint(
			double longitud,
			double latitud,
			double elevacion,
			long tiempo,
			int frec,
			int cadencia,
			double pos
			){
		
	}
	
	public int getCad() {
		return cad;
	}
	
	public double getEle() {
		return ele;
	}
	
	public int getFrec() {
		return frec;
	}
	
	public long getIdTrack() {
		return idTrack;
	}

	public long getIdTrackSegment() {
		return idTrackSegment;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
	
	public double getPos(){
		return this.pos;
	}
	
	public float getPot() {
		return pot;
	}
	
	public long getTiempo() {
		return tiempo;
	}
	
	public float getVel(){
		return vel;
	}
	
	public void setCad(int cadencia) {
		this.cad = cadencia;
	}
	
	public void setEle(double elevacion) {
		this.ele = elevacion;
	}
	
	public void setFrec(int frec) {
		this.frec = frec;
	}
	
	public void setIdTrack(long idTrack) {
		this.idTrack = idTrack;
	}
	
	public void setIdTrackSegment(long idTrackSegment) {
		this.idTrackSegment = idTrackSegment;
	}
	public void setLat(double latitud) {
		this.lat = latitud;
	}

	public void setLon(double longitud) {
		this.lon = longitud;
	}

	public void setPos(double pos){
		this.pos = pos;
	}

	public void setPot(float pot) {
		this.pot = pot;
	}
	
	public void setTiempo(long tiempo) {
		this.tiempo = tiempo;
	}

	public void setVel(float vel){
		this.vel = vel;
	}

}
