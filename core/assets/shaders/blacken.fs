varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float pass;
uniform float strength;

void main()
{

	vec2 uv = v_texCoords;
    vec4 color = vec4(0.0, 0.0, 0.0, 0.0);

    for(float i = -pass; i < pass; i ++){
        for(float j = -pass; j < pass; j ++){
        	color += texture2D(u_texture, uv + vec2(i * strength, j * strength));
    	}
    }
    
    color /= pass*2.0*pass*2.0;
    
    
    // Output to screen
    gl_FragColor = color;
}