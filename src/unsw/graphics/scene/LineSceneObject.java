package unsw.graphics.scene;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Line2D;
import unsw.graphics.geometry.Point2D;

/**
 * A game object that has is a line.
 * 
 * This class extend SceneObject to draw lines.
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * @author Benny Hwang
 * 
 */
public class LineSceneObject extends SceneObject {
	
	private Color myLineColor;
	private Point2D p1;
	private Point2D p2;

	//Create a LineSceneObject from (0,0) to (1,0)
	public LineSceneObject(SceneObject parent, Color lineColor) {
		super(parent);
		myLineColor = lineColor;
		p1 = new Point2D(0,0);
		p2 = new Point2D(1,0);
	}

	//Create a LineSceneObject from (x1,y1) to (x2,y2)
	public LineSceneObject(SceneObject parent, float x0, float y0, float x1, float y1, Color lineColor) {
		super(parent);
		myLineColor = lineColor;
		p1 = new Point2D(x0,y0);
		p2 = new Point2D(x1,y1);
	}

	/**
	 * @return the lineColor
	 */
	public Color getLineColor() {
		return myLineColor;
	}

	/**
	 * @param myLineColor the myLineColor to set
	 */
	public void setLineColor(Color lineColor) {
		this.myLineColor = lineColor;
	}

	/**
	 * @return the p1
	 */
	public Point2D getP1() {
		return p1;
	}

	/**
	 * @param p1 the p1 to set
	 */
	public void setP1(Point2D p1) {
		this.p1 = p1;
	}

	/**
	 * @return the p2
	 */
	public Point2D getP2() {
		return p2;
	}

	/**
	 * @param p2 the p2 to set
	 */
	public void setP2(Point2D p2) {
		this.p2 = p2;
	}

	/**
	 * Draw a line
	 * 
	 * if the line color is non-null, draw the line with this color
	 * 
	 */
	@Override
	public void drawSelf(GL3 gl, CoordFrame2D frame) {
		if (getLineColor() != null) {
			Line2D lineToDraw = new Line2D(this.getP1(), this.getP2());
			Shader.setPenColor(gl, getLineColor());
			lineToDraw.draw(gl, frame);
		}
	}

}
