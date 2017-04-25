package org.lacitysan.landfill.server.report;

import org.lacitysan.landfill.server.persistence.enums.exceedance.ExceedanceType;
import org.lacitysan.landfill.server.service.report.model.ExceedanceReport;
import org.lacitysan.landfill.server.service.report.model.data.ProbeExceedanceReportData;
import org.lacitysan.landfill.server.service.report.model.data.ServiceEmissionExceedanceReportData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.datatable.*;


import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class ReportExport {
	
	public void exportReport() throws IOException{

		final String fileName = "C:/Users/Allen/Desktop/pdfxlamples/User Permissions.xlsx";
	//----------------------------------------------------------------------------------------------------
		PDDocument document = new PDDocument();		
		PDDocumentInformation pdd = document.getDocumentInformation();
		pdd.setAuthor("Allen Ma");
		pdd.setTitle("Landfill report");
		pdd.setCreator("Allen Ma");
		
		PDPage blankPage = new PDPage();
		blankPage.setMediaBox(new PDRectangle(PDRectangle.A4.getHeight(),PDRectangle.A4.getWidth()));
		document.addPage( blankPage );
    
	//----------------------------------------------------------------------------------------------------	
		
		try {
			
			
            FileInputStream excelFile = new FileInputStream(new File(fileName));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            	            
            Iterator<Row> iterator = datatypeSheet.iterator();
            int t = 1;
            String text = "";
            
            float margin = 10;
            float tableWidth = blankPage.getMediaBox().getWidth() - (2 * margin);
            float yStartNewPage = blankPage.getMediaBox().getHeight() - (2 * margin);
            float yStart = yStartNewPage;
            float bottomMargin = 20;
            List<List> data = new ArrayList<>();
            List<List> singleData = new ArrayList<>();
            
            String compare = "";
            String compare2 = "";
            int greatestLength = 0;
            int emptyLineCounter = 0;
            boolean oneElement = true;
            
            while (iterator.hasNext()) {  	
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                
                ArrayList<String> rowInfo = new ArrayList<>();	                
                
                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();
                    
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
//                        System.out.print(currentCell.getStringCellValue() + "\t");
                        
                        text = currentCell.getStringCellValue();
                        
                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
//                        System.out.print(currentCell.getNumericCellValue() + "\t");
                        
                        text = "" + currentCell.getNumericCellValue();
                    }
                    
                    if(compare2.equals(text)){
                    	break;
                    }
                    
                    rowInfo.add(text);
                   
                    compare2 = text;
                    
                }
                
                if(greatestLength < rowInfo.size()){
                	greatestLength = rowInfo.size();
                }
                
                if( rowInfo.isEmpty()){	
                	emptyLineCounter++;
                	if(emptyLineCounter > 5){
                		break;
                	}
                	
                }
                else{
                	emptyLineCounter = 0;
                	
                	if(rowInfo.size() == 1){
                    	char [] numArray = rowInfo.get(0).toCharArray();
                    	for(int h = 0; h < numArray.length; h++){
                    		if( (!Character.isDigit(numArray[h]) && numArray[h] != '.') ){
                    			oneElement = false;
                    		}
                    	}
                    }
                    
                    if(rowInfo.size() > 1){
                    	data.add(rowInfo);
                    	
    					for(int i = 0; i < rowInfo.size(); i++){
    						System.out.print(rowInfo.size() + " " + rowInfo.get(i));
    					}
    					System.out.println();
                    }
                    else if(oneElement){
                    	data.add(rowInfo);
                    }
                    else{
                    	singleData.add(rowInfo);
                    }	
                    
                    oneElement = true;
                }
                compare = text;     
                
            }
            
            for(int y = 0; y < data.size(); y++){
            	if(data.get(y).size() < greatestLength){
            		while(data.get(y).size() < greatestLength){
            			data.get(y).add(new ArrayList<>(Arrays.asList(" ")));
            		}
            	}
            }
            BaseTable s_dataTable = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, document, blankPage, true, true);
    		DataTable s_tab = new DataTable(s_dataTable, blankPage);
    		s_tab.addListToTable(singleData, DataTable.NOHEADER);
    		s_dataTable.draw();
    		
    		float zStartNewPage = blankPage.getMediaBox().getHeight() - ( (s_dataTable.getMinimumHeight() * singleData.size()) + (2*10) );
            float zStart = zStartNewPage;
            
