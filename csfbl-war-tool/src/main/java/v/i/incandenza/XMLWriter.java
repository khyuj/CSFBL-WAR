package v.i.incandenza;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

public class XMLWriter implements ActionListener {
    private String FILE_NAME;
    private Object[][] playerData;
    private String years;
    private String league;    
    
    public void actionPerformed(ActionEvent e) {
    	XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
    	int rowNum = 0;
        System.out.println("Creating excel");
        for (Object[] playerRow : playerData) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : playerRow) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof Double) {
                	cell.setCellValue((Double) field);
                }
                
            }
        }
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(null);
        FILE_NAME = chooser.getSelectedFile().getAbsolutePath();
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException a) {
            a.printStackTrace();
        } catch (IOException a) {
            a.printStackTrace();
        }
    }
    
    public XMLWriter(DataTable tableModel, ArrayList<String> yearList, String league) {        
        this.playerData = tableModel.getData();
        this.league = league;
        if(yearList.size() > 1) {
        	this.years = yearList.get(0) + "-" + yearList.get(yearList.size()-1);
        }
        else if(yearList.size() == 1) {
        	this.years = yearList.get(0);
        }        
    }
}



