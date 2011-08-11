package com.mariocaster.gie;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


public class Track extends ArrayList<TrackSegment>{
	public double distancia;
	public Calendar tiempo = null;
	public int cadMedia;
	public int frecMedia;
	public float porcNegativoMedio;
	public float porcPositivoMedio;
	public float potenciaMedia;
	public int cadenciaMaxima = 0;
	public int frecMaxima = 0;
	public double alturaMaxima = 0;
	public double alturaMinima = 10000;
	public float rampaNegativaMaxima = 0;
	public float rampaPositivaMaxima = 0;
	public float potenciaMaxima = 0;
	public double ascensionTotal;
	public double descensoTotal;
	public String name;
	public String desc;
	
	public void procesarTrack(){
		TrackSegment trkseg;
		int numTrkseg = this.size();
		
		//TIEMPO
		long p1Tiempo, p2Tiempo;
		p1Tiempo = this.get(0).tiempo.getTimeInMillis();			//Primer punto de todos
		TrackSegment ultimoTrkseg = this.get(numTrkseg-1);
		p2Tiempo = ultimoTrkseg.get(ultimoTrkseg.size()-1).getTiempo();
		tiempo = Calendar.getInstance();
		tiempo.setTimeInMillis(p2Tiempo-p1Tiempo);
		
		//Iterar por los trackSegment
		for(int i = 0; i<numTrkseg; i++){
			trkseg = this.get(i);
			
			//Sumar los valores
			distancia += trkseg.distancia;
			cadMedia += trkseg.cadMedia;
			frecMedia += trkseg.frecMedia;
			porcNegativoMedio += trkseg.porcNegativoMedio;
			porcPositivoMedio += trkseg.porcPositivoMedio;
			potenciaMedia += trkseg.potenciaMedia;
			ascensionTotal += trkseg.ascensionTotal;
			descensoTotal += trkseg.descensoTotal;
			
			//Valores Maximos
			cadenciaMaxima = trkseg.cadenciaMaxima>cadenciaMaxima ? trkseg.cadenciaMaxima : cadenciaMaxima;
			frecMaxima = trkseg.frecMaxima>frecMaxima ? trkseg.frecMaxima : frecMaxima;
			alturaMaxima = trkseg.alturaMaxima>alturaMaxima ? trkseg.alturaMaxima : alturaMaxima;
			alturaMinima = trkseg.alturaMinima<alturaMinima ? trkseg.alturaMinima : alturaMinima;
			rampaNegativaMaxima = trkseg.rampaNegativaMaxima<rampaNegativaMaxima ? trkseg.rampaNegativaMaxima : rampaNegativaMaxima;
			rampaPositivaMaxima = trkseg.rampaPositivaMaxima>rampaPositivaMaxima ? trkseg.rampaPositivaMaxima : rampaPositivaMaxima;
			potenciaMaxima = trkseg.potenciaMaxima>potenciaMaxima ? trkseg.potenciaMaxima : potenciaMaxima;
			
		}
		
		//Hacer las medias
		this.cadMedia = (int)cadMedia/numTrkseg;
		frecMedia = (int)frecMedia/numTrkseg;
		porcNegativoMedio = porcNegativoMedio/numTrkseg;
		porcPositivoMedio = porcPositivoMedio/numTrkseg;
		potenciaMedia = potenciaMedia/numTrkseg;
		
	}
}
