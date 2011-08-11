package com.mariocaster.gie;
import java.util.Calendar;

public class GpxProcessor implements ProcessorInterface{

	@Override
	/**
	 * Devuelve la cadencia media del segmento
	 * @param ts		Segmento a analizar
	 * @return			Cadencia media usada en el segmento
	 */
	public int getAverageCadence(TrackSegment ts){
		int cuenta = 0;
		int total = 0;
		
		for(TrackPoint p: ts){
			cuenta ++;
			total += p.getCad();
		}
		
		return ((int)total/cuenta);
	}
	
	@Override
	/**
	 * Devuelve la media de pulsaciones del segmento
	 * @param ts		Segmento a analizar
	 * @return			Media de pulsaciones en todo el segmento
	 */
	public int getAverageHR(TrackSegment ts){
		int cuenta = 0;
		int total = 0;
		
		for(TrackPoint p: ts){
			cuenta ++;
			total += p.getFrec();
		}
		
		return ((int)total/cuenta);
	}

	@Override
	public float getAverageNegativePercent(TrackSegment ts){
		//TODO Devuelve el desnivel negativo medio del segmento
		return 0f;
	}

	@Override
	/**
	 * Devuelve el desnivel positivo medio del segmento
	 * @param ts		Segmento a analizar
	 * @return			Desnivel positivo medio del segmento
	 */
	public float getAveragePositivePercent(TrackSegment ts){
		double distancia = getTotalDistance(ts);
		int numPuntos = ts.size();
		
		double puntoMasAlto = ts.get(numPuntos-1).getEle();
		double puntoMasBajo = ts.get(0).getEle();
		
		double ascension = puntoMasAlto - puntoMasBajo;
		//ascension = getTotalAscent(ts);
		float resultado = 0;
		resultado = (float)(ascension/1000)*100;
		
		//float resultado = (float) ((diferencia/(distancia*1000))*100);
		resultado = (float)Math.round(resultado*100)/100;
		
		//System.out.println("Diferencia = " + diferencia + ", Ascension = " + ascension + ", inicio " + inicio + ", Resultado = " + resultado);
		System.out.println("Resultado = " + resultado);
		return resultado;
	}

	@Override
	public float getAveragePower(TrackSegment ts){
		//TODO Devuelve la potencia media del segmento
		return 0f;
	}

	@Override
	/**
	 * Devuelve un TrackSegment con las distancias calculadas y seteadas en cada punto
	 * Esto es para poder ahorrar algo de procesamiento de codigo en futuros algoritmos
	 * @param ts	Segmento a calcular
	 * @return	TrackSegment con las distancias calculadas
	 */
	public TrackSegment getDistance(TrackSegment ts){
		double distTotal = 0;
		
		//TrackSegment a devolver
		TrackSegment tsTemp = new TrackSegment();
		
		//Asignar el inicio al primer punto y meterlo en el listado temporal
		ts.get(0).setPos(0);
		tsTemp.add(ts.get(0));
		
		/*
		 * Solo hay que a;adir la distancia total que se este calculando al punto2
		 * que se este procesando
		 */
		
		int numPuntos = ts.size();
		
		for(int i = 0; i<numPuntos; i++){
			if(i+1!=numPuntos){
				TrackPoint p1 = ts.get(i);
				TrackPoint p2 = ts.get(i+1);
				
				distTotal += getDistanciaEntreDosPuntos(p1.getLat(), p1.getLon(), p2.getLat(), p2.getLon());
				p2.setPos(distTotal);
				
				tsTemp.add(p2);
			}
		}
		return tsTemp;
	}

	@Override
	public double getDistanceByTime(TrackSegment ts, int seconds){
		//TODO Devuelve la distancia recorrida en un determinado numero de segundos
		int pointNumber = ts.size();
		
		//Usar solo este metodo si el numero es menor que un millon
		if(pointNumber<1000000){
			long time = seconds*1000;

			Calendar tiempoInicio = Calendar.getInstance();
			Calendar intervalo = Calendar.getInstance();
			double maxDistance = 0;
			for(int iter = 0; iter<pointNumber; iter++){

				tiempoInicio.setTimeInMillis(ts.get(iter).getTiempo());
				intervalo.setTimeInMillis(tiempoInicio.getTimeInMillis()+time);
				
				/* Busqueda binaria del tiempo transcurrido
				 * La condicion de salida sera que el tiempo intervalo sea
				 * similar al tiempo del punto
				 */
				TrackPoint p;
				int indice = 0;
				long tiempoPunto = 0;
				long tiempoIntervalo = 0;
				TrackSegment nuevoTs = new TrackSegment();
				for(int i = iter; i<pointNumber; i++){
					p = ts.get(i);
					tiempoPunto = p.getTiempo();
					tiempoIntervalo = intervalo.getTimeInMillis();
					if(tiempoPunto>tiempoIntervalo){
						break;
					}
					nuevoTs.add(p);
				}
				
				if(nuevoTs.size() == 0){
					//Error
					System.out.println("Se ha producido un error");
					//return 0;
				}
				
				double distanciaActual = getTotalDistance(nuevoTs);
				if (distanciaActual>maxDistance){
					maxDistance = distanciaActual;
				}			
			}
			return maxDistance;
		} else {
			System.out.println("Fichero demasiado grande");
			return 0;
		}
	}

