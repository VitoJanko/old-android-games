uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
attribute vec4 aColor;
attribute vec2 aTexCoordinate;
varying vec2 vTexCoordinate;
varying vec4 vColor;

void main() {
	vColor = aColor;
    vTexCoordinate = aTexCoordinate;
    gl_Position = uMVPMatrix* vPosition;
}