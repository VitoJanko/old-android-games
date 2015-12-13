precision mediump float;


uniform sampler2D u_TextureUnit;//actual texture data
varying vec2 v_TextureCoordinates;

void main(){
	gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
}