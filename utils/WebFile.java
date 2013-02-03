package it.unibz.testhunter.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Get a web file.
 */
public final class WebFile {
    // Saved response.
    private java.util.Map<String,java.util.List<String>> responseHeader = null;
    private java.net.URL responseURL = null;
    private int responseCode = -1;
    private String MIMEtype  = null;
    private String charset   = null;
    private Object content   = null;
 
    /** Open a web file. */
    public WebFile( String urlString )
        throws java.net.MalformedURLException, java.io.IOException {
        // Open a URL connection.
        final java.net.URL url = new java.net.URL( urlString );
        final java.net.URLConnection uconn = url.openConnection( );
        if ( !(uconn instanceof java.net.HttpURLConnection) )
            throw new java.lang.IllegalArgumentException(
                "URL protocol must be HTTP." );
        final java.net.HttpURLConnection conn =
            (java.net.HttpURLConnection)uconn;
 
        // Set up a request.
        conn.setConnectTimeout( 60000 );    // 60 sec
        conn.setReadTimeout( 60000 );       // 60 sec
        conn.setInstanceFollowRedirects( true );
        conn.setRequestProperty( "User-agent", "spider" );
 
        // Send the request.
        conn.connect( );
 
        // Get the response.
        responseHeader    = conn.getHeaderFields( );
        responseCode      = conn.getResponseCode( );
        responseURL       = conn.getURL( );
        final int length  = conn.getContentLength( );
        final String type = conn.getContentType( );
        if ( type != null ) {
            final String[] parts = type.split( ";" );
            MIMEtype = parts[0].trim( );
            for ( int i = 1; i < parts.length && charset == null; i++ ) {
                final String t  = parts[i].trim( );
                final int index = t.toLowerCase( ).indexOf( "charset=" );
                if ( index != -1 )
                    charset = t.substring( index+8 );
            }
        }

        // Get the content.
        final java.io.InputStream stream = conn.getErrorStream( );
        if ( stream != null )
        	// write an error             
        	readStreamToFile(length, stream);
        else 
        	if ( (content = conn.getContent( )) != null && content instanceof java.io.InputStream )
        		// write content
        		readStreamToFile( length, (java.io.InputStream)content );
        conn.disconnect( );
    }
 
    /** Read stream bytes to File. */
    private void readStreamToFile( int length, java.io.InputStream stream ) throws java.io.IOException {
    	File destination = new  File("download1.xml");
    	BufferedOutputStream bos = new BufferedOutputStream( 
    			new FileOutputStream(destination.getName()));
        
        final int buflen = Math.max( 1024, Math.max( length, stream.available() ) );
        byte[] buf   = new byte[buflen];;
        
        for ( int nRead = stream.read(buf); nRead != -1; nRead = stream.read(buf) ) {
           bos.write(buf);
           System.out.print('.');
        }
    }
 
    /** Get the response code. */
    public int getResponseCode( ) {
        return responseCode;
    }
 
    /** Get the response header. */
    public java.util.Map<String,java.util.List<String>> getHeaderFields( ) {
        return responseHeader;
    }
 
    /** Get the URL of the received page. */
    public java.net.URL getURL( ) {
        return responseURL;
    }
 
    /** Get the MIME type. */
    public String getMIMEType( ) {
        return MIMEtype;
    }
}