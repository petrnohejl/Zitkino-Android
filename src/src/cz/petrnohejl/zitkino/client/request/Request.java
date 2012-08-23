package cz.petrnohejl.zitkino.client.request;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonParseException;

import cz.petrnohejl.zitkino.client.response.Response;


public abstract class Request
{	
	protected final String BASE_URL = "http://zitkino.cz/";
	
	public abstract String getRequestMethod();
	public abstract String getAddress();
	public abstract byte[] getContent();
	public abstract String getBasicAuthUsername();
	public abstract String getBasicAuthPassword();
	public abstract Response parseResponse(InputStream stream) throws IOException, JsonParseException;
}
