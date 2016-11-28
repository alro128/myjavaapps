package app.process;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import app.App;

public class Csv2xlsx {
    
    final static Logger LOG = Logger.getLogger(Csv2xlsx.class);
    Path filepath;
    
    public Csv2xlsx(Path filepath) {
        this.filepath = filepath;
    }

    public void convert() {

        final String DELIMITER = ";";
        String fileinput = filepath.toString();
        
        LOG.info("converting " + fileinput);
        try {
            String fileoutput = FilenameUtils.getBaseName(fileinput) + ".xlsx";

            SXSSFWorkbook workbook = new SXSSFWorkbook();
            Sheet sheet = workbook.createSheet(fileoutput);

            XSSFCellStyle headerstyle = (XSSFCellStyle) workbook.createCellStyle();
            headerstyle.setAlignment(HorizontalAlignment.CENTER);
            headerstyle.setVerticalAlignment(VerticalAlignment.TOP);
            XSSFFont font = (XSSFFont) workbook.createFont();
            font.setBold(true);
            headerstyle.setFont(font);

            String currentLine = null;
            int RowNum = 0;
            Row headerRow1 = sheet.createRow(RowNum);

            // sheet.addMergedRegion(new CellRangeAddress(firstRow,lastRow,firstColumn,lastColumn);

            Cell cell = headerRow1.createCell(0);
            cell.setCellValue("Article UUID");
            cell.setCellStyle(headerstyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

            cell = headerRow1.createCell(1);
            cell.setCellValue("Key UID");
            cell.setCellStyle(headerstyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));

            cell = headerRow1.createCell(2);
            cell.setCellValue("TTE Preferred Name");
            cell.setCellStyle(headerstyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 5));

            RowNum++;
            Row headerRow2 = sheet.createRow(RowNum);
            Cell cell2 = headerRow2.createCell(2);
            cell2.setCellValue("People");
            cell2.setCellStyle(headerstyle);
            cell2 = headerRow2.createCell(3);
            cell2.setCellValue("Organisations");
            cell2.setCellStyle(headerstyle);
            cell2 = headerRow2.createCell(4);
            cell2.setCellValue("Geographics");
            cell2.setCellStyle(headerstyle);
            cell2 = headerRow2.createCell(5);
            cell2.setCellValue("GeoBuildings");
            cell2.setCellStyle(headerstyle);

            BufferedReader br = new BufferedReader(new FileReader(fileinput));
            while ((currentLine = br.readLine()) != null) {
                String str[] = currentLine.split(DELIMITER);
                RowNum++;
                Row currentRow = sheet.createRow(RowNum);

                try {
                    // Article UUID
                    currentRow.createCell(0).setCellValue(str[0]);
                    // Key UID
                    currentRow.createCell(1).setCellValue(str[1]);

                    //Vocabulary Class
                    String currentType = str[3];
                    switch (currentType) {
                    case "_Person":
                        currentRow.createCell(2).setCellValue(str[2]);
                        break;
                    case "_Organisation":
                        currentRow.createCell(2).setCellValue("");
                        currentRow.createCell(3).setCellValue(str[2]);
                        break;
                    case "_Geographic":
                        currentRow.createCell(2).setCellValue("");
                        currentRow.createCell(3).setCellValue("");
                        currentRow.createCell(4).setCellValue(str[2]);
                        break;
                    case "_GeoBuilding":
                        currentRow.createCell(2).setCellValue("");
                        currentRow.createCell(3).setCellValue("");
                        currentRow.createCell(4).setCellValue("");
                        currentRow.createCell(5).setCellValue(str[2]);
                        break;                        
                    case "_People":
                        currentRow.createCell(2).setCellValue(str[2]);
                        break;
                    case "_Organisations":
                        currentRow.createCell(2).setCellValue("");
                        currentRow.createCell(3).setCellValue(str[2]);
                        break;
                    case "_Geographics":
                        currentRow.createCell(2).setCellValue("");
                        currentRow.createCell(3).setCellValue("");
                        currentRow.createCell(4).setCellValue(str[2]);
                        break;
                    case "_GeoBuildings":
                        currentRow.createCell(2).setCellValue("");
                        currentRow.createCell(3).setCellValue("");
                        currentRow.createCell(4).setCellValue("");
                        currentRow.createCell(5).setCellValue(str[2]);
                        break;  
                    case "_Organisation,_GeoBuilding":
                        currentRow.createCell(2).setCellValue("");
                        currentRow.createCell(3).setCellValue(str[2]);
                        break;
                    case "_GeoBuilding,_Organisation":
                        currentRow.createCell(2).setCellValue("");
                        currentRow.createCell(3).setCellValue("");
                        currentRow.createCell(4).setCellValue("");
                        currentRow.createCell(5).setCellValue(str[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid Vocabulary class: " + currentType);
                    }
                } catch (Exception e) {
                    LOG.error("ERROR: " + e.getMessage());
                }

            }
            FileOutputStream fileOutputStream = new FileOutputStream(App.DIR_OUTPUT + "/" + fileoutput);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            br.close();
            workbook.close();
            
            LOG.info("file created: " + fileoutput);

        } catch (Exception ex) {
            LOG.error("ERROR: " + ex.getMessage());
        }
    }

}