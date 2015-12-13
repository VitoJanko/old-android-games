uniform mat4 u_Matrix[16];

attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;
attribute float a_mvpMatrixIndex;

varying vec2 v_TextureCoordinates;

uniform mat4 u_ModelMatrix[16];
varying vec4 mvPos;
uniform vec4 u_LightPositionVector;

void main(){
	v_TextureCoordinates = a_TextureCoordinates;
	
	int mvpIndex = int(a_mvpMatrixIndex);
	gl_Position = u_Matrix[mvpIndex] * a_Position;
	
	
	mvPos = u_ModelMatrix[mvpIndex] * a_Position;
}