//    		System.out.print(s_dataTable.getMinimumHeight());
            
            BaseTable dataTable = new BaseTable(zStart, zStartNewPage, bottomMargin, tableWidth, margin, document, blankPage, true, true);
    		DataTable tab = new DataTable(dataTable, blankPage);
    		tab.addListToTable(data, DataTable.HASHEADER);
    		dataTable.draw();
    		
            document.save("C:/Users/Allen/Desktop/generatePDF/Test.pdf");
    		document.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void ExceedanceReport(ExceedanceReport report) throws IOException {
		
		//----------------------------------------------------------------------------------------------------
		PDDocument document = new PDDocument();		
		PDDocumentInformation pdd = document.getDocumentInformation();
		pdd.setAuthor("Allen Ma");
		pdd.setTitle("Landfill report");
		pdd.setCreator("Allen Ma");
		
		
		PDPage blankPage1 = new PDPage();
		PDPage blankPage2 = new PDPage();
		PDPage blankPage3 = new PDPage();	
		blankPage1.setMediaBox(new PDRectangle(PDRectangle.A4.getHeight(),PDRectangle.A4.getWidth()));
		blankPage2.setMediaBox(new PDRectangle(PDRectangle.A4.getHeight(),PDRectangle.A4.getWidth()));
		blankPage3.setMediaBox(new PDRectangle(PDRectangle.A4.getHeight(),PDRectangle.A4.getWidth()));
		
		document.addPage( blankPage1 );
		document.addPage( blankPage2 );
		document.addPage( blankPage3 );
		
	//----------------------------------------------------------------------------------------------------				
        String text = "";	        
        float margin = 10;
        float tableWidth = blankPage1.getMediaBox().getWidth() - (2 * margin);
        float yStartNewPage = blankPage1.getMediaBox().getHeight() - (2 * margin);
        float yStart = yStartNewPage;
        float bottomMargin = 20;
        
        Set<ExceedanceType> types = report.getReportQuery().getExceedanceTypes();
        List<List> reportData = new ArrayList<>();
        List<? extends Object> listType;
        
        int pageIndex = 0;
        String exceedType = "";
        for (Iterator<ExceedanceType> it = types.iterator(); it.hasNext(); ) {
        	ExceedanceType exType = it.next();
        	if (exType.getName().equals("Instantaneous") ){
        		exceedType = "Instantaneous Exceedances";
        		listType = report.getImeReportData();
        	}
        	else if(exType.getName().equals("Integrated Exceedances")){
        		exceedType = "Integrated";
        		listType = report.getIseReportData();
        	}
        	else{
        		exceedType = "Perimeter Probe Exceedances";
        		listType = report.getProbeExceedanceReportData();
        	}
        	
        	ArrayList<String> header = new ArrayList<>();
            if (exType.getName().equals("Instantaneous") || exType.getName().equals("Integrated")){
            	
            	header.add("Date Discovered");
            	if(exType.getName().equals("Instantaneous")){
            		header.add("IME#");
            		header.add("Grid(s)");
            	}
            	else{
            		header.add("ISE#");
            		header.add("Grid");
            	}      	
                header.add("Repair Description");
                header.add("Initial Reading (ppm)");
                header.add("Recheck value (ppm)");
                header.add("Date Cleared");
                                 
                for(int i = 0; i < listType.size(); i++){
                	ArrayList<String> info = new ArrayList<>();
                	info.add( ((ServiceEmissionExceedanceReportData) listType.get(i)).getDiscoveredDate() );                	
            		info.add( ((ServiceEmissionExceedanceReportData) listType.get(i)).getExceedanceNumber() );
            		info.add( ((ServiceEmissionExceedanceReportData) listType.get(i)).getMonitoringPoints() );     	
                	info.add( ((ServiceEmissionExceedanceReportData) listType.get(i)).getRepairDescription() );
                	info.add( ((ServiceEmissionExceedanceReportData) listType.get(i)).getInitial() );
                	info.add( ((ServiceEmissionExceedanceReportData) listType.get(i)).getRecheck() );
                	info.add( ((ServiceEmissionExceedanceReportData) listType.get(i)).getClearedDate() );
                	                	
                	reportData.add(info);
                }
            }
            else if (exType.getName().equals("Probe")){
            	header.add("Date Discovered");
        		header.add("Probe ID");
        		header.add("Depth");           	            	     	
                header.add("Repair Description");
                header.add("Initial Reading (ppm)");
                header.add("Recheck value (ppm)");
                header.add("Date Cleared");
                
                for(int i = 0; i < listType.size(); i++){
                	ArrayList<String> info = new ArrayList<>();
                	info.add( ((ProbeExceedanceReportData) listType.get(i)).getDiscoveredDate() );                	
            		info.add( ((ProbeExceedanceReportData) listType.get(i)).getProbeId() );
            		info.add( ((ProbeExceedanceReportData) listType.get(i)).getDepth() );      	      	
                	info.add( ((ProbeExceedanceReportData) listType.get(i)).getRepairDescription() );
                	info.add( ((ProbeExceedanceReportData) listType.get(i)).getInitial() );
                	info.add( ((ProbeExceedanceReportData) listType.get(i)).getRecheck() );
                	info.add( ((ProbeExceedanceReportData) listType.get(i)).getClearedDate() );
                	                	
                	reportData.add(info);
                }
            }
            
            BaseTable dataTable = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, document, document.getPage(pageIndex), true, true);
    		
            //Create Header row
     		be.quodlibet.boxable.Row<PDPage> headerRow = dataTable.createRow(15f);
     		be.quodlibet.boxable.Cell<PDPage> cell = headerRow.createCell(100, exceedType);			
     		cell.setFont(PDType1Font.HELVETICA_BOLD);
     		
     		
     		
     		be.quodlibet.boxable.Row<PDPage> hea = dataTable.createRow(15f);
     		cell = hea.createCell((100 / 9.0f) * 2, "" + header.get(0) );
     		for (int i = 1; i < header.size(); i++) {
                    cell = hea.createCell((100 / 9f), "" + header.get(i));
                 }		
     		cell.setFont(PDType1Font.HELVETICA_BOLD);
     		dataTable.addHeaderRow(hea); //derRow
     		
     		for (List info : reportData) {
     			be.quodlibet.boxable.Row<PDPage> row = dataTable.createRow(10f);
                 
             	cell = row.createCell((100 / 9.0f) * 2, "" + info.get(0) ); //3
                 for (int i = 1; i < info.size(); i++) {
                    cell = row.createCell((100 / 9f), "" + info.get(i));
                 }
                 
                 
     		}
     		
     		dataTable.draw(); 
     		pageIndex++;
        }
                
        
        
		System.out.println(document.getNumberOfPages());
//        DataTable tab = new DataTable(dataTable, blankPage);
//		tab.addListToTable(reportData, DataTable.HASHEADER);
//		dataTable.draw();
		
        document.save("C:/Users/Allen/Desktop/generatePDF/exceedTest.pdf");
		document.close();
	}

}
