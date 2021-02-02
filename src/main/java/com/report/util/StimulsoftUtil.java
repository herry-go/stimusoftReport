package com.report.util;


import com.stimulsoft.report.StiExportManager;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.report.StiSerializeManager;
import com.stimulsoft.report.dictionary.databases.StiXmlDatabase;
import com.stimulsoft.report.enums.StiCalculationMode;
import com.stimulsoft.report.export.settings.StiPdfExportSettings;
import com.stimulsoft.report.export.tools.StiPdfAllowEditable;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;

public class StimulsoftUtil {

    private static final Logger logger = Logger.getLogger(StimulsoftUtil.class);

    public static String StiExportPdf(String pathData, String pathPdf,String pathMrt, String pathSchema) {
        logger.info("exportPdf pathData :"+pathData);
        logger.info("exportPdf pathPdf :"+pathPdf);
        logger.info("exportPdf pathMrt :"+pathMrt);
        logger.info("exportPdf pathSchema :"+pathSchema);


        StiXmlDatabase xmlDatabase = new StiXmlDatabase("simpleFlag", pathSchema, pathData);

        try {
            File mrtFile = new File(pathMrt);
            if(!mrtFile .exists()) {
                mrtFile .createNewFile();
                logger.info("create mrtFile success :"+pathMrt);
            }
            StiReport renderReport = StiSerializeManager.deserializeReport(mrtFile);
            logger.info("renderReport :"+renderReport.toString());
           renderReport.getDictionary().getDatabases().add(xmlDatabase);
            logger.info("Render____ 1_______success _______________________________");
            renderReport.setCalculationMode(StiCalculationMode.Interpretation);
            logger.info("Render_____ 2______success _______________________________");
            renderReport.Render(false);
            logger.info("Render success _______________________________");
            FileOutputStream outputStreamPdf = null;

            File file = new File(pathPdf);
            if(!file .exists()) {
                file .createNewFile();
                logger.info("create file success :"+pathPdf);
            }
            outputStreamPdf = new FileOutputStream(file);
            System.out.println(pathPdf);
            logger.info("exportPdf outputStreamPdf :"+outputStreamPdf.toString());
            StiPdfExportSettings pdfExportSettings = new StiPdfExportSettings();
            pdfExportSettings.setAllowEditable(StiPdfAllowEditable.Yes);
            pdfExportSettings.setPdfACompliance(true);
            pdfExportSettings.setEmbeddedFonts(true);
            pdfExportSettings.setUseUnicode(true);

            logger.info("exportPdf pdfExportSettings success _______________________________");


            StiExportManager.exportPdf(renderReport, pdfExportSettings, outputStreamPdf);
            System.out.println("PDF输出成功！！");
            logger.info("exportPdf success _______________________________");
            String pdf = Base64Utils.GetPDFBinary(file);
//            File pathDataFile = new File(pathData);
//            if (pathDataFile.isFile() && pathDataFile.exists()) {
//                pathDataFile.delete();
//            }
            File pathPdfFile = new File(pathPdf);
            if (pathPdfFile.isFile() && pathPdfFile.exists()) {
                pathPdfFile.delete();
            }


            return pdf;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.info("exportPdf err :"+e.getMessage());
            logger.info("exportPdf err :"+e);
            logger.info("exportPdf err :"+e.toString());
            return "生成失败";
        }


    }




}
