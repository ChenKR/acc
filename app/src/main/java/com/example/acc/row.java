package com.example.acc;

public class row {
	
	int cos;
	double cosh,dotx,doty,dotz,dotsum,dotxy,dotyz,dotxz,lenxy,lenxz,lenyz,cosx,cosy,cosz ;
	double av;
	public  void cos(double x, double y, double z) {
		
		 dotx = x*x;
		 doty = y*y;
		 dotz = z*z;
		
		 dotsum=dotx+doty+dotz;
		 
		 dotxy=dotx+doty;
		 lenxy=Math.sqrt(dotsum)*Math.sqrt(dotxy);
		 cosx=dotxy/lenxy;
		 
		 dotyz=doty+dotz;
		 lenyz=Math.sqrt(dotsum)*Math.sqrt(dotyz);
		 cosy=dotyz/lenyz;
		 
		 dotxz=dotx+dotz;
		 lenxz=Math.sqrt(dotsum)*Math.sqrt(dotxz);
		 cosz=dotxz/lenxz;
		 
		
	}

	
	
}
