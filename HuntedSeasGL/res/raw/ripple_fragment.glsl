precision mediump float;
uniform sampler2D uTexture;
varying vec2 vTexCoordinate;
varying vec4 vColor;
uniform float mTime;
uniform float offX; //offset iz centra WARNING: koordinate od 0-1
uniform float offY;
uniform float size;
uniform vec4 touchEvent[3];

void main() {       
    vec2 myvec = vTexCoordinate;
    vec2 cPos = -1.0 + 2.0 * vTexCoordinate.xy; //normalizira
    for(int i=0;i<3;i++){
        vec2 off3 = vec2(touchEvent[i].x,touchEvent[i].y);
        vec2 ofvec3 = cPos+off3;
        float r = length(ofvec3);
        float mvm = touchEvent[i].w;
        myvec = myvec+(ofvec3/r)*sin(r*48.0-mTime*8.0)*touchEvent[i].z/(r*1.0);
   }
   gl_FragColor = texture2D(uTexture,myvec); 
}