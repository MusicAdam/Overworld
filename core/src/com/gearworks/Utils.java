package com.gearworks;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Utils {
	public static float PI 				= (float)Math.PI;
	public static float PI_TIMES_2 		= PI * 2;
	public static float PI_OVER_2 		= PI / 2;
	public static float angleEpsilon 	= degToRad(1);
	
	//TODO: Update this to check for overlapping entity bounds rather than entity point position in bounds
	public static ArrayList<Entity> findEntitiesInBox(ArrayList<Entity> haystack, Rectangle bounds){
		ArrayList<Entity> found = new ArrayList<Entity>();
		for(Entity e : haystack){
			if(entityIsInBox(e, bounds))
				found.add(e);
		}
		
		return found;
	}
	
	public static boolean entityIsInBox(Entity e, Rectangle bounds){
		float entX 		= e.position().x;
		float entY 		= e.position().y;
		float bndX 		= bounds.getX();
		float bndY 		= bounds.getY();
		float bndXMax 	= bndX + bounds.getWidth();
		float bndYMax 	= bndY + bounds.getHeight();
		
		if(entX <= bndXMax 	&& 
		   entX >= bndX		&&
		   entY <= bndYMax	&&
		   entY >= bndY)
		{
			return true;
		}
		
		return false;
		
	}
	
	public static ArrayList<Entity> selectEntitiesInBox(ArrayList<Entity> haystack, Rectangle bounds){
		ArrayList<Entity> found = new ArrayList<Entity>();
		for(Entity e : haystack){
			if(e.selectable()){
				if(entityIsInBox(e, bounds))
					found.add(e);
			}
		}
		
		return found;		
	}
	
	public static void drawRect(ShapeRenderer r, Color color, float x, float y, float w, float h){
		r.identity();
		r.begin(ShapeType.Line);
			r.setColor(color);
			r.translate(x, y, 0f);
			r.rect(0, 0, w, h);
		r.end();
	}
	
	public static void fillRect(ShapeRenderer r, Color color, float x, float y, float w, float h){
		r.identity();
		r.begin(ShapeType.Filled);
			r.setColor(color);
			r.translate(x, y, 0f);
			r.rect(0, 0, w, h);
		r.end();
	}
	
	public static void drawLine(ShapeRenderer r, Color color, float x1, float y1, float x2, float y2){
		r.identity();
		r.begin(ShapeType.Line);
			r.setColor(color);
			r.line(x1,  y1, x2, y2);
		r.end();
	}
	
	public static void drawPoint(ShapeRenderer r, Color color, float x, float y){
		r.identity();
		r.begin(ShapeType.Point);
			r.setColor(color);
			r.scale(10, 10, 10);
			r.point(x, y, 0f);
		r.end();
	}
	
	public static boolean epsilonEquals(float x, float y, float e){
		return ( x < y + e && x > y - e );
	}
	
	public static float angle(Vector2 v1, Vector2 v2){
		float angle = (float)Math.acos( v1.cpy().dot(v2) /
						( v1.len() * v2.len() ) );
		
		if(angle < angleEpsilon || angle != angle){
			angle = 0;
		}
		
		return angle;
	}
	
	public static int sign(float n){
		if(n < 0)
			return -1;
		return 1;
		
	}
	
	public static float radToDeg(float rad){
		return rad * (180f / Utils.PI);
	}
	
	public static float degToRad(float deg){
		return deg * (Utils.PI / 180f);
	}
	
	public static float[] colorToArray(Color c){
		return new float[]{c.r, c.g, c.b, c.a};
	}

}
