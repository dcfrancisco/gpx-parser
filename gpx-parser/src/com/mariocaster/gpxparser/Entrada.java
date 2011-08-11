package com.mariocaster.gpxparser;
import java.io.File;

import org.jdom.DataConversionException;

import com.mariocaster.gie.CargadorGpx;
import com.mariocaster.gie.GpxProcessor;
import com.mariocaster.gie.Track;
import com.mariocaster.gie.TrackSegment;


public class Entrada {
	
	public static void main(String[] args) {
		//Recoger la ruta al archivo y pasarsela al cargador gpx
		//String ruta = args[0];
		//String ruta = "/home/mario/Escritorio/activity_96198305.gpx";
		//String ruta = "/home/mario/Escritorio/track.gpx";
		//String ruta = "c:/activity_reducido.gpx";
		String ruta = "/home/mario/no_name.gpx";
		//String ruta = "c:/activity_94999338.gpx";
		File file = new File(ruta);
		CargadorGpx cg = null;
		try {
			cg = new CargadorGpx(file);
		} catch (DataConversionException e) {
			e.printStackTrace();
		}
		
		TrackSegment trkSeg;
		Track trk = cg.getTrack();
		GpxProcessor gpxP = new GpxProcessor();
		for (int i = 0; i<trk.size(); i++){
			System.out.println("*************** TrackSegment " + i);
			trkSeg = trk.get(i);
			System.out.println("getAverageCadence = " + gpxP.getAverageCadence(trkSeg));
			System.out.println("getAverageHR = " + gpxP.getAverageHR(trkSeg));
			System.out.println("getAverageNegativePercent = " + gpxP.getAverageNegativePercent(trkSeg));
			System.out.println("getAveragePositivePercent = " + gpxP.getAveragePositivePercent(trkSeg));
			System.out.println("getAveragePower = " + gpxP.getAveragePower(trkSeg));
			System.out.println("getDistanceByTime 6000 sec = " + gpxP.getDistanceByTime(trkSeg, 6000));
			System.out.println("getMaxAscentByDistance = " + gpxP.getMaxAscentByDistance(trkSeg, 20000));
			System.out.println("getMaxCadence = " + gpxP.getMaxCadence(trkSeg));
			System.out.println("getMaxHR = " + gpxP.getMaxHR(trkSeg));
			System.out.println("getMaximumHeight = " + gpxP.getMaximumHeight(trkSeg));
			System.out.println("getMinimumHeight = " + gpxP.getMinimumHeight(trkSeg));
			System.out.println("getMaxNegativePercent = " + gpxP.getMaxNegativePercent(trkSeg));
			System.out.println("getMaxPositivePercent = " + gpxP.getMaxPositivePercent(trkSeg));
			System.out.println("getMaxPower = " + gpxP.getMaxPower(trkSeg));
			System.out.println("getTotalAscent = " + gpxP.getTotalAscent(trkSeg));
			System.out.println("getTotalDescent = " + gpxP.getTotalDescent(trkSeg));
			System.out.println("getTotalDistance = " + gpxP.getTotalDistance(trkSeg));
			System.out.println("getDistance = " + gpxP.getDistance(trkSeg));
			System.out.println("getTime = " + gpxP.getTime(trkSeg));
			System.out.println("getTimeByDistance = " + gpxP.getTimeByDistance(trkSeg, 10));
			System.out.println("getTrackSplitted = " + gpxP.getTrackSplitted(trkSeg, 1));
			System.out.println("");
		}
		
		/*
		ElevationChart eleChart = new ElevationChart(1000, 300);
		eleChart.setGraficaNormal(ts);
		
		JFrame frame = new JFrame("Puerto De Canencia");
		frame.getContentPane().add(eleChart, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 400);
		frame.setResizable(false);
		frame.setEnabled(false);
		frame.setVisible(true);
		*/
	}

}
