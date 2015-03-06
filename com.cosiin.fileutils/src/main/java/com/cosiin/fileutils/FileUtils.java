package com.cosiin.fileutils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * This utility is used for IBM BPM application. It is called from integrated service of IBM BPM
 * 
 * @author: lan.le
 * 
 */

public class FileUtils {
	
	/**
	 * Extract a zip file and then store the file list of it to a indicated folder
	 * @param zipFile the file path needs to be extract
	 * @param toFolder store the file list to folder
	 * @throws IOException 
	 */
	public static void extract(String zipFile, String toFolder) throws IOException {
		ZipUtils.unzip(zipFile, toFolder);
	}
	
	/**
	 * Delete permanent a zip file out of storage
	 * @param zipFile the file path needs to be deleted
	 * @throws IOException 
	 */
	public static void delete(String zipFile) throws IOException {
		org.apache.commons.io.FileUtils.forceDelete(new File(zipFile));
	}
	
	/**
	 * Archive a zip file to a folder
	 * @param zipFile the file path needs to be archived
	 * @param toFolder store the file list to folder
	 * @throws IOException 
	 */
	public static void archive(String zipFile, String toFolder) throws IOException {
		File file = new File(zipFile);
		org.apache.commons.io.FileUtils.copyFile(file, new File(toFolder + File.separator + file.getName()));
		org.apache.commons.io.FileUtils.forceDelete(file);
	}
	
	/**
	 * List all files from an indicated folder
	 * @param fromFolder the folder contains the list of file
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static List<String> listFiles(String fromFolder) throws IOException {
		List<String> fileList = new ArrayList<String>();
		File dir = new File(fromFolder);

		List<File> files = (List<File>) org.apache.commons.io.FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for (File file : files) {
			fileList.add(file.getCanonicalPath());
		}
		return fileList;
	}
	
	/**
	 * Zip a file to a zip file
	 * @param fromFile
	 * @param toFile 
	 * @throws IOException 
	 */
	public static void zip(String fromFile, String toFile) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(toFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        ZipEntry zipEntry = new ZipEntry(new File(fromFile).getName());
        zipOutputStream.putNextEntry(zipEntry);

        FileInputStream fileInputStream = new FileInputStream(fromFile);
        byte[] buf = new byte[1024];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buf)) > 0) {
            zipOutputStream.write(buf, 0, bytesRead);
        }

        fileInputStream.close();
        zipOutputStream.closeEntry();
        zipOutputStream.close();
        fileOutputStream.close();
	}
	
	/**
	 * Save file from base 64 content
	 * @param fileDir
	 * @param fileName 
	 * @param based64Content 
	 * @throws IOException 
	 */
	public static void saveFile(String fileDir, String fileName,
			String based64Content) throws Exception {
		File destDir = new File(fileDir);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		String filePath = fileDir + fileName;
		File file = new File(filePath);
		byte dearr[] = Base64.decodeBase64(based64Content.getBytes());
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(dearr);
		fos.flush();
		fos.close();
	}
	
	/**
	 * Get base 64 data of file 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String getFileDataBased64(String filePath) throws Exception {
		int data;
		File file = new File(filePath);
		FileReader fr = new FileReader(file);
		ByteArrayOutputStream bf = new ByteArrayOutputStream();
		while ((data = fr.read()) != -1) {
			bf.write(data);
		}
		byte content[] = Base64.encodeBase64(bf.toByteArray());
		String ret = new String(content);
		bf.close();
		fr.close();
		return ret;
	}
	
	public static List<FileItem> extractZipToListFiles(String zipFileName, String toFolder) throws Exception {
		File zipFile = new File(zipFileName);
		List<FileItem> ret = new ArrayList<FileItem>();
		String zipFileBase64 = getFileDataBased64(zipFileName);
		saveFile(toFolder, zipFile.getName(), zipFileBase64);
		extract(zipFileName, toFolder);
		
		List<String> listFiles = listFiles(toFolder);
		for (String filePath : listFiles) {
			if (!filePath.equalsIgnoreCase(zipFileName)) {
				File file = new File(filePath);
				FileItem fileItem = new FileItem();
				fileItem.setFileName(file.getCanonicalPath());
				fileItem.setBase64Content(getFileDataBased64(filePath));
				fileItem.setMimeType(new MimetypesFileTypeMap().getContentType(file));
				
				ret.add(fileItem);
			}
		}
		
		return ret;
	}
}
