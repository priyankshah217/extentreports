package com.relevantcodes.cubereports;

import java.net.InetAddress;
import com.relevantcodes.cubereports.support.*;

class LogInsight {
	public static void updateSummary(String filePath, String newSummary) {
		String txtCurrent = FileOps.readAllText(filePath);
		String pattern = "<p><!--%%REPORTSUMMARY%%-->.*<!--%%REPORTSUMMARY%%--></p>";
		newSummary = pattern.replace(".*", newSummary); 
		
		String oldSummary = RegexMatcher.getNthMatch(txtCurrent, pattern, 0);
		txtCurrent = txtCurrent.replace(oldSummary, newSummary);
		
		FileOps.write(filePath, txtCurrent);		
	}
	
	public static void updateSystemSpecs(String filePath) {
		String txtCurrent = FileOps.readAllText(filePath);
		String hostName = "<!--%%HOSTNAME%%-->.*<!--%%HOSTNAME%%-->";
		String ip = "<!--%%IP%%-->.*<!--%%IP%%-->";
		String os = "<!--%%OS%%-->.*<!--%%OS%%-->";
		String locale = "<!--%%LOCALE%%-->.*<!--%%LOCALE%%-->";
		String totalMem = "<!--%%TOTALMEM%%-->.*<!--%%TOTALMEM%%-->";
		String availMem = "<!--%%AVAILMEM%%-->.*<!--%%AVAILMEM%%-->";
		String oldValue = "";
		
		oldValue = RegexMatcher.getNthMatch(txtCurrent, hostName, 0);
		try {
			txtCurrent = txtCurrent.replace(oldValue, hostName.replace(".*", InetAddress.getLocalHost().getHostName()));
		}
		catch (Exception e) {
			txtCurrent = txtCurrent.replace(oldValue, hostName.replace(".*", "NOT_AVAILABLE"));
		}
		
		oldValue = RegexMatcher.getNthMatch(txtCurrent, ip, 0);
		try {
			txtCurrent = txtCurrent.replace(oldValue, ip.replace(".*", InetAddress.getLocalHost().getHostAddress()));
		}
		catch (Exception e) {
			txtCurrent = txtCurrent.replace(oldValue, ip.replace(".*", "NOT_AVAILABLE"));
		}
		
		oldValue = RegexMatcher.getNthMatch(txtCurrent, os, 0);
		txtCurrent = txtCurrent.replace(oldValue, os.replace(".*", System.getProperty("os.name")));
		
		oldValue = RegexMatcher.getNthMatch(txtCurrent, locale, 0);
		txtCurrent = txtCurrent.replace(oldValue, locale.replace(".*", System.getProperty("user.language")));
		
		oldValue = RegexMatcher.getNthMatch(txtCurrent, totalMem, 0);
		txtCurrent = txtCurrent.replace(oldValue, totalMem.replace(".*", "" + Runtime.getRuntime().totalMemory() + ""));
		
		oldValue = RegexMatcher.getNthMatch(txtCurrent, availMem, 0);
		txtCurrent = txtCurrent.replace(oldValue, availMem.replace(".*", "" + Runtime.getRuntime().freeMemory() + ""));
		
		FileOps.write(filePath, txtCurrent);
	}
	
	public static void useCustomStylesheet(String filePath, String cssFilePath) {
		String txtCurrent = FileOps.readAllText(filePath);
		String placeHolder = "<!--%%CUSTOMCSS%%-->.*<!--%%CUSTOMCSS%%-->";
		String link = "<link href='file:///" + cssFilePath + "' rel='stylesheet' type='text/css' />";
		
		String match = RegexMatcher.getNthMatch(txtCurrent, placeHolder, 0);
		txtCurrent = txtCurrent.replace(match, placeHolder.replace(".*", link));
		
		FileOps.write(filePath, txtCurrent);
	}
}