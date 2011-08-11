package com.mariocaster.gie;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TrackSegment extends ArrayList<TrackPoint> {
	public double distancia;
	public Calendar tiempo;
	public int cadMedia;
	public int frecMedia;
	public float porcNegativoMedio;
	public float porcPositivoMedio;
	public float potenciaMedia;
	public int cadenciaMaxima;
	public int frecMaxima;
	public double alturaMaxima;
	public double alturaMinima;
	public float rampaNegativaMaxima;
	public float rampaPositivaMaxima;
	public float potenciaMaxima;
	public double ascensionTotal;
	public double descensoTotal;
}
