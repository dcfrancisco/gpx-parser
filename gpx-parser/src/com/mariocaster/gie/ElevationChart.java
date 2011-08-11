package com.mariocaster.gie;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;


public class ElevationChart extends JPanel{
	private int width;
	private int height;
	
	public ElevationChart(int width, int height){
		super(new BorderLayout());
		this.width = width;
		this.height = height;
		setSize(width, height);
	}
	
	@SuppressWarnings("unchecked")
	public void setGraficaNormal(TrackSegment ts){
		DataTable data = new DataTable(Double.class, Integer.class);
		DataTable dataP = new DataTable(Float.class, Integer.class);

		GpxProcessor gp = new GpxProcessor();
		int alturaMinima = (int)gp.getMinimumHeight(ts)+10;
		

		Track tsList = new Track();
		tsList = gp.getTrackSplitted(ts, 1f);
		
		for(float i = 0.5f; i<tsList.size(); i+=1f){
			dataP.add(i, alturaMinima);
		}
		
		for(TrackPoint p: ts){
			data.add(p.getPos(), (int)(p.getEle()));
		}		
		
		DataSource datos = new DataSeries("Elevacion", data, 0, 1);
		DataSource datosP = new DataSeries("Pendiente", dataP, 0, 1);
		
		XYPlot plot = new XYPlot(datos, datosP);
		
		plot.setInsets(new Insets2D.Double(20.0, 90.0, 60.0, 30.0));
		AreaRenderer area1 = new DefaultAreaRenderer2D();
		area1.setSetting(AreaRenderer.COLOR, new LinearGradientPaint(0f, 0f, 0f, 1f, new float[] {0f, 1f},
				new Color[] {new Color(1.0f, 0.0f, 0.0f, 0.5f), new Color(1.0f, 0.0f, 0.0f, 0.1f)}));
		
		plot.setAreaRenderer(datos, area1);
		PointRenderer pr = new DefaultPointRenderer();
		plot.setPointRenderer(datosP, pr);
		
		
		//plot.setSetting(Plot.LEGEND, true);
		//plot.setSetting(Plot.ANTIALISING, true);
		
		//Hacer los puntos invisibles cambiando su tama;o a 0 (intentar no renderizarlos mejor)
		PointRenderer pointRenderer = plot.getPointRenderer(datos);
		Shape oldShape = pointRenderer.getSetting(PointRenderer.SHAPE);
		Shape newShape = AffineTransform.getScaleInstance(0, 0).createTransformedShape(oldShape);
		pointRenderer.setSetting(PointRenderer.SHAPE, newShape);

		//Colorear los puntos de los porcentajes
		pr = plot.getPointRenderer(datosP);
		oldShape = pr.getSetting(PointRenderer.SHAPE);
		newShape = AffineTransform.getScaleInstance(1, 1).createTransformedShape(oldShape);
		pr.setSetting(PointRenderer.SHAPE, newShape);
		
		//Leyenda del eje Y
		AxisRenderer axisY = plot.getAxisRenderer(XYPlot.AXIS_Y);
		axisY.setSetting(AxisRenderer.TICKS_SPACING,  25);
		axisY.setSetting(AxisRenderer.TICKS_MINOR_COUNT, 20);
		axisY.setSetting(AxisRenderer.LABEL, "Metros");
		axisY.setSetting(AxisRenderer.LABEL_DISTANCE, 4);
		axisY.setSetting(AxisRenderer.TICK_LABELS_DISTANCE, 2);
		
		//Leyenda del eje X
		AxisRenderer axisX = plot.getAxisRenderer(XYPlot.AXIS_X);
		axisX.setSetting(AxisRenderer.LABEL, "Kilómetros");
		Map<Double, String> etiquetas = new HashMap<Double, String>();
		
		int iter = 0;
		for(double i=0; i<tsList.size(); i+=0.5){
			if(i%1 != 0){
				TrackSegment tsTemp = tsList.get(iter);
				float porcentaje = gp.getAveragePositivePercent(tsTemp);
				etiquetas.put(i, porcentaje+"%");
				iter++;
			} else {
				etiquetas.put(i, ""+i);
			}
		}
			
		axisX.setSetting(AxisRenderer.TICKS_CUSTOM, etiquetas);
		

		//Mete un nuevo eje
		Axis x2 = new Axis(0, 20);
		plot.setAxis(XYPlot.AXIS_X2, x2);
		LinearRenderer2D rendererX2 = new LinearRenderer2D();
		plot.setAxisRenderer(XYPlot.AXIS_X2, rendererX2);
		
		InteractivePanel panel = new InteractivePanel(plot);
		panel.setAutoscrolls(false);
		
		
		add(panel, BorderLayout.CENTER);
	}

}
