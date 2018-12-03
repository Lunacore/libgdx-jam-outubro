varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D v_texture;
uniform float intensity;

void main()
{
	vec2 distortion = texture2D(v_texture, v_texCoords).rg - 0.5;
    gl_FragColor = texture2D(u_texture, v_texCoords + distortion * intensity);
}