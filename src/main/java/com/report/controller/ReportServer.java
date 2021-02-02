package com.report.controller;


import com.google.gson.reflect.TypeToken;
import com.report.entity.Result;
import com.report.entity.SimpleFlag;
import com.report.util.Base64Utils;
import com.report.util.StimulsoftUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.google.gson.Gson;



@Controller
@RequestMapping("/report")
public class ReportServer {

    private static Gson gson = new Gson();

    @RequestMapping("exportPdf")
    @ResponseBody
    public Result exportPdf(@RequestParam("simpleFlagsJson")String simpleFlagsJson, @RequestParam("pathData")String  pathData, @RequestParam("pathPdf")String pathPdf, @RequestParam("pathMrt")String pathMrt, @RequestParam("pathSchema")String pathSchema){

        ArrayList<SimpleFlag> simpleFlagList = new ArrayList<SimpleFlag>();
        simpleFlagList = GetJsonToArrayList(simpleFlagsJson);
        pathData = createXml(simpleFlagList);
        pathMrt ="data/Report.mrt";
        pathSchema ="data/simpleFlag.xsd";
        pathPdf = "data/"+UUID.randomUUID().toString()+".pdf";


        String result = StimulsoftUtil.StiExportPdf(pathData, pathPdf,pathMrt,pathSchema);
        if(result == "生成失败"){
            return Result.success(result,"生成失败");
        }

        return Result.success(result,"生成成功");

    }

    public static String createXml(ArrayList<SimpleFlag> simpleFlagList){

        String filePath = "";
        try {
            // 1、创建document对象
            Document document = DocumentHelper.createDocument();
            // 2、创建根节点rss
            Element rss = document.addElement("flag");
            // 3、向rss节点添加version属性
            rss.addAttribute("version", "1.0");
            // 4、生成子节点及子节点内容
            for (SimpleFlag simpleFlag : simpleFlagList) {
                Element channel = rss.addElement("simpleFlag");

                Element name = channel.addElement("name");
                name.setText(simpleFlag.getName() != "null"?simpleFlag.getName():"");
                Element no = channel.addElement("no");
                no.setText(simpleFlag.getNo() != "null"?simpleFlag.getNo():"");
                Element gcbw = channel.addElement("gcbw");
                gcbw.setText(replaceNullString(simpleFlag.getGcbw()));
                Element jyrq = channel.addElement("jyrq");
                jyrq.setText(replaceNullString(simpleFlag.getJyrq()));
                Element ypcz = channel.addElement("ypcz");
                ypcz.setText(replaceNullString(simpleFlag.getYpcz()));
                Element ztms = channel.addElement("ztms");
                ztms.setText(replaceNullString(simpleFlag.getZtms()));
                Element extValue1 = channel.addElement("extValue1");
                extValue1.setText(replaceNullString(simpleFlag.getExtValue1()));
                Element extValue2 = channel.addElement("extValue2");
                extValue2.setText(replaceNullString(simpleFlag.getExtValue2()));
                Element extValue3 = channel.addElement("extValue3");
                extValue3.setText(replaceNullString(simpleFlag.getExtValue3()));
                Element extValue4 = channel.addElement("extValue4");
                extValue4.setText(replaceNullString(simpleFlag.getExtValue4()));
                Element extName1 = channel.addElement("extName1");
                extName1.setText(replaceNullString(simpleFlag.getExtName1()));
                Element extName2 = channel.addElement("extName2");
                extName2.setText(replaceNullString(simpleFlag.getExtName2()));
                Element extName3 = channel.addElement("extName3");
                extName3.setText(replaceNullString(simpleFlag.getExtName3()));
                Element extName4 = channel.addElement("extName4");
                extName4.setText(replaceNullString(simpleFlag.getExtName4()));
                Element qrcode = channel.addElement("qrcode");
                //qrcode.setText(replaceNullString(Base64Utils.base64StringToImage(simpleFlag.getQrcode())));
                qrcode.setText(replaceNullString(simpleFlag.getQrcode()));


            }




            // 5、设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding("UTF-8");

            // 6、生成xml文件
            filePath ="data/"+ UUID.randomUUID().toString()+".xml";
            File file = new File(filePath);
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            System.out.println("生成xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成xml失败");
        }

        return filePath;
    }



    public ArrayList<SimpleFlag> GetJsonToArrayList(String jsonString){

        ArrayList<SimpleFlag> list =
                gson.fromJson(jsonString, new TypeToken<ArrayList<SimpleFlag>>(){}.getType());
        return list;
    }


    public static String replaceNullString(String str){
        if(str == null ) return "";
        else return str;
    }

    public static String getYpcz(String str){
        String ypcz = "";
        if( str.equals("废弃")){
            ypcz = "     ☑废弃    ☐留样    ☐归还";
        } else if(str.equals("留样")) {
            ypcz = "     ☐废弃    ☑留样    ☐归还";
        } else if (str.equals("归还")){
            ypcz = "     ☐废弃    ☐留样    ☑归还";
        } else {
            ypcz = "     ☐废弃    ☐留样    ☐归还";
        }

        return ypcz;
    }




}
