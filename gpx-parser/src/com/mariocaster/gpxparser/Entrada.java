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
		String ruta = "c:/activity_reducido.gpx";
		//String ruta = "c:/activity_94999338.gpx";
		File file = new File(ruta);
		CargadorGpx cg = null;
		try {
			cg = new CargadorGpx(file);
		} catch (DataConversionException e) {
			e.printStackTrace();
		}
		
		Track trk = cg.getTrack();
		System.out.println("TrackSegments = " + trk.size());
		TrackSegment ts = trk.get(0);
		GpxProcessor gpxP = new GpxProcessor();
		System.out.println(gpxP.getTotalAscent(ts));
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
