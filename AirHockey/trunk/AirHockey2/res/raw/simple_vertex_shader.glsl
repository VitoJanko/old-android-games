attribute vec4 a_Position;
attribute vec4 a_Color;
							
varying vec4 v_Color;

void main(){
	v_Color = a_Color; // varying: blends colors for fragment based line,triangle
	
	gl_Position = a_Position;						
	gl_PointSize = 10.0;
}