package com.mariocaster.gie;

import java.util.Calendar;

public interface ProcessorInterface {
	/*
	 * A efectos de usar los Personal Bests y los Track Bests se definen estos tipos de Bests
	 * 0 = Menor tiempo empleado para una distancia dada
	 * 1 = Mayor distancia para un tiempo dado
	 * 2 = Mayor altura ganada en una distancia dada
	 * 3 = Mayor altura ganada en un tiempo dado
	 */
	
	//Personal Bests
	
	public int getAverageCadence(TrackSegment ts);
	public int getAverageHR(TrackSegment ts);
	public float getAverageNegativePercent(TrackSegment ts);
	public float getAveragePositivePercent(TrackSegment ts);
	
	public float getAveragePower(TrackSegment ts);
	//Track processing
	public TrackSegment getDistance(TrackSegment ts);		//Escribe en cada punto la distancia recorrida y devuelve el trkseg
	//Track Bests
	public double getDistanceByTime(TrackSegment ts, int seconds);
	public double getDistanciaEntreDosPuntos(double lat1, double lon1, double lat2, double lon2);
	public double getMaxAscentByDistance(TrackSegment ts, float distance);
	public double getMaxAscentByTime(TrackSegment ts, int seconds);
	public int getMaxCadence(TrackSegment ts);
	public int getMaxHR(TrackSegment ts);
	public double getMaximumHeight(TrackSegment ts);
	public float getMaxNegativePercent(TrackSegment ts);
	public float getMaxPositivePercent(TrackSegment ts);
	public float getMaxPower(TrackSegment ts);
	public double getMinimumHeight(TrackSegment ts);
	public Calendar getTime(TrackSegment ts);
	public Calendar getTimeByDistance(TrackSegment ts, float distance);
	public double getTotalAscent(TrackSegment ts);
	
	public int getTotalDescent(TrackSegment ts);
	//Track Info
	public double getTotalDistance(TrackSegment ts);		//DISTANCIA TOTAL, tiene que pasar antes por el metodo getDistance
	public Track getTrackSplitted(TrackSegment ts, float meters);
	
}