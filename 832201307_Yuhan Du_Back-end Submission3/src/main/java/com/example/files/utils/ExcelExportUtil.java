package com.example.files.utils;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @description
 */
public class ExcelExportUtil {
    /**
     * 导出Excel文件到HTTP响应
     *
     * @param response       HTTP响应对象
     * @param fileNamePrefix 文件名前缀，将用于生成完整的文件名
     * @param dataList       要导出的数据列表
     * @param dataClass      数据列表中的元素类型，用于EasyExcel识别
     * @throws IOException 如果写入响应时发生错误
     */
    public static void exportExcel(HttpServletResponse response, String fileNamePrefix, List<?> dataList, Class<?> dataClass) throws IOException {
        // 设置响应内容类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 对文件名进行URL编码并添加.xlsx后缀
        String fileName = URLEncoder.encode(fileNamePrefix, "UTF-8") + ".xlsx";

        // 设置响应头，以附件形式发送
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        // 使用EasyExcel写入数据到响应的输出流
        try (OutputStream out = response.getOutputStream()) {
            EasyExcel.write(out, dataClass)
                    .sheet(fileNamePrefix) // 使用文件名前缀作为工作表名
                    .doWrite(dataList);
        } catch (IOException e) {
            throw new IOException("Error writing Excel file to response", e);
        }
    }

}
