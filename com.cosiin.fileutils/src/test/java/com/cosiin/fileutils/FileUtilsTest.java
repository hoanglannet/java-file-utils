package com.cosiin.fileutils;

import java.io.IOException;

import org.junit.Test;

import com.cosiin.fileutils.FileUtils;

public class FileUtilsTest {
	
	private final String FILE_TO_ZIP = "C:\\lanle\\test\\extract\\1117_apa.pdf";
	private final String FILE_TO_ZIPPED = "C:\\lanle\\test\\extract\\1117_apa.zip";
	private final String ZIP_FILE = "C:\\lanle\\test\\zipFile.zip";
	private final String EXTRACT_FOLDER = "C:\\lanle\\test\\extract";
	private final String ARCHIVE_FOLDER = "C:\\lanle\\test\\archive";

	//@Test
	public void testExtractFile() throws IOException {
		FileUtils.extract(ZIP_FILE, EXTRACT_FOLDER);
	}

	//@Test
	public void testDeleteFile() throws IOException {
		FileUtils.delete(ZIP_FILE);
	}
	
	//@Test
	public void testArchiveFile() throws IOException {
		FileUtils.archive(ZIP_FILE, ARCHIVE_FOLDER);
	}
	
	@Test
	public void testZipFile() throws IOException {
		FileUtils.zip(FILE_TO_ZIP, FILE_TO_ZIPPED);
	}
}
