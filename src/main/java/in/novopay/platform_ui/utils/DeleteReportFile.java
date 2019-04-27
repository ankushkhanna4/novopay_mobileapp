package in.novopay.platform_ui.utils;

import java.io.File;

import org.testng.annotations.Test;

public class DeleteReportFile {
	@Test
	public void deleteReport(){
		File file = new File("./reports/TestReport.xlsx");
		if(file.exists()){
			file.delete();
		}
		file = new File("./testData.ini");
		if(file.exists()){
			file.delete();
		}
	}
}