	@Override
	/**
	 * Devuelve la distancia total entre dos puntos
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	public double getDistanciaEntreDosPuntos(double lat1, double lon1, double lat2, double lon2){
		int r = 100;
		double d = Math.acos(Math.sin(lat1)*Math.sin(lat2) +
					Math.cos(lat1) * Math.cos(lat2) *
					Math.cos(lon2-lon1)) * r;
		if(new Double(Double.NaN).equals(d)){
			//System.out.println(d);
			return 0;
		}
		return d;
	}

	@Override
	public double getMaxAscentByDistance(TrackSegment ts, float distance) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double getMaxAscentByTime(TrackSegment ts, int seconds) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	/**
	 * Devuelve la cadencia maxima del segmento
	 * @param ts		Segmento a analizar
	 * @return			Cadencia maxima encontrada en el track
	 */
	public int getMaxCadence(TrackSegment ts){
		int max = 0;
		int cad = 0;
		for(TrackPoint p: ts){
			cad = p.getCad();
			if(cad>max){
				max = cad;
			}
		}
		
		return max;
	}

	@Override
	/**
	 * Devuelve el maximo de pulsaciones en el segmento
	 * @param ts			Segmento a analizar
	 * @return				Pulsaciones maximas registradas en el segmento
	 */
	public int getMaxHR(TrackSegment ts){
		int max = 0;
		
		for(TrackPoint p: ts){
			if(p.getCad()>max){
				max = p.getFrec();
			}
		}
		
		return max;
	}

	@Override
	/**
	 * Devuelve el punto mas alto en altitud del segmento
	 * @param ts			Segmento a analizar
	 * @return				Altura maxima registrada en el segmento
	 */
	public double getMaximumHeight(TrackSegment ts){
		double max = 0;
		
		for(TrackPoint p: ts){
			if(p.getCad()>max){
				max = p.getEle();
			}
		}
		
		return max;
	}

	@Override
	public float getMaxNegativePercent(TrackSegment ts){
		//TODO Devuelve la pendiente negativa maxima registrada en el segmento
		return 0f;
	}

	@Override
	public float getMaxPositivePercent(TrackSegment ts){
		//TODO Devuelve la pendiente maxima del segmento
		return 0f;
	}

	@Override
	public float getMaxPower(TrackSegment ts){
		//TODO Devuelve la potencia maxima registrada en el segmento
		return 0f;
	}

	@Override
	/**
	 * Devuelve el punto mas bajo en altitud del segmento
	 * @param ts			Segmento a analizar
	 * @return				Altura minima encontrada en el segmento
	 */
	public double getMinimumHeight(TrackSegment ts){
		double min = 999999;
		
		for(TrackPoint p: ts){
			if(p.getEle()<min){
				min = p.getEle();
			}
		}
		return min;
	}

	@Override
	/**
	 * Devuelve el tiempo total invertido en el segmento
	 * @param ts		Segmento a analizar
	 * @return			Tiempo en un objeto Calendar
	 */
	public Calendar getTime(TrackSegment ts){
		long tiempoActual = ts.get(0).getTiempo();
		
		for(int i = 1; i<ts.size(); i++){
			tiempoActual = ts.get(i).getTiempo()-tiempoActual;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(tiempoActual);
		return calendar;
	}

	public Calendar getTimeByDistance(TrackSegment ts, float distance){
		//TODO Devuelve el tiempo empleado en recorrer una distancia
		return null;
	}
	
	@Override
	/**
	 * Devuelve el total de ascenso en altitud del segmento
	 * @param ts		Segmento a analizar
	 * @return			La altura en metros que se ha llegado a ascender
	 */
	public double getTotalAscent(TrackSegment ts){
		double eleActual = ts.get(0).getEle();
		double eleTotal = 0;

		for(TrackPoint p: ts){
			if(p.getEle()>eleActual){
				eleTotal += p.getEle()-eleActual;
				eleActual = p.getEle();
			} else {
				eleActual = p.getEle();
			}
		}
		return eleTotal;
	}
	
	public int getTotalDescent(TrackSegment ts){
		//TODO Devuelve el total en descenso en altitud del segmento
		return 0;
	}

	public double getTotalDistance(TrackSegment ts){
		return ts.get(ts.size()-1).getPos();
	}

	@Override
	/**
	 * Devuelve un track dividido en un numero de metros
	 * @param ts
	 * @param meters
	 * @return
	 */
	public Track getTrackSplitted(TrackSegment ts, float meters){
		int numPuntos = ts.size();
		float distancia = 0;
		TrackSegment tsTemp = new TrackSegment();
		Track tsList = new Track();
		
		for(int i = 0; i<numPuntos; i++){
			if(i+1!=numPuntos){
				TrackPoint p1 = ts.get(i);
				TrackPoint p2 = ts.get(i+1);
				
				distancia += getDistanciaEntreDosPuntos(p1.getLat(), p1.getLon(), p2.getLat(), p2.getLon());
				
				tsTemp.add(p1);
				if(distancia>=meters || i+2==numPuntos){
					tsTemp.add(p2);
					int a = i+1;
					distancia = 0;
					tsList.add(tsTemp);
					tsTemp = new TrackSegment();
				}
			}
		}
		return tsList;
	}
}
