uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
attribute vec4 aColor;
attribute vec2 aTexCoordinate;
varying vec2 vTexCoordinate;
varying vec4 vColor;

uniform float mTime;
uniform float offX;
uniform float offY;
uniform float size;
uniform vec4 touchEvent[3];

void main() {
	vColor = aColor;
    vTexCoordinate = aTexCoordinate;
    vec4 newPosition = vPosition;
    newPosition.y+=5.5*sin(0.5*(newPosition.x+mTime*7.0));
    gl_Position = uMVPMatrix* newPosition;
    
    //for(int i=0;i<3;i++){
    //    gl_Position = gl_Position+10*sin(48.0-mTime*8.0)*touchEvent[i].z;
   //}
}


