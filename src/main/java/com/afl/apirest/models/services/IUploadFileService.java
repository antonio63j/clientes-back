/**
 * 
 */
package com.afl.apirest.models.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author antonio
 *
 */
public interface IUploadFileService {

	Resource cargar (String nombreImagen) throws MalformedURLException; 
	String copia (MultipartFile archivo) throws IOException;
	boolean eliminar (String archivo);
	Path getPath(String path, String archivo);
	
}
