precision mediump float;

uniform sampler2D u_TextureUnit;//actual texture data
varying vec2 v_TextureCoordinates;

varying vec4 mvPos;
uniform vec4 u_LightPositionVector;

void main(){

	gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
	if(gl_FragColor.w < 0.9){
		gl_FragColor.x = 0.0;
		gl_FragColor.y = 0.0;
		gl_FragColor.z = 0.0;
		gl_FragColor.w = 0.0;
	}
	else{
		gl_FragColor.x = 0.2;
		gl_FragColor.y = 0.2;
		gl_FragColor.z = 0.2;
	}

